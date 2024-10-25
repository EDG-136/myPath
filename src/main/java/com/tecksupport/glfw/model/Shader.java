package com.tecksupport.glfw.model;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programId;
    private int vertexShaderID;
    private int fragmentShaderID;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    private int camLocation;

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
        try{
            return new String(Files.readAllBytes(Paths.get(filePath)));

        } catch(IOException e){
            System.err.println("Error: Couldn't Read shader file.");
            e.printStackTrace();
            return new String("");
        }
    }
    private int compileShader(String shaderCode, int type){
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
            System.err.println("Error: shader compilation failed.");
            System.err.println(glGetShaderInfoLog(shaderID));
            return -1;
        }
        return shaderID;
    }

    protected int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programId, uniformName);
    }
    protected void getAllUniformLocations(){
        camLocation = getUniformLocation("camera");

    }

    public void setUniform(String uniformName, Matrix4f matrix) {
        int location = getUniformLocation(uniformName);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            glUniformMatrix4fv(location, true, buffer);
        }
    }
    public void setUniformMat4(int location, Matrix4f matrix){
        matrix.get(matrixBuffer);
        matrixBuffer.flip();
        glUniformMatrix4fv(location, false, matrixBuffer);
    }

    public void bindAttributes(){
        glBindAttribLocation(programId, 0, "position");
        glBindAttribLocation(programId, 1, "aTex");


    }

    public int getProgramId() {
        return programId;
    }
}
