package de.Bethibande.Engine.Rendering.shaders;

import de.Bethibande.Engine.Camera;
import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Lights.PointLight;
import de.Bethibande.Engine.utils.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class DefaultShader extends ShaderProgram {

    public static final String vertex_shader = "/shader/vertexShader.txt";
    public static final String fragment_shader = "/shader/fragmentShader.txt";

    private int location_projectionMatrix;
    private int location_transformationMatrix;
    private int location_viewMatrix;

    public static final int maxLights = 100;
    private int[] location_lightPos = new int[maxLights];
    private int[] location_lightColor = new int[maxLights];
    private int[] location_lightRange = new int[maxLights];
    private int location_loadedLights;

    private int location_clip;
    private int location_zIndex;

    public DefaultShader() {
        super(vertex_shader, fragment_shader);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = getUniformLocation("projectionMatrix");
        location_viewMatrix = getUniformLocation("viewMatrix");
        location_transformationMatrix = getUniformLocation("transformationMatrix");

        location_loadedLights = getUniformLocation("loadedLights");

        location_lightPos = new int[maxLights];
        location_lightColor = new int[maxLights];
        location_lightRange = new int[maxLights];

        for(int i = 0; i < maxLights; i++) {
            location_lightPos[i] = getUniformLocation("lightPos[" + i + "]");
            location_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
            location_lightRange[i] = getUniformLocation("lightRange[" + i + "]");
        }

        location_clip = getUniformLocation("clip");
        location_zIndex = getUniformLocation("zIndex");
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

    public void loadScreenClip() {
        //loadVector4(location_clip, new Vector4f(-EngineCore.cam.getPosition().x, -EngineCore.cam.getPosition().y,
        //        EngineCore.fullscreenMode.getWidth()-EngineCore.cam.getPosition().x, EngineCore.fullscreenMode.getHeight()-EngineCore.cam.getPosition().y));
        loadVector4(location_clip, new Vector4f(0, 0, EngineCore.fullscreenMode.getWidth(), EngineCore.fullscreenMode.getHeight()));
    }

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

    public void setZIndex(float index) {
        loadFloat(location_zIndex, -index);
    }

}
