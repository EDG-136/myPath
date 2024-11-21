package com.tecksupport.Pathfinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class DistanceMatrixService {
    private static final String API_KEY = System.getenv("ORS_API_KEY");

    public static String getDistanceAndTime(String origin, String destination) {
        try {
            // Validate coordinates
            if (origin.equals("0.0,0.0") || destination.equals("0.0,0.0")) {
                return "Error: Invalid coordinates for origin or destination.";
            }

            // Construct request URL
            String urlString = String.format(
                    "https://api.openrouteservice.org/v2/directions/foot-walking?api_key=%s&start=%s&end=%s",
                    API_KEY, origin, destination
            );
            System.out.println("Request URL: " + urlString);

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print full response for debugging
            System.out.println("API Response: " + response.toString());

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Access the "features" array in the response
            if (!jsonResponse.has("features")) {
                return "Error: No 'features' field in the response.";
            }
            JSONArray features = jsonResponse.getJSONArray("features");
            if (features.length() == 0) {
                return "Error: 'features' array is empty.";
            }

            // Access the first feature
            JSONObject feature = features.getJSONObject(0);
            JSONObject properties = feature.getJSONObject("properties");

            // Access the "segments" array within "properties"
            if (!properties.has("segments")) {
                return "Error: No 'segments' field in the properties.";
            }
            JSONArray segments = properties.getJSONArray("segments");
            if (segments.length() == 0) {
                return "Error: 'segments' array is empty.";
            }

            // Access the first segment
            JSONObject firstSegment = segments.getJSONObject(0);

            // Check for "distance" and "duration" fields directly within the first segment
            double distance = firstSegment.optDouble("distance", -1); // in meters
            double duration = firstSegment.optDouble("duration", -1); // in seconds

            // Ensure both distance and duration are found; otherwise, return an error
            if (distance == -1 || duration == -1) {
                return "Error: 'distance' or 'duration' field not found in the segment.";
            }

            // Convert distance to miles and duration to minutes
            distance /= 1609.34;  // Convert meters to miles
            duration /= 60;       // Convert seconds to minutes

            return String.format("Distance: %.2f miles, Time: %.2f minutes", distance, duration);

        } catch (IOException e) {
            e.printStackTrace();
            return "Error retrieving data from OpenRouteService API.";
        } catch (org.json.JSONException e) {
            e.printStackTrace();
            return "Error: Unexpected response format from OpenRouteService API.";
        }
    }
}