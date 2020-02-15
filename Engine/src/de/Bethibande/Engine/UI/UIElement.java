package de.Bethibande.Engine.UI;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.RawModel;
import de.Bethibande.Engine.UI.rendering.UIDrawable;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class UIElement implements UIDrawable {

    @Getter
    @Setter
    private Vector2f position;
    @Getter
    @Setter
    private Vector2f size;
    @Getter
    @Setter
    private Color color;
    @Getter
    @Setter
    private float rotation;
    @Getter
    private RawModel VAO;

    public UIElement(Vector2f pos, Vector2f size, float rotation, Color c) {
        this.position = pos;
        this.size = size;
        this.color = c;
        this.rotation = rotation;
        this.VAO = UIMaster.load(this);
    }

}
