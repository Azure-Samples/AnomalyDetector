// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

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

public class JavaDetect {

    // Replace the subscriptionKey string value with your valid subscription key.
    static final String subscriptionKey = "[YOUR_SUBSCRIPTION_KEY]";

    //replace the endpoint URL with the correct one for your subscription. Your endpoint can be found in the Azure portal.
    //for example: https://westus2.api.cognitive.microsoft.com
    static final String endpoint = "[YOUR_ENDPOINT_URL]";

    // Replace the dataPath string with a path to the JSON formatted time series data.
    static final String dataPath = "[PATH_TO_TIME_SERIES_DATA]";

    // Urls for anomaly detection on:
    // A batch of data points, or
    // The latest data point in the time series
    static final String latestPointDetectionUrl = "/anomalydetector/v1.0/timeseries/last/detect";
    static final String batchDetectionUrl = "/anomalydetector/v1.0/timeseries/entire/detect";


    public static void main(String[] args) throws Exception {

        String requestData = new String(Files.readAllBytes(Paths.get(dataPath)), "UTF-8");

        detectAnomaliesBatch(requestData);
        detectAnomaliesLatest(requestData);
    }

    static void detectAnomaliesBatch(String requestData) {
        System.out.println("Detecting anomalies as a batch");

        String result = request(batchDetectionUrl, endpoint, subscriptionKey, requestData);
        if (result != null) {
            System.out.println(result);

            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("isAnomaly");
            System.out.println("Anomalies found in the following data positions:");
            for (int i = 0; i < jsonArray.length(); ++i) {
                if (jsonArray.getBoolean(i))
                    System.out.print(i + ", ");
            }
            System.out.println();
        }
    }

    static void detectAnomaliesLatest(String requestData) {
        System.out.println("Determining if latest data point is an anomaly");
        String result = request(latestPointDetectionUrl, endpoint, subscriptionKey, requestData);
        System.out.println(result);
    }

    static String request(String apiAddress, String endpoint, String subscriptionKey, String requestData) {
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
}