package com.microsoft.cognitiveservice.anomalydetection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Series {

    public static final String TIMESTAMP = "timestamp";
    public static final String VALUE = "value";

    @JsonCreator
    public static Series create(@JsonProperty(TIMESTAMP) String timestamp, @JsonProperty(VALUE) Float value) {
        return new AutoValue_Series(timestamp, value);
    }

    @JsonProperty(TIMESTAMP)
    public abstract String timestamp();

    @JsonProperty(VALUE)
    public abstract Float value();

}
