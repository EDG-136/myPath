package Model;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import Model.vbo;

public class vao {

    private int vaoID;

    public vao(){
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

    }
    public void LinkVBO(vbo vbo, int layout ){
        vbo.bind();
        glVertexAttribPointer(layout, 3, GL_FLOAT, false, 0, 0);
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

