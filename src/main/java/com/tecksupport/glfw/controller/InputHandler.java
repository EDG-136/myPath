package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.Callback;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private RawModel rawModel;
    private Renderer renderer;
    private Loader loader;
    private TexturedModel texturedModel;
    private RawModel square;
    private Entity entity;
    private ModelData modelData;
    private Callback mouseMovement;
    private Callback mouseButton;
    private Vector3f mouseRotatePos = new Vector3f();

    float[] vertices = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, 0.5f, 0,

            -0.5f, 0.5f, 1,
            -0.5f, -0.5f, 1,
            0.5f, -0.5f, 1,
            0.5f, 0.5f, 1,

            0.5f, 0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, -0.5f, 1,
            0.5f, 0.5f, 1,

            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            -0.5f, -0.5f, 1,
            -0.5f, 0.5f, 1,

            -0.5f, 0.5f, 1,
            -0.5f, 0.5f, 0,
            0.5f, 0.5f, 0,
            0.5f, 0.5f, 1,

            -0.5f, -0.5f, 1,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, -0.5f, 1

    };

    float[] textureCoords = {

            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0


    };

    int[] indices = {
            0, 1, 3,
            3, 1, 2,
            4, 5, 7,
            7, 5, 6,
            8, 9, 11,
            11, 9, 10,
            12, 13, 15,
            15, 13, 14,
            16, 17, 19,
            19, 17, 18,
            20, 21, 23,
            23, 21, 22


    };


    public void init() {
        window = new Window(800, 600, "myPath");
        window.init();
        loader = new Loader();
        shader = new Shader(
                "src/main/java/com/tecksupport/glfw/shader/vertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt"
        );
        renderer = new Renderer(shader, window);

//        rawModel = loader.loadToVAO(vertices, textureCoords, indices);

        rawModel = loader.loadToVAO(OBJFileLoader.loadOBJ("School"));

        texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("SchoolTexture")));

        entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 10);

        camera = new Camera();
//        camera.createMatrix(45.0f, 0.1f, 100, shader, "camera");
//
//        Matrix4f camMat = camera.getMatrix(45.0f, 0.1f, 100, shader, "camera");
//        shader.setUniform("camera", camMat);

        mouseMovement = glfwSetCursorPosCallback(window.getWindowID(), this::cursorCallback);
        mouseButton = glfwSetMouseButtonCallback(window.getWindowID(), this::mouseButtonCallback);

    }

    public void run() {
        while (!window.shouldClose()) {
//            entity.increasePosition(1, 1, 0);
//            entity.increaseRotation(0, 1, 0);
            processInput();
            renderer.prepare();
            shader.bind();
            shader.loadViewMatrix(camera);
            //camera.createMatrix(45.0f, 0.1f, 100, shader, "camera");

            renderer.render(entity, shader);
//            renderer.render(rawModel);
//            renderer.render(entity, shader);
            shader.unbind();
            window.update();
        }
        cleanup();
    }

    public void processInput() {
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_W) == GLFW_PRESS) {
            camera.forward();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_A) == GLFW_PRESS) {
            camera.left();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_S) == GLFW_PRESS) {
            camera.backward();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_D) == GLFW_PRESS) {
            camera.right();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_SPACE) == GLFW_PRESS) {
            camera.up();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_LEFT_ALT) == GLFW_PRESS) {
            camera.down();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_Q) == GLFW_PRESS) {
            camera.yawLeft();
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_E) == GLFW_PRESS) {
            camera.yawRight();
        }
        // Handle more inputs here
    }


    void mouseButtonCallback(long window, int button, int action, int mods) {
        if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            DoubleBuffer yaw = BufferUtils.createDoubleBuffer(1);
            DoubleBuffer pitch = BufferUtils.createDoubleBuffer(1);

            glfwGetCursorPos(window, yaw, pitch);

            mouseRotatePos.x = (float) yaw.get(0);
            mouseRotatePos.y = (float) pitch.get(0);
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    void cursorCallback(long window, double xPos, double yPos) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) != GLFW_PRESS)
            return;
        double yaw = xPos - mouseRotatePos.x;
        double pitch = yPos - mouseRotatePos.y;

        camera.addRotation((float) pitch, (float) yaw, 0);

        mouseRotatePos.x = (float) xPos;
        mouseRotatePos.y = (float) yPos;
    }

    public void cleanup() {
        shader.cleanup();
        loader.cleanUp();
        // renderer.cleanup();
        // mesh.cleanup();
        window.cleanup();
    }

}
