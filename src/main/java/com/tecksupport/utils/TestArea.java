package com.tecksupport.utils;

import com.tecksupport.glfw.pathfinder.OSM.ORSAPI;
import com.tecksupport.glfw.pathfinder.Route.RouteSummary;
import com.tecksupport.glfw.pathfinder.Route.SegmentSummary;
import com.tecksupport.schedulePlanner.EDayInWeek;
import com.tecksupport.schedulePlanner.Faculty;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestArea {
    @Test
    public void testTimeParser() {
        Faculty clinical = new Faculty("CLINICAL", "", "", 33.131307,-117.158195);
        Faculty sci2 = new Faculty("SCI2", "", "", 33.130588,-117.157846);
        Faculty sbsb = new Faculty("SBSB", "", "", 33.1308,-117.157013);
        Faculty arts = new Faculty("ARTS", "", "", 33.129876,-117.158198);
        Faculty kel = new Faculty("KEL", "", "", 33.129128,-117.159499);
        Faculty univ = new Faculty("UNIV", "", "", 33.128627,-117.158922);
        Faculty acd = new Faculty("ACD", "", "", 33.128008,-117.158635);
        Faculty mark = new Faculty("MARK", "", "", 33.12818,-117.157935);
        Faculty sci = new Faculty("SCI", "", "", 33.127835,-117.158656);
        Faculty vep = new Faculty("VEP", "", "", 33.127185,-117.157964);
        Faculty lotF = new Faculty("LotF", "", "", 33.12624,-117.156808);
        Faculty lotC = new Faculty("LotC", "", "", 33.126503,-117.161172);
        Faculty lotB = new Faculty("LotB", "", "", 33.126678,-117.163069);
        Faculty PSI = new Faculty("PSI", "", "", 33.127349,-117.160456);
        Faculty ELB = new Faculty("ELB", "", "", 33.127349,-117.160456);

        List<Faculty> monday = new ArrayList<>();
        monday.add(acd);
        monday.add(sci2);

        List<Faculty> tuesday = new ArrayList<>();
        tuesday.add(mark);
        tuesday.add(sci);
        tuesday.add(kel);
        tuesday.add(vep);
        tuesday.add(lotF);
        tuesday.add(PSI);
        tuesday.add(lotC);
        tuesday.add(lotB);
        tuesday.add(arts);
        tuesday.add(sbsb);
        tuesday.add(clinical);
        tuesday.add(univ);

        List<Faculty> wednesday = new ArrayList<>();

        List<Faculty> thursday = new ArrayList<>();
        thursday.add(ELB);

        List<Faculty> friday = new ArrayList<>();

        List<Faculty> saturday = new ArrayList<>();
        List<Faculty> sunday = new ArrayList<>();

        HashMap<EDayInWeek, List<Faculty>> facultyHashMap = new HashMap<>();
        facultyHashMap.put(EDayInWeek.MONDAY, monday);
        facultyHashMap.put(EDayInWeek.TUESDAY, tuesday);
        facultyHashMap.put(EDayInWeek.WEDNESDAY, wednesday);
        facultyHashMap.put(EDayInWeek.THURSDAY, thursday);
        facultyHashMap.put(EDayInWeek.FRIDAY, friday);
        facultyHashMap.put(EDayInWeek.SATURDAY, saturday);
        facultyHashMap.put(EDayInWeek.SUNDAY, sunday);

        RouteSummary routeSummary = ORSAPI.getRouteSummary(facultyHashMap);

        if (routeSummary == null)
            return;

        System.out.println("Total Distance: " + routeSummary.getTotalDistanceInMeter());
        System.out.println("Total Duration: " + routeSummary.getTotalTimeInSec());

        for (EDayInWeek dayInWeek : EDayInWeek.values()) {
            System.out.println("Day: " + dayInWeek.toString());
            List<SegmentSummary> segmentSummaries = routeSummary.getSegmentSummaryMap().get(dayInWeek);
            if (segmentSummaries == null)
                continue;
            for (SegmentSummary segmentSummary : segmentSummaries) {
                System.out.println("Distance: " + segmentSummary.getDistanceInMeter());
                System.out.println("Duration: " + segmentSummary.getTimeInSec());
            }
            System.out.println(String.format("%10s", "-").replace(' ', '-'));
        }
    }
}
