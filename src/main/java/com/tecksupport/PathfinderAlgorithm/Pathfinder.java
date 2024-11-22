package com.tecksupport.PathfinderAlgorithm;

import java.util.*;

public class Pathfinder {
    private Node[][] grid;
    private int width, height;
    private static final int MAX_NODES_TO_EXPLORE = 10000;
    private OSMParser osmParser;

    // Define bounds for your map's latitude and longitude range
    private static final double minLat = 33.126; // Example min latitude of campus
    private static final double maxLat = 33.135; // Example max latitude of campus
    private static final double minLon = -117.164; // Example min longitude of campus
    private static final double maxLon = -117.154; // Example max longitude of campus

    private double latRange = maxLat - minLat;
    private double lonRange = maxLon - minLon;

    public Pathfinder(int width, int height, String osmFilePath) {
        this.width = width;
        this.height = height;
        this.osmParser = new OSMParser(osmFilePath);
        grid = new Node[width][height];
        initializeGrid();
    }

    private void initializeGrid() {
        // Create a blank grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Node(x, y, true); // Initialize as walkable
            }
        }
        // Use OSM data to mark paths and buildings
        markPathsAndBuildings();
    }

    private void markPathsAndBuildings() {
        Map<Long, OSMNode> nodes = osmParser.getNodes();
        Map<Long, OSMWay> ways = osmParser.getWays();

        for (OSMWay way : ways.values()) {
            List<Long> nodeRefs = way.getNodeRefs();
            for (int i = 0; i < nodeRefs.size() - 1; i++) {
                OSMNode startNode = nodes.get(nodeRefs.get(i));
                OSMNode endNode = nodes.get(nodeRefs.get(i + 1));

                if (startNode != null && endNode != null) {
                    int startX = convertToGridX(startNode.getLatitude());
                    int startY = convertToGridY(startNode.getLongitude());
                    int endX = convertToGridX(endNode.getLatitude());
                    int endY = convertToGridY(endNode.getLongitude());

                    // Mark path from start to end as walkable
                    drawLineOnGrid(startX, startY, endX, endY);
                }
            }
        }
    }

    private void drawLineOnGrid(int x1, int y1, int x2, int y2) {
        // Bresenham's line algorithm to mark cells as walkable along the line
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (isWithinGrid(x1, y1)) {
                grid[x1][y1].setWalkable(true);
            }
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    private int convertToGridX(double latitude) {
        return (int) ((latitude - minLat) / latRange * width);
    }

    private int convertToGridY(double longitude) {
        return (int) ((longitude - minLon) / lonRange * height);
    }

    public List<Node> findPath(Node start, Node target) {
        if (!start.isWalkable() || !target.isWalkable()) {
            System.out.println("No valid path: Start or target node is blocked.");
            return Collections.emptyList();
        }

        if (isIsolated(target)) {
            System.out.println("No valid path: Target node is isolated.");
            return Collections.emptyList();
        }

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getFCost));
        Set<Node> closedSet = new HashSet<>();

        resetGridState();
        start.setGCost(0);
        start.setHCost(getDistance(start, target));
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(target)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current)) {
                if (!neighbor.isWalkable() || closedSet.contains(neighbor)) {
                    continue;
                }

                double tentativeGCost = current.getGCost() + getDistance(current, neighbor);
                if (tentativeGCost < neighbor.getGCost()) {
                    neighbor.setGCost(tentativeGCost);
                    neighbor.setHCost(getDistance(neighbor, target));
                    neighbor.setParent(current);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        System.out.println("No valid path to target at (" + target.getX() + ", " + target.getY() + ")");
        return Collections.emptyList();
    }

    private List<Node> reconstructPath(Node target) {
        LinkedList<Node> path = new LinkedList<>();
        Node current = target;

        while (current != null) {
            path.addFirst(current);
            current = current.getParent();
        }

        return path;
    }

    private void resetGridState() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Node node = grid[x][y];
                node.setGCost(Double.POSITIVE_INFINITY);
                node.setHCost(0);
                node.setParent(null);
            }
        }
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();

        if (isWithinGrid(x - 1, y) && grid[x - 1][y].isWalkable()) neighbors.add(grid[x - 1][y]);
        if (isWithinGrid(x + 1, y) && grid[x + 1][y].isWalkable()) neighbors.add(grid[x + 1][y]);
        if (isWithinGrid(x, y - 1) && grid[x][y - 1].isWalkable()) neighbors.add(grid[x][y - 1]);
        if (isWithinGrid(x, y + 1) && grid[x][y + 1].isWalkable()) neighbors.add(grid[x][y + 1]);

        // Diagonal neighbors with corner checks
        if (isWithinGrid(x - 1, y - 1) && grid[x - 1][y - 1].isWalkable() &&
                grid[x - 1][y].isWalkable() && grid[x][y - 1].isWalkable()) neighbors.add(grid[x - 1][y - 1]);
        if (isWithinGrid(x - 1, y + 1) && grid[x - 1][y + 1].isWalkable() &&
                grid[x - 1][y].isWalkable() && grid[x][y + 1].isWalkable()) neighbors.add(grid[x - 1][y + 1]);
        if (isWithinGrid(x + 1, y - 1) && grid[x + 1][y - 1].isWalkable() &&
                grid[x + 1][y].isWalkable() && grid[x][y - 1].isWalkable()) neighbors.add(grid[x + 1][y - 1]);
        if (isWithinGrid(x + 1, y + 1) && grid[x + 1][y + 1].isWalkable() &&
                grid[x + 1][y].isWalkable() && grid[x][y + 1].isWalkable()) neighbors.add(grid[x + 1][y + 1]);

        return neighbors;
    }

    private boolean isIsolated(Node target) {
        return target.isWalkable() && getNeighbors(target).isEmpty();
    }

    private double getDistance(Node a, Node b) {
        int dx = Math.abs(a.getX() - b.getX());
        int dy = Math.abs(a.getY() - b.getY());
        return (dx > dy) ? (14 * dy + 10 * (dx - dy)) : (14 * dx + 10 * (dy - dx));
    }

    private boolean isWithinGrid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Node[][] getGrid() {
        return grid;
    }
}