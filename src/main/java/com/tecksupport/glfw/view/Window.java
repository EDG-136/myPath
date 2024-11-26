package com.tecksupport.glfw.view;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL40.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {

    private long windowHandle;
    private final int width;
    private final int height;
    private final String title;
    private final boolean resized;
    private String glslVersion = null;
    private GLFWVidMode vidmode;

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
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW: Setting hints for the window (OpenGL version, etc.)
        glfwDefaultWindowHints();
        decideGlGlslVersions();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Window is initially hidden
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Enable resizing

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(windowHandle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        glfwMakeContextCurrent(windowHandle);
        // Enable v-sync
        glfwSwapInterval(1);

        // Show the window
        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glfwSetFramebufferSizeCallback(windowHandle, this::onSizeCallBack);
    }

    private void decideGlGlslVersions() {
        final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
        if (isMac) {
            glslVersion = "#version 150";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);  // 3.2+ only
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);          // Required on Mac
        } else {
            glslVersion = "#version 130";
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
        }
    }

    public void onSizeCallBack(long window, int width, int height) {
        glViewport(0, 0, width, height);
    }

    public void update() {
        glfwSwapBuffers(windowHandle); // Swap the color buffer (double buffering)
        System.out.println("Swapped");
        glfwPollEvents();
    }

    public void cleanup() {
        // Release the key callback
        if (keyCallback != null) {
            keyCallback.free();
        }
        glfwDestroyWindow(windowHandle);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void pollEvents() {
        glfwPollEvents();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public long getWindowID() {
        return this.windowHandle;
    }

    public int getWindowWidth() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowHandle, w, null);
        return w.get(0);
    }

    public int getWindowHeight() {
        IntBuffer h = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(windowHandle, null, h);
        return h.get(0);
    }

    public String getGlslVersion() {
        return glslVersion;
    }

    public float getScreenWidth() {
        return vidmode.width();
    }
    public float getScreenHeight() {
        return vidmode.height();
    }
}
