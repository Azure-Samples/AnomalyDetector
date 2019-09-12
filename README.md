---
page_type: sample
languages:
- csharp
- java
- python
- typescript
- go
- ruby
products:
- azure
description: "This repository contains samples for Anomaly Detector API. The Anomaly Detector API enables you to monitor and find abnormalities in your time series data by automatically identifying and applying the correct statistical models, regardless of industry, scenario, or data volume."
urlFragment: AnomalyDetector
---

# Anomaly Detector API Samples

This repository contains samples for [Anomaly Detector API](https://aka.ms/anomalydetector). The Anomaly Detector API enables you to monitor and find abnormalities in your time series data by automatically identifying and applying the correct statistical models, regardless of industry, scenario, or data volume. Using your time series data, the API can find anomalies as a batch throughout your data, or determine if your latest data point is an anomaly.

## Contents

| File/folder | Description |
|-------------|-------------|
| `example-data`       | Example data to be sent to the API, along with example API responses.  |
| `ipython-notebook` | Jupyter notebooks. |
| `quickstarts`       | Sample code for the Anomaly Detector API quickstarts.  |

## Prerequisites

You must have an [Anomaly Detector API resource](https://aka.ms/adnew). Before continuing, you will need the API key and the endpoint from your Azure dashboard.
   ![Get Anomaly Detector access keys](./media/cognitive-services-get-access-keys.png "Get Anomaly Detector access keys")

Or you could create a 7-day free resource of Anomaly Detector from [here](https://azure.microsoft.com/en-us/try/cognitive-services/my-apis/).

If you want to run the notebook with an on-premise version of [Anomaly Detector as container](https://aka.ms/adcontainerdocs), there're four prerequisites that must be met:

1. You have access to the Azure Container Registry which hosts the Anomaly Detector container images. Please complete and submit the [Anomaly Detector Container Request form](https://aka.ms/adcontainer) to request access to the container.
1. You have created an Anomaly Detector resource on Azure.
1. You have the proper container environment ready to host the Anomaly Detector container. Please read [Prerequisites](https://docs.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/anomaly-detector-container-howto#prerequisites) and [The host computer](https://docs.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/anomaly-detector-container-howto#the-host-computer) for details.
1. You have [Jupyter Notebook](https://jupyter.org/install.html) installed on your computer. We recommend installing Python and Jupyter using the [Anaconda Distribution](https://www.anaconda.com/downloads).

After you pull the container image and spin it up, ensure there's an HTTP endpoint accessible to the APIs and this will be your **endpoint** for the demo.

## Online demo

To quickly start using the Anomaly Detector API, try an [online demo](https://notebooks.azure.com/AzureAnomalyDetection/projects/anomalydetector). This demo runs in a web-hosted Jupyter notebook and shows you how to send an API request, and visualize the result.

To run the demo, complete the following steps:
  
1. Sign in, and click **Clone**, in the upper right corner.
1. Uncheck the "public" option in the dialog box before completing the clone operation, otherwise your notebook, including any subscription keys, will be public.
1. Click **Run on free compute**
1. Open one of the notebooks, for example, Anomaly Finder API Example Private Preview (Batch Method).ipynb
1. Fill in the API key and the endpoint from your Anomaly Detector resource on Azure
1. In the Notebook main menu, click Cell->run all


## Container demo

To run the notebook with your Anomaly Detector container instance, complete the following steps:

1. Clone this project to your local directory
1. Start **Anaconda Prompt**
1. In the command line, change the working directory to your project directory using **cd**
1. Type **jupyter notebook** and run which opens http://localhost:8888/tree in a browser window
1. Open one of the notebooks under **ipython-notebook** folder
1. Fill in the API key (from your Anomaly Detector resource on Azure) and the endpoint (from your Anomaly Detector container instance)
1. In the Notebook main menu, click Cell->run all

## Data requirements

Example data is provided in this repository, along with example JSON responses from the API. To use the Anomaly Detector API on your time series data, ensure the following:

* Your data points are separated by the same interval, with no missing points.
* Your data has at least 13 data points if it doesn't have clear perodicity.
* Your data has at least 4 periods if it does have clear perodicity.
Please read [Best practices for using the Anomaly Detector API](https://aka.ms/adbest) for details.
