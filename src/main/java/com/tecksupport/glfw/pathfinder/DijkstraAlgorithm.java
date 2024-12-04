package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    private static final double MAX_HEIGHT_DIFFERENCE = Double.MAX_VALUE; // Maximum Z difference

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

        // Debug: Initial state

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            // Debug: Node being processed

            if (!visited.add(current)) {
                continue;
            }

            if (current.equals(end)) {
                break;
            }

            // Debug: Neighbors of the current node

            for (Node neighbor : current.getNeighborList()) {
                if (visited.contains(neighbor)) {
                    continue;
                }

                int weight = calculateWeight(current, neighbor);
                int newDist = distances.getOrDefault(current, Integer.MAX_VALUE) + weight;

                // Debug: Distance comparison

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    pq.remove(neighbor);
                    neighbor.setDistance(newDist);
                    pq.add(neighbor);

                    // Debug: Updated states
                }
            }
        }

        // Debug: Final state before path reconstruction

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

        for (Node at = end; at != null; at = previous.get(at)) {
            path.add(at);
            // Debug: Add to path
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
    private static int calculateWeight(Node a, Node b) {
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }
}
