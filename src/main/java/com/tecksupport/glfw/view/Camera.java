package com.tecksupport.glfw.view;

import org.joml.Vector3f;

public class Camera {

    private final float MOVE_SPEED = 1f;
    private final Vector3f position = new Vector3f(0, 0, 0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
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

    public void forward() {
        position.z -= MOVE_SPEED;
    }

    public void left() {
        position.x -= MOVE_SPEED;
    }

    public void right() {
        position.x += MOVE_SPEED;
    }

    public void backward() {
        position.z += MOVE_SPEED;
    }

    public void down() {
        position.y -= MOVE_SPEED;
    }

    public void up() {
        position.y += MOVE_SPEED;
    }
}
