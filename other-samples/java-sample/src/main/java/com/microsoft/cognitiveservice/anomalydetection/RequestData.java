package com.microsoft.cognitiveservice.anomalydetection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class RequestData {
    public static final String GRANULARITY = "granularity";
    public static final String SERIES = "series";

    @JsonCreator
    public static RequestData create(@JsonProperty(GRANULARITY) String granularity, @JsonProperty(SERIES) List<Series> series) {
        return new AutoValue_RequestData(granularity, series);
    }

    @JsonProperty(GRANULARITY)
    public abstract String granularity();

    @JsonProperty(SERIES)
    public abstract List<Series> series();

}
