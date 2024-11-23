package com.tecksupport.glfw.controller;

import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.ui.AuthUI;
import com.tecksupport.glfw.ui.BuildingInfoUI;
import com.tecksupport.glfw.renderer.GridRenderer;
import com.tecksupport.glfw.utils.Maths;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Matrix4f;
import org.joml.Vector3f;

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
    private double oldYaw;
    private double oldPitch;
    private BuildingInfoUI buildingInfoUI;
    private AuthUI authUI;

    // New GridRenderer instance for rendering the grid
    private GridRenderer gridRenderer;

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public void init() {
        window = new Window(800, 600, "myPath");
        window.init();
        loader = new Loader();
        shader = new Shader(
                "src/main/java/com/tecksupport/glfw/shader/vertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt"
        );
        renderer = new Renderer(shader, window);

        rawModel = loader.loadToVAO(OBJFileLoader.loadOBJ("School"));
        texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("SchoolTexture")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 0, 0, 10);

        camera = new Camera();

        // Initialize GridRenderer
        gridRenderer = new GridRenderer();

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

            if (!authUI.isLoggedIn()) {
                authUI.renderLoginPage();
            } else {
                // Only render the main application if the user is logged in
                processInput();
                renderer.prepare();

                // Calculate the map's transformation matrix
                Matrix4f mapPositionMatrix = Maths.createTransformationMatrix(
                        entity.getPosition(),
                        entity.getRotX(),
                        entity.getRotY(),
                        entity.getRotZ(),
                        entity.getScale()
                );

                // Render the grid
                gridRenderer.render(camera.getProjectionMatrix(), camera.getViewMatrix(), mapPositionMatrix);

                // Render the entity (map)
                shader.bind();
                shader.loadViewMatrix(camera);
                renderer.render(entity, shader);
                shader.unbind();

                // Render building UI
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
        gridRenderer.cleanup(); // Cleanup grid renderer resources
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
