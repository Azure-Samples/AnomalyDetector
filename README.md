---
topic: sample
languages:
  - csharp
  - python
  - java
products:
  - azure-cognitive-services
---

# Anomaly Detection API Samples

This repository contains samples for the Anomaly Detection API, which is an [Azure Cognitive Service](https://docs.microsoft.com/azure/cognitive-services/). The Anomaly Detector API enables you to monitor and find abnormalities in your time series data by automatically identifying and applying the correct statistical models, regardless of industry, scenario, or data volume. Using your time series data, the API can find anomalies as a batch throughout your data, or determine if your latest data point is an anomaly. 

## Contents

| File/folder | Description |
|-------------|-------------|
| `example-data`       | Example data to be sent to the API, along with example API responses.  |
| `quickstarts`       | Sample code for the Anomaly Detector API quickstarts.  |
| `example-data`       | Example data to be sent to the API, along with example API responses.  |
| `.gitignore` | Define what to ignore at commit time. |
| `README.md` | This README file. |
| `LICENSE`   | The license for the sample. |

## Prerequisites

You must have a [Cognitive Services API account](https://docs.microsoft.com/azure/cognitive-services/cognitive-services-apis-create-account) with access to the Anomaly detector API. If you don't have an Azure subscription, you can [create an account](https://azure.microsoft.com/try/cognitive-services/?api=bing-web-search-api) for free. Before continuing, You will need the access key provided after activating your free trial, or a paid subscription key from your Azure dashboard.

## Demo

To quickly begin using the Anomaly Detector API, try an [online demo](https://notebooks.azure.com/AzureAnomalyDetection/projects/anomalydetector). This demo runs in a web-hosted Jupyter notebook and shows you how to send an API request, and visualize the result.

To run the demo, complete the following steps:
  
1.	Sign in, and click **Clone**, in the upper right corner.
3.	click **Run on free compute**
4.	Open one of the notebook, for example, Anomaly Finder API Example Private Preview (Batch Method).ipynb
5.	Fill in the key in cell containing:  subscription_key = '' #Here you have to paste your primary key. You can get the key by creating a [Cognitive Services account](../cognitive-services-apis-create-account.md).
6.	In the Notebook main menu, click Cell->run all

> Warning: Uncheck the "Public" option when you clone your instance or your code and data in the project is visible to everyone. You could also change the privacy settings in your clone. 

## Data requirements

Example data is provided in this repository, along with example JSON responses from the API. To use the Anomaly Detector API on your time series data, ensure the following:

* Your data points are separated by the same interval, with no missing points.
* Your data has at least 13 data points if it doesn't have clear perodicity.
* Your data has at least 4 data points if it does have clear perodicity.
