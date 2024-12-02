package com.tecksupport.glfw.pathfinder.OSM;

import com.tecksupport.schedulePlanner.Faculty;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ORSAPI {
    private static final int DAYS_IN_A_WEEK = 7;
    private static final String API_URL = "https://api.openrouteservice.org/v2/directions/foot-walking";
    private static final String API_KEY = System.getenv("ORS_API_KEY");
    private static final Logger LOGGER = Logger.getLogger(ORSAPI.class.getName());

    public static RouteSummary getRouteSummary(HashMap<Integer, List<Faculty>> facultyMap) {
        int totalCoordinate = 0;
        for (List<Faculty> facultyList : facultyMap.values()) {
            totalCoordinate += facultyList.size();
        }
        double[][] coordinates = new double[totalCoordinate][2];

        // Have to do this instead of map.values()
        // Because we need to keep days in order
        // In theory days should be in order, but it's just in case
        int currentIndex = 0;
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            List<Faculty> facultyList = facultyMap.get(i);
            for (Faculty faculty : facultyList) {
                coordinates[currentIndex][0] = faculty.getLatitude();
                coordinates[currentIndex][1] = faculty.getLongitude();
                currentIndex++;
            }
        }

        System.out.println("Total Coord: " + coordinates.length);

        JSONObject jsonResult = callORSApi(coordinates);
        if (jsonResult == null)
            return null;

        JSONArray routes = jsonResult.getJSONArray("routes");
        float totalDistance = 0;
        float totalDuration = 0;

        RouteSummary routeSummary = new RouteSummary();

        JSONArray segments = routes.getJSONObject(0).getJSONArray("segments");
        System.out.println(segments);
        currentIndex = 0;
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            List<Faculty> facultyList = facultyMap.get(i);
            for (int j = 0; j < facultyList.size() - 1; j++) {
                JSONObject segment = segments.getJSONObject(currentIndex);
                float segmentDistance = segment.getFloat("distance");
                float segmentDuration = segment.getFloat("duration");
                totalDistance += segmentDistance;
                totalDuration += segmentDuration;

                SegmentSummary segmentSummary = new SegmentSummary(segmentDistance, segmentDuration);
                routeSummary.addSegment(i, segmentSummary);
                currentIndex++;
            }
            currentIndex++;
        }

        routeSummary.setTotalDistanceInMeter(totalDistance);
        routeSummary.setTotalTimeInSec(totalDuration);

        return routeSummary;
    }

    private static JSONObject callORSApi(double[][] coordinates) {
        Client client = ClientBuilder.newClient();
        JSONObject payload = new JSONObject();
        payload.put("coordinates", coordinates);
        payload.put("preference", "shortest");

        try (Response response = client.target(API_URL)
                .request()
                .header("Authorization", API_KEY)
                .post(Entity.entity(payload.toString(), MediaType.APPLICATION_JSON))) {

            if (response.getStatus() == 200) {
                return new JSONObject(response.readEntity(String.class));
            } else {
                LOGGER.warning("API Error: " + response.getStatus() + " - " + response.readEntity(String.class));
                return null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception during API call", e);
            return null;
        } finally {
            client.close();
        }
    }
}
