package de.Bethibande.Engine.Entities;

import lombok.Getter;

public class RawModel {

    @Getter
    private final int id;
    @Getter
    private final int vertices;

    public RawModel(int id, int vertices) {
        this.id = id;
        this.vertices = vertices;
    }

}
