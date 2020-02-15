package de.Bethibande.Engine.Rendering.shaders;

import de.Bethibande.Engine.Camera;
import de.Bethibande.Engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;

public class DefaultShader extends ShaderProgram {

    public static final String vertex_shader = "/shader/vertexShader.txt";
    public static final String fragment_shader = "/shader/fragmentShader.txt";

    private int location_projectionMatrix;
    private int location_transformationMatrix;
    private int location_viewMatrix;

    public DefaultShader() {
        super(vertex_shader, fragment_shader);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_transformationMatrix = getUniformLocation("transformationMatrix");
    }
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinate");
    }

    public void loadProjectionMatrix(Matrix4f m) {
        loadMatrix(location_projectionMatrix, m);
    }

    public void loadTransformatiobMatrix(Matrix4f m) {
        loadMatrix(location_transformationMatrix, m);
    }

    public void loadViewMatrix(Camera c) { loadMatrix(location_viewMatrix, Maths.createViewMatrix(c)); }

}
