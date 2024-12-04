package com.tecksupport.glfw.view;

import com.tecksupport.glfw.model.Entity;
import com.tecksupport.glfw.model.RawModel;
import com.tecksupport.glfw.model.Shader;
import com.tecksupport.glfw.model.TexturedModel;
import com.tecksupport.glfw.utils.Maths;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL40.*;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 2000;

    private final Window window;

    private Matrix4f projectionMatrix;
    private final Shader shader;
    private final Camera camera;
    private final Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public Renderer(Shader s, Window w, Camera c) {
        this.window = w;
        this.shader = s;
        this.camera = c;
        shader.bind();
        createProjectionMatrix();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unbind();

    }
    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel model : entities.keySet()){
            prepareTexturedModel(model);
            List<Entity> instance = entities.get(model);
            for(Entity entity: instance){
                prepareInstance(entity);
                glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }
    private void prepareTexturedModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());

    }
    private void unbindTexturedModel(){
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

    }
    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()*20);
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadColorVec(entity.getColor());
    }


    public void prepare(float x, float y, float z, float w) {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(x, y, z, w);
    }


    public void render() {
//        TexturedModel model = entity.getModel();
//        RawModel rawModel = model.getRawModel();
//        glBindVertexArray(rawModel.getVaoID());
//        glEnableVertexAttribArray(0);
//        glEnableVertexAttribArray(1);
//        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
//                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()*20);
//        shader.loadTransformationMatrix(transformationMatrix);
//        glActiveTexture(GL_TEXTURE0);
//        glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
//        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
//        glDisableVertexAttribArray(0);
//        glDisableVertexAttribArray(1);
//        glBindVertexArray(0);
        shader.bind();
        shader.loadViewMatrix(camera);
        render(entities);
        shader.unbind();
        entities.clear();
    }

    public void cleanUp(){
        shader.cleanup();
    }

    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), (float) window.getWindowWidth() / window.getWindowWidth(), NEAR_PLANE, FAR_PLANE);
    }
    public void processEntity(Entity entity){
        if (entity == null)
            return;
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if(batch!=null){
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }
}