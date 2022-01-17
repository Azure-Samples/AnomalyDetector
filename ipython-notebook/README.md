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

- A [Cognitive Services API account](https://docs.microsoft.com/azure/cognitive-services/cognitive-services-apis-create-account) with access to the Anomaly Detector API. Before continuing, You will need the access key from your Azure dashboard.
## Running the sample

You can run this sample locally as a Jupyter notebook.

1. Make sure Jupyter Notebook is running.
2. Navigate to the Jupyter Notebooks for this sample, and click on one.
3. Add your valid Anomaly Detector API subscription key to the `subscription_key` variable.
4. Click **Kernel**, then **Restart & Run All** to run the notebook. 

## Key concepts

The Anomaly Detector API lets you monitor and detect abnormalities in your time series data without previous experience in machine learning. The API adapts by automatically identifying and applying the best fitting statistical models to your data, regardless of industry, scenario, or data volume. These python notebooks cover the following examples. 

|Example |Description  |
|---------|---------|
| Latest-point anomaly detection | Use previously seen data points to determine if the latest one in the data set is an anomaly. This example simulates using the Anomaly Detector API on streaming data by iterating over the data set and sending API requests at predetermined positions. By calling the API with each new data point you generate, you can monitor your data as it's created. |
|Batch anomaly detection  | Use a time series data set to detect any anomalies that might exist as a batch. This example sends example data sets in a single Anomaly Detector API request. |

## Next steps

For more information, see the [Anomaly Detector API documentation](https://aka.ms/anomaly-detector-documentation). 
