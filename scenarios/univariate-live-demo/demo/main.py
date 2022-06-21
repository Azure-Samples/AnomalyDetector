# -------------------------------------------------------------
#
# Copyright (c) Microsoft Corporation. All rights reserved.
#
# -------------------------------------------------------------

from dataclasses import dataclass
import datetime
import logging
import os
from datetime import timedelta, timezone
from os.path import join as pjoin

import numpy as np
import pandas as pd
from bokeh.driving import count
from bokeh.layouts import column, row
from bokeh.models import ColumnDataSource, Select, Slider
from bokeh.plotting import curdoc, figure
from dateutil.parser import parse

from utils import ADTimeSeries, UnivariateAnomalyDetector

# TODO:
# Update readme, and add a gif of the demo
# Dataset..
# change this to a CLI with default values later


@dataclass
class config:
    csv_name = "sensor_data.csv"
    value_column = "sensor_readings"
    timestamp_column = "timestamp"
    sensor_column = "sensor_name"
    window_size = 20
    minute_resample = 5
    ad_mode = "entire"


logging.disable()

apikey = os.getenv("ANOMALY_DETECTOR_API_KEY")
endpoint = os.getenv("ANOMALY_DETECTOR_ENDPOINT")

np.random.seed(1)

# Read CSV:
df = pd.read_csv(pjoin("data", config.csv_name))

# Drop any columns that are no longer needed (and might have NaNs):
df = df[[config.timestamp_column, config.sensor_column, config.value_column]]

# Drop rows with NaNs:
df.dropna(inplace=True)

# Pivot dataframe to show time vs. sensor data
df = df.pivot_table(
    index=config.timestamp_column,
    columns=config.sensor_column,
    values=config.value_column,
    aggfunc="mean",
)

# Parse timestamp column
df.index = df.index.map(lambda x: parse(str(x).replace("@", "")))

# Convert index to datatime:
df.index = pd.to_datetime(df.index)



# -------------------------
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
# -------------------------
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
    alpha=0.0,
    color="orange",
    source=source,
    legend_label="Expected Range",
)

p.line(
    x="timestamp",
    y="upperband",
    alpha=0.0,
    line_width=1,
    color="orange",
    source=source,
)

p.line(
    x="timestamp",
    y="lowerband",
    alpha=0.0,
    line_width=1,
    color="orange",
    source=source,
)

p.legend.location = "top_left"

# -------------------------
sensitivity = Slider(title="sensitivity", value=95, start=0, end=99, step=1)
max_anomaly_ratio = Slider(
    title="max_anomaly_ratio", value=0.25, start=0, end=1, step=0.05
)

sensor_names = list(df.columns)
scenario = Select(value=sensor_names[-1], options=sensor_names)
# -------------------------

adclient = UnivariateAnomalyDetector(key=apikey, endpoint=endpoint)


def _get_value(t):
    """
    Loops over the series contiuosly based on the scenario selected by the user
    """
    return df[scenario.value][t % len(df)]


def _get_timestamp(t):
    timestamp = datetime.datetime(2015, 1, 1, tzinfo=timezone.utc) + timedelta(
        minutes=config.minute_resample * t
    )
    return timestamp, timestamp.isoformat().split("+")[0] + "Z"


def _call_ad_api(t):
    values = source.data["values"][-config.window_size :]
    timestamps = source.data["timestamp_str"][-config.window_size :]
    request = {}
    request["series"] = []
    for i in range(config.window_size):
        request["series"].append({"value": values[i], "timestamp": timestamps[i]})

    request["granularity"] = "minutely"
    request["maxAnomalyRatio"] = max_anomaly_ratio.value
    request["sensitivity"] = sensitivity.value
    request["customInterval"] = config.minute_resample

    # validate that the request is valid:
    request = ADTimeSeries(request)
    request.validate()

    results = adclient.detect_anomaly(mode=config.ad_mode, data_dict=request)

    if config.ad_mode == "entire":
        results["expected_value"] = results["expected_values"][-1]
        results["upper_margin"] = results["upper_margins"][-1]
        results["lower_margin"] = results["lower_margins"][-1]
        results["is_anomaly"] = results["is_anomaly"][-1]

    print(
        f"Point: {str(t)}: Is anomaly? {results['is_anomaly']} -- Expected value: {results['expected_value']}"
    )

    upperband = results["expected_value"] + results["upper_margin"]
    lowerband = results["expected_value"] - results["lower_margin"]


    if results["is_anomaly"]:
        color = "red"
    else:
        color = "navy"

    return (
        results["expected_value"],
        upperband,
        lowerband,
        results["is_anomaly"],
        color,
    )


@count()
def update(t):
    value = _get_value(t)
    ts, ts_str = _get_timestamp(t)

    if t > config.window_size:
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

    p.title.text = f"Live Anomaly Detection on Methane Measurement Data - Sensitivity: {sensitivity.value} - M.A.R.: {max_anomaly_ratio.value}"
    source.stream(
        new_data, rollover=100
    )  # the rollover number must be > window size. It governs the amount of data visible in the window.


curdoc().add_root(column(row(max_anomaly_ratio, sensitivity, scenario), p))
curdoc().add_periodic_callback(update, 50)
curdoc().title = "Live Anomaly Detection on Methane Measurement Data"
