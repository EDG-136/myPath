package View;

import Model.Shader;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

public class Camera {

    public int width;
    public int height;
    public Vector3f position;
    public Vector3f orientation;
    public Vector3f Up;
    public float speed = 0.1f;
    public float sensitivity = 100.0f;

    public Camera(int w, int h, Vector3f p){
        this.width = w;
        this.height =h;
        this.position = p;
        this.orientation = new Vector3f(0.0f,0.0f,-1.0f);
        this.Up = new Vector3f(0.0f, 1.0f, 0.0f);

    }
    public void Matrix(float FOVdeg, float nearPlane, float farPlane, Shader shader, String uniform){
        Matrix4f view = new Matrix4f();
        view.identity();
        Matrix4f projection = new Matrix4f();
        projection.identity();

        float FOVrads = (float) Math.toRadians(FOVdeg);

        view.lookAt(position, position.add(orientation), Up);
        projection.perspective(FOVrads, (float)(width / height), nearPlane, farPlane);

        Matrix4f cameraMatrix = projection.mul(view);

        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer buffer = stack.mallocFloat(16);
            cameraMatrix.get(buffer);
            glUniformMatrix4fv(glGetUniformLocation(shader.getProgramId(), uniform), false, buffer );
        }
    }
}

