package de.Bethibande.Engine.UI.rendering;

import de.Bethibande.Engine.Rendering.shaders.ShaderProgram;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

public class UIShader extends ShaderProgram {

    private static final String VERTEX_FILE = "/de/Bethibande/Engine/UI/rendering/uiVertex.txt";
    private static final String FRAGMENT_FILE = "/de/Bethibande/Engine/UI/rendering/uiFragment.txt";

    private static int location_transformation = 0;
    private static int location_color = 0;
    private static int location_size = 0;

    public UIShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void getAllUniformLocations() {
        location_transformation = super.getUniformLocation("transformationMatrix");
        location_color = super.getUniformLocation("color");
    }

    public void bindAttributes() {
        bindAttribute(0, "pos");
    }

    public void loadColor(Color c) {
        loadVector4(location_color, new Vector4f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1.0f));
    }

    public void loadTransformationMatrix(Matrix4f mat) {
        super.loadMatrix(location_transformation, mat);
    }

}
