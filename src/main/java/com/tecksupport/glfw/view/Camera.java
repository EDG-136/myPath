package com.tecksupport.glfw.view;

import com.tecksupport.glfw.model.Shader;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Camera {

    public int width;
    public int height;
    public Vector3f position ;
    public Vector3f orientation;
    public Vector3f Up;
    public float speed = 0.1f;
    public float sensitivity = 100.0f;
    private float pitch;
    private float yaw;
    private float roll;

    private Matrix4f model;
    private Matrix4f view;
    private Matrix4f projection;

    public Camera(){
        this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        this.orientation = new Vector3f(0.0f,0.0f,-1.0f);
        this.Up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.height = 800;
        this.width = 800;

    }
    public void createMatrix(float FOVdeg, float nearPlane, float farPlane, Shader shader, String uniform){
        model = new Matrix4f();
        view = new Matrix4f();
        view.identity();
        projection = new Matrix4f();
        projection.identity();

        float FOVrads = Math.toRadians(FOVdeg);

        view.lookAt(position, position.add(orientation), Up);
        projection.perspective(FOVrads, (float)(width / height), nearPlane, farPlane);

        Matrix4f camera = projection.mul(view);
        float[] matrixData = new float[16];
        camera.get(matrixData);

        glUniformMatrix4fv(glGetUniformLocation(shader.getProgramId(), uniform), false, matrixData);

    }
    public Matrix4f getModel(){
        return model;
    }

    public Matrix4f getProjection() {
        return projection;
    }
    public Matrix4f getView() {
        return view;
    }

    public void forward(){
        position = position.add(orientation.mul(speed));
    }
    public void left(){
        position = position.add(orientation.cross(Up).normalize().negate().mul(speed));
    }
    public void right(){
        position = position.add(orientation.cross(Up).normalize().mul(speed));
    }
    public void backward(){
        position = position.add(orientation.mul(-speed));
    }
}
