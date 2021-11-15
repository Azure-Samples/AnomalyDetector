package com.microsoft.cognitiveservice.anomalydetection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Series {
    private final String timestamp;
    private final int value;

    @JsonCreator
    public Series(@JsonProperty("timestamp") String timestamp, @JsonProperty("value") int value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getValue() {
        return value;
    }
}
