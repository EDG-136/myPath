package com.tecksupport.glfw.model;


import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL40.*;

public class FrameBuffer {
    private int fboID;
    private int textureID;
    private int rbo;

    public FrameBuffer(int width, int height) {
        createFBO();
        bind();
        createTexture(width, height);
        attachTexture();
        createRBO(width, height);
        attachRBO();

        if (!isFrameBufferCompleted())
            System.out.println("Error: Frame Buffer is not completed");

        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glDisable(GL_DEPTH_TEST);
        glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    private void createFBO() {
        fboID = glGenFramebuffers();
    }

    private void createTexture(int width, int height) {
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void attachTexture() {
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, textureID, 0);
    }

    private void createRBO(int width, int height) {
        rbo = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rbo);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH24_STENCIL8, width, height);
    }

    private void attachRBO() {
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_RENDERBUFFER, rbo);
    }

    public boolean isFrameBufferCompleted() {
        return glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;
    }

    public int getTextureID() {
        return textureID;
    }
}
