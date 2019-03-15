package com.microsoft.cognitiveservice.anomalydetection;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Main {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    public static final String subscriptionKey = "<Subscription Key>";

    // Choose which anomaly detection way you want to use and change the uriBase's second part
    public static final String rootUrl = "https://westus2.api.cognitive.microsoft.com/anomalydetector/v1.0";
    public static final String lastDetect = "/timeseries/last/detect";
    public static final String entireDetect = "/timeseries/entire/detect";
    public static final String uriBase = rootUrl + lastDetect;

    public static void main(String[] args) {
        final String content = "{\n" +
                "  \"Series\": [\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-01T00:00:00Z\",\n" +
                "      \"Value\": 32858923.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-02T00:00:00Z\",\n" +
                "      \"Value\": 29615278.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-03T00:00:00Z\",\n" +
                "      \"Value\": 22839355.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-04T00:00:00Z\",\n" +
                "      \"Value\": 25948736.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-05T00:00:00Z\",\n" +
                "      \"Value\": 34139159.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-06T00:00:00Z\",\n" +
                "      \"Value\": 33843985.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-07T00:00:00Z\",\n" +
                "      \"Value\": 33637661.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-08T00:00:00Z\",\n" +
                "      \"Value\": 32627350.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-09T00:00:00Z\",\n" +
                "      \"Value\": 29881076.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-10T00:00:00Z\",\n" +
                "      \"Value\": 22681575.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-11T00:00:00Z\",\n" +
                "      \"Value\": 24629393.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-12T00:00:00Z\",\n" +
                "      \"Value\": 34010679.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-13T00:00:00Z\",\n" +
                "      \"Value\": 33893888.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-14T00:00:00Z\",\n" +
                "      \"Value\": 33760076.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-15T00:00:00Z\",\n" +
                "      \"Value\": 33093515.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-16T00:00:00Z\",\n" +
                "      \"Value\": 29945555.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-17T00:00:00Z\",\n" +
                "      \"Value\": 22676212.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-18T00:00:00Z\",\n" +
                "      \"Value\": 25262514.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-19T00:00:00Z\",\n" +
                "      \"Value\": 33631649.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-20T00:00:00Z\",\n" +
                "      \"Value\": 34468310.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-21T00:00:00Z\",\n" +
                "      \"Value\": 34212281.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-22T00:00:00Z\",\n" +
                "      \"Value\": 38144434.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-23T00:00:00Z\",\n" +
                "      \"Value\": 34662949.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-24T00:00:00Z\",\n" +
                "      \"Value\": 24623684.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-25T00:00:00Z\",\n" +
                "      \"Value\": 26530491.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-26T00:00:00Z\",\n" +
                "      \"Value\": 35445003.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-27T00:00:00Z\",\n" +
                "      \"Value\": 34250789.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-28T00:00:00Z\",\n" +
                "      \"Value\": 33423012.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-29T00:00:00Z\",\n" +
                "      \"Value\": 30744783.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-30T00:00:00Z\",\n" +
                "      \"Value\": 25825128.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-03-31T00:00:00Z\",\n" +
                "      \"Value\": 21244209.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-01T00:00:00Z\",\n" +
                "      \"Value\": 22576956.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-02T00:00:00Z\",\n" +
                "      \"Value\": 31957221.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-03T00:00:00Z\",\n" +
                "      \"Value\": 33841228.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-04T00:00:00Z\",\n" +
                "      \"Value\": 33554483.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-05T00:00:00Z\",\n" +
                "      \"Value\": 32383350.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-06T00:00:00Z\",\n" +
                "      \"Value\": 29494850.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-07T00:00:00Z\",\n" +
                "      \"Value\": 22815534.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-08T00:00:00Z\",\n" +
                "      \"Value\": 25557267.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-09T00:00:00Z\",\n" +
                "      \"Value\": 34858252.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-10T00:00:00Z\",\n" +
                "      \"Value\": 34750597.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-11T00:00:00Z\",\n" +
                "      \"Value\": 34717956.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-12T00:00:00Z\",\n" +
                "      \"Value\": 34132534.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-13T00:00:00Z\",\n" +
                "      \"Value\": 30762236.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-14T00:00:00Z\",\n" +
                "      \"Value\": 22504059.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-15T00:00:00Z\",\n" +
                "      \"Value\": 26149060.0\n" +
                "    },\n" +
                "    {\n" +
                "      \"Timestamp\": \"2018-04-16T00:00:00Z\",\n" +
                "      \"Value\": 35250105.0\n" +
                "    }\n" +
                "  ],\n" +
                "  \"Granularity\": \"daily\"\n" +
                "}";


        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(uriBase);

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        try {
            StringEntity params = new StringEntity(content);
            request.setEntity(params);

            CloseableHttpResponse response = client.execute(request);
            try {
                HttpEntity respEntity = response.getEntity();
                if (respEntity != null) {
                    System.out.println("----------");
                    System.out.println(response.getStatusLine());
                    System.out.println("Response content is :\n");
                    System.out.println(EntityUtils.toString(respEntity, "utf-8"));
                    System.out.println("----------");
                }
            } catch (Exception respEx) {
                respEx.printStackTrace();
            } finally {
                response.close();
            }

        } catch (Exception ex) {
            System.err.println("Exception on Anomaly Detection: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
                System.err.println("Exception on closing HttpClient: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
