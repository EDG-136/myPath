package com.tecksupport.glfw.utils;

public class Node {
    private float x;
    private float y;
    private float z;
    private int id;

    public Node (int id, float x, float y, float z){
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;

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
    public int getId() {
        return id;
    }
}
