package de.Bethibande.Engine.events;

import de.Bethibande.Engine.Entities.GameObject2D;
import lombok.Getter;
import lombok.Setter;

public class CollisionEvent extends Event {

    @Getter
    @Setter
    private GameObject2D objectA;
    @Getter
    @Setter
    private GameObject2D objectB;
    @Getter
    @Setter
    private boolean cancelled = false;

    public CollisionEvent(GameObject2D a, GameObject2D b) {
        this.objectA = a;
        this.objectB = b;
    }

}
