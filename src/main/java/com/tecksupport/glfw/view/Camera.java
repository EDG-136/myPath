package com.tecksupport.glfw.view;

import org.joml.Math;
import org.joml.Vector3f;


public class Camera {
    private static final float MOVE_SPEED = 1.0f;
    private static final float ROTATE_SENSITIVE = 0.1f;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw = 0.0f;
    private float roll;


    public Camera(){}

    public void addRotation(float pitch, float yaw, float roll) {
        this.yaw += yaw * ROTATE_SENSITIVE;
        this.pitch += pitch * ROTATE_SENSITIVE;
        this.roll += roll * ROTATE_SENSITIVE;
    }



    public Vector3f getPosition() {
        return position;
    }


    public float getPitch() {
        return pitch;
    }


    public float getYaw() {
        return yaw;
    }


    public float getRoll() {
        return roll;
    }

    public void forward(){
        float xVector = -Math.sin(Math.toRadians(-yaw));
        float zVector = -Math.cos(Math.toRadians(-yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }
    public void left(){
        float xVector = -Math.cos(Math.toRadians(yaw));
        float zVector = -Math.sin(Math.toRadians(yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }
    public void right() {
        float xVector = Math.cos(Math.toRadians(yaw));
        float zVector = Math.sin(Math.toRadians(yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }
    public void backward(){
        float xVector = Math.sin(Math.toRadians(-yaw));
        float zVector = Math.cos(Math.toRadians(-yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }

    public void up(){
        position.y += MOVE_SPEED;
    }
    public void down() {
        position.y -= MOVE_SPEED;
    }

    public void yawLeft(){
        yaw -= MOVE_SPEED;
    }
    public void yawRight(){
        yaw += MOVE_SPEED;
    }

}