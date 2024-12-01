package com.tecksupport.glfw.pathfinder.node;

import java.util.*;

public class LabelAssigner {

    private final Map<Long, String> buildingLabels;

    public LabelAssigner() {
        buildingLabels = new HashMap<>();

        // Manually map node IDs to building labels
        buildingLabels.put(1L, "MARK");
        buildingLabels.put(2L, "KELLOGG");
        buildingLabels.put(3L, "CRAVEN");
    }

    /**
     * Assigns labels to building nodes.
     *
     * @param nodes The map of Node objects.
     */
    public void assignLabels(Map<Long, Node> nodes) {
        for (Map.Entry<Long, String> entry : buildingLabels.entrySet()) {
            Node node = nodes.get(entry.getKey());
            if (node != null) {
                node.setLabel(entry.getValue());
            }
        }
    }

    /**
     * Extracts the building acronym from a faculty ID.
     *
     * @param facultyID The faculty ID (e.g., "MARK 101").
     * @return The extracted building acronym (e.g., "MARK").
     */
    public static String extractAcronym(String facultyID) {
        if (facultyID == null || facultyID.isEmpty()) return "";
        return facultyID.split("\\s+")[0]; // Extract the first word
    }

    /**
     * Finds a node by its label.
     *
     * @param label The building label (e.g., "MARK").
     * @param nodes The map of Node objects.
     * @return The matching Node, or null if not found.
     */
    public Node findNodeByLabel(String label, Map<Long, Node> nodes) {
        for (Node node : nodes.values()) {
            if (label.equalsIgnoreCase(node.getLabel())) {
                return node;
            }
        }
        return null;
    }
}
