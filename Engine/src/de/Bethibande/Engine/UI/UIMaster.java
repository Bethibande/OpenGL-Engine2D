package de.Bethibande.Engine.UI;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.UI.rendering.UIDrawable;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIMaster {

    public static final List<UIDrawable> elements = new ArrayList<>();

    public static RawModel load(UIElement element) {
        float[] verts = {
                0.5f, 0.5f,
                0.5f, -0.5f,
                -0.5f, -0.5f,
                -0.5f, 0.5f
        };
        float[] texture = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        int[] indices = {
                0, 1, 2,
                0, 3, 2
        };
        Color c = element.getColor();
        RawModel quad = EngineCore.loader.loadUI(verts, indices, new Vector3f(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f));
        return quad;
    }

}
