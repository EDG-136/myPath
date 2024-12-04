package com.tecksupport.glfw.utils;

import com.tecksupport.glfw.model.Entity;
import com.tecksupport.glfw.model.TexturedModel;
import com.tecksupport.glfw.pathfinder.node.Node;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathHandler {

    ArrayList<Node> corners;
    ArrayList<Node> points;

    public PathHandler(){
        read();
    }

    public ArrayList<Node> getCorners() {
        return corners;
    }
    public ArrayList<Node> getPoints(){
        return points;
    }

    private void read(){
        String filename = "nodes.txt";

        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            while ((line = reader.readLine()) != null){
                Node node = parseLine(line);
                corners.add(node);
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
    public static ArrayList<Node> generatePoints(Node a , Node b, int numberOfPoints){
        ArrayList<Node> points = new ArrayList<>();

        //P=A+t⋅(B−A)
        for (int i = 0; i <= numberOfPoints; i ++){
            float t = i / (float) numberOfPoints;
            float x = a.getX() + t * (b.getX() - a.getX());
            float y = a.getY() + t * (b.getY() - a.getY());
            float z = a.getZ() + t * (b.getZ() - a.getZ());
            points.add(new Node(0, x,y,z));
        }
        return points;
    }
    private void generateFullPath(){

        for(int i = 0; i < corners.size(); i++){
            if(i != corners.size() -1){
                generatePath(corners.get(i), corners.get(i+1));
            }
        }
    }
    private void generatePath(Node a, Node b){
        int numberOfPoints = 10;
        ArrayList<Node> pathPoints = generatePoints(a,b,numberOfPoints);
        for(int i = 0; i < pathPoints.size(); i++){
            float x = pathPoints.get(i).getX();
            float y = pathPoints.get(i).getY();
            float z = pathPoints.get(i).getZ();

            if(i+1 != pathPoints.size()){

                points.add(new Node(0, x,y,z));
//                Vector3f lookAt = Maths.lookAtPostion(pathPoints.get(i+1).getPositon(), pathPoints.get(i).getPositon());
//                allNodes.add(new Entity(nodeTextured, new Vector3f(x,y,z), (float) Math.toDegrees(lookAt.x()),(float) Math.toDegrees(lookAt.y) , 0,0.25f));
            }
            else{
                points.add(new Node(0, x,y,z));
//                allNodes.add(new Entity(nodeTextured, new Vector3f(x,y,z), 0,0 , 0,0.25f));
            }
        }
    }


}
