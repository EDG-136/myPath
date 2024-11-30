package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    private static final double MAX_HEIGHT_DIFFERENCE = 10.0; // Maximum Z difference

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
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        for (Node node : nodes.values()) {
            distances.put(node, Integer.MAX_VALUE);
            node.setDistance(Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        start.setDistance(0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (!visited.add(current)) continue;
            if (current.equals(end)) break;

            List<Long> neighbors = edges.get(current.getId());
            if (neighbors == null) continue;

            for (Long neighborId : neighbors) {
                Node neighbor = nodes.get(neighborId);
                if (neighbor == null || visited.contains(neighbor)) continue;

                double heightDiff = Math.abs(current.getZ() - neighbor.getZ());
                if (heightDiff > MAX_HEIGHT_DIFFERENCE) continue;

                int weight = calculateWeight(current, neighbor);
                int newDist = distances.get(current) + weight;

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    neighbor.setDistance(newDist);
                    pq.add(neighbor);
                }
            }
        }

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
        for (Node at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
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
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
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

        for (Node destination : destinations) {
            List<Node> segmentPath = getShortestPath(current, destination, nodes, edges);
            if (segmentPath.isEmpty()) {
                System.err.println("No path found between " + current + " and " + destination);
                continue;
            }

            // Avoid duplicate nodes when appending segments
            if (!completePath.isEmpty()) {
                segmentPath.remove(0);
            }
            completePath.addAll(segmentPath);

            // Update current location for the next leg
            current = destination;
        }

        return completePath;
    }
}
