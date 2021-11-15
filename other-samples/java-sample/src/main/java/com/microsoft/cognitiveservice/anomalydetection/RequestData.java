package com.microsoft.cognitiveservice.anomalydetection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RequestData {
    public static final String GRANULARITY = "granularity";
    public static final String SERIES = "series";
    private final String granularity;
    private final List<Series> series;

    @JsonCreator
    public RequestData(@JsonProperty(GRANULARITY) String granularity, @JsonProperty(SERIES) List<Series> series) {
        this.granularity = granularity;
        this.series = series;
    }

    public String getGranularity() {
        return granularity;
    }

    public List<Series> getSeries() {
        return series;
    }
}