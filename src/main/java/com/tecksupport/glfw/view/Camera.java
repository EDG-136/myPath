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

    private Vector3f position = new Vector3f(0,0,0);

    private float pitch;
    private float yaw = 0.0f;
    private float roll;

    private float speed = 1.0f;


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
        float adjacentx = -Math.sin(Math.toRadians(-yaw));
        float adjacentz = -Math.cos(Math.toRadians(-yaw));

        position.x += adjacentx * speed;
        position.z+= adjacentz * speed;
    }
    public void left(){

        float adjacentx = -Math.sin(Math.toRadians(yaw));
        float adjacentz = -Math.cos(Math.toRadians(yaw));

        position.x -= adjacentx * speed;
        position.z-= adjacentz * speed;
    }
    public void right(){
        float adjacentx = Math.sin(Math.toRadians(yaw));
        float adjacentz = Math.cos(Math.toRadians(yaw));

        position.x -= adjacentx * speed;
        position.z-= adjacentz * speed;


    }
    public void backward(){
        float adjacentx = Math.sin(Math.toRadians(-yaw));
        float adjacentz = Math.cos(Math.toRadians(-yaw));

        position.x += adjacentx * speed;
        position.z+= adjacentz * speed;
    }

    public void up(){position.y += 0.4f;}
    public void down(){position.y -= 0.4f;}

    public void yawLeft(){
        yaw -= 0.4f;
    }
    public void yawRight(){yaw += 0.4f;}

}
