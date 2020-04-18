package de.Bethibande.Engine.Rendering.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class FBOMergeShader extends ShaderProgram {

    public static final String vertex = "/shader/mergeVertexShader.txt";
    public static final String fragment = "/shader/mergeFragmentShader.txt";


    public FBOMergeShader() {
        super(vertex, fragment);
    }

    @Override
    public void getAllUniformLocations() {
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinate");
    }

}
