package com.tecksupport.glfw.renderer;

import com.tecksupport.glfw.model.Vao;
import com.tecksupport.glfw.model.Vbo;
import com.tecksupport.glfw.model.Shader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class GridRenderer {
    private final Shader gridShader;
    private final Vao gridVao;
    private int lineCount; // Number of grid lines
    private final Matrix4f modelMatrix = new Matrix4f(); // Model matrix for grid transformations

    public GridRenderer() {
        // Load a simple shader for the grid
        gridShader = new Shader("src/main/java/com/tecksupport/glfw/shader/gridVertexShader.txt",
                "src/main/java/com/tecksupport/glfw/shader/gridFragmentShader.txt");

        // Initialize the VAO and VBO for gridlines
        gridVao = new Vao();
        createGrid(500, 500, 5.0f); // Adjusted: 500x500 grid with 5 units per cell
    }

    private void createGrid(int rows, int cols, float cellSize) {
        lineCount = (rows + 1) + (cols + 1); // Total horizontal and vertical lines
        float[] gridVertices = new float[lineCount * 4]; // 4 floats per line (x1, y1, x2, y2)

        int index = 0;

        // Horizontal lines
        for (int i = 0; i <= rows; i++) {
            float y = i * cellSize;
            gridVertices[index++] = 0.0f;   // x1
            gridVertices[index++] = y;     // y1
            gridVertices[index++] = cols * cellSize; // x2
            gridVertices[index++] = y;     // y2
        }

        // Vertical lines
        for (int i = 0; i <= cols; i++) {
            float x = i * cellSize;
            gridVertices[index++] = x;     // x1
            gridVertices[index++] = 0.0f;  // y1
            gridVertices[index++] = x;     // x2
            gridVertices[index++] = rows * cellSize; // y2
        }

        // Create and bind the VBO
        Vbo gridVbo = new Vbo(gridVertices);
        gridVao.LinkVBO(gridVbo, 0, 2, 8, 0); // Each vertex has 2 floats (x, y)
    }

    public void render(Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f mapPositionMatrix) {
        gridShader.bind();

        // Extract map's position from its transformation matrix
        float mapX = mapPositionMatrix.m30(); // Map center X
        float mapY = mapPositionMatrix.m31(); // Map center Y
        float mapZ = mapPositionMatrix.m32(); // Map center Z

        // Translate the grid directly under the map's position
        modelMatrix.identity()
                .translate(mapX - 100.0f, mapY - 0.1f, mapZ - 100.0f) // Adjust position to align the grid
                .rotate((float) Math.toRadians(-90.0f), 1.0f, 0.0f, 0.0f);

        // Load matrices into the shader
        loadMatrixToShader(gridShader, "modelMatrix", modelMatrix);
        loadMatrixToShader(gridShader, "projectionMatrix", projectionMatrix);
        loadMatrixToShader(gridShader, "viewMatrix", viewMatrix);

        gridVao.bind();
        GL40.glDrawArrays(GL40.GL_LINES, 0, lineCount * 2); // Draw all lines
        gridVao.unbind();

        gridShader.unbind();
    }

    public void cleanup() {
        gridShader.cleanup();
        gridVao.cleanup();
    }

    private void loadMatrixToShader(Shader shader, String uniformName, Matrix4f matrix) {
        int location = shader.getUniformLocation(uniformName);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.get(buffer);
        GL40.glUniformMatrix4fv(location, false, buffer);
    }
}
