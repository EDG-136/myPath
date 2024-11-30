package com.tecksupport.glfw.pathfinder.OSM;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class DistanceMatrixService {
    private static final String API_KEY = System.getenv("ORS_API_KEY");

    /**
     * Fetches the real-world distance and time between two render map nodes.
     *
     * @param node1 The first Node object.
     * @param node2 The second Node object.
     * @return A formatted string showing the distance and time.
     */
    public static String getDistanceAndTime(Node node1, Node node2) {
        try {
            String origin = node1.getX() + "," + node1.getY();
            String destination = node2.getX() + "," + node2.getY();

            String urlString = String.format(
                    "https://api.openrouteservice.org/v2/directions/foot-walking?api_key=%s&start=%s&end=%s",
                    API_KEY, origin, destination
            );

            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject segment = jsonResponse.getJSONArray("features")
                    .getJSONObject(0)
                    .getJSONObject("properties")
                    .getJSONArray("segments")
                    .getJSONObject(0);

            double distance = segment.getDouble("distance");
            double duration = segment.getDouble("duration");

            return String.format("Distance: %.2f meters, Time: %.2f seconds", distance, duration);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching distance and time";
        }
    }
}
