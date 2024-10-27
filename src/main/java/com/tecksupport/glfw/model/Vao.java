package com.tecksupport.glfw.model;

import static org.lwjgl.opengl.GL40.*;

public class Vao {

    private final int vaoID;

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

    public int getVaoID() {
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
