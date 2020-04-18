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
    private float zoom;

    public Camera(Vector2f position, float rotation, float zoom) {
        this.position = position;
        this.rotation = rotation;
        this.zoom = zoom;
    }

}
