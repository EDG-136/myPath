package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.node.Node;

import java.util.*;

public class DijkstraAlgorithm {
    private static final double MAX_HEIGHT_DIFFERENCE = 10.0; // Maximum Z difference
    private final Map<Integer, Node> nodeMap = new HashMap<>();

    public List<Node> getShortestPath(Node start, Node end, Map<Integer, Node> nodes, Map<Integer, List<Integer>> edges) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getDistance));
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        Set<Node> visited = new HashSet<>();

        // Initialize only the start node
        distances.put(start, 0);
        start.setDistance(0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            if (!visited.add(current)) continue;
            if (current.equals(end)) break;

            List<Integer> neighbors = edges.get(current.getId());
            if (neighbors == null) continue;

            for (Integer neighborId : neighbors) {
                Node neighbor = nodes.get(neighborId);
                if (neighbor == null || visited.contains(neighbor)) continue;

                double heightDiff = Math.abs(current.getZ() - neighbor.getZ());
                if (heightDiff > MAX_HEIGHT_DIFFERENCE) continue;

                int weight = calculateWeight(current, neighbor);
                int newDist = distances.getOrDefault(current, Integer.MAX_VALUE) + weight;

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    neighbor.setDistance(newDist);
                    pq.add(neighbor);
                }
            }
        }

        return reconstructPath(previous, start, end);
    }

    private List<Node> reconstructPath(Map<Node, Node> previous, Node start, Node end) {
        List<Node> path = new ArrayList<>();
        for (Node at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    private int calculateWeight(Node a, Node b) {
        return (int) Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getZ() - b.getZ(), 2));
    }

    public List<Node> calculateItinerary(Node start, List<Node> destinations, Map<Integer, Node> nodes, Map<Integer, List<Integer>> edges) {
        List<Node> completePath = new ArrayList<>();
        Node current = start;

        for (Node destination : destinations) {
            List<Node> segmentPath = getShortestPath(current, destination, nodes, edges);
            if (segmentPath.isEmpty()) {
                System.err.println("No path found between " + current + " and " + destination);
                continue;
            }

            if (!completePath.isEmpty()) {
                segmentPath.remove(0);
            }
            completePath.addAll(segmentPath);

            current = destination;
        }

        return completePath;
    }

    public void addNode(Node node) {
        nodeMap.put(node.getId(), node);
    }
}
