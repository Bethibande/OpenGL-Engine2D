package de.Bethibande.Engine.Physics;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.GameObject2D;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class PhysicsEngine {

    public static Vector2f gravity = new Vector2f(0, 0.9f);

    /*
    Rectangle2D myRect = new Rectangle2D.Double(100, 100, 200, 200);
AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 4, 150, 150);
Shape rotatedRect = at.createTransformedShape(myRect);
     */

    public static void update() {
        for(String l : EngineCore.currentScene.getLayers().keySet()) {
            for(GameObject2D obj : EngineCore.currentScene.getLayers().get(l)) {
                if(obj.isGravity() && obj.getMotion() != null) {
                    obj.getMotion().x += gravity.x;
                    obj.getMotion().y += gravity.y;
                }
                if (obj.getMotion() != null && (obj.getMotion().x != 0 || obj.getMotion().y != 0)) {
                    Vector2f pos = new Vector2f(obj.getPosition().x, obj.getPosition().y);
                    pos.x += obj.getMotion().x/2f;
                    pos.y += obj.getMotion().y/2f;

                    move(l, obj, pos);

                    if(Math.abs(obj.getMotion().x) <= 0.0025f) obj.getMotion().x = 0;
                    if(Math.abs(obj.getMotion().y) <= 0.0025f) obj.getMotion().y = 0;
                }
            }
        }
    }

    public static void move(String layer, GameObject2D obj, Vector2f newPosition) {
        boolean collided = false;
        for(GameObject2D o : EngineCore.currentScene.getLayers().get(layer)) {
            if(collides(obj, o) && o != obj) collided = true;
        }
        if(!collided) { obj.setPosition(newPosition); }
    }

    private static boolean collides(GameObject2D a, GameObject2D b) {
        Vector2f ac = a.getCollider();
        Vector2f bc = b.getCollider();
        Rectangle r = new Rectangle((int)(a.getPosition().x*100), (int)(a.getPosition().y*100), (int)(ac.x*100), (int)(ac.y*100));
        Rectangle r2 = new Rectangle((int)(b.getPosition().x*100), (int)(b.getPosition().y*100), (int)(bc.x*100), (int)(bc.y*100));
        return r.intersects(r2);
    }

}
