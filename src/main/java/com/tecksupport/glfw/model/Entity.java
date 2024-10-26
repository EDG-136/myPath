package com.tecksupport.glfw.model;

import org.joml.Vector3f;

public class Entity {

    private TexturedModel model;
    private Vector3f position;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale;

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale){
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;

    }

    public void increasePosition(float x, float y, float z){
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }
    public void increaseRotation(float x, float y, float z){
        this.rotX += x;
        this.rotY += y;
        this.rotZ += z;
    }
    public Vector3f getPosition() {
        return position;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

}
