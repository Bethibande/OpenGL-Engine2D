package de.Bethibande.Engine;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector2f;

public class Camera {

    @Getter
    @Setter
    private Vector2f position;
    @Getter
    @Setter
    private float rotation;
    @Getter
    @Setter
    private float scale;

    public Camera(Vector2f position, float rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

}
