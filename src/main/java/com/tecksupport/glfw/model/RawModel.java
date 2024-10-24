package com.tecksupport.glfw.model;

public class RawModel {
    private final int vaoID;
    private final int vertexCount;

    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }
    public int getVaoID(){
        return this.vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
