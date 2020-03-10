package de.Bethibande.Engine.Physics;

import org.lwjgl.util.vector.Vector2f;

public class PositionUtils {

    public static double distance(Vector2f a, Vector2f b) {
        double dSquared = Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2);
        return Math.sqrt(dSquared);
    }

    public static double angleToVector(Vector2f a, Vector2f b) {
        return Math.atan2(a.y-b.y, a.x-b.x) * 180 / Math.PI;
    }

}
