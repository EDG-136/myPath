package com.tecksupport.glfw.model;

import org.joml.Vector3f;

public class Entity {

    private TexturedModel model;
    private final Vector3f position;
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale;


    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public void increasePosition(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
    }

    public void increaseRotation(float x, float y, float z) {
        this.rotX += x;
        this.rotY += y;
        this.rotZ += z;
    }

    public Vector3f getPosition() {
        return position;
    }
    public float getX(){return position.x;}
    public float getY(){return position.y;}
    public float getZ(){return position.z;}

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
