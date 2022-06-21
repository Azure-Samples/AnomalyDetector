# -------------------------------------------------------------
#
# Copyright (c) Microsoft Corporation. All rights reserved.
#
# -------------------------------------------------------------

import json
import logging
import os
import re
from datetime import datetime
from typing import ClassVar, Optional, Union

from pydantic import BaseModel

from azure.ai.anomalydetector import AnomalyDetectorClient
from azure.ai.anomalydetector.models import DetectRequest, TimeGranularity
from azure.core.credentials import AzureKeyCredential
from azure.core.exceptions import HttpResponseError


logger = logging.getLogger(__name__)


class ADTimeSeries:
    """Class to read, format, and validate the data for the anomaly detector service.

    Note: the format of the data must match the following:
    data = {
        "period": 2 # OPTIONAL: Specifying this value can reduce anomaly detection latency by up to 50%. The
            period is an integer that specifies roughly how many data points the time series takes to repeat a pattern
        "series": [ # REQUIRED: list containing Time series data points. This should be sorted by timestamp in
            # ascending order to match the anomaly detection result. If the data is not sorted correctly or
            # there is a duplicate timestamp, the API will not work. In such case, an error message will be
            # returned. Each data point must have at least a "timestamp" in iso-8601 format and a "value"
            {"timestamp": "2014-12-07T21:00:00Z", "value": 7290.0979278853},
            {"timestamp": "2014-12-07T22:00:00Z", "value": 7884.4973233823}
            ],
        "granularity": "hourly", # REQUIRED: The sampling rate of the data. Must contain one of the granularities
            listed below.
        "sensitivity": 99 # OPTIONAL: This advanced model parameter in an integer between 0-99 that represents
             the sensitivity of the AD algorithm. The lower the value, the larger the margin will be, meaning
             fewer anomalies will be detected.
        "maxAnomalyRatio": 0.25 # OPTIONAL: This is an advanced model parameter is a float between 0 and 1
             representing the maximum anomaly ratio.
        "customInterval": is optional and used to set non-standard time interval. For example, if the series is
             5 minutes, the request can be set as: {"granularity":"minutely", "customInterval":5}
    }
    """

    GRANULARITIES: ClassVar[dict] = {
        "yearly": TimeGranularity.YEARLY,
        "monthly": TimeGranularity.MONTHLY,
        "weekly": TimeGranularity.WEEKLY,
        "daily": TimeGranularity.DAILY,
        "hourly": TimeGranularity.HOURLY,
        "minutely": TimeGranularity.PER_MINUTE,
        "secondly": TimeGranularity.PER_SECOND,
    }

    def __init__(self, data: Union[dict, None] = None):
        self.data = data
        self.validated = False

    def validate(self):
        """Validates that self.data is in the correct format for the anomaly detector service.

        For documentation on the correct format, see: https://docs.microsoft.com/en-us/azure/
        cognitive-services/anomaly-detector/concepts/anomaly-detection-best-practices#data-preparation

        Once validation is done, the self.validated flag is set to True.
        """
        try:
            # Series:
            series = self.data.get("series", None)
            series_len = len(series) if series else 0
            if series_len < 12 or series_len > 8460:
                raise AssertionError(
                    f"The length of series must be in the range [12-8460] but is {series_len}"
                )
            utc_pattern = re.compile(
                r"\b[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2}Z\b"
            )  # ref: https://stackoverflow.com/questions/25568134/regex-to-verify-utc-date-time-format

            timestamp = None
            sort_datapoints = False

            for entry in self.data["series"]:
                # Validate timestamp format, and make sure the entries are sorted in ascending order
                entry_dict = entry.dict() if isinstance(entry, BaseModel) else entry
                current_timestamp = datetime.strptime(
                    entry["timestamp"], "%Y-%m-%dT%H:%M:%SZ"
                )

                # Check if datapoints are sorted correctly:
                if timestamp is not None:
                    if not current_timestamp >= timestamp:
                        sort_datapoints = True
                        logger.debug(
                            "Datapoints are not in ascending order! Will sort points in ascending order.. "
                        )

                timestamp = current_timestamp
                if not utc_pattern.match(entry_dict["timestamp"]):
                    raise AssertionError("timestamp data does not match UTC format.")
                if "value" not in entry_dict:
                    raise AssertionError("data['series'] entry is missing value field.")
                if not isinstance(entry_dict["value"], float):
                    raise AssertionError("'value' should be of type float.")

            if sort_datapoints:
                self.data["series"].sort(key=lambda x: x["timestamp"], reverse=False)

            # Granularity:
            if "granularity" not in self.data:  # Required field
                raise AssertionError("self.data missing required 'granularity' field.")
            self.data["granularity"] = self.data["granularity"].lower()
            if self.data["granularity"] not in list(ADTimeSeries.GRANULARITIES):
                raise AssertionError(
                    "granularity value is not one of those listed in ADTimeSeries.GRANULARITIES."
                )

            # Custom interval:
            if "customInterval" not in self.data:
                # 'custom_interval' is optional and used to set non-standard time interval.
                # For example, if the series is 5 minutes, the request can be set as:
                # {"granularity":"minutely", "customInterval":5}
                self.data["customInterval"] = 1

        except AssertionError as err:
            logger.error(err, exc_info=True)
            raise AssertionError(err) from err

        self.validated = True

    @classmethod
    def from_json_path(cls, filepath: str):
        """Read data from a JSON file.

        Args:
            filepath (str): Path to a JSON file containing the data.

        Returns:
            (AnomalyDetectorSeries): an AnomalyDetectorSeries object containing the data.
        """
        with open(filepath, encoding="utf-8") as file_handle:
            data = cls(json.load(file_handle))
            data.validate()
            return data


class UnivariateAnomalyDetector:
    """Class to interact with the Azure Anomaly Detector Service."""

    def __init__(self, key: str = None, endpoint: str = None):
        self.key = key
        self.endpoint = endpoint
        self.client: AnomalyDetectorClient = None
        logger.debug("Instantiated UnivariateAnomalyDetector object.")

    def connect(self):
        """Reads key and endpoint environment variables and creates AnomalyDetectorClient client object."""
        if self.key is None:
            logger.debug(
                "self.key is None -- reading the key from the environment variable."
            )
            self.key = os.getenv("ANOMALY_DETECTOR_KEY", default=None)
        if self.endpoint is None:
            logger.debug(
                "self.endpoint is None -- reading the endpoint from the environment variable."
            )
            self.endpoint = os.getenv("ANOMALY_DETECTOR_ENDPOINT", default=None)

        if self.key and self.endpoint:
            self.client = AnomalyDetectorClient(
                AzureKeyCredential(self.key), self.endpoint
            )
            logger.info("Successfully instantiated AnomalyDetectorClient object.")
        else:
            msg = "The key or endpoint for the Anomaly Detector resource are missing."
            logger.error(msg)
            raise ValueError(msg)

    def detect_anomaly(
        self, mode: str, data_dict: Union[dict, ADTimeSeries]
    ) -> Optional[dict]:
        """Create and send a request to the Anomaly Detector API.

        Args:
            mode (str): The API accepts three modes of detection: 'entire', 'last', 'change'.
            data_dict (Union[dict, ADTimeSeries]): a dictionary or a ADTimeSeries containing the time
                series data.

        Returns:
            dict: the result from the anomaly detector API call. The feilds in this result depend on the mode:

            -    'entire' mode: ['period','expected_values', 'upper_margins', 'lower_margins', 'is_anomaly',
                    'is_negative_anomaly','is_positive_anomaly']

            -   'last' mode: ['period', 'suggested_window', 'expected_value', 'upper_margin', 'lower_margin',
                    'is_anomaly', 'is_negative_anomaly', 'is_positive_anomaly'])

            -   'change' mode: ['period', 'is_change_point', 'confidence_scores']

        """

        if self.client is None:
            logger.debug("self.client is None -- Calling self.connect()..")
            self.connect()

        if isinstance(data_dict, ADTimeSeries):
            if not data_dict.validated:  # Make sure the data is valid.
                raise AssertionError("data_dict is not validated.")
            data_dict = (
                data_dict.data
            )  # Extract the data, we don't need ADTimeSeries anymore.

        # Create the request:
        request = DetectRequest(
            series=data_dict["series"], granularity=data_dict["granularity"]
        )

        try:
            if mode == "entire":
                logger.debug("Calling Anomaly Detector API in entire data series mode.")
                resp = self.client.detect_entire_series(request).as_dict()
                resp["severity_scores"] = self.compute_severity_scores(data_dict, resp)
                return resp

            if mode == "last":
                logger.debug("Calling Anomaly Detector API in last point mode.")
                return self.client.detect_last_point(request).as_dict()

            if mode == "change":
                logger.debug(
                    "Calling Anomaly Detector API in detect change point mode."
                )
                return self.client.detect_change_point(request).as_dict()

            err_message = "Unknown anomaly mode: " + mode
            logger.error(err_message)
            raise ValueError(err_message)

        except HttpResponseError as err:
            logger.error(err, exc_info=True)
            raise HttpResponseError from err
