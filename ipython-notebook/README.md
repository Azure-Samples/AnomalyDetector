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
| `API Sample`       | Jupyter notebooks of how to use the Anomaly Detector APIs. |
| `SDK Sample` | Jupyter notebooks of how to use the Anomaly Detector SDKs. |
| `Media` | Images that used in the notebooks. |
| `README.md` | This README file. |
| `LICENSE`   | The license for the sample. |

## Prerequisites

- [Create an Anomaly Detector resource](https://ms.portal.azure.com/#create/Microsoft.CognitiveServicesAnomalyDetector) with access to the Anomaly Detector API. Before continuing, you will need the endpoint and key of this resource.
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
Need support? [Join the Anomaly Detector Community](https://forms.office.com/pages/responsepage.aspx?id=v4j5cvGGr0GRqy180BHbR2Ci-wb6-iNDoBoNxrnEk9VURjNXUU1VREpOT0U1UEdURkc0OVRLSkZBNC4u).
