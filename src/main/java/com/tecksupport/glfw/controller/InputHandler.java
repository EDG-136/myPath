package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.ui.AuthUI;
import com.tecksupport.glfw.ui.BuildingInfoUI;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.Callback;

import java.nio.DoubleBuffer;
import java.util.*;

import imgui.type.ImString;


import javax.swing.*;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private RawModel rawModel;
    private RawModel fishModel;
    private Renderer renderer;
    private Loader loader;
    private TexturedModel texturedModel;
    private TexturedModel fishTextured;
    private RawModel square;
    private Entity entity;
    private ModelData modelData;
    private double oldYaw;
    private double oldPitch;
    private BuildingInfoUI buildingInfoUI;
    private AuthUI authUI;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private Random random = new Random();

    private List<Entity> allEntities = new ArrayList<Entity>();

    public void init() {
        window = new Window(800, 600, "myPath");
        window.init();
        loader = new Loader();
        shader = new Shader(
                "src/main/java/com/tecksupport/glfw/shader/vertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt"
        );
        rawModel = loader.loadToVAO(OBJFileLoader.loadOBJ("School"));

        texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("SchoolTexture")));

        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 20);

        camera = new Camera();
        renderer = new Renderer(shader, window, camera);
        fishModel = loader.loadToVAO(OBJFileLoader.loadOBJ("Fish"));
        fishTextured = new TexturedModel(fishModel, new ModelTexture(loader.loadTexture("fish_texture")));
        allEntities.add(entity);

        for(int i =0; i < 1000; i++){
            float x = random.nextFloat() * 1000;
            float y = random.nextFloat()* 1000;
            float z = random.nextFloat()* 1000;
            allEntities.add(new Entity(fishTextured, new Vector3f(x,y,z - 25f), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
        }
//        camera.createMatrix(45.0f, 0.1f, 100, shader, "camera");
//        Matrix4f camMat = camera.getMatrix(45.0f, 0.1f, 100, shader, "camera");
//        shader.setUniform("camera", camMat);

        glfwSetCursorPosCallback(window.getWindowID(), this::cursorCallback);
        glfwSetMouseButtonCallback(window.getWindowID(), this::mouseButtonCallback);

        System.out.println("Initializing ImGui");
        ImGui.createContext();
        imGuiGlfw.init(window.getWindowID(), true);
        imGuiGl3.init(window.getGlslVersion());
        System.out.println("Initialized ImGui");

        authUI = new AuthUI(window);
        buildingInfoUI = new BuildingInfoUI(window);
    }

    public void run() {
        while (!window.shouldClose()) {
            startFrameImGui();

//            if (!authUI.isLoggedIn()) {
//                authUI.renderLoginPage();
//            } else {
                // Only render the main application if the user is logged in
                processInput();
                for(Entity instance : allEntities){
                    renderer.processEntity(instance);
                }
                renderer.render();
                buildingInfoUI.renderUI();
//            }

            endFrameImGui();
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
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    void cursorCallback(long window, double xPos, double yPos) {
        if (glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_LEFT) != GLFW_PRESS) {
            oldYaw = xPos;
            oldPitch = yPos;
            return;
        }

        double yaw = xPos - oldYaw;
        double pitch = yPos - oldPitch;

        camera.addRotation((float) pitch, (float) yaw, 0);

        oldYaw = xPos;
        oldPitch = yPos;
    }

    public void cleanup() {
        shader.cleanup();
        loader.cleanUp();
        // renderer.cleanup();
        // mesh.cleanup();
        imGuiGl3.shutdown();
        imGuiGlfw.shutdown();
        ImGui.destroyContext();
        window.cleanup();
    }
    void startFrameImGui() {
        imGuiGl3.newFrame();
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    void endFrameImGui() {
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }
    }
}
