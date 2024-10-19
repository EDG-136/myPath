package glfw.model;

import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;

public class Ebo {

    private final int eboID; //Vertex Buffer Object

    public Ebo(int[] indices) {

        //create a gl buffer and assign the functional pointer to vboID
        eboID = glGenBuffers();
        //bind created buffer to the OpenGL
        bind();

        //create a floatBuffer to store the buffer data
        IntBuffer buffer = MemoryUtil.memAllocInt(indices.length);

        buffer.put(indices);
        buffer.flip();

        //give the buffer data to OpenGL Vertex Buffer Object
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

    }

    public void bind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void cleanup() {
        unbind();
        //delete all buffer objects until vbo returns 0
        if (eboID != 0) {
            glDeleteBuffers(eboID);
        }


    }
}