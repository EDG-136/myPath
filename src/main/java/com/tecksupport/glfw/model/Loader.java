package com.tecksupport.glfw.model;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL40.*;

public class Loader {

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(ModelData modelData) {
        return loadToVAO(
                modelData.getVertices(),
                modelData.getTextureCoords(),
                modelData.getIndices()
        );
    }

    public RawModel loadToVAO(float[] vertices, float[] textureCoords, int[] indices) {
        int vaoID = createVAO();

        bindIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);

        unbindVAO();
        return new RawModel(vaoID, vertices.length);
    }

    public RawModel loadToVAO(float[] vertices, int[] indices) {
        int vaoID = createVAO();

        bindIndicesBuffer(indices);

        storeDataInAttributeList(0, 3, vertices);

        unbindVAO();
        return new RawModel(vaoID, vertices.length);

    }

    public int loadTexture(String filename) {
        Texture texture;
        try {
            texture = new Texture("src/main/resources/textures/" + filename + ".png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }


    private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attributeNumber, coordinateSize, GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }


    public void cleanUp() {
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            glDeleteTextures(texture);
        }
    }
}
