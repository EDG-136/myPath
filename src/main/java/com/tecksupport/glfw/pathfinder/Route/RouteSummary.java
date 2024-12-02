package com.tecksupport.glfw.pathfinder.Route;

import java.util.Map;
import java.util.List;

public class RouteSummary {

    private final float totalDistanceInMeter;
    private final float totalTimeInSec;
    private final Map<Integer, List<SegmentSummary>> segmentSummaryMap;

    public RouteSummary(float totalDistanceInMeter, float totalTimeInSec,
                        Map<Integer, List<SegmentSummary>> segmentSummaryMap) {
        this.totalDistanceInMeter = totalDistanceInMeter;
        this.totalTimeInSec = totalTimeInSec;
        this.segmentSummaryMap = segmentSummaryMap;
    }

    public float getTotalDistanceInMeter() {
        return totalDistanceInMeter;
    }

    public float getTotalTimeInSec() {
        return totalTimeInSec;
    }

    public Map<Integer, List<SegmentSummary>> getSegmentSummaryMap() {
        return segmentSummaryMap;
    }
}
