package com.tecksupport.glfw.pathfinder.Route;

import com.tecksupport.schedulePlanner.Faculty;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RouteSummaryProcessor {


    private static final String API_URL = "https://api.openrouteservice.org/v2/directions/foot-walking";

    // Method to fetch route data from OpenRouteService API
    private static String fetchRouteData(double startLat, double startLon, double endLat, double endLon, String apiKey) {
        try {
            // Build the request URL with the given coordinates and API key
            String requestUrl = API_URL + "?api_key=" + apiKey + "&start=" + startLon + "," + startLat + "&end=" + endLon + "," + endLat;

            // Set up the connection
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString(); // Return the raw JSON response

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }

    // Method to process the fetched route data and return a RouteSummary
    public static RouteSummary processRouteData(Map<Integer, List<Faculty>> courseSchedule, String apiKey) {
        float totalDistance = 0;
        float totalTime = 0;
        Map<Integer, List<SegmentSummary>> segmentSummaryMap = new HashMap<>();

        // Example coordinates for start and end (replace with actual coordinates)
        double startLat = 33.131307;
        double startLon = -117.158195;
        double endLat = 33.130588;
        double endLon = -117.157846;

        // Fetch route data from the API
        String routeJson = fetchRouteData(startLat, startLon, endLat, endLon, apiKey);
        if (routeJson == null) {
            System.out.println("Error fetching route data.");
            return null; // Return null if route data is not fetched
        }

        // Parse the JSON response
        JSONObject routeResponse = new JSONObject(routeJson);
        JSONArray routes = routeResponse.getJSONArray("routes");

        // Iterate through the routes (normally you'd have multiple routes)
        for (int i = 0; i < routes.length(); i++) {
            JSONObject route = routes.getJSONObject(i);
            JSONArray segments = route.getJSONArray("segments");

            List<SegmentSummary> segmentSummaries = new ArrayList<>();

            // Process each segment in the route
            for (int j = 0; j < segments.length(); j++) {
                JSONObject segment = segments.getJSONObject(j);
                float distance = segment.getFloat("distance");
                float time = segment.getFloat("duration");

                // Create SegmentSummary and add to the list
                SegmentSummary segmentSummary = new SegmentSummary(distance, time);
                segmentSummaries.add(segmentSummary);

                // Accumulate total distance and time
                totalDistance += distance;
                totalTime += time;
            }

            // Add the segments for this route (grouped by index)
            segmentSummaryMap.put(i, segmentSummaries);
        }

        // Return the created RouteSummary object
        return new RouteSummary(totalDistance, totalTime, segmentSummaryMap);
    }

    // Test function
    public static void main(String[] args) {
        // Sample course schedule
        Map<Integer, List<Faculty>> courseSchedule = new HashMap<>();
        courseSchedule.put(1, Arrays.asList(new Faculty("CS370"), new Faculty("CS436")));
        courseSchedule.put(2, Arrays.asList(new Faculty("HIST131")));

        // API key for OpenRouteService (replace with your actual key)
        String apiKey = "YOUR_API_KEY";

        // Process the route data and get the summary
        RouteSummary routeSummary = processRouteData(courseSchedule, apiKey);

        if (routeSummary != null) {
            // Output the route summary
            System.out.println("Route Summary:");
            System.out.println("Total Distance: " + routeSummary.getTotalDistanceInMeter() + " meters");
            System.out.println("Total Time: " + routeSummary.getTotalTimeInSec() + " seconds");

            // Print out the segments for each day/course
            for (Map.Entry<Integer, List<SegmentSummary>> entry : routeSummary.getSegmentSummaryMap().entrySet()) {
                System.out.println("Route " + entry.getKey() + " Segments:");
                for (SegmentSummary segment : entry.getValue()) {
                    System.out.println("  - Distance: " + segment.getDistanceInMeter() + " meters, Time: " + segment.getTimeInSec() + " seconds");
                }
            }
        }
    }
}
