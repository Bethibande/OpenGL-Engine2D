package de.Bethibande.Engine.Rendering.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class FinalShader extends ShaderProgram {

    public static final String vertex = "/shader/finalVertexShader.txt";
    public static final String fragment = "/shader/finalFragmentShader.txt";

    private int location_projectionMatrix;

    public FinalShader() {
        super(vertex, fragment);
    }

    @Override
    public void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinate");
    }

    public void loadProjectioMatrix(Matrix4f mat) { loadMatrix(location_projectionMatrix, mat); }

}
