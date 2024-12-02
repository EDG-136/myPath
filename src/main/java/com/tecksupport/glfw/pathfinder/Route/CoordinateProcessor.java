package com.tecksupport.glfw.pathfinder.Route;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CoordinateProcessor {
    private Map<String, double[]> coordinatesMap;

    public CoordinateProcessor(String filePath) {
        coordinatesMap = new HashMap<>();
        loadCoordinates(filePath);
    }

    private void loadCoordinates(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String buildingId = parts[0].trim();
                    String[] coords = parts[1].trim().split(",");
                    if (coords.length == 2) {
                        double lat = Double.parseDouble(coords[0]);
                        double lon = Double.parseDouble(coords[1]);
                        coordinatesMap.put(buildingId, new double[]{lat, lon});
                    } else {
                        System.out.println("Invalid coordinates format for: " + buildingId);
                    }
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading coordinates file: " + e.getMessage());
        }
    }

    public List<double[]> getCoordinatesForSections(List<String> sectionIds) {
        List<double[]> coordinatesList = new ArrayList<>();

        for (String sectionId : sectionIds) {
            // Extract base acronym (e.g., COM from COM206)
            String baseKey = sectionId.replaceAll("\\d", ""); // Remove digits
            if (coordinatesMap.containsKey(baseKey)) {
                coordinatesList.add(coordinatesMap.get(baseKey));
            } else {
                System.out.println("No coordinates found for section: " + sectionId);
            }
        }

        return coordinatesList;
    }
}
