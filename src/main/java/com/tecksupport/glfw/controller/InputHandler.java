package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.Mesh;
import com.tecksupport.glfw.model.Shader;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Window;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;

    float[] vertices = {
            0.5f,  0.5f, 0.0f,  // top right
            0.5f, -0.5f, 0.0f,  // bottom right
            -0.5f, -0.5f, 0.0f,  // bottom left
            -0.5f,  0.5f, 0.0f   // top left

    };

    int[] indices = {

            0, 1, 3,   // first triangle
            3, 1, 2    // second triangle

    };
    Vector3f vec3 = new Vector3f(0.0f, 0.0f, 2.0f);

    public void init() {
        window = new Window(800, 600, "myPath");

        window.init();
        shader = new Shader("src/main/java/com/tecksupport/glfw/shader/vertexShader.txt", "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt");
        mesh = new Mesh(vertices, indices);
        camera = new Camera(800, 600, vec3);


    }

    public void run() {
        while (!window.shouldClose()) {
            processInput();
            mesh.Draw(shader);

            window.update();
            window.pollEvents();

        }

        cleanup();
    }

    public void processInput() {
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_W) == GLFW_PRESS) {
            camera.forward();
        }
        if(glfwGetKey(window.getWindowID(), GLFW_KEY_A) == GLFW_PRESS){
            camera.left();
        }
        if(glfwGetKey(window.getWindowID(), GLFW_KEY_S) == GLFW_PRESS){
            camera.backward();
        }
        if(glfwGetKey(window.getWindowID(), GLFW_KEY_D) == GLFW_PRESS){
            camera.right();
        }
        // Handle more inputs here
    }

    public void cleanup() {
        // renderer.cleanup();
        // mesh.cleanup();
        window.cleanup();
    }

}
