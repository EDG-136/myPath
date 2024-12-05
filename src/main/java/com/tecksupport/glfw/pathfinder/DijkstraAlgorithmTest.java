package com.tecksupport.glfw.pathfinder;

import com.tecksupport.glfw.pathfinder.DijkstraAlgorithm;
import com.tecksupport.glfw.pathfinder.node.Node;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class DijkstraAlgorithmTest {

    @Test
    void testShortestPath() {
        // Create sample graph nodes
        List<Node> nodes = createNodes();
        Map<Integer, List<Integer>> neighborData = loadNeighborData();
        setupNeighbors(neighborData, nodes);

        // Find start and end nodes
        Node start = nodes.stream().filter(n -> n.getId() == 1).findFirst().orElseThrow();
        Node end = nodes.stream().filter(n -> n.getId() == 24).findFirst().orElseThrow();

        // Run Dijkstra's algorithm
        List<Node> shortestPath = DijkstraAlgorithm.getShortestPath(start, end);

        // Print the path
        System.out.println("Shortest Path:");
        shortestPath.forEach(node -> System.out.print(node.getId() + " -> "));
        System.out.println("END");

        // Assertions
        assertFalse(shortestPath.isEmpty(), "Path should not be empty!");
        assertEquals(1, shortestPath.get(0).getId(), "Path should start at Node 1!");
        assertEquals(24, shortestPath.get(shortestPath.size() - 1).getId(), "Path should end at Node 24!");
    }

    // Helper methods to set up graph
    private List<Node> createNodes() {
        String nodeData = """
                1:-641.30896,109.0,631.5263
                11:-3003.634,-25.0,705.34894
                12:-2817.6184,-21.0,571.2986
                13:-2194.43,21.0,355.88135
                14:-2022.5737,26.0,313.59048
                15:-1665.3224,38.0,138.28093
                16:-1300.3896,98.0,83.06362
                17:-1230.323,98.0,36.74711
                24:-197.66583,114.0,428.17184
                """;

        List<Node> nodes = new ArrayList<>();
        for (String line : nodeData.split("\n")) {
            String[] parts = line.split(":");
            int id = Integer.parseInt(parts[0]);
            String[] coords = parts[1].split(",");
            float x = Float.parseFloat(coords[0]);
            float y = Float.parseFloat(coords[1]);
            float z = Float.parseFloat(coords[2]);
            nodes.add(new Node(id, x, y, z));
        }
        return nodes;
    }

    private Map<Integer, List<Integer>> loadNeighborData() {
        String neighborData = """
                1:11
                11:12
                12:13
                13:14
                14:15
                15:16
                16:17
                17:24
                """;

        Map<Integer, List<Integer>> neighborMap = new HashMap<>();
        for (String line : neighborData.split("\n")) {
            String[] parts = line.split(":");
            int nodeId = Integer.parseInt(parts[0]);
            List<Integer> neighbors = Arrays.stream(parts[1].split(","))
                    .map(Integer::parseInt)
                    .toList();
            neighborMap.put(nodeId, neighbors);
        }
        return neighborMap;
    }

    private void setupNeighbors(Map<Integer, List<Integer>> neighborData, List<Node> nodes) {
        Map<Integer, Node> nodeMap = nodes.stream()
                .collect(Collectors.toMap(Node::getId, node -> node));

        for (Map.Entry<Integer, List<Integer>> entry : neighborData.entrySet()) {
            Node node = nodeMap.get(entry.getKey());
            for (int neighborId : entry.getValue()) {
                Node neighbor = nodeMap.get(neighborId);
                node.addNeighborNode(neighbor);
            }
        }
    }
}
