package com.tecksupport.glfw.controller;


import com.tecksupport.glfw.model.*;
import com.tecksupport.glfw.ui.AuthUI;
import com.tecksupport.glfw.ui.BuildingInfoUI;
import com.tecksupport.glfw.utils.Node;

import com.tecksupport.glfw.view.Camera;
import com.tecksupport.glfw.view.Renderer;
import com.tecksupport.glfw.view.Window;
import imgui.ImGui;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector3f;

import java.io.*;
import java.util.*;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11C.GL_LINE_STRIP;

public class InputHandler {
    private Window window;
    private Shader shader;
    private Mesh mesh;
    private Camera camera;
    private RawModel rawModel;
    private RawModel nodeModel;
    private Renderer renderer;
    private Loader loader;
    private TexturedModel texturedModel;
    private TexturedModel nodeTextured;
    private RawModel square;
    private Entity entity;
    private ModelData modelData;
    private double oldYaw;
    private double oldPitch;
    private BuildingInfoUI buildingInfoUI;
    private AuthUI authUI;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    ArrayList<Node> nodes = new ArrayList<>();


    private static double lastPressedTime = 0;
    private static final double pressDelay = 0.3;

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
        nodeModel = loader.loadToVAO(OBJFileLoader.loadOBJ("Arrow"));
        nodeTextured = new TexturedModel(nodeModel, new ModelTexture(loader.loadTexture("ArrowTexture")));
        allEntities.add(entity);
        read();
        for(int i = 1; i < nodes.size(); i++){
            float x = nodes.get(i).getX();
            float y = nodes.get(i).getY();
            float z = nodes.get(i).getZ();

            allEntities.add(new Entity(nodeTextured, new Vector3f(x,y+20,z), 90.0f,0,0, 0.5f));

        }

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
        //export();
        cleanup();
    }

    public void processInput() {

        double currentTime = glfwGetTime();

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
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_N)== GLFW_PRESS){
            Vector3f pos = camera.getPosition();
            if (currentTime - lastPressedTime >= pressDelay){
                allEntities.add(new Entity(nodeTextured, new Vector3f(pos.x(), pos.y(), pos.z()), 0,  0, 0f, 0.5f));
                lastPressedTime = currentTime;
            }
        }
        if (glfwGetKey(window.getWindowID(), GLFW_KEY_M)== GLFW_PRESS){
            if (currentTime - lastPressedTime >= pressDelay){
                allEntities.removeLast();
                lastPressedTime = currentTime;
            }
        }
        if(glfwGetKey(window.getWindowID(), GLFW_KEY_ENTER)== GLFW_PRESS){
            if(currentTime - lastPressedTime >= pressDelay){
                drawFullPath(nodes);
            }
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
    private void read(){
        String filename = "nodes.txt";

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null){
                Node node = parseLine(line);
                nodes.add(node);
            }

        }catch (IOException e){
            System.err.println("Error reading the file: " + e.getMessage());
        }

    }
    private Node parseLine(String line){
        try{
            String[] parts = line.split(":");
            int id = Integer.parseInt(parts[0].trim());
            String[] coords = parts[1].split(",");
            float x = Float.parseFloat(coords[0].trim());
            float y = Float.parseFloat(coords[1].trim());
            float z = Float.parseFloat(coords[2].trim());

            return new Node(id,x,y,z);

        }catch (Exception e){
            System.err.println("Error Parsing Strings: "+line);
            return null;
        }

    }
//    private void createNodes(){
//        ArrayList<Node> nodes;
//        for(Node node : nodeList){
//
//        }
//    }
    private void export(){
        String filename = "nodes.txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            int id = 0;
            for(Entity e: allEntities){
                Vector3f position = e.getPosition();
                String line = id + ":" + position.x + "," + position.y+ "," + position.z;
                //String line = position.x + "," + position.y+ "," + position.z;
                writer.write(line);
                writer.newLine();
                id++;
            }
        }catch(IOException e){
            System.err.println("ERROR: "+ e.getMessage());
        }
    }
    private void drawPath(List<Entity> corner){

        glBegin(GL_LINE_STRIP);
        glColor3f(0.0f, 1.0f, 0.0f);

//        glLineWidth(100.0f);
//        for(int i = 1; i <corner.size(); i++){
//            Entity node = corner.get(i);
//            float x = node.getX();
//            float y= node.getY();
//            float z= node.getZ();
//            glVertex3f(x,y,z);
//        }
        glVertex3f(0.0f, 0.0f, 0.0f);
        glVertex3f(50.0f, 50.0f, 50.0f);


        glEnd();

    }
    private void drawFullPath(ArrayList<Node> nodes){
        for(int i = 0; i < nodes.size(); i++){
            if(i != nodes.size() -1){
                drawPath(nodes.get(i), nodes.get(i+1));
            }

           // drawPath(nodes.get(i), nodes.get(i+1));
        }

    }
    private void drawPath(Node a, Node b){
        int numberOfPoints = 10;
        ArrayList<Node> pathPoints = generatePoints(a,b,numberOfPoints);
        for(int i = 0; i < pathPoints.size(); i++){
            float x = pathPoints.get(i).getX();
            float y = pathPoints.get(i).getY();
            float z = pathPoints.get(i).getZ();

            if(i+1 != pathPoints.size()){
                Vector3f lookAt = lookAtPostion(pathPoints.get(i+1).getPositon(), pathPoints.get(i).getPositon());
                allEntities.add(new Entity(nodeTextured, new Vector3f(x,y,z), (float) Math.toDegrees(lookAt.x()),(float) Math.toDegrees(lookAt.y) , 0,0.25f));
            }
            else{
                allEntities.add(new Entity(nodeTextured, new Vector3f(x,y,z), 0,0 , 0,0.25f));
            }

        }
    }

    private Vector3f lookAtPostion(Vector3f target, Vector3f positon){

        //math is dope
        Vector3f direction = new Vector3f(target.sub(positon).normalize());
        float yaw = (float) Math.atan2(direction.x, direction.z);
        float pitch = (float) Math.asin(direction.y);
        return new Vector3f(pitch, yaw, 0.0f);

    }

    public static ArrayList<Node> generatePoints(Node a , Node b, int numberOfPoints){
        ArrayList<Node> points = new ArrayList<>();

        //P=A+t⋅(B−A)
        for (int i = 0; i <= numberOfPoints; i ++){
            float t = i / (float) numberOfPoints;
            float x = a.getX() + t * (b.getX() - a.getX());
            float y = a.getY() + t * (b.getY() - a.getY());
            float z = a.getZ() + t * (b.getZ() - a.getZ());
            points.add(new Node(x,y,z));
        }
        return points;
    }


    public void cleanup() {
        shader.cleanup();
        loader.cleanUp();

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
