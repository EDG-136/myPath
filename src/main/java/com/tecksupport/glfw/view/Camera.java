package com.tecksupport.glfw.view;

import com.tecksupport.glfw.model.Shader;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Camera {

    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera(){}

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
        position.z-=1f;
    }
    public void left(){
        position.x-=1f;
    }
    public void right(){
        position.x+=1f;
    }
    public void backward(){
        position.z+=1f;
    }
}
