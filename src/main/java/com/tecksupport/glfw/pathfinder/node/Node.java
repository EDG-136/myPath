package com.tecksupport.glfw.pathfinder.node;

import com.tecksupport.glfw.model.Entity;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private final int id;
    private final float x, y, z;
    private String label;  // Labels can be updated dynamically
    private int distance;
    private Entity entity;
    private List<Node> neighborList = new ArrayList<>();

    public Node(int id, float x, float y, float z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.label = null; // Default label is null
        this.distance = Integer.MAX_VALUE; // Default distance to "infinity"
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Vector3f getPosition() {
        return new Vector3f(x, y, z);
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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void addNeighborNode(Node node) {
        neighborList.add(node);
    }

    public List<Node> getNeighborList() {
        return neighborList;
    }

    @Override
    public String toString() {
        return String.format("Node{id=%d, x=%.2f, y=%.2f, z=%.2f, label=%s}", id, x, y, z, label);
    }
}
