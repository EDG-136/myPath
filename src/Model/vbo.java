package Model;

import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;

public class vbo {

    private int vboID; //Vertex Buffer Object

    public vbo(float[] vertices, long size){

        //create a gl buffer and assign the functional pointer to vboID
        vboID = glGenBuffers();
        //bind created buffer to the OpenGL
        bind();

        //create a floatBuffer to store the buffer data
        FloatBuffer buffer = MemoryUtil.memAllocFloat(vertices.length);
        buffer.put(vertices).flip();

        //give the buffer data to OpenGL Vertex Buffer Object
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

    }
    public void bind(){
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

    }
    public void unbind(){
        glBindBuffer(GL_ARRAY_BUFFER, 0);

    }
    public void cleanup(){
        unbind();
        //delete all buffer objects until vbo returns 0
        if(vboID!=0){
            glDeleteBuffers(vboID);
        }


    }


}
