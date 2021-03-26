package com.microsoft.cognitiveservice.anomalydetection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Series {

    public static final String TIEMSTAMP = "timestamp";
    public static final String VALUE = "value";

    @JsonCreator
    public static Series create(@JsonProperty(TIEMSTAMP) String timestamp, @JsonProperty(VALUE) Float value) {
        return new com.microsoft.cognitiveservice.anomalydetection.AutoValue_Series(timestamp, value);
    }

    @JsonProperty(TIEMSTAMP)
    public abstract String timestamp();

    @JsonProperty(VALUE)
    public abstract Float value();

}
