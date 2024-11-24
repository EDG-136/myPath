package com.tecksupport.glfw.view;

import com.tecksupport.glfw.model.Entity;
import com.tecksupport.glfw.model.RawModel;
import com.tecksupport.glfw.model.Shader;
import com.tecksupport.glfw.model.TexturedModel;
import com.tecksupport.glfw.utils.Maths;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL40.*;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;

    private final Window window;

    private Matrix4f projectionMatrix;

    public Renderer(Shader shader, Window window) {
        this.window = window;
        createProjectionMatrix();
        shader.bind();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.unbind();
    }


    public void prepare(float red, float green, float blue, float alpha) {
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glClearColor(red, green, blue, alpha);
    }


    public void render(Entity entity, Shader shader) {
        TexturedModel model = entity.getModel();
        RawModel rawModel = model.getRawModel();
        glBindVertexArray(rawModel.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()*20);
        shader.loadTransformationMatrix(transformationMatrix);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
        glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    private void createProjectionMatrix() {
        projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), (float) window.getWindowWidth() / window.getWindowWidth(), NEAR_PLANE, FAR_PLANE);
    }
}
