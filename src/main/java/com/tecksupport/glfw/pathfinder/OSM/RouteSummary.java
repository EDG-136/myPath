package com.tecksupport.glfw.pathfinder.OSM;

import com.tecksupport.schedulePlanner.EDayInWeek;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteSummary {
    private float totalDistanceInMeter;
    private float totalTimeInSec;
    private final HashMap<EDayInWeek, List<SegmentSummary>> segmentSummaryMap = new HashMap<>();

    public void addSegment(EDayInWeek day, SegmentSummary segmentSummary) {
        List<SegmentSummary> summaries = segmentSummaryMap.computeIfAbsent(day, k -> new ArrayList<>());

        summaries.add(segmentSummary);
    }

    public float getTotalDistanceInMeter() {
        return totalDistanceInMeter;
    }

    public float getTotalTimeInSec() {
        return totalTimeInSec;
    }

    public HashMap<EDayInWeek, List<SegmentSummary>> getSegmentSummaryMap() {
        return segmentSummaryMap;
    }

    public void setTotalDistanceInMeter(float totalDistanceInMeter) {
        this.totalDistanceInMeter = totalDistanceInMeter;
    }

    public void setTotalTimeInSec(float totalTimeInSec) {
        this.totalTimeInSec = totalTimeInSec;
    }

    public float getLongestDistance() {
        float longest = Float.MIN_VALUE;
        for (List<SegmentSummary> segmentSummaryList : segmentSummaryMap.values()) {
            for (SegmentSummary segmentSummary : segmentSummaryList) {
                if (segmentSummary.getDistanceInMeter() > longest)
                    longest = segmentSummary.getDistanceInMeter();
            }
        }
        return longest == Float.MIN_VALUE ? 0 : longest;
    }

    public float getAverageDistance() {
        float total = 0;
        float segmentCounter = 0;
        for (List<SegmentSummary> segmentSummaryList : segmentSummaryMap.values()) {
            for (SegmentSummary segmentSummary : segmentSummaryList) {
                total += segmentSummary.getDistanceInMeter();
                segmentCounter++;
            }
        }

        return segmentCounter == 0 ? 0 : total / segmentCounter;
    }

    public float getShortestDistance() {
        float shortest = Float.MAX_VALUE;
        for (List<SegmentSummary> segmentSummaryList : segmentSummaryMap.values()) {
            for (SegmentSummary segmentSummary : segmentSummaryList) {
                if (segmentSummary.getDistanceInMeter() < shortest)
                    shortest = segmentSummary.getDistanceInMeter();
            }
        }
        return shortest == Float.MAX_VALUE ? 0 : shortest;
    }
}

