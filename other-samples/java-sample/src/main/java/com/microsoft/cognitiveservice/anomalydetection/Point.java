package com.microsoft.cognitiveservice.anomalydetection;

import org.apache.commons.lang3.StringUtils;

public class Point implements Comparable<Point> {
    private String timestamp;

    private long value;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return this.value;
    }

    @Override
    public int compareTo(Point o) {
        if (o == null || StringUtils.isEmpty(o.getTimestamp()))
            return 1;
        else if (StringUtils.isEmpty(this.getTimestamp()))
        return -1;

        return this.getTimestamp().compareTo(o.timestamp);
    }
}
