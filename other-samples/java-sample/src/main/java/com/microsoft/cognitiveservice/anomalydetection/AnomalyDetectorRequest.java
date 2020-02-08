package com.microsoft.cognitiveservice.anomalydetection;

import java.util.Collection;

public class AnomalyDetectorRequest{
    public AnomalyDetectorRequest(Collection<Point> points,String granularity){
        this.series = points;
        this.granularity = granularity;
    }
    private String granularity;

    private Collection<Point> series;

    public void setGranularity(String granularity){
        this.granularity = granularity;
    }
    public String getGranularity(){
        return this.granularity;
    }
    public void setSeries(Collection<Point> series){
        this.series = series;
    }
    public Collection<Point> getSeries(){
        return this.series;
    }
}