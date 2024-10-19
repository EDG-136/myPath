package com.tecksupport.glfw.model;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Texture {
    public int programID;
    public int widthIMG, heightIMG, compIMG;

    public Texture(String filePath) {
        try (MemoryStack stack = stackPush()) {
            //create int buffers for stbi
            IntBuffer widthBuffer = stack.mallocInt(1); //think of buffer as just arrays
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer compBuffer = stack.mallocInt(1);

            ByteBuffer image = stbi_load(filePath, widthBuffer, heightBuffer, compBuffer, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + stbi_failure_reason());
            }

            programID = glGenTextures(); //create a texture id so OpenGL knows where to look for the texture
            glActiveTexture(1);

            glBindTexture(GL_TEXTURE_2D, programID);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

            widthIMG = widthBuffer.get(0);
            heightIMG = heightBuffer.get(0);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, widthIMG, heightIMG, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            glGenerateMipmap(GL_TEXTURE_2D);

            stbi_image_free(image);

        }

    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, programID);
    }

    public void cleanup() {
        glDeleteTextures(programID);
    }

    public int getWidth() {
        return widthIMG;
    }

    public int getHeight() {
        return heightIMG;
    }
}
