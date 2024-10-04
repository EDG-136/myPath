package Controller;


import View.Window;
import org.lwjgl.glfw.GLFW;
import org.joml.Vector3f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;


public class inputHandler {
    private Window window;

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


    }

    public void run() {
        while (!window.shouldClose()) {
            window.pollEvents();

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
