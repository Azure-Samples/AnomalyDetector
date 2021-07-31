package com.microsoft.cognitiveservice.anomalydetection;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class StreamMain{

    private static final String subscriptionKey = "xxxxx";

    // Choose which anomaly detection way you want to use and change the uriBase's second part
    private static final String rootUrl = "http://localhost:5000/anomalydetector/v1.0";
    private static final String lastDetect = "/timeseries/last/detect";
    private static final String uriBase = rootUrl + lastDetect;
    
    public static void main(String[] args) throws Exception{
        String resourceName = "/request-data.json";
        String rawRequest = IOUtils.toString(StreamMain.class.getResourceAsStream(resourceName),StandardCharsets.UTF_8);
        AnomalyDetectorRequest anomalyDetectorRequest = new Gson().fromJson(rawRequest, AnomalyDetectorRequest.class);
        Collection<Point> pointCollection = anomalyDetectorRequest.getSeries();
        int i = 0;
        AnomalyDetectorRequest request = new AnomalyDetectorRequest(new ArrayList<Point>(), anomalyDetectorRequest.getGranularity());
        for(Point p : pointCollection){
            
            request.getSeries().add(p);
            if(++i == 12){
                doRequest(request);
                request = new AnomalyDetectorRequest(new ArrayList<Point>(), anomalyDetectorRequest.getGranularity());
                i = 0;
            }

        }
    }

    private static void doRequest(AnomalyDetectorRequest anomalyDetectorRequest){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(uriBase);

        // Request headers.
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

        try {
            StringEntity params = new StringEntity(new Gson().toJson(anomalyDetectorRequest));
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