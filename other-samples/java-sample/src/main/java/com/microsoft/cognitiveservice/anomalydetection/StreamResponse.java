package com.microsoft.cognitiveservice.anomalydetection;

public class StreamResponse {
    private float expectedValue;
    private boolean isAnomaly;
    private boolean isNegativeAnomaly;
    private boolean isPositiveAnomaly;
    private float lowerMargin;
    private float period;
    private float suggestedWindow;
    private float upperMargin;

    // Getter Methods

    public float getExpectedValue() {
        return expectedValue;
    }

    public boolean getIsAnomaly() {
        return isAnomaly;
    }

    public boolean getIsNegativeAnomaly() {
        return isNegativeAnomaly;
    }

    public boolean getIsPositiveAnomaly() {
        return isPositiveAnomaly;
    }

    public float getLowerMargin() {
        return lowerMargin;
    }

    public float getPeriod() {
        return period;
    }

    public float getSuggestedWindow() {
        return suggestedWindow;
    }

    public float getUpperMargin() {
        return upperMargin;
    }

    // Setter Methods

    public void setExpectedValue(float expectedValue) {
        this.expectedValue = expectedValue;
    }

    public void setIsAnomaly(boolean isAnomaly) {
        this.isAnomaly = isAnomaly;
    }

    public void setIsNegativeAnomaly(boolean isNegativeAnomaly) {
        this.isNegativeAnomaly = isNegativeAnomaly;
    }

    public void setIsPositiveAnomaly(boolean isPositiveAnomaly) {
        this.isPositiveAnomaly = isPositiveAnomaly;
    }

    public void setLowerMargin(float lowerMargin) {
        this.lowerMargin = lowerMargin;
    }

    public void setPeriod(float period) {
        this.period = period;
    }

    public void setSuggestedWindow(float suggestedWindow) {
        this.suggestedWindow = suggestedWindow;
    }

    public void setUpperMargin(float upperMargin) {
        this.upperMargin = upperMargin;
    }
}