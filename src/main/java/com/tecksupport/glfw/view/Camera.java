package com.tecksupport.glfw.view;

import com.tecksupport.glfw.model.Shader;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import java.util.Vector;

import static org.lwjgl.opengl.GL20.*;

public class Camera {
    private static final float MOVE_SPEED = 1.2f;
    private static final float ROTATE_SENSITIVE = 0.1f;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw = 0.0f;
    private float roll;


    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.yaw = rotation.x;
        this.pitch = rotation.y;
        this.roll = rotation.z;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    public void setRotation(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public void addRotation(float yaw, float pitch, float roll) {
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