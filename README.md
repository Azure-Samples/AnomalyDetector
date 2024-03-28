# Anomaly Detector Samples

This repository contains API samples and SDK samples for [Anomaly Detector service](https://aka.ms/anomalydetector). Anomaly Detector enables you to monitor and find abnormalities in your time series data by automatically identifying and applying the correct statistical models, regardless of industry, scenario, or data volume.

## What's new?

### March 2024: Anomaly Detector Now Available on PYPI 🎉

In March 2024, we proudly announce the release of the Anomaly Detector package on PYPI! 

While the existing Anomaly Detector as a service will be deprecated by 2026, you can now seamlessly utilize the new package directly on your local machine. No need to create an Azure Anomaly Detector resource—simply install the package and start detecting anomalies right away.

For the latest details and usage instructions, refer to our Python notebook available here: [anomaly-detector-pypi-demo.ipynb](anomaly-detector-pypi-demo.ipynb)

## 👋About Anomaly Detector
[Anomaly Detector](https://learn.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/overview) is an AI service with a set of APIs, which enables you to monitor and detect anomalies in your time series data with little ML knowledge, either batch validation or real-time inference.

[Univariate Anomaly Detection API](https://learn.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/how-to/identify-anomalies) enables you to monitor and detect abnormalities in your single variable without having to know machine learning. The Anomaly Detector API's algorithms adapt by automatically identifying and applying the best-fitting models to your data, regardless of industry, scenario, or data volume. Using your time series data, the API determines boundaries for anomaly detection, expected values, and which data points are anomalies.

[Multivariate anomaly detection API](https://learn.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/how-to/create-resource) further enable developers by easily integrating advanced AI for detecting anomalies from groups of metrics, without the need for machine learning knowledge or labeled data. Dependencies and inter-correlations between up to 300 different signals are now automatically counted as key factors. This new capability helps you to proactively protect your complex systems such as software applications, servers, factory machines, spacecraft, or even your business, from failures.

## Prerequisites

You must have an [Anomaly Detector API resource](https://aka.ms/adnew). Before continuing, you will need the API **key** and the **endpoint** from your Azure dashboard.
   ![Get Anomaly Detector access keys](./media/cognitive-services-get-access-keys.png "Get Anomaly Detector access keys")

Or you could create a 7-day free resource of Anomaly Detector from [here](https://azure.microsoft.com/en-us/try/cognitive-services/my-apis/).

## Content

This repository is organized in the following structure, we recommend you go to `demo-notebook` first to try the simple samples if you are a fan of Python. 🤗

| Folder | Description |
|-------------|-------------|
| 🆕[ipython-notebook](/ipython-notebook/)      | [API](/ipython-notebook/API%20Sample/) and [SDK](/ipython-notebook/SDK%20Sample/) sample codes written in python notebook for UVAD adn MVAD. The latest update will start from here first. 😉 |
| [sampledata](/sampledata/) | All the sample datasets that are used in this repository. |
| [sample-multivariate](/samples-multivariate/)       | Sample SDK codes for MVAD(preview version) using 4 languages.  |
| [sample-univariate](/samples-univariate/)       | Sample API and SDK codes for UVAD using 4 languages. |
| [univariate-live-demo](/univariate-live-demo/)| This includes a live demo that you could clone directly and ran on your data or make any modifications.  |
| [postman-demo](/postman-demo/)  |  This includes the tutorial of using postman to trigger Anomaly Detector, which could help better understand from API perspective.  |

## 🔗Important links

### 1️⃣Microsoft Learn - Anomaly Detector

- Learning module: [Identify abnormal time-series data with Anomaly Detector](https://learn.microsoft.com/en-us/training/modules/identify-abnormal-time-series-data-anomaly-detector/?WT.mc_id=data-12171-ruyakubu)

### 2️⃣API/SDK Sample

- [Anomaly Detector Sample](https://github.com/Azure-Samples/AnomalyDetector)
- [Anomaly Detector Sample in python notebook](https://github.com/Azure-Samples/AnomalyDetector/tree/master/ipython-notebook)

### 3️⃣Anomaly Detector in Synapse

- [Tutorial: Use Multivariate Anomaly Detector in Azure Synapse Analytics](https://learn.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/tutorials/multivariate-anomaly-detection-synapse)
- [Sample notebook for MVAD in Synapse](https://github.com/jr-MS/MVAD-in-Synapse)

### 4️⃣Anomaly Detector in Azure Databricks

- [Blog: Detect Anomalies in Equipment with Anomaly Detector in Azure Databricks](https://techcommunity.microsoft.com/t5/ai-cognitive-services-blog/detect-anomalies-in-equipment-with-anomaly-detector-in-azure/ba-p/3390688)

### 5️⃣Anomaly Detector in Azure Data Explorer

- [Blog: Announcing Univariate Anomaly Detector in Azure Data Explorer](https://techcommunity.microsoft.com/t5/ai-applied-ai-blog/announcing-univariate-anomaly-detector-in-azure-data-explorer/ba-p/3285400)
- [Documentation about anomaly detection function in ADX](https://learn.microsoft.com/en-us/azure/data-explorer/kusto/functions-library/series-uv-anomalies-fl?tabs=adhoc)

### 6️⃣Anomaly Detector PowerBI

- [Anomaly Detection in PowerBI - UI](https://learn.microsoft.com/en-us/power-bi/visuals/power-bi-visualization-anomaly-detection)
- [Anomaly Detection in PowerBI - PowerQuery](https://learn.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/tutorials/batch-anomaly-detection-powerbi)

## Container demo

(Only support UVAD)

If you want to run the notebook with an on-premise UVAD version of [Anomaly Detector as container](https://aka.ms/adcontainerdocs), there're four prerequisites that must be met:

1. You have access to the Azure Container Registry which hosts the Anomaly Detector container images. Please complete and submit the [Anomaly Detector Container Request form](https://aka.ms/adcontainer) to request access to the container.
1. You have created an Anomaly Detector resource on Azure.
1. You have the proper container environment ready to host the Anomaly Detector container. Please read [Prerequisites](https://docs.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/anomaly-detector-container-howto#prerequisites) and [The host computer](https://docs.microsoft.com/en-us/azure/cognitive-services/anomaly-detector/anomaly-detector-container-howto#the-host-computer) for details.
1. You have [Jupyter Notebook](https://jupyter.org/install.html) installed on your computer. We recommend installing Python and Jupyter using the [Anaconda Distribution](https://www.anaconda.com/downloads).

After you pull the container image and spin it up, ensure there's an HTTP endpoint accessible to the APIs and this will be your **endpoint** for the demo.
To run the notebook with your Anomaly Detector container instance, complete the following steps:

1. Clone this project to your local directory
1. Start **Anaconda Prompt**
1. In the command line, change the working directory to your project directory using **cd**
1. Type **jupyter notebook** and run which opens http://localhost:8888/tree in a browser window
1. Open one of the notebooks under **ipython-notebook** folder
1. Fill in the API key (from your Anomaly Detector resource on Azure) and the endpoint (from your Anomaly Detector container instance)
1. In the Notebook main menu, click Cell->run all


## ❤️Support
Need support? [Join the Anomaly Detector Community](https://forms.office.com/pages/responsepage.aspx?id=v4j5cvGGr0GRqy180BHbR2Ci-wb6-iNDoBoNxrnEk9VURjNXUU1VREpOT0U1UEdURkc0OVRLSkZBNC4u).
