package glfw.model;

import org.joml.Vector3f;

public class Vertex {

    public Vector3f position;
    public Vector3f normal;
    public Vector3f color;
    public Vector3f texUV;

    public Vertex(Vector3f p, Vector3f n, Vector3f c, Vector3f uv) {

        this.position = p;
        this.normal = n;
        this.color = c;
        this.texUV = uv;
    }
}

