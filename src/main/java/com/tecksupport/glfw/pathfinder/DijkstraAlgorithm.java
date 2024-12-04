package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    private static final double MAX_HEIGHT_DIFFERENCE = 400.0; // Adjustable height difference threshold
    private static final double SCALING_FACTOR = 1.2; // For normalized height penalty
    private static final double STEEPNESS_FACTOR = 500; // For angle-based penalty
    private static final double HEIGHT_THRESHOLD = 50; // Acceptable height difference without penalty
    private static double averageHeightDifference = 1.0; // Default value, should be calculated

    /**
     * Finds the shortest path between two nodes using Dijkstra's algorithm.
     *
     * @param start The starting node.
     * @param end   The destination node.
     * @return A list of Node objects representing the shortest path.
     */
    public static List<Node> getShortestPath(Node start, Node end) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        // Initialize distances for the start node
        distances.put(start, 0);
        start.setDistance(0);
        pq.add(start);

        System.out.println("Starting Pathfinding from Node " + start.getId() + " to Node " + end.getId());

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Skip stale nodes
            if (current.getDistance() > distances.getOrDefault(current, Integer.MAX_VALUE)) {
                continue;
            }

            // Mark as visited
            if (!visited.add(current)) {
                continue;
            }

            // Check if we reached the end
            if (current.equals(end)) {
                System.out.println("Reached Destination Node: " + end.getId());
                break;
            }

            for (Node neighbor : current.getNeighborList()) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int weight = calculateWeight(current, neighbor);

                int newDist = distances.getOrDefault(current, Integer.MAX_VALUE) + weight;

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    neighbor.setDistance(newDist);

                    pq.remove(neighbor); // Ensure no stale entries
                    pq.add(neighbor);    // Add the updated node

                    System.out.println("Updated Distance for Node " + neighbor.getId() + ": " + newDist);
                    System.out.println("Previous Node for " + neighbor.getId() + ": " + current.getId());
                }
            }
        }

        // Final debug output
        System.out.println("Final Distances Map: " + distances);
        System.out.println("Final Previous Map: " + previous);

        return reconstructPath(previous, start, end);
    }

    /**
     * Reconstructs the shortest path from the start node to the end node.
     *
     * @param previous A map of each node to its previous node in the shortest path.
     * @param start    The starting node.
     * @param end      The destination node.
     * @return A list of Node objects representing the shortest path.
     */
    private static List<Node> reconstructPath(Map<Node, Node> previous, Node start, Node end) {
        List<Node> path = new ArrayList<>();
        System.out.println("Reconstructing Path from Node " + end.getId() + " to Node " + start.getId());

        for (Node at = end; at != null; at = previous.get(at)) {
            path.add(at);
            System.out.println("Adding Node " + at.getId() + " to Path");
        }

        Collections.reverse(path);
        System.out.println("Final Path: " + path);

        // Calculate total path weight for debugging
        int totalWeight = 0;
        for (int i = 1; i < path.size(); i++) {
            Node from = path.get(i - 1);
            Node to = path.get(i);
            int weight = calculateWeight(from, to);
            totalWeight += weight;
            System.out.println("From " + from.getId() + " to " + to.getId() + " | Weight: " + weight);
        }
        System.out.println("Total Path Weight: " + totalWeight);

        return path;
    }

    /**
     * Calculates the weight (distance) between two nodes using the refined formula.
     *
     * @param a The first node.
     * @param b The second node.
     * @return The distance as an integer.
     */
    private static int calculateWeight(Node a, Node b) {
        double baseDistance = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
        double heightDifference = Math.abs(a.getZ() - b.getZ());

        // Normalize height penalty
        double normalizedHeightPenalty = (heightDifference / averageHeightDifference) * SCALING_FACTOR;

        // Angle-based penalty
        double angle = Math.atan2(heightDifference, baseDistance);
        double anglePenalty = Math.sin(angle) * STEEPNESS_FACTOR;

        // Dynamic penalty for large height differences
        double dynamicPenalty = heightDifference > HEIGHT_THRESHOLD ? (heightDifference - HEIGHT_THRESHOLD) * SCALING_FACTOR : 0;

        // Combine all penalties
        double totalPenalty = normalizedHeightPenalty + anglePenalty + dynamicPenalty;

        System.out.println("Calculating weight from Node " + a.getId() + " to Node " + b.getId());
        System.out.println("Base Distance: " + baseDistance + ", Height Penalty: " + totalPenalty);

        return (int) (baseDistance + totalPenalty);
    }

    /**
     * Sets the average height difference for normalization.
     * This should be called before the pathfinding algorithm.
     *
     * @param averageHeightDiff The average height difference across the graph.
     */
    public static void setAverageHeightDifference(double averageHeightDiff) {
        averageHeightDifference = averageHeightDiff;
        System.out.println("Set average height difference to: " + averageHeightDifference);
    }
}
