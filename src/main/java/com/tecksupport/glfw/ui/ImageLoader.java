package com.tecksupport.glfw.ui;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBImage;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class ImageLoader {

    public static int loadTexture(String path) {
        int width, height;
        ByteBuffer image;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            IntBuffer channelsBuffer = stack.mallocInt(1);

            // Load image using STBImage from classpath
            image = loadImage(path, widthBuffer, heightBuffer, channelsBuffer);
            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + path);
            }

            width = widthBuffer.get(0);
            height = heightBuffer.get(0);
        }

        // Generate texture ID
        int textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Upload texture to GPU
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);

        // Generate mipmaps for better texture scaling
        glGenerateMipmap(GL_TEXTURE_2D);

        // Free the image memory
        STBImage.stbi_image_free(image);

        return textureId;
    }

    private static ByteBuffer loadImage(String path, IntBuffer width, IntBuffer height, IntBuffer channels) {
        // Try to load from filesystem first
        ByteBuffer image = STBImage.stbi_load(path, width, height, channels, 4);
        if (image == null) {
            // Try to load from classpath if filesystem fails
            try (InputStream input = ImageLoader.class.getClassLoader().getResourceAsStream(path)) {
                if (input == null) {
                    throw new RuntimeException("Resource not found: " + path);
                }

                // Read input stream into a byte buffer
                byte[] bytes = input.readAllBytes();
                ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length).put(bytes);
                buffer.flip();
                return STBImage.stbi_load_from_memory(buffer, width, height, channels, 4);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load resource: " + path, e);
            }
        }
        return image;
    }
}
