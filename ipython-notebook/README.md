---
topic: sample
languages:
  - python
products:
  - azure-cognitive-services
---

# Detect and visualize anomalies in your data with the Anomaly Detector API 

![License](https://img.shields.io/badge/license-MIT-green.svg)

These python notebooks show you how to start detecting anomalies in your data with the Anomaly Detector API, and visualizing the information returned by it. 

## Contents

| File/folder | Description |
|-------------|-------------|
| `src`       | Jupyter notebooks and source code. |
| `.gitignore` | Define what to ignore at commit time. |
| `README.md` | This README file. |
| `LICENSE`   | The license for the sample. |

## Prerequisites

- A [Cognitive Services API account](../articles/cognitive-services/cognitive-services-apis-create-account.md) with access to the Anomaly Detector API. 

If you don't have an Azure subscription, you can [create an account](https://azure.microsoft.com/try/cognitive-services/) for free. You can get your subscription key from the [Azure portal](../articles/cognitive-services/cognitive-services-apis-create-account.md#access-your-resource) after creating your account, or [Azure website](https://azure.microsoft.com/try/cognitive-services/my-apis) after activating a free trial.

## Running the sample

You can either run this sample locally as a Jupyter notebook, or deploy it to Azure as an Azure Notebook.

1. Make sure Jupyter Notebook is running.
2. Navigate to the Jupyter Notebooks for this sample, and click on one.
3. Add your valid Anomaly Detector API subscription key to the `subscription_key` variable.
4. Click **Kernel**, then **Restart & Run All** to run the notebook. 

## Deploy to Azure

<a href="https://notebooks.azure.com/AzureAnomalyDetection/projects/anomalydetector" target="_blank">
<img src="http://azuredeploy.net/deploybutton.png"/>
</a>

1. Sign in, and click Clone, in the upper right corner.
2. Click **Run on free compute**
3. Select one of the notebooks for this sample.
4. Add your valid Anomaly Detector API subscription key to the `subscription_key` variable.
5. On the top menu bar, click **Cell**, then **Run All**.

> Warning: Uncheck the "Public" option when you clone your instance or your code and data in the project is visible to everyone. You could also change the privacy settings in your clone.

## Key concepts

The Anomaly Detector API lets you monitor and detect abnormalities in your time series data without previous experience in machine learning. The API adapts by automatically identifying and applying the best fitting statistical models to your data, regardless of industry, scenario, or data volume. These python notebooks cover the following examples. 

|Example |Description  |
|---------|---------|
| Latest-point anomaly detection | Use previously seen data points to determine if the latest one in the data set is an anomaly. This example simulates using the Anomaly Detector API on streaming data by iterating over the data set and sending API requests at predetermined positions. By calling the API with each new data point you generate, you can monitor your data as it's created. |
|Batch anomaly detection  | Use a time series data set to detect any anomalies that might exist as a batch. This example sends example data sets in a single Anomaly Detector API request. |

## Next steps

For more information, see the [Anomaly Detector API documentation](https://aka.ms/anomaly-detector-documentation). 
