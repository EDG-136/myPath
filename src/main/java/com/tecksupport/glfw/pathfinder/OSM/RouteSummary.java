package com.tecksupport.glfw.pathfinder.OSM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteSummary {
    private float totalDistanceInMeter;
    private float totalTimeInSec;
    private final HashMap<Integer, List<SegmentSummary>> segmentSummaryMap = new HashMap<>();

    public void addSegment(int day, SegmentSummary segmentSummary) {
        List<SegmentSummary> summaries = segmentSummaryMap.computeIfAbsent(day, k -> new ArrayList<>());

        summaries.add(segmentSummary);
    }

    public float getTotalDistanceInMeter() {
        return totalDistanceInMeter;
    }

    public float getTotalTimeInSec() {
        return totalTimeInSec;
    }

    public HashMap<Integer, List<SegmentSummary>> getSegmentSummaryMap() {
        return segmentSummaryMap;
    }

    public void setTotalDistanceInMeter(float totalDistanceInMeter) {
        this.totalDistanceInMeter = totalDistanceInMeter;
    }

    public void setTotalTimeInSec(float totalTimeInSec) {
        this.totalTimeInSec = totalTimeInSec;
    }
}

