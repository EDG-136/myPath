package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    private static final double MAX_HEIGHT_DIFFERENCE = 100.0; // Maximum height difference allowed

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

            if (!visited.add(current)) {
                System.out.println("Node " + current.getId() + " already visited.");
                continue;
            }

            if (current.equals(end)) {
                System.out.println("Reached Destination Node: " + current.getId());
                break;
            }

            for (Node neighbor : current.getNeighborList()) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                double heightDiff = Math.abs(current.getZ() - neighbor.getZ());
                if (heightDiff > MAX_HEIGHT_DIFFERENCE) {
                    continue;
                }

                int weight = calculateWeight(current, neighbor);
                int newDist = distances.getOrDefault(current, Integer.MAX_VALUE) + weight;

                // Debug: Distance comparison
                System.out.println("Checking Neighbor " + neighbor.getId() +
                        " | Current Distance: " + distances.getOrDefault(neighbor, Integer.MAX_VALUE) +
                        " | New Distance: " + newDist);

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    neighbor.setDistance(newDist);
                    pq.add(neighbor); // Re-add neighbor with updated distance

                    // Debug: Updated states
                    System.out.println("Updated Distance for Node " + neighbor.getId() + ": " + newDist);
                    System.out.println("Previous Node for " + neighbor.getId() + ": " + current.getId());
                }
            }
        }

        // Debug: Final state before path reconstruction
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
            // Debug: Add to path
            System.out.println("Adding Node " + at.getId() + " to Path");
        }

        Collections.reverse(path);
        System.out.println("Final Path: " + path);
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
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }
}
