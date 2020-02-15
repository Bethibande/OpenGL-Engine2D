package de.Bethibande.Engine.UI.rendering;

import de.Bethibande.Engine.Entities.RawModel;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public interface UIDrawable {

    public Vector2f getPosition();
    public Vector2f getSize();
    public Color getColor();
    public float getRotation();
    public RawModel getVAO();

}
