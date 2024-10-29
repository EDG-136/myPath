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
    private static final float MOVE_SPEED = 0.6f;
    private static final float ROTATE_SENSITIVE = 0.8f;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw = 0.0f;
    private float roll;


    public Camera(){}

    public void addRotation(float yaw, float pitch, float roll) {
        this.yaw += Math.signum(yaw) * ROTATE_SENSITIVE;
        this.pitch += Math.signum(pitch) * ROTATE_SENSITIVE;
        this.roll += Math.signum(roll) * ROTATE_SENSITIVE;
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
        Vector3f moveVector = new Vector3f();
        moveVector.x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
        moveVector.y = -Math.sin(Math.toRadians(pitch));
        moveVector.z = -Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));

        position.x += moveVector.x * MOVE_SPEED;
        position.y += moveVector.y * MOVE_SPEED;
        position.z += moveVector.z * MOVE_SPEED;
    }
    public void left(){
        position.x-=0.4f;
    }
    public void right(){
        position.x+=0.4f;
    }
    public void backward(){
        position.z+=0.4f;
    }

    public void up(){position.y += 0.4f;}
    public void down(){position.y -= 0.4f;}

    public void yawLeft(){
        yaw -= 0.4f;
    }
    public void yawRight(){yaw += 0.4f;}

}
