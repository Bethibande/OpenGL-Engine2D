package de.Bethibande.Engine.Physics;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.GameObject2D;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;

public class PhysicsEngine {

    public static Vector2f gravity = new Vector2f(0f, -0.9f);

    /*
    Rectangle2D myRect = new Rectangle2D.Double(100, 100, 200, 200);
AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 4, 150, 150);
Shape rotatedRect = at.createTransformedShape(myRect);
     */

    public static void update() {
        for(String l : EngineCore.currentScene.getLayers().keySet()) {
            for(GameObject2D obj : EngineCore.currentScene.getLayers().get(l)) {
                if(obj.isGravity()) {
                    if(obj.getMotion() == null) obj.setMotion(new Vector2f(0, 0));
                    obj.getMotion().x += gravity.x;
                    obj.getMotion().y += gravity.y;
                }
                if (obj.getMotion() != null && (obj.getMotion().x != 0 || obj.getMotion().y != 0)) {
                    Vector2f pos = new Vector2f(obj.getPosition().x, obj.getPosition().y);
                    pos.x += obj.getMotion().x/2f;
                    pos.y += obj.getMotion().y/2f;
                    if(move(l, obj, pos) == false) {
                        move(l, obj, new Vector2f(pos.x, obj.getPosition().y));
                        obj.setOnGround(!move(l, obj, new Vector2f(obj.getPosition().x, pos.y)));
                    } else obj.setOnGround(false);
                    obj.setMotion(new Vector2f(obj.getMotion().x/2f, obj.getMotion().y/2f));

                    if(Math.abs(obj.getMotion().x) <= 0.0025f) obj.getMotion().x = 0;
                    if(Math.abs(obj.getMotion().y) <= 0.0025f) obj.getMotion().y = 0;
                }
            }
        }
    }

    public static boolean move(String layer, GameObject2D a, Vector2f newPosition) {
        boolean collided = false;
        Vector2f ac = a.getCollider();
        Rectangle r = new Rectangle((int)(newPosition.x*100f-(ac.x*100f/2f)), (int)(newPosition.y*100f-(ac.y*100f/2f)), (int)(ac.x*100), (int)(ac.y*100));
        if(a.getColliderOffset() != null) {
            r.setLocation(r.getLocation().x+(int)(a.getColliderOffset().x*100f), r.getLocation().y+(int)(a.getColliderOffset().y*100f));
        }
        for(GameObject2D b : EngineCore.currentScene.getLayers().get(layer)) {
            Vector2f bc = b.getCollider();
            Rectangle r2 = new Rectangle((int)(b.getPosition().x*100f-(bc.x*100f/2f)), (int)(b.getPosition().y*100f-(bc.y*100f/2f)), (int)(bc.x*100), (int)(bc.y*100));
            if(b.getColliderOffset() != null) {
                r2.setLocation(r2.getLocation().x+(int)(b.getColliderOffset().x*100f), r2.getLocation().y+(int)(b.getColliderOffset().y*100f));
            }
            if(r.intersects(r2) && b != a) {
                collided = true;
            }
        }
        if(!collided) { a.setPosition(newPosition); }
        return !collided;
    }

    private static boolean collides(GameObject2D a, GameObject2D b) {
        Vector2f ac = a.getCollider();
        Vector2f bc = b.getCollider();
        Rectangle r = new Rectangle((int)(a.getPosition().x*100f-(ac.x*100f/2f)), (int)(a.getPosition().y*100f-(ac.y*100f/2f)), (int)(ac.x*100), (int)(ac.y*100));
        Rectangle r2 = new Rectangle((int)(b.getPosition().x*100f-(bc.x*100f/2f)), (int)(b.getPosition().y*100f-(bc.y*100f/2f)), (int)(bc.x*100), (int)(bc.y*100));
        return r.intersects(r2);
    }
    /*
    public boolean intersects(Rectangle r)
    {
      return r.width > 0 && r.height > 0 && width > 0 && height > 0
       && r.x < x + width && r.x + r.width > x
       && r.y < y + height && r.y + r.height > y;
    }
     */
    private static boolean intersects(Vector4f a, Vector4f b) {
        return b.z > 0 && b.w > 0 && a.z > 0 && a.w > 0
       && b.x < a.x + a.z && b.x + b.z > a.x
       && b.y < a.y + a.w && b.y + b.w > a.y;
    }

}
