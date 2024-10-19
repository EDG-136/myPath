package com.tecksupport.glfw.view;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import org.lwjgl.opengl.*;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {

    private long windowHandle;
    private int width;
    private int height;
    private String title;
    private boolean resized;

    private GLFWKeyCallback keyCallback;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.resized = false;
    }

    /**
     * Initializes the GLFW window and OpenGL context.
     */
    public void init() {
        // Set an error callback
        //GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW: Setting hints for the window (OpenGL version, etc.)
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Window is initially hidden
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Enable resizing
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // OpenGL 3.x
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE); // Forward compatible profile

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(0);

        // Show the window
        glfwShowWindow(windowHandle);

        GL.createCapabilities();



    }

    public void update() {
        glfwSwapBuffers(windowHandle); // Swap the color buffer (double buffering)
    }
    public void cleanup() {
        // Release the key callback
        if (keyCallback != null) {
            keyCallback.free();
        }
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null)
            callback.free();
    }

    public void pollEvents(){
        glfwPollEvents();
    }

    public boolean shouldClose(){
        return glfwWindowShouldClose(windowHandle);
    }
    public long getWindowID(){
        return this.windowHandle;
    }


}
