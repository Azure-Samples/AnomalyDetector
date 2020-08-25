// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// <imports>
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
// </imports>

public class JavaDetect {
    // <vars>
    // This sample assumes you have created an environment variable for your key and endpoint
    static final String subscriptionKey = System.getenv("ANOMALY_DETECTOR_KEY");
    static final String endpoint = System.getenv("ANOMALY_DETECTOR_ENDPOINT");

    // Replace the dataPath string with a path to the JSON formatted time series data.
    static final String dataPath = "[PATH_TO_TIME_SERIES_DATA]";

    // Urls for anomaly detection on:
    // A batch of data points, or
    // The latest data point in the time series
    static final String latestPointDetectionUrl = "/anomalydetector/v1.0/timeseries/last/detect";
    static final String batchDetectionUrl = "/anomalydetector/v1.0/timeseries/entire/detect";
    static final String changePointDetectionUrl = "/anomalydetector/v1.0/timeseries/changepoint/detect";
    // </vars>
    // <main>
    public static void main(String[] args) throws Exception {

        String requestData = new String(Files.readAllBytes(Paths.get(dataPath)), "utf-8");

        detectAnomaliesBatch(requestData);
        detectAnomaliesLatest(requestData);
        detectChangePoints(requestData);
    }
    // </main>
    // <detectBatch>
    static void detectAnomaliesBatch(String requestData) {
        System.out.println("Detecting anomalies as a batch");

        String result = sendRequest(batchDetectionUrl, endpoint, subscriptionKey, requestData);
        if (result != null) {
            System.out.println(result);

            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("code")) {
                System.out.println(String.format("Detection failed. ErrorCode:%s, ErrorMessage:%s", jsonObj.getString("code"), jsonObj.getString("message")));
            } else {
                JSONArray jsonArray = jsonObj.getJSONArray("isAnomaly");
                System.out.println("Anomalies found in the following data positions:");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    if (jsonArray.getBoolean(i))
                        System.out.print(i + ", ");
                }
                System.out.println();
            }
        }
    }
    // </detectBatch>
    // <detectLatest>
    static void detectAnomaliesLatest(String requestData) {
        System.out.println("Determining if latest data point is an anomaly");
        String result = sendRequest(latestPointDetectionUrl, endpoint, subscriptionKey, requestData);
        System.out.println(result);
    }
    // </detectLatest>
    // <detectChangePoint>
    static void detectChangePoints(String requestData) {
        System.out.println("Detecting change points");

        String result = sendRequest(changePointDetectionUrl, endpoint, subscriptionKey, requestData);
        if (result != null) {
            System.out.println(result);

            JSONObject jsonObj = new JSONObject(result);
            if (jsonObj.has("code")) {
                System.out.println(String.format("Detection failed. ErrorCode:%s, ErrorMessage:%s", jsonObj.getString("code"), jsonObj.getString("message")));
            } else {
                JSONArray jsonArray = jsonObj.getJSONArray("isChangePoint");
                System.out.println("Change points found in the following data positions:");
                for (int i = 0; i < jsonArray.length(); ++i) {
                    if (jsonArray.getBoolean(i))
                        System.out.print(i + ", ");
                }
                System.out.println();
            }
        }
    }
    // </detectChangePoint>
    // <request>
    static String sendRequest(String apiAddress, String endpoint, String subscriptionKey, String requestData) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(endpoint + apiAddress);
            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            request.setEntity(new StringEntity(requestData));
            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity respEntity = response.getEntity();
                if (respEntity != null) {
                    return EntityUtils.toString(respEntity, "utf-8");
                }
            } catch (Exception respEx) {
                respEx.printStackTrace();
            }
        } catch (IOException ex) {
            System.err.println("Exception on Anomaly Detector: " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
    // </request>
}
