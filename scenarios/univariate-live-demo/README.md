# Anomaly Detection Live Demo Instructions

Please note: this live demo is only intended to demonstration the anomaly detector API on any CSV file that follows a simple schema. This demo loops over the provided data (with fake timestamps) to demonstrate the anomaly detection API. It does not use the real timestamps provided in the raw data, and should not be used in any production scenario. Once the demo is running, you will be able to see the raw data, and the results from the anomaly detection API in the browser. You will also be able to change a few parameters, and interact with the data in real time. 

## Step 1

We recommend installing [VS Code](https://code.visualstudio.com/) locally to be able to run this demo if you are using Azure Machine Learning (AML) compute.

With VS Code installed, navigate to your AML resource in the Azure portal, and click on `Launch Studio`. On the left, click on the `Compute` tab. Finally, under the `Applications` column, click the `VS Code` link corresponding to the compute instance that you would like to use. When asked by VS Code to `Allow an extension to open this URI?` click `Open`.

## Step 2

Open a terminal in VS Code, and run the following command to create and activate a new conda virtual environment:

```bash
conda create --name anomaly-detection-demo python=3.7
source activate anomaly-detection-demo
pip install -r requirements.txt
```

After the environment is activated, make sure that you have populated the values for `ANOMALY_DETECTOR_API_KEY` and `ANOMALY_DETECTOR_ENDPOINT` in your #TODO

## Step 3

You must save your CSV files in the `data` directory for the demo to read from. For each file you want to run the demo on, you must also create a JSON configuration file in the same `data` directory.

The JSON configuration file specifies the following values:

```yaml
{
    "csv_name": "combined_emissions.csv", # name of the CSV file in the data/ directory
    "value_column": "methane_concentration", # name of the column in the CSV that stores the values you want to perform anomaly detection on
    "timestamp_column": "date_time_utc", # name of the column that stores the timestamps for each value
    "sensor_column": "sensor_id", # name of the column that stores the sensor or source name(s)
    "window_size": 20, # number of data points to send to the AD endpoint in every request
    "minute_resample": 5, # number of minutes to resample your data to. If your data has 5 minute granularity, and you would like a 15 minute granularity, set this value to 15. If you set this value to a higher granularity, the extra points will just be set to 0.
    "ad_mode": "entire" # anomaly detection mode you would like to use. Choose from 'entire' and 'last'
}
```

Once you have the configuration file set up. You must manually adjust the `config_path` parameter in `main.py` script in the `ad_live_demo` directory to point to the JSON config file you would like to use.

Finally, open the terminal in VSCode, and navigate to the parent directory of the folder `ad_live_demo`. Make sure you have the `anomaly_detection` virtual environment activated, then run the following command (it will not run inside a notebook):

```bash
bokeh serve --port 5599 --show demo
```

A browser tab should open, and the demo should start running. If the browser tab doesn't open, try to open it manually by navigating to `http://localhost:5599/ad_live_demo` in the browser. If the demo does not start running, please double-check that your data and your configuration file are properly set up, and that you are pointing to the right configuration file in `main.py`.