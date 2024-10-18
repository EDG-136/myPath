package Model;

import View.Camera;

import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;

public class Mesh {

    public float[] vertices;
    public int[] indices;
    //public Vector<Texture> textures;
    public vao vao = new vao();

    public Mesh(float[] v, int[] i){
        this.vertices = v;
        this.indices = i;
        //this.textures = t;

        vao.bind();

        vbo vbo = new vbo(vertices);
        ebo ebo = new ebo(indices);


        vao.LinkVBO(vbo, 0, 3);
        vao.LinkVBO(vbo, 1, 3);
        vao.LinkVBO(vbo, 2, 3);
        vao.LinkVBO(vbo, 3, 2);

        vao.unbind();
        vbo.unbind();
        ebo.unbind();

    }

    public void Draw(Shader s){

        s.bind();
        vao.bind();

        int numDiffuse = 0;
        int numSpecular = 0;

        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);


    }

}
