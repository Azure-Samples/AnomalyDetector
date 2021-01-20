package com.microsoft.cognitiveservice.anomalydetection;

import com.azure.ai.anomalydetector.AnomalyDetectorAsyncClient;
import com.azure.ai.anomalydetector.AnomalyDetectorClientBuilder;
import com.azure.ai.anomalydetector.models.*;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.HttpHeaders;
import com.azure.core.http.HttpPipeline;
import com.azure.core.http.HttpPipelineBuilder;
import com.azure.core.http.policy.AddHeadersPolicy;
import com.azure.core.http.policy.AzureKeyCredentialPolicy;
import com.azure.core.http.policy.HttpPipelinePolicy;
import com.azure.core.http.rest.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
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
    private static final String subscriptionKey = "<Subscription Key>";

    private static final String END_POINT = "<Anomaly Detector End Point>";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new Jdk8Module());

    private static final HttpPipelinePolicy authPolicy =
            new AzureKeyCredentialPolicy("Ocp-Apim-Subscription-Key",
                    new AzureKeyCredential(subscriptionKey));

    private static final AddHeadersPolicy addHeadersPolicy =
            new AddHeadersPolicy(new HttpHeaders().put("Content-Type", "application/json"));

    private static final HttpPipeline httpPipeline =
            new HttpPipelineBuilder().policies(authPolicy, addHeadersPolicy).build();

    public static void main(String[] args) throws IOException {

        AnomalyDetectorAsyncClient asyncClient = new AnomalyDetectorClientBuilder()
                .endpoint(END_POINT).pipeline(httpPipeline).buildAsyncClient();

        RequestData requestData = OBJECT_MAPPER.readValue(Main.class.getResource("/request-data.json"), RequestData.class);
        List<TimeSeriesPoint> timeSeriesPointList = new ArrayList<TimeSeriesPoint>();
        for (Series series : requestData.series()) {
            TimeSeriesPoint timeSeriesPoint = new TimeSeriesPoint()
                    .setTimestamp(OffsetDateTime.ofInstant(Instant.parse(series.timestamp()), ZoneOffset.UTC))
                    .setValue(series.value().floatValue());
            timeSeriesPointList.add(timeSeriesPoint);
        }

        DetectRequest request = new DetectRequest()
                .setGranularity(TimeGranularity.fromString(requestData.granularity()))
                .setSeries(timeSeriesPointList);

        asyncClient.detectLastPointWithResponse(request).subscribe(response -> {
            System.out.println("Request detail information...................................");
            System.out.println("**************************************************************");
            System.out.println(String.format("Request url is : %s", response.getRequest().getUrl().toString()));
            response.getRequest().getHeaders().forEach(httpHeader -> {
                System.out.println("**************************************************************");
                System.out.println(String.format("Request httpHeader name is : %s", httpHeader.getName()));
                System.out.println(String.format("Request httpHeader Value is : %s", httpHeader.getValue()));
            });
            response.getRequest().getBody().subscribe(byteBuffer -> {
                System.out.println("**************************************************************");
                Charset charset = Charset.forName("UTF-8");
                CharsetDecoder decoder = charset.newDecoder();
                try {
                    CharBuffer charBuffer = decoder.decode(byteBuffer.asReadOnlyBuffer());
                    System.out.println(String.format("Request body is : %s", charBuffer.toString()));
                } catch (CharacterCodingException e) {
                    e.printStackTrace();
                }
            });
            System.out.println("Response detail information...................................");
            System.out.println("**************************************************************");
            System.out.println(String.format("Status code is : %s", response.getStatusCode()));
            response.getHeaders().stream().forEach(httpHeader -> {
                System.out.println("**************************************************************");
                System.out.println(String.format("Response httpHeader name is : %s", httpHeader.getName()));
                System.out.println(String.format("Response httpHeader Value is : %s", httpHeader.getValue()));
            });
            System.out.println("**************************************************************");
            System.out.println(String.format("Expected value is : %s, Period is : %s, Lower margin is : %s, Upper margin : %s, Suggested window is : %s",
                    response.getValue().getExpectedValue(),
                    response.getValue().getPeriod(),
                    response.getValue().getLowerMargin(),
                    response.getValue().getUpperMargin(),
                    response.getValue().getSuggestedWindow()));
        });
    }
}
