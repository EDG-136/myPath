package glfw.model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Vao {

    private int vaoID;

    public Vao(){
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

    }
    public void LinkVBO(Vbo vbo, int layout, int dimension){
        vbo.bind();
        glVertexAttribPointer(layout, dimension, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(layout);
        vbo.unbind();
    }
    public void bind(){
        glBindVertexArray(vaoID);

    }
    public void unbind(){
        glBindVertexArray(0);
    }
    public void cleanup(){
        unbind();

        if(vaoID!=0){
            glDeleteBuffers(vaoID);
        }
    }
}

