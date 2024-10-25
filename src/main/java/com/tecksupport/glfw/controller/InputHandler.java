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
    private RawModel rawModel;
    private Renderer renderer = new Renderer();
    private Loader loader;
    private TexturedModel texturedModel;
    private RawModel square;

    float[] vertices = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, 0.5f, 0
    };

    int[] indices ={
            0,1,3,
            3,1,2
    };


    public void init() {
        window = new Window(800, 600, "myPath");
        window.init();

        loader = new Loader();

        square = loader.loadToVAO(vertices, indices);


        shader = new Shader("src/main/java/com/tecksupport/glfw/shader/vertexShader.txt", "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt");;
        //rawModel = Model.loadModel("src/main/java/com/tecksupport/glfw/model/stall.obj", loader);
        texturedModel = new TexturedModel(rawModel, new Texture("src/main/java/com/tecksupport/glfw/model/stallTexture.png"));

        //camera = new Camera();
        //camera.createMatrix(70.0f, 0.1f, 1000, shader, "camera");



    }

    public void run() {


        while (!window.shouldClose()) {


            renderer.prepare();
            shader.bind();
            renderer.render(square);
            shader.unbind();
            //renderer.render(mesh);
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
        shader.cleanup();
        loader.cleanUp();
        // renderer.cleanup();
        // mesh.cleanup();
        window.cleanup();
    }

}
