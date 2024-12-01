package com.tecksupport.glfw.pathfinder.Route;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class RouteProcessor {

    /**
     * Parses a JSON file for a specific day and extracts distance and time for routes.
     *
     * @param filePath Path to the JSON file.
     * @param day      The day of the week the JSON file corresponds to.
     * @param routesByDay A map to store routes by day.
     */
    public static void parseRoutesForDay(String filePath, DaysInWeek day, Map<DaysInWeek, List<Route>> routesByDay) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            // Parse JSON file
            JSONObject json = new JSONObject(jsonString.toString());
            JSONArray routesArray = json.getJSONArray("routes");

            List<Route> dailyRoutes = routesByDay.get(day);

            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject route = routesArray.getJSONObject(i);
                JSONArray segments = route.getJSONArray("segments");

                for (int j = 0; j < segments.length(); j++) {
                    JSONObject segment = segments.getJSONObject(j);

                    // Extract segment distance and duration
                    double distance = segment.getDouble("distance");
                    double duration = segment.getDouble("duration");

                    dailyRoutes.add(new Route(distance, duration));
                }
            }
        } catch (Exception e) {
            System.err.printf("Error processing file for %s: %s%n", day, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays the distance and time for each route, grouped by days.
     *
     * @param routesByDay A map of DaysInWeek to lists of Route objects.
     */
    public static void displayRoutes(Map<DaysInWeek, List<Route>> routesByDay) {
        for (DaysInWeek day : DaysInWeek.values()) {
            System.out.println(day + ":");
            List<Route> routes = routesByDay.get(day);
            if (routes != null && !routes.isEmpty()) {
                double totalDistance = 0;
                double totalDuration = 0;

                for (Route route : routes) {
                    System.out.println(route);
                    totalDistance += route.getDistance();
                    totalDuration += route.getTime();
                }

                System.out.printf("Total Distance = %.2f meters, Total Duration = %.2f seconds%n%n", totalDistance, totalDuration);
            } else {
                System.out.println("No routes for this day.\n");
            }
        }
    }

    public static void main(String[] args) {
        // Directory containing JSON files
        String directoryPath = "routes";

        // Initialize a map to hold routes for all days
        Map<DaysInWeek, List<Route>> routesByDay = new EnumMap<>(DaysInWeek.class);

        // Initialize the map with empty lists for each day
        for (DaysInWeek day : DaysInWeek.values()) {
            routesByDay.put(day, new ArrayList<>());
        }

        // Read each file in the directory
        for (DaysInWeek day : DaysInWeek.values()) {
            String filePath = directoryPath + "/" + day.toString().toLowerCase() + ".json";
            parseRoutesForDay(filePath, day, routesByDay);
        }

        // Display the routes
        displayRoutes(routesByDay);
    }


}
