package de.Bethibande.Engine.Entities;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector2f;

import java.awt.geom.Area;

public class GameObject2D {

    @Getter
    @Setter
    private Vector2f position;
    @Getter
    @Setter
    private Vector2f size;
    @Getter
    @Setter
    private TexturedModel model;
    @Getter
    @Setter
    private float rotation;
    @Getter
    @Setter
    private String prefab;
    @Getter
    @Setter
    private boolean canCollide = false;
    @Getter
    @Setter
    // x = width, y = height
    private Vector2f collider = null;
    @Getter
    @Setter
    private Vector2f motion;
    @Getter
    @Setter
    private boolean gravity = false;

    public GameObject2D(Vector2f position, Vector2f size, TexturedModel model, float rotation, String prefab) {
        this.position = position;
        this.size = size;
        this.model = model;
        this.rotation = rotation;
        this.prefab = prefab;
    }

}
