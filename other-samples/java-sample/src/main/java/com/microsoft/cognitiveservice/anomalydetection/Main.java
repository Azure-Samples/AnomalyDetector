package com.microsoft.cognitiveservice.anomalydetection;

import com.azure.ai.anomalydetector.AnomalyDetectorAsyncClient;
import com.azure.ai.anomalydetector.AnomalyDetectorClientBuilder;
import com.azure.ai.anomalydetector.models.DetectRequest;
import com.azure.ai.anomalydetector.models.LastDetectResponse;
import com.azure.ai.anomalydetector.models.TimeGranularity;
import com.azure.ai.anomalydetector.models.TimeSeriesPoint;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.HttpHeaders;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.AddHeadersPolicy;
import com.azure.core.http.policy.AzureKeyCredentialPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Main {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    private static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";

    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    private static final String SUBSCRIPTION_KEY = "<YOUR-SUBSCRIPTION-KEY>";

    private static final String END_POINT = "<YOUR-ANOMALY-DETECTOR-END-POINT>";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new Jdk8Module());

    public static void main(String[] args) throws IOException {

        AnomalyDetectorAsyncClient asyncClient = new AnomalyDetectorClientBuilder()
                .endpoint(END_POINT)
                .pipeline(
                        new HttpPipelineBuilder().policies(
                                new AzureKeyCredentialPolicy(OCP_APIM_SUBSCRIPTION_KEY, new AzureKeyCredential(SUBSCRIPTION_KEY)),
                                new AddHeadersPolicy(new HttpHeaders().put(CONTENT_TYPE, APPLICATION_JSON))
                        ).build()
                ).buildAsyncClient();

        RequestData requestData = OBJECT_MAPPER.readValue(Main.class.getResource("/request-data.json"), RequestData.class);
        List<TimeSeriesPoint> timeSeriesPointList = new ArrayList<>();
        for (Series series : requestData.series()) {
            TimeSeriesPoint timeSeriesPoint = new TimeSeriesPoint()
                    .setTimestamp(OffsetDateTime.ofInstant(Instant.parse(series.timestamp()), ZoneOffset.UTC))
                    .setValue(series.value());
            timeSeriesPointList.add(timeSeriesPoint);
        }

        DetectRequest request = new DetectRequest()
                .setGranularity(TimeGranularity.fromString(requestData.granularity()))
                .setSeries(timeSeriesPointList);

        LastDetectResponse lastDetectResponse = asyncClient.detectLastPoint(request).block();
        System.out.println("**************************************************************");
        System.out.println(String.format("Expected value is : %s, Period is : %s, Lower margin is : %s, Upper margin : %s, Suggested window is : %s",
                lastDetectResponse.getExpectedValue(),
                lastDetectResponse.getPeriod(),
                lastDetectResponse.getLowerMargin(),
                lastDetectResponse.getUpperMargin(),
                lastDetectResponse.getSuggestedWindow()));
    }
}
