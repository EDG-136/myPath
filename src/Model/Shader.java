package Model;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int programId;
    private int UniformID;

    public Shader(String vertexFile, String fragmentFile) {
        programId = glCreateProgram();
        int vertexShaderId = loadShader(vertexFile, GL_VERTEX_SHADER);
        int fragmentShaderId = loadShader(fragmentFile, GL_FRAGMENT_SHADER);

        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        glLinkProgram(programId);

        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }

    private int loadShader(String filePath, int type) {
        // Loading and compiling shaders would be handled here
        // Assume shaders are read from file and compiled
        return programId;
    }

    protected int getUniformLocation(String uniformName){
        return glGetUniformLocation(programId, uniformName);
    }

    public void setUniform(String uniformName, Matrix4f viewMatrix ){
        int location = getUniformLocation(uniformName);
        try(MemoryStack stack = MemoryStack.stackPush()){
            FloatBuffer buffer = stack.mallocFloat(16);
            viewMatrix.get(buffer);
            glUniformMatrix4fv(location,true, buffer);
        }

    }
}
