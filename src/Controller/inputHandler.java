package Controller;

import View.Renderer;
import Model.Mesh;
import Model.Camera;
import View.Window;
import org.lwjgl.glfw.GLFW;
import org.joml.Vector3f;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;


public class TECK_Engine {
    private Window window;
    private Renderer renderer;
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
            1, 2, 3    // second triangle

    };

    public void init() {
        window = new Window(800, 600, "3D Engine");
        window.init();

        renderer = new Renderer();
        renderer.init();

        mesh = new Mesh(vertices, indices); // Load mesh with data

        camera = new Camera();
    }

    public void run() {
        while (!window.shouldClose()) {
            window.pollEvents();

            processInput();
            renderer.render(mesh, camera);

            window.update();
        }

        cleanup();
    }

    public void processInput() {
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_W) == GLFW_PRESS) {
            camera.move(new Vector3f(1, 0, -1f));
        }
        // Handle more inputs here
    }

    public void cleanup() {
        renderer.cleanup();
        mesh.cleanup();
        window.cleanup();
    }

}
