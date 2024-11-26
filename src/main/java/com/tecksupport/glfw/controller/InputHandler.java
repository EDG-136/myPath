package com.tecksupport.glfw.controller;


import com.tecksupport.database.CourseQuery;
import com.tecksupport.database.FacultyQuery;
import com.tecksupport.database.UserAuthQuery;
import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.ui.AuthUI;
import com.tecksupport.glfw.ui.BuildingInfoUI;
import com.tecksupport.glfw.ui.CourseSelectionUI;
import com.tecksupport.glfw.ui.FacultyInfoUI;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;
import org.lwjgl.system.Callback;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imgui.type.ImString;


import javax.swing.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL40.*;


public class InputHandler {
    private static final int ORIGINAL_WIDTH = 800;
    private static final int ORIGINAL_HEIGHT = 600;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final CourseQuery courseQuery;
    private final UserAuthQuery userAuthQuery;
    private final FacultyQuery facultyQuery;
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private RawModel rawModel;
    private Renderer renderer;
    private FrameBuffer frameBuffer;
    private Shader frameBufferShader;
    private Loader loader;
    private TexturedModel texturedModel;
    private RawModel square;
    private Entity entity;
    private ModelData modelData;
    private double oldYaw;
    private double oldPitch;
    private BuildingInfoUI buildingInfoUI;
    private AuthUI authUI;
    private FacultyInfoUI facultyInfoUI;
    private CourseSelectionUI courseSelectionUI;

    public InputHandler(CourseQuery courseQuery, UserAuthQuery userAuthQuery, FacultyQuery facultyQuery)
    {
        this.courseQuery = courseQuery;
        this.userAuthQuery = userAuthQuery;
        this.facultyQuery = facultyQuery;
    }

    public void init() {
        window = new Window(ORIGINAL_WIDTH, ORIGINAL_HEIGHT, "myPath");
        window.init();
        loader = new Loader();
        shader = new Shader(
                "src/main/java/com/tecksupport/glfw/shader/vertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt"
        );

        renderer = new Renderer(shader, window);
//
//        rawModel = loader.loadToVAO(OBJFileLoader.loadOBJ("School"));
//
//        texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("SchoolTexture")));

        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 200);
        camera = new Camera();
        camera.setPosition(0, 500, 0);
        camera.setRotation(0, 90, 0);
//         camera.createMatrix(45.0f, 0.1f, 100, shader, "camera");
//        Matrix4f camMat = camera.getMatrix(45.0f, 0.1f, 100, shader, "camera");
//         shader.setUniform("camera", camMat);

        glfwSetCursorPosCallback(window.getWindowID(), this::cursorCallback);
        glfwSetMouseButtonCallback(window.getWindowID(), this::mouseButtonCallback);


        System.out.println("Initializing ImGui");
        ImGui.createContext();

        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

        imGuiGlfw.init(window.getWindowID(), true);
        imGuiGl3.init(window.getGlslVersion());
        System.out.println("Initialized ImGui");

        io.setIniFilename(null);

        authUI = new AuthUI(window, userAuthQuery);
        buildingInfoUI = new BuildingInfoUI(window);
        facultyInfoUI = new FacultyInfoUI(window, facultyQuery);
        courseSelectionUI = new CourseSelectionUI(window, courseQuery);
    }

    public void run() {
        while (!window.shouldClose()) {
            startFrameImGui();

            if (!authUI.isLoggedIn()) {
                renderer.prepare(0f,0f,0f,0f);
//                authUI.renderLoginPage();
//                courseSelectionUI.render();
//                facultyInfoUI.render();
            } else {
                // Only render the main application if the user is logged in
                processInput();
                renderer.prepare(0.6f, 0.8f, 1f, 1f);
                shader.bind();
                shader.loadViewMatrix(camera);
                renderer.render(entity, shader);

                buildingInfoUI.renderUI();
            }

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

        camera.addRotation((float) yaw, (float) pitch, 0);

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
