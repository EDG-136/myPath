package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.Mesh;
import com.tecksupport.glfw.model.Shader;
import com.tecksupport.glfw.view.Window;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;

    float[] vertices = {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left

    };

    int[] indices = {

            0, 1, 3,   // first triangle
            1, 2, 3    // second triangle

    };

    public void init() {
        window = new Window(800, 600, "myPath");

        window.init();
        shader = new Shader("src/main/java/com/tecksupport/glfw/shader/vertexShader.txt", "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt");
        mesh = new Mesh(vertices, indices);


    }

    public void run() {
        while (!window.shouldClose()) {
            window.pollEvents();
            mesh.Draw(shader);

            processInput();



            window.update();
        }

        cleanup();
    }

    public void processInput() {
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_W) == GLFW_PRESS) {
            //camera.move(new Vector3f(1, 0, -1f));
        }
        // Handle more inputs here
    }

    public void cleanup() {
        // renderer.cleanup();
        // mesh.cleanup();
        window.cleanup();
    }

}

