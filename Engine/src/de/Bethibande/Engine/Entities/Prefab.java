package de.Bethibande.Engine.Entities;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector2f;

import java.io.Serializable;

public class Prefab implements Serializable {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String texture;
    @Getter
    @Setter
    private boolean resourceLoader;
    @Getter
    @Setter
    private Vector2f size;

    public Prefab(String name, String texture, boolean resourceLoader, Vector2f size) {
        this.name = name;
        this.texture = texture;
        this.resourceLoader = resourceLoader;
        this.size = size;
    }

}
