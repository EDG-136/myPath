package com.tecksupport.glfw.pathfinder.Route;

import java.util.*;

public class TestRoute {
    public static void main(String[] args) {
        // Initialize CoordinateProcessor with the building coordinates file path
        String buildingCoordinatesFile = "src/main/resources/BuildingCoordinates.txt";
        CoordinateProcessor coordinateProcessor = new CoordinateProcessor(buildingCoordinatesFile);

        // Example course schedule in the format:
        // "CS370" -> TuTh 08:00AM - 08:50AM SCI2 302, MW 11:30AM - 12:20PM UNIV 272

        Map<String, List<String>> weeklySchedule = new HashMap<>();
        weeklySchedule.put("Monday", Arrays.asList("SCI2"));
        weeklySchedule.put("Tuesday", Arrays.asList("SCI2", "UNIV"));
        weeklySchedule.put("Wednesday", Arrays.asList("UNIV"));
        weeklySchedule.put("Thursday", Arrays.asList("SCI2"));
        weeklySchedule.put("Friday", Arrays.asList("SCI2", "UNIV"));

        // Get the route summaries for the entire week
        RouteSummaryProcessor routeSummaryProcessor = new RouteSummaryProcessor();
        Map<String, String> weeklyRouteSummaries = routeSummaryProcessor.getRouteSummariesForWeek(weeklySchedule, coordinateProcessor);

        // Print out the route summaries for each day of the week
        for (Map.Entry<String, String> entry : weeklyRouteSummaries.entrySet()) {
            System.out.println("Route Summary for " + entry.getKey());
            System.out.println(entry.getValue());
            System.out.println("---------------------------------------------------");
        }
    }
}
