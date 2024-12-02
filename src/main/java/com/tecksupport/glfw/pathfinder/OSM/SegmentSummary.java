package com.tecksupport.glfw.pathfinder.OSM;

public class SegmentSummary {
    private final float distanceInMeter;
    private final float timeInMeter;

    public SegmentSummary(float distanceInMeter, float timeInMeter) {
        this.distanceInMeter = distanceInMeter;
        this.timeInMeter = timeInMeter;
    }

    public float getDistanceInMeter() {
        return distanceInMeter;
    }

    public float getTimeInMeter() {
        return timeInMeter;
    }
}
