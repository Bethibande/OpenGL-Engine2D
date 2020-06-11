package de.Bethibande.Engine.Rendering.shaders;

public class FBOMergeShader extends ShaderProgram {

    public static final String vertex = "/shader/mergeVertexShader.txt";
    public static final String fragment = "/shader/mergeFragmentShader.txt";

    private int location_index;

    public FBOMergeShader() {
        super(vertex, fragment);
    }

    @Override
    public void getAllUniformLocations() {
        location_index = getUniformLocation("index");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinate");
    }

    public void setIndex(float index) { loadFloat(location_index, index); }

}
