---
page_type: sample
languages:
- csharp
- java
- python
- javascript
products:
- azure
- azure-cognitive-services
- azure-anomaly-detector
description: "This repository contains samples for Anomaly Detector multivariate API. The Anomaly Detector multivariate API enables you to monitor and find abnormalities in your time series data by automatically identifying and applying the correct statistical models, regardless of industry, scenario, or data volume."
---

# Anomaly Detector multivariate API Samples

This repository contains samples for [Anomaly Detector API](https://aka.ms/anomalydetector). The Anomaly Detector multivariate API enables you to monitor and find abnormalities in your time series data by automatically identifying and applying the correct statistical models, regardless of industry, scenario, or data volume. Using your time series data, the API can find anomalies as a batch throughout your data, or determine if your latest data point is an anomaly.

## Prerequisites

You must have an [Anomaly Detector API resource](https://aka.ms/adnew). Before continuing, you will need the API key and the endpoint from your Azure dashboard.
   ![Get Anomaly Detector access keys](../media/cognitive-services-get-access-keys.png "Get Anomaly Detector access keys")

Or you could create a 7-day free resource of Anomaly Detector from [here](https://azure.microsoft.com/en-us/try/cognitive-services/my-apis/).

## Data requirements

Example data is provided in this repository, along with example JSON responses from the API. To use the Anomaly Detector API on your time series data, ensure the following:

* Your data points are separated by the same interval, with no missing points.
* Your data has at least 13 data points if it doesn't have clear perodicity.
* Your data has at least 4 periods if it does have clear perodicity.
Please read [Best practices for using the Anomaly Detector API](https://aka.ms/adbest) for details.

## Sample Code

| Langauge | Sample Code|
|:---------|:-----------|
| Python    | [Sample](./Python) |
| C#        | [Sample](./CSharp) |
| Java      | [Sample](./Java)   |
| JavaScript| [Sample](./JavaScript) |

## Example data
Properly formatted multivariate sample data can be found in this [zip file](./multivariate_sample_data)
