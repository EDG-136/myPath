package com.tecksupport.PathfinderAlgorithm;

public class Node {
    private int x, y;
    private boolean walkable;
    private boolean isBuilding;
    private double gCost, hCost;
    private Node parent;

    public Node(int x, int y, boolean walkable) {
        this.x = x;
        this.y = y;
        this.walkable = walkable;
        this.isBuilding = false;
        this.gCost = Double.POSITIVE_INFINITY;
        this.hCost = 0;
        this.parent = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isWalkable() { return walkable; }
    public void setWalkable(boolean walkable) { this.walkable = walkable; }
    public boolean isBuilding() { return isBuilding; }
    public void setBuilding(boolean isBuilding) { this.isBuilding = isBuilding; }
    public double getGCost() { return gCost; }
    public void setGCost(double gCost) { this.gCost = gCost; }
    public double getHCost() { return hCost; }
    public void setHCost(double hCost) { this.hCost = hCost; }
    public double getFCost() { return gCost + hCost; }
    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }
}