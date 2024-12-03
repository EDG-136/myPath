package com.tecksupport.glfw.utils;

import org.joml.Vector3f;

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
    public Node (float x, float y, float z){
        this.id = 0;
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
    public Vector3f getPositon(){
        return new Vector3f(x,y,z);
    }
    public int getId() {
        return id;
    }
}