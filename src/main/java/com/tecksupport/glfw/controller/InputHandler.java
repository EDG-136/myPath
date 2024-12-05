package com.tecksupport.glfw.controller;


import com.tecksupport.database.CourseQuery;
import com.tecksupport.database.FacultyQuery;
import com.tecksupport.database.NodeQuery;
import com.tecksupport.database.UserAuthQuery;
import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.pathfinder.DijkstraAlgorithm;
import com.tecksupport.glfw.pathfinder.node.Node;
import com.tecksupport.glfw.ui.AuthUI;
import com.tecksupport.glfw.ui.BuildingInfoUI;
import com.tecksupport.glfw.ui.ScheduleGeneratorUI;
import com.tecksupport.glfw.ui.FacultyInfoUI;
import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import imgui.ImColor;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Color;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector3f;
import org.joml.Vector4f;


import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;


public class InputHandler {
    private static final int ORIGINAL_WIDTH = 800;
    private static final int ORIGINAL_HEIGHT = 600;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private final List<Entity> allEntities = new ArrayList<>();
    private final List<Entity> pathNodeList = new ArrayList<>();
    private final CourseQuery courseQuery;
    private final UserAuthQuery userAuthQuery;
    private final FacultyQuery facultyQuery;
    private final NodeQuery nodeQuery;
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private RawModel rawModel;
    private TexturedModel texturedModel;
    private RawModel nodeModel;
    private TexturedModel nodeTextured;
    private Renderer renderer;
    private Loader loader;
    private RawModel square;
    private Entity entity;
    private ModelData modelData;
    private double oldYaw;
    private double oldPitch;
    private BuildingInfoUI buildingInfoUI;
    private AuthUI authUI;
    private FacultyInfoUI facultyInfoUI;
    private ScheduleGeneratorUI scheduleGeneratorUI;
    private List<Node> majorNodes;
    private float[] rgb = new float[3];
    private float[] hsv = new float[3];

    public InputHandler(CourseQuery courseQuery, UserAuthQuery userAuthQuery, FacultyQuery facultyQuery, NodeQuery nodeQuery)
    {
        this.courseQuery = courseQuery;
        this.userAuthQuery = userAuthQuery;
        this.facultyQuery = facultyQuery;
        this.nodeQuery = nodeQuery;
    }

    public void init() {
        window = new Window(ORIGINAL_WIDTH, ORIGINAL_HEIGHT, "myPath");
        window.init();
        loader = new Loader();
        shader = new Shader(
                "src/main/java/com/tecksupport/glfw/shader/vertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/fragmentShader.txt"
        );
        camera = new Camera(new Vector3f(0, 1100, 0), new Vector3f(0, 90, 0));
        renderer = new Renderer(shader, window, camera);

        rawModel = loader.loadToVAO(OBJFileLoader.loadOBJ("School"));
        texturedModel = new TexturedModel(rawModel, new ModelTexture(loader.loadTexture("SchoolTexture")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 20);


        nodeModel = loader.loadToVAO(OBJFileLoader.loadOBJ("Arrow"));
        nodeTextured = new TexturedModel(nodeModel, new ModelTexture(loader.loadTexture("ArrowTexture")));

        RawModel wayPointModel = loader.loadToVAO(OBJFileLoader.loadOBJ("Waypoint"));
        TexturedModel wayPointTexture = new TexturedModel(wayPointModel, new ModelTexture(loader.loadTexture("Waypoint")));

        allEntities.add(entity);
        // camera.createMatrix(45.0f, 0.1f, 100, shader, "camera");
        //Matrix4f camMat = camera.getMatrix(45.0f, 0.1f, 100, shader, "camera");
        // shader.setUniform("camera", camMat);

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
        scheduleGeneratorUI = new ScheduleGeneratorUI(window, courseQuery, facultyQuery, this, nodeQuery);

        majorNodes = nodeQuery.getNodes();

//        Node kelNode = nodeQuery.getEntryNodeFromFacultyID("KEL");
//        Entity majorNodeEntity = new Entity(wayPointTexture, kelNode.getPosition(), 0, 0, 0, 2);
//        majorNodeEntity.addPosition(0, 50, 0);
//        kelNode.setEntity(majorNodeEntity);
//
//        Node markNode = nodeQuery.getEntryNodeFromFacultyID("MARK");
//        Entity markEntity = new Entity(wayPointTexture, markNode.getPosition(), 0, 0, 0, 2);
//        markEntity.addPosition(0, 50, 0);
//        markNode.setEntity(markEntity);
//
////        Node node3 = nodeQuery.getNodes().get(13);
////        Entity node3Entity = new Entity(wayPointTexture, node3.getPosition(), 0, 0, 0, 2);
////        node3Entity.addPosition(0, 50, 0);
////        node3.setEntity(node3Entity);
////        System.out.println("Node: " + node3.getId());
////        System.out.println("Node: " + node3.getX() + " " + node3.getY() + " " + node3.getZ());
//
//        List<Node> shortestPath = DijkstraAlgorithm.getShortestPath(kelNode, markNode);
//        for (int i = 0; i < shortestPath.size() - 1; i++) {
//            drawPath(shortestPath.get(i), shortestPath.get(i + 1));
//            System.out.println("From " + shortestPath.get(i).getId() + " to " + shortestPath.get(i + 1).getId());
//        }

        for (Node node : majorNodes) {
            Entity majorNodeEntity = new Entity(wayPointTexture, node.getPosition(), 0, 0, 0, 2);
            majorNodeEntity.addPosition(0, 50, 0);
            node.setEntity(majorNodeEntity);
        }

//        for (Node from : nodeQuery.getNodes()) {
//            for (Node to : nodeQuery.getNodes()) {
//                List<Node> shortestPath = DijkstraAlgorithm.getShortestPath(from, to);
//                System.out.println("Finding Path From " + from.getId() + " to " + to.getId());
//                if (shortestPath.isEmpty()) {
//                    System.out.println("Can't find path from " + from.getId() + " to " + to.getId());
//                    continue;
//                }
//                System.out.print("\t");
//                for (Node node : shortestPath) {
//                    System.out.print(node.getId() + " -> ");
//                }
//                System.out.println();
//            }
//        }
    }

    public float getHueValue(int index, int total) {
        return (float) index / total;
    }

    public void run() {
        while (!window.shouldClose()) {
            startFrameImGui();

            if (!authUI.isLoggedIn()) {
                renderer.prepare(0f,0f,0f,0f);
                authUI.renderLoginPage();
            } else {
                // Only render the main application if the user is logged in
                processInput();
                renderer.prepare(0.6f, 0.8f, 1f, 1f);
                for (Entity instance : allEntities){
                    renderer.processEntity(instance);
                }

                for (Entity nodeOnPath : pathNodeList) {
                    renderer.processEntity(nodeOnPath);
                }
                renderer.render();
                buildingInfoUI.renderUI();
                facultyInfoUI.render();
                scheduleGeneratorUI.render();
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

    public void clearPath() {
        pathNodeList.clear();
    }

    public void drawPath(Node a, Node b) {
        float distanceBetweenPoints = 100;
        List<Node> pathPoints = generatePoints(a, b, distanceBetweenPoints);
        for (int i = 0; i < pathPoints.size(); i++) {
            float x = pathPoints.get(i).getX();
            float y = pathPoints.get(i).getY();
            float z = pathPoints.get(i).getZ();
            if (i + 1 != pathPoints.size()) {
                Vector3f lookAt = lookAtPostion(pathPoints.get(i + 1).getPosition(), pathPoints.get(i).getPosition());
                Entity nodeEntity = new Entity(nodeTextured, new Vector3f(x, y + 50, z), (float) Math.toDegrees(lookAt.x()), (float) Math.toDegrees(lookAt.y), 0, 0.25f);
                nodeEntity.setColor(new Vector4f(rgb[0], rgb[1], rgb[2], 0.1f));
                pathNodeList.add(nodeEntity);
            }
        }
    }

    public void addNodeToRender(Node node) {
        pathNodeList.add(node.getEntity());
    }

    private Vector3f lookAtPostion(Vector3f target, Vector3f positon){
        //math is dope
        Vector3f direction = new Vector3f(target.sub(positon).normalize());
        float yaw = (float) Math.atan2(direction.x, direction.z);
        float pitch = (float) Math.asin(direction.y);
        return new Vector3f(pitch, yaw, 0.0f);

    }

    public static List<Node> generatePoints(Node a , Node b, float distanceBetweenPoints){
        List<Node> points = new ArrayList<>();

        float x = b.getX() - a.getX();
        float y = b.getY() - a.getY();
        float z = b.getZ() - a.getZ();

        double distance = Math.sqrt(x * x + y * y + z * z);

        int numberOfPoints = (int) (distance / distanceBetweenPoints);

        //P=A+t⋅(B−A)
        for (int i = 0; i <= numberOfPoints; i ++){
            float t = i / (float) numberOfPoints;
            float pointX = a.getX() + t * (b.getX() - a.getX());
            float pointY = a.getY() + t * (b.getY() - a.getY());
            float pointZ = a.getZ() + t * (b.getZ() - a.getZ());
            points.add(new Node(0, pointX,pointY,pointZ));
        }
        return points;
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
