package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    // Maximum allowable height difference (Z-coordinate) between nodes
    private static final double MAX_HEIGHT_DIFFERENCE = 10.0;

    // Map to store nodes by their unique ID
    private final Map<Long, Node> nodeMap = new HashMap<>();

    /**
     * Finds the shortest path between two nodes using Dijkstra's algorithm.
     *
     * @param start  The starting node.
     * @param end    The destination node.
     * @param nodes  A map of all nodes in the graph (node ID to Node).
     * @param edges  A map representing the graph's adjacency list (node ID to a list of connected node IDs).
     * @return A list of Node objects representing the shortest path.
     */
    public List<Node> getShortestPath(Node start, Node end, Map<Long, Node> nodes, Map<Long, List<Long>> edges) {
        // Priority queue to process nodes in order of their distance from the start
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));

        // Map to store the shortest known distance to each node
        Map<Node, Integer> distances = new HashMap<>();

        // Map to track the previous node on the shortest path
        Map<Node, Node> previous = new HashMap<>();

        // Set to track visited nodes and prevent reprocessing
        Set<Node> visited = new HashSet<>();

        // Initialize only the start node with distance 0
        distances.put(start, 0);
        start.setDistance(0);
        pq.add(start);

        // Main loop: process nodes in the priority queue
        while (!pq.isEmpty()) {
            // Retrieve and remove the node with the smallest distance
            Node current = pq.poll();

            // Skip if the node was already visited
            if (!visited.add(current)) continue;

            // Stop processing if we reach the destination node
            if (current.equals(end)) break;

            // Get the neighbors of the current node from the adjacency list
            List<Long> neighbors = edges.get(current.getId());
            if (neighbors == null) continue; // No neighbors to process

            // Iterate through each neighbor
            for (Long neighborId : neighbors) {
                Node neighbor = nodes.get(neighborId);

                // Skip if the neighbor doesn't exist or has already been visited
                if (neighbor == null || visited.contains(neighbor)) continue;

                // Calculate the height difference between the current node and the neighbor
                double heightDiff = Math.abs(current.getZ() - neighbor.getZ());
                if (heightDiff > MAX_HEIGHT_DIFFERENCE) continue; // Skip if height difference is too large

                // Calculate the weight (distance) of the edge between the current node and the neighbor
                int weight = calculateWeight(current, neighbor);
                int newDist = distances.getOrDefault(current, Integer.MAX_VALUE) + weight;

                // Update the shortest distance to the neighbor if a shorter path is found
                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current); // Update the previous node map
                    neighbor.setDistance(newDist);  // Update the distance for the priority queue
                    pq.add(neighbor);               // Add the neighbor to the queue for processing
                }
            }
        }

        // Reconstruct and return the shortest path
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
    private List<Node> reconstructPath(Map<Node, Node> previous, Node start, Node end) {
        List<Node> path = new ArrayList<>();

        // Trace back from the destination node to the start node
        for (Node at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }

        // Reverse the path to get it in the correct order (start to end)
        Collections.reverse(path);
        return path;
    }

    /**
     * Calculates the weight (distance) between two nodes using the Euclidean distance formula.
     *
     * @param a The first node.
     * @param b The second node.
     * @return The distance as an integer.
     */
    private int calculateWeight(Node a, Node b) {
        // Euclidean distance between two nodes based on their X and Z coordinates
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }

    /**
     * Computes the complete itinerary based on a schedule.
     *
     * @param start        The starting node.
     * @param destinations A list of destination nodes (e.g., class locations).
     * @param nodes        A map of all nodes in the graph (node ID to Node).
     * @param edges        A map representing the graph's adjacency list (node ID to a list of connected node IDs).
     * @return A list of Node objects representing the complete itinerary.
     */
    public List<Node> calculateItinerary(Node start, List<Node> destinations, Map<Long, Node> nodes, Map<Long, List<Long>> edges) {
        List<Node> completePath = new ArrayList<>();
        Node current = start;

        // Iterate through each destination in the list
        for (Node destination : destinations) {
            // Compute the shortest path for the current leg of the journey
            List<Node> segmentPath = getShortestPath(current, destination, nodes, edges);

            // Handle case where no path is found
            if (segmentPath.isEmpty()) {
                System.err.println("No path found between " + current + " and " + destination);
                continue;
            }

            // Avoid duplicate nodes when appending segments
            if (!completePath.isEmpty()) {
                segmentPath.remove(0); // Remove the starting node of the segment if it's already in the path
            }
            completePath.addAll(segmentPath); // Append the segment to the complete path

            // Update the current node for the next leg of the journey
            current = destination;
        }

        return completePath;
    }

    /**
     * Adds a node to the internal node map.
     *
     * @param node The node to add.
     */
    public void addNode(Node node) {
        nodeMap.put(node.getId(), node);
    }
}
