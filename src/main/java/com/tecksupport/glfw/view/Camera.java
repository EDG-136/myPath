package com.tecksupport.glfw.view;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private static final float MOVE_SPEED = 1.2f;
    private static final float ROTATE_SENSITIVE = 0.1f;

    private final Vector3f position = new Vector3f(0, 0, 0);
    private float pitch; // Rotation around X-axis
    private float yaw = 0.0f; // Rotation around Y-axis
    private float roll; // Rotation around Z-axis

    private final Matrix4f projectionMatrix = new Matrix4f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public Camera() {
        // Set up the default projection matrix (70° FOV, aspect ratio, near, and far planes)
        projectionMatrix.setPerspective((float) Math.toRadians(70.0), 800.0f / 600.0f, 0.1f, 1000.0f);
    }

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

    public void forward() {
        float xVector = -(float) Math.sin(Math.toRadians(-yaw));
        float zVector = -(float) Math.cos(Math.toRadians(-yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }

    public void left() {
        float xVector = -(float) Math.cos(Math.toRadians(yaw));
        float zVector = -(float) Math.sin(Math.toRadians(yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }

    public void right() {
        float xVector = (float) Math.cos(Math.toRadians(yaw));
        float zVector = (float) Math.sin(Math.toRadians(yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }

    public void backward() {
        float xVector = (float) Math.sin(Math.toRadians(-yaw));
        float zVector = (float) Math.cos(Math.toRadians(-yaw));

        position.x += xVector * MOVE_SPEED;
        position.z += zVector * MOVE_SPEED;
    }

    public void up() {
        position.y += MOVE_SPEED;
    }

    public void down() {
        position.y -= MOVE_SPEED;
    }

    public void yawLeft() {
        yaw -= MOVE_SPEED;
    }

    public void yawRight() {
        yaw += MOVE_SPEED;
    }

    // Generate and return the view matrix
    public Matrix4f getViewMatrix() {
        viewMatrix.identity(); // Reset to identity matrix
        viewMatrix.rotate((float) Math.toRadians(pitch), new Vector3f(1, 0, 0));
        viewMatrix.rotate((float) Math.toRadians(yaw), new Vector3f(0, 1, 0));
        viewMatrix.rotate((float) Math.toRadians(roll), new Vector3f(0, 0, 1));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }

    // Return the projection matrix
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
