package com.tecksupport.glfw.model;

import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Model {
    private ArrayList<Mesh> meshes;
    private ArrayList<Character> data;
    //json type
    private ArrayList<Vector3f> translationsMeshes;
    private ArrayList<Quaterniond> rotationMeshes;
    private ArrayList<Vector3f> scalesMeshes;
    private ArrayList<Matrix4f> matricesMeshes;
    private ArrayList<String> loadedTexName;
    private ArrayList<Texture> loadedTex;


    public Model(char file){

    }
    public void Draw(Shader shader){

    }
}
