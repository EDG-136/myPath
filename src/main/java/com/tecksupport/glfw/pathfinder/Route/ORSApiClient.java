package com.tecksupport.glfw.pathfinder.Route;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ORSApiClient {
    private static final String API_URL = "https://api.openrouteservice.org/v2/directions/foot-walking/json";
    private static final String API_KEY = "5b3ce3597851110001cf6248b1f4cd365d7c45eeaced5f4c2d025be7";
    private static final String ROUTE_DIRECTORY = "routes"; // Directory to save JSON files

    /**
     * Sends a POST request to the ORS API with the given coordinates.
     *
     * @param coordinates A 2D array of coordinates (longitude, latitude).
     * @return The response as a String.
     */
    public static String getRouteFromORS(double[][] coordinates) {
        Client client = ClientBuilder.newClient();
        JSONObject payload = new JSONObject();
        payload.put("coordinates", coordinates);

        try {
            Entity<String> entity = Entity.entity(payload.toString(), MediaType.APPLICATION_JSON);
            Response response = client.target(API_URL)
                    .request()
                    .header("Authorization", API_KEY)
                    .header("Accept", "application/json")
                    .post(entity);

            if (response.getStatus() == 200) {
                return response.readEntity(String.class);
            } else {
                System.err.println("Error: " + response.getStatus());
                System.err.println("Response: " + response.readEntity(String.class));
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            client.close();
        }
    }

    /**
     * Saves the response to a JSON file in the designated directory.
     *
     * @param jsonResponse The JSON response as a string.
     * @param fileName     The name of the file to save the response.
     */
    public static void saveResponseToFile(String jsonResponse, String fileName) {
        File directory = new File(ROUTE_DIRECTORY);

        // Ensure the directory exists
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Failed to create directory: " + ROUTE_DIRECTORY);
                return;
            }
        }

        // Save the file in the directory
        File file = new File(directory, fileName);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonResponse);
            System.out.println("Response saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User inputs the day of the week
        System.out.println("Enter the day of the week (e.g., MONDAY, TUESDAY): ");
        String dayOfWeek = scanner.nextLine().trim().toUpperCase();

        // Validate day of week
        try {
            DaysInWeek.valueOf(dayOfWeek); // Check if it's a valid day
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid day of the week.");
            return;
        }

        // Example: Requesting a route between multiple coordinates
        double[][] coordinates = {
                {-117.159493, 33.126198},
                {-117.156601, 33.130588}
        };

        // Fetch the response
        String response = getRouteFromORS(coordinates);

        // Save the response to a file named after the day
        if (response != null) {
            String fileName = dayOfWeek.toLowerCase() + ".json"; // e.g., "monday.json"
            saveResponseToFile(response, fileName);
        }
    }
}
