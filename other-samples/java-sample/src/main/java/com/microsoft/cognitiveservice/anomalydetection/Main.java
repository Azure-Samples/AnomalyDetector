package com.microsoft.cognitiveservice.anomalydetection;

import com.azure.ai.anomalydetector.AnomalyDetectorClient;
import com.azure.ai.anomalydetector.AnomalyDetectorClientBuilder;
import com.azure.ai.anomalydetector.models.DetectRequest;
import com.azure.ai.anomalydetector.models.LastDetectResponse;
import com.azure.ai.anomalydetector.models.TimeGranularity;
import com.azure.ai.anomalydetector.models.TimeSeriesPoint;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.HttpPipeline;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.AzureKeyCredentialPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************
    private static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";

    // Replace the subscriptionKey string value with your valid subscription key.
    private static final String SUBSCRIPTION_KEY = "<YOUR-SUBSCRIPTION-KEY>";

    private static final String ENDPOINT = "https://<YOUR-ANOMALY-DETECTOR-ENDPOINT>";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        final RequestData requestData;
        try (InputStream resourceAsStream = Main.class.getResourceAsStream("/request-data.json")) {
            requestData = OBJECT_MAPPER.readValue(resourceAsStream, RequestData.class);
        }

        final AzureKeyCredentialPolicy credentialPolicy = new AzureKeyCredentialPolicy(OCP_APIM_SUBSCRIPTION_KEY,
                new AzureKeyCredential(SUBSCRIPTION_KEY));
        final HttpPipeline httpPipeline = new HttpPipelineBuilder()
                .policies(credentialPolicy)
                .build();

        // Create the synchronous client using the builder.
        final AnomalyDetectorClient asyncClient = new AnomalyDetectorClientBuilder()
                .endpoint(ENDPOINT)
                .pipeline(httpPipeline)
                .buildClient();

        final List<TimeSeriesPoint> timeSeriesPoints = requestData.getSeries()
                .stream()
                .map(series -> {
                    final OffsetDateTime timestamp = Instant.parse(series.getTimestamp()).atOffset(ZoneOffset.UTC);

                    return new TimeSeriesPoint()
                            .setTimestamp(timestamp)
                            .setValue(series.getValue());
                })
                .collect(Collectors.toList());

        final DetectRequest request = new DetectRequest()
                .setGranularity(TimeGranularity.fromString(requestData.getGranularity()))
                .setSeries(timeSeriesPoints);

        final LastDetectResponse lastDetectResponse = asyncClient.detectLastPoint(request);

        System.out.println("**************************************************************");
        System.out.printf("Expected value is: %.2f, Period is: %d, Lower margin is: %.2f, Upper margin: %.2f, "
                        + " Suggested window is: %d%n",
                lastDetectResponse.getExpectedValue(),
                lastDetectResponse.getPeriod(),
                lastDetectResponse.getLowerMargin(),
                lastDetectResponse.getUpperMargin(),
                lastDetectResponse.getSuggestedWindow());
    }
}