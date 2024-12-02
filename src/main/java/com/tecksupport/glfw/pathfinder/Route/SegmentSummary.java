package com.tecksupport.glfw.pathfinder.Route;

public class SegmentSummary {
    private final float distanceInMeter;
    private final float timeInSec;

    public SegmentSummary(float distanceInMeter, float timeInSec) {
        this.distanceInMeter = distanceInMeter;
        this.timeInSec = timeInSec;
    }

    public float getDistanceInMeter() {
        return distanceInMeter;
    }

    public float getTimeInSec(){
        return timeInSec;
    }
}
