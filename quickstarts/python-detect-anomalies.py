# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.
# <imports>
import requests
import json
# </imports>
# <vars>
# URLs for anomaly detection with the Anomaly Detector API
batch_detection_url = "/anomalydetector/v1.0/timeseries/entire/detect"
latest_point_detection_url = "/anomalydetector/v1.0/timeseries/last/detect"

# This sample assumes you have created an environment variable for your key and endpoint
endpoint = os.environ["ANOMALY_DETECTOR_ENDPOINT"]
subscription_key = os.environ["ANOMALY_DETECTOR_KEY"]

# Replace with a path to the JSON formatted time series data.
data_location = "[PATH_TO_TIME_SERIES_DATA]"
# </vars>

"""
Sends an anomaly detection request to the Anomaly Detector API. 
If the request is successful, the JSON response is returned.
"""
# <request>
def send_request(endpoint, url, subscription_key, request_data):
    headers = {'Content-Type': 'application/json', 'Ocp-Apim-Subscription-Key': subscription_key}
    response = requests.post(endpoint+url, data=json.dumps(request_data), headers=headers)
    return json.loads(response.content.decode("utf-8"))
# </request>
"""
Detect anomalies throughout the time series data by submitting it as a batch to the API.
"""
# <detectBatch>
def detect_batch(request_data):
    print("Detecting anomalies as a batch")
    # Send the request, and print the JSON result
    result = send_request(endpoint, batch_detection_url, subscription_key, request_data)
    print(json.dumps(result, indent=4))

    if result.get('code') != None:
        print("Detection failed. ErrorCode:{}, ErrorMessage:{}".format(result['code'], result['message']))
    else:
        # Find and display the positions of anomalies in the data set
        anomalies = result["isAnomaly"]
        print("Anomalies detected in the following data positions:")

        for x in range(len(anomalies)):
            if anomalies[x] == True:
                print (x)
# </detectBatch>
"""
Detect if the latest data point in the time series is an anomaly.
"""
# <detectLatest>
def detect_latest(request_data):
    print("Determining if latest data point is an anomaly")
    # send the request, and print the JSON result
    result = send_request(endpoint, latest_point_detection_url, subscription_key, request_data)
    print(json.dumps(result, indent=4))
# </detectLatest>

# read json time series data from file
# <fileLoad>
file_handler = open(data_location)
json_data = json.load(file_handler)
detect_batch(json_data)
detect_latest(json_data)
# </fileLoad>

