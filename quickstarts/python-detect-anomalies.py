# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.

import requests
import json

# URLs for anomaly detection with the Anomaly Detector API
batch_detection_url = "/anomalydetector/v1.0/timeseries/entire/detect"
latest_point_detection_url = "/anomalydetector/v1.0/timeseries/last/detect"

###############################################
#### Update or verify the following values. ###
###############################################

# Replace the endpoint URL with the correct one for your subscription. Your endpoint can be found in the Azure portal.
# for example: https://westus2.api.cognitive.microsoft.com
endpoint = "[YOUR_ENDPOINT_URL]"

# Replace with your valid subscription key.
subscription_key = "[YOUR_SUBSCRIPTION_KEY]"

# Replace with a path to the JSON formatted time series data.
data_location = "[PATH_TO_TIME_SERIES_DATA]"

###############################################

"""
Sends an anomaly detection request to the Anomaly Detector API. 
If the request is successful, the JSON response is returned.
"""
def send_request(endpoint, url, subscription_key, request_data):
    headers = {'Content-Type': 'application/json', 'Ocp-Apim-Subscription-Key': subscription_key}
    response = requests.post(endpoint+url, data=json.dumps(request_data), headers=headers)
    if response.status_code == 200:
        return json.loads(response.content.decode("utf-8"))
    else:
        print(response.status_code)
        raise Exception(response.text)

"""
Detect anomalies throughout the time series data by submitting it as a batch to the API.
"""
def detect_batch(request_data):
    print("Detecting anomalies as a batch")
    # Send the request, and print the JSON result
    result = send_request(endpoint, batch_detection_url, subscription_key, request_data)
    print(json.dumps(result, indent=4))

    # Find and display the positions of anomalies in the data set
    anomalies = result["isAnomaly"]
    print("Anomalies detected in the following data positions:")

    for x in range(len(anomalies)):
        if anomalies[x] == True:
            print (x)

"""
Detect if the latest data point in the time series is an anomaly.
"""
def detect_latest(request_data):
    print("Determining if latest data point is an anomaly")
    # send the request, and print the JSON result
    result = send_request(endpoint, latest_point_detection_url, subscription_key, request_data)
    print(json.dumps(result, indent=4))


# read json time series data from file
file_handler = open (data_location)
json_data = json.load(file_handler)

# send the request
detect_batch(json_data)
detect_latest(json_data)
