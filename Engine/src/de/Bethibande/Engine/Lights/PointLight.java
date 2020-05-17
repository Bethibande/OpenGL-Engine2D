package de.Bethibande.Engine.Lights;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.io.Serializable;

public class PointLight implements Serializable {

    @Getter
    @Setter
    private Vector2f position;
    @Getter
    @Setter
    private float range;
    @Getter
    @Setter
    private Color color;

    public PointLight(Vector2f pos, float range, Color color) {
        this.position = pos;
        this.range = range;
        this.color = color;
    }

}
