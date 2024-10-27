package com.tecksupport.glfw.model;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;


import static org.lwjgl.opengl.GL15.*;

public class Vbo {

    private final int vboID; //Vertex Buffer Object

    public Vbo(float[] vertices) {
        //create a gl buffer and assign the buffer ID to vboID
        vboID = glGenBuffers();
        //bind created buffer to the OpenGL
        bind();
        //create a floatBuffer to store the buffer data
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        //give the buffer data to OpenGL Vertex Buffer Object
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }

    public int getVboID() {
        return this.vboID;
    }

    public void cleanup() {
        unbind();
        //delete all buffer objects until vbo returns 0
        if (vboID != 0) {
            glDeleteBuffers(vboID);
        }
    }
}
