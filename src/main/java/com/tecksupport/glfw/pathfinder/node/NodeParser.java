package com.tecksupport.glfw.pathfinder.node;

import java.io.*;
import java.util.*;

public class NodeParser {
    private static Map<Long, Node> nodes = new HashMap<>();

    // Static block for initialization
    static {
        try {
            initialize("src/main/resources/nodes.txt"); // Path to your nodes.txt file
        } catch (IOException e) {
            System.err.println("Failed to initialize NodeParser: " + e.getMessage());
        }
    }

    /**
     * Initializes and parses the nodes.txt file.
     *
     * @param filePath The path to the nodes.txt file.
     * @throws IOException If an error occurs while reading the file.
     */
    private static void initialize(String filePath) throws IOException {
        parseNodes(filePath);
    }

    /**
     * Retrieves the parsed nodes.
     *
     * @return A map of node IDs to Node objects.
     */
    public static Map<Long, Node> getNodes() {
        return nodes;
    }

    /**
     * Parses the nodes.txt file and stores them in the `nodes` map.
     */
    private static void parseNodes(String filePath) throws IOException {
        nodes = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length < 4) continue;

                try {
                    long id = Long.parseLong(parts[0].trim());
                    double x = Double.parseDouble(parts[1].trim());
                    double y = Double.parseDouble(parts[2].trim());
                    double z = Double.parseDouble(parts[3].trim());

                    Node node = new Node(id, x, y, z);
                    nodes.put(id, node);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid line in nodes.txt: " + line);
                }
            }
        }
    }
}
