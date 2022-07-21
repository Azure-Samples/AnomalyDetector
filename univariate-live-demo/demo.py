# -------------------------------------------------------------
#
# Copyright (c) Microsoft Corporation. All rights reserved.
#
# -------------------------------------------------------------

import datetime
import os
from dataclasses import dataclass
from datetime import timedelta, timezone

import pandas as pd
from bokeh.driving import count
from bokeh.layouts import column, row
from bokeh.models import ColumnDataSource, Select, Slider
from bokeh.plotting import curdoc, figure
from dateutil.parser import parse

from utils import ADTimeSeries, UnivariateAnomalyDetector


@dataclass
class Config:
    """
    Dataclass to store the default configuration for the demo. Please change the values if you
    want to use your own data.
    """

    csv_name: str = "sensor_data.csv"  # Name of the csv file containing the data
    value_column: str = "sensor_readings"  # Name of the column containing the values
    timestamp_column: str = "timestamp"  # Name of the column containing the timestamps
    dimension_column: str = "sensor_name"  # (Optional) Name of the column containing a dimension (e.g. sensor name, or location, etc). If your data does not have this column, set it to None.
    window_size: int = 50  # Size of the window used to compute the anomaly score
    minute_resample: int = 5  # Resample the data to this minute resolution
    ad_mode: str = "entire"  # Anomaly detection mode to use. Can be "entire" for batch mode or "last" for last point mode.


class MissingEnvironmentVariable(Exception):
    """
    Exception to be thrown when a required environment variable is not set.
    """

    pass


# Read environment variables:
apikey = os.getenv("ANOMALY_DETECTOR_API_KEY")
endpoint = os.getenv("ANOMALY_DETECTOR_ENDPOINT")

if apikey is None or endpoint is None:
    raise MissingEnvironmentVariable(
        "Please ensure ANOMALY_DETECTOR_API_KEY and ANOMALY_DETECTOR_ENDPOINT environment variables are set!"
    )

# Read CSV:
try:
    df = pd.read_csv(Config.csv_name)
except FileNotFoundError:
    raise FileNotFoundError(
        f"Please ensure the file {Config.csv_name} exists in the current directory!"
    )

# Validate the configuration:
if Config.timestamp_column not in df.columns:
    raise ValueError("Please ensure the timestamp column is present in the CSV!")
elif Config.value_column not in df.columns:
    raise ValueError("Please ensure the value column is present in the CSV!")

if Config.dimension_column is None:
    Config.dimension_column = "dimension"
    df[[Config.dimension_column]] = "main_dimension"
else:
    if Config.dimension_column not in df.columns:
        raise ValueError(
            f"Please ensure the dimension column is present in the CSV! ({Config.dimension_column})"
        )

# Extract the relevant columns:
df = df[[Config.timestamp_column, Config.dimension_column, Config.value_column]]

# Drop rows with NaNs:
df.dropna(inplace=True)

# Pivot dataframe to show time vs. sensor data
df = df.pivot_table(
    index=Config.timestamp_column,
    columns=Config.dimension_column,
    values=Config.value_column,
    aggfunc="mean",
)

# Parse timestamp column
df.index = df.index.map(lambda x: parse(str(x).replace("@", "")))

# Convert index to datatime:
df.index = pd.to_datetime(df.index)

source = ColumnDataSource(
    dict(
        time=[],
        timestamp=[],
        timestamp_str=[],
        values=[],
        expectedValues=[],
        upperband=[],
        lowerband=[],
        isAnomaly=[],
        color=[],
    )
)

p = figure(
    height=500,
    width=1200,
    tools="xpan,xwheel_zoom,xbox_zoom,reset",
    x_axis_type="datetime",
    y_axis_location="right",
)

p.x_range.follow = "end"
p.y_range.start = 0
p.xaxis.axis_label = "Time"

p.line(
    x="timestamp",
    y="values",
    alpha=0.8,
    line_width=2,
    color="navy",
    source=source,
    legend_label="Measured Value",
)

p.line(
    x="timestamp",
    y="expectedValues",
    alpha=0.8,
    line_width=2,
    color="orange",
    source=source,
    legend_label="Expected Value",
)

p.circle(
    "timestamp",
    "values",
    size=5,
    color="color",
    alpha=1,
    source=source,
    legend_label="Data points",
)

p.segment(
    x0="timestamp",
    y0="lowerband",
    x1="timestamp",
    y1="upperband",
    line_width=10,
    alpha=0.4,
    color="orange",
    source=source,
    legend_label="Expected Range",
)

p.line(
    x="timestamp",
    y="upperband",
    alpha=0.4,
    line_width=1,
    color="orange",
    source=source,
)

p.line(
    x="timestamp",
    y="lowerband",
    alpha=0.4,
    line_width=1,
    color="orange",
    source=source,
)

p.legend.location = "top_left"

sensitivity = Slider(title="sensitivity", value=95, start=0, end=99, step=1)
max_anomaly_ratio = Slider(
    title="max_anomaly_ratio", value=0.20, start=0, end=1, step=0.05
)

sensor_names = list(df.columns)
scenario = Select(value=sensor_names[-1], options=sensor_names)
adclient = UnivariateAnomalyDetector(key=apikey, endpoint=endpoint)


def _get_value(t):
    """
    Loops over the series contiuosly based on the scenario selected by the user
    """
    return df[scenario.value][t % len(df)]


def _get_timestamp(t):
    """
    Generates a fake timestamp
    """
    timestamp = datetime.datetime(2015, 1, 1, tzinfo=timezone.utc) + timedelta(
        minutes=Config.minute_resample * t
    )
    return timestamp, timestamp.isoformat().split("+")[0] + "Z"


def _call_ad_api(t):
    """
    Creates a request and calls the anomaly detector API, then processes and returns the response
    """
    values = source.data["values"][-Config.window_size :]
    timestamps = source.data["timestamp_str"][-Config.window_size :]
    request = {}
    request["series"] = []
    for i in range(Config.window_size):
        request["series"].append({"value": values[i], "timestamp": timestamps[i]})

    request["granularity"] = "minutely"
    request["maxAnomalyRatio"] = max_anomaly_ratio.value
    request["sensitivity"] = sensitivity.value
    request["customInterval"] = Config.minute_resample

    # validate that the request is valid:
    request = ADTimeSeries(request)
    request.validate()

    response = adclient.detect_anomaly(mode=Config.ad_mode, data_dict=request)

    if Config.ad_mode == "entire":
        response["expected_value"] = response["expected_values"][-1]
        response["upper_margin"] = response["upper_margins"][-1]
        response["lower_margin"] = response["lower_margins"][-1]
        response["is_anomaly"] = response["is_anomaly"][-1]

    print(
        f"Point: {str(t)}: Is anomaly? {response['is_anomaly']} -- Expected value: {response['expected_value']}"
    )

    upperband = response["expected_value"] + response["upper_margin"]
    lowerband = response["expected_value"] - response["lower_margin"]

    if response["is_anomaly"]:
        color = "red"
    else:
        color = "navy"

    return (
        response["expected_value"],
        upperband,
        lowerband,
        response["is_anomaly"],
        color,
    )


@count()
def update(t):
    value = _get_value(t)
    ts, ts_str = _get_timestamp(t)

    if t > Config.window_size:
        # we have enough data to send an API request
        expectedValue, upperband, lowerband, isAnomaly, color = _call_ad_api(t)
    else:
        # Use default values for the first few points
        expectedValue, upperband, lowerband, isAnomaly, color = 0, 0, 0, False, None

    new_data = dict(
        time=[t],
        timestamp=[ts],
        timestamp_str=[ts_str],
        expectedValues=[expectedValue],
        values=[value],
        upperband=[upperband],
        lowerband=[lowerband],
        isAnomaly=[isAnomaly],
        color=[color],
    )

    p.title.text = f"Live anomaly detection results | Sensitivity: {sensitivity.value} | Maximum Anomaly Ratio: {max_anomaly_ratio.value}"
    source.stream(
        new_data, rollover=100
    )  # the rollover number must be > window size. It governs the amount of data visible in the window.


curdoc().add_root(column(row(max_anomaly_ratio, sensitivity, scenario), p))
curdoc().add_periodic_callback(update, 100)
curdoc().title = "Anomaly Detector API Demo"
