package com.tecksupport.glfw.model;

import static org.lwjgl.opengl.GL11.*;

public class Mesh {

    public float[] vertices;
    public int[] indices;
    //public Vector<Texture> textures;
    public Vao vao = new Vao();
    private final int vaoID;

    public Mesh(float[] v, int[] i) {
        this.vertices = v;
        this.indices = i;
        //this.textures = t;

        vao.bind();

        Vbo vbo = new Vbo(vertices);
        Ebo ebo = new Ebo(indices);
        vaoID = vao.getVaoID();

        //vao.LinkVBO(vbo, 0, 3);

        vao.unbind();
        vbo.unbind();
        ebo.unbind();
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertices.length;
    }

    public void Draw(Shader s) {

        glClearColor(0, 0, 0.4f, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        s.bind();
        vao.bind();

        //int numDiffuse = 0;
        //int numSpecular = 0;

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        vao.cleanup();
        s.unbind();
    }
}
