package com.tecksupport.glfw.model;

import com.tecksupport.glfw.controller.InputHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class Loader {

    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    private List<Integer> ebos = new ArrayList<Integer>();
    private List<Integer> textures = new ArrayList<Integer>();


    public RawModel loadToVAO(float[] vertices, float[] textureCoords, int[] indices) {
        Vao vao = new Vao();
        int vaoID = vao.getVaoID();
        vaos.add(vaoID);

        storeIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);

        vao.unbind();
        return new RawModel(vaoID, vertices.length / 3);

    }
    public RawModel loadToVAO(float[] vertices, int[] indices) {
        Vao vao = new Vao();
        int vaoID = vao.getVaoID();
        vaos.add(vaoID);

        storeIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, vertices);

        vao.unbind();
        return new RawModel(vaoID, vertices.length / 3);

    }

    private void storeDataInAttributeList(int attributeNumber, int size, float[] vertices) {
        Vbo vbo = new Vbo(vertices);
        int vboID = vbo.getVboID();
        vbos.add(vboID);
        glVertexAttribPointer(attributeNumber, size, GL_FLOAT, false, 8 * Float.BYTES, 0);
        vbo.unbind();
    }

    private void storeIndicesBuffer(int[] indices) {
        Ebo ebo = new Ebo(indices);
        int eboID = ebo.getEboID();
        ebos.add(eboID);

    }
    public int loadTexture(String fileName){

        Texture texture = new Texture(fileName);
        int textureID = texture.getTextureID();
        textures.add(textureID);

        return textureID;

    }

    public void cleanUp() {
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
        }
        for(int texture:textures){
            glDeleteTextures(texture);
        }
    }
}
