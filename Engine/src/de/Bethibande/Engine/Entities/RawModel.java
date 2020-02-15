package de.Bethibande.Engine.Entities;

import lombok.Getter;

public class RawModel {

    @Getter
    private int id;
    @Getter
    private int vertices;

    public RawModel(int id, int vertices) {
        this.id = id;
        this.vertices = vertices;
    }

}
