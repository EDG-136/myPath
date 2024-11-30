package com.tecksupport.glfw.pathfinder.node;

public class Node {
    private final long id;
    private final double x, y, z;
    private String label;  // Labels can be updated dynamically
    private int distance;

    public Node(long id, double x, double y, double z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.label = null; // Default label is null
        this.distance = Integer.MAX_VALUE; // Default distance to "infinity"
    }

    public long getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return String.format("Node{id=%d, x=%.2f, y=%.2f, z=%.2f, label=%s}", id, x, y, z, label);
    }
}
