package com.tecksupport.glfw.model;

import com.tecksupport.glfw.controller.InputHandler;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> ebos = new ArrayList<Integer>();


    public RawModel loadToVAO(float[] vertices, int[] indices) {
        Vao vao = new Vao();
        int vaoID = vao.getVaoID();
        vaos.add(vaoID);

        storeIndicesBuffer(indices);

        storeDataInAttributeList(0, vertices);
        vao.unbind();
        return new RawModel(vaoID, vertices.length / 3);

    }

    private void storeDataInAttributeList(int attributeNumber, float[] vertices) {
        Vbo vbo = new Vbo(vertices);
        int vboID = vbo.getVboID();
        vbos.add(vboID);
        glVertexAttribPointer(attributeNumber, 3, GL_FLOAT, false, 0, 0);
        vbo.unbind();
    }

    private void storeIndicesBuffer(int[] indices) {
        Ebo ebo = new Ebo(indices);
        int eboID = ebo.getEboID();
        ebos.add(eboID);

    }

    public void cleanUp() {
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
        }
    }
}
