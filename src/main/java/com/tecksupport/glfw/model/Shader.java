package com.tecksupport.glfw.model;

import com.tecksupport.glfw.utils.Maths;
import com.tecksupport.glfw.view.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_color;

    public Shader(String vertexFile, String fragmentFile) {

        String vertexCode = loadShaderSource(vertexFile);
        String fragmentCode = loadShaderSource(fragmentFile);

        vertexShaderID = compileShader(vertexCode, GL_VERTEX_SHADER);
        fragmentShaderID = compileShader(fragmentCode, GL_FRAGMENT_SHADER);
        programId = glCreateProgram();

        glAttachShader(programId, vertexShaderID);
        glAttachShader(programId, fragmentShaderID);
        bindAttributes();
        glLinkProgram(programId);
        glValidateProgram(programId);

        glDetachShader(programId, vertexShaderID);
        glDetachShader(programId, fragmentShaderID);
        getAllUniformLocations();
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {

        unbind();
        glDetachShader(programId, vertexShaderID);
        glDetachShader(programId, fragmentShaderID);
        glDeleteShader(vertexShaderID);
        glDeleteShader(fragmentShaderID);
        glDeleteProgram(programId);
    }

    private String loadShaderSource(String filePath) {
        // Loading and compiling shaders would be handled here
        // Assume shaders are read from file and compiled
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));

        } catch (IOException e) {
            System.err.println("Error: Couldn't Read shader file.");
            e.printStackTrace();
            return "";
        }
    }

    private int compileShader(String shaderCode, int type) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Error: shader compilation failed.");
            System.err.println(glGetShaderInfoLog(shaderID));
            return -1;
        }
        return shaderID;
    }

    public void loadVector(int location, Vector3f vector) {
        glUniform3f(location, vector.x, vector.y, vector.z);
    }

    public void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) {
            toLoad = 1;
        }
        glUniform1f(location, toLoad);
    }

    public void loadMatrix(int location, Matrix4f matrix) {
        matrix.get(matrixBuffer);
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void bindAttributes() {
        glBindAttribLocation(programId, 0, "position");
        glBindAttribLocation(programId, 1, "aTex");
    }

    int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }


    void getAllUniformLocations() {
        location_transformationMatrix = getUniformLocation("transformationMatrix");
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_color = getUniformLocation("addColor");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }

    public void loadColorVec(Vector4f color) {
        glUniform4f(location_color, color.x, color.y, color.z, color.w);
    }

    public int getProgramId() {
        return programId;
    }
}
