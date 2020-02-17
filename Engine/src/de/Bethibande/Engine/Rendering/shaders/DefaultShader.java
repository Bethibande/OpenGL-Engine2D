package de.Bethibande.Engine.Rendering.shaders;

import de.Bethibande.Engine.Camera;
import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Lights.PointLight;
import de.Bethibande.Engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class DefaultShader extends ShaderProgram {

    public static final String vertex_shader = "/shader/vertexShader.txt";
    public static final String fragment_shader = "/shader/fragmentShader.txt";

    private int location_projectionMatrix;
    private int location_transformationMatrix;
    private int location_viewMatrix;

    private int[] location_lightPos;
    private int[] location_lightColor;
    private int[] location_lightRange;
    private int location_loadedLights;
    public static final int maxLights = 100;

    public DefaultShader() {
        super(vertex_shader, fragment_shader);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_transformationMatrix = getUniformLocation("transformationMatrix");

        location_loadedLights = getUniformLocation("loadedLights");
        for(int i = 0; i < maxLights; i++) {
            location_lightPos[i] = getUniformLocation("lightPos[" + i + "]");
            location_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
            location_lightRange[i] = getUniformLocation("lightRange[" + i + "]");
        }

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

    public void loadLights() {
        if(EngineCore.currentScene != null ) {
            loadInt(location_loadedLights, EngineCore.currentScene.getLights().size());
            int i = 0;
            for(PointLight l : EngineCore.currentScene.getLights()) {
                loadVector2(location_lightPos[i], l.getPosition());
                loadVector(location_lightColor[i], new Vector3f(l.getColor().getRed()/255f, l.getColor().getGreen()/255f, l.getColor().getBlue()/255f));
                loadFloat(location_lightRange[i], l.getRange());
                i++;
            }
        }
    }

}
