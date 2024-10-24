package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private Loader loader = new Loader();
    private Renderer renderer = new Renderer();

    float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f

    };

    int[] indices = {

            0, 1, 3,   // first triangle
            3, 1, 2    // second triangle

    };
    Vector3f vec3 = new Vector3f(0.0f, 0.0f, 2.0f);

    public void init() {
        window = new Window(800, 600, "myPath");

        window.init();
//        shader = new Shader("src/main/java/com/tecksupport/glfw/shader/vertexShader.txt", "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt");
//        mesh = new Mesh(vertices, indices);
//        camera = new Camera(800, 600, vec3);


    }

    public void run() {
//        Model model;
//        try {
//            model = new Model("src/main/java/com/tecksupport/glfw/model/stall.obj", loader);
//        } catch (IOException e) {
//            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Model Loading Error", e);
//            return;
//        }
        RawModel rawModel = loader.loadToVAO(vertices, indices);
        while (!window.shouldClose()) {
//            processInput();
//            mesh.Draw(shader);
//            model.Draw(shader);
            renderer.prepare();
            renderer.render(rawModel);

            window.update();
            window.pollEvents();

        }

        cleanUp();
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

    public void cleanUp() {
        // renderer.cleanup();
        // mesh.cleanup();
        loader.cleanUp();
        window.cleanup();
    }

}
