package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    private static final double MAX_HEIGHT_DIFFERENCE = 1000.0; // Adjustable height difference threshold

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

                double heightDiff = Math.abs(current.getZ() - neighbor.getZ());
                int weight = calculateWeight(current, neighbor);

                // Apply penalty instead of skipping
                if (heightDiff > MAX_HEIGHT_DIFFERENCE) {
                    double penalty = (heightDiff - MAX_HEIGHT_DIFFERENCE) * 1.5; // Penalty factor
                    System.out.println("Applying height penalty for neighbor " + neighbor.getId() + ": " + penalty);
                    weight += penalty;
                }

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
     * Calculates the weight (distance) between two nodes using the Euclidean distance formula.
     *
     * @param a The first node.
     * @param b The second node.
     * @return The distance as an integer.
     */
    private static int calculateWeight(Node a, Node b) {
        double baseDistance = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
        double heightPenalty = Math.abs(a.getY() - b.getY()) * 2; // Adjusted penalty for height difference
        System.out.println("Calculating weight from Node " + a.getId() + " to Node " + b.getId());
        System.out.println("Base Distance: " + baseDistance + ", Height Penalty: " + heightPenalty);
        return (int) (baseDistance + heightPenalty);
    }
}
