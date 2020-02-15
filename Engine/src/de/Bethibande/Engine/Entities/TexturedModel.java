package de.Bethibande.Engine.Entities;

import lombok.Getter;
import lombok.Setter;

public class TexturedModel {

    @Getter
    private RawModel model;
    @Getter
    @Setter
    private Texture texture;

    public TexturedModel(RawModel model, Texture texture) {
        this.model = model;
        this.texture = texture;
    }

}
