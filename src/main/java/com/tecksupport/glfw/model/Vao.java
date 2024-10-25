package com.tecksupport.glfw.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Vao {

    private int vaoID;

    public Vao() {
        vaoID = glGenVertexArrays();
        bind();
    }

    public void LinkVBO(Vbo vbo, int layout, int dimension, int stride, long offset) {
        vbo.bind();
        glVertexAttribPointer(layout, dimension, GL_FLOAT, false, stride, offset);
        glEnableVertexAttribArray(layout);
        vbo.unbind();
    }

    public void bind() {
        glBindVertexArray(vaoID);
    }
    public int getVaoID(){
        return this.vaoID;
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public void cleanup() {
        if (vaoID != 0) {
            glDeleteBuffers(vaoID);
            glDisableVertexAttribArray(0);
        }
        unbind();
    }
}
