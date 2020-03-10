package de.Bethibande.Engine.Physics;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.events.CollisionEvent;
import de.Bethibande.Engine.events.EventManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class PhysicsEngine {

    public static float gravity = -0.9f;

    /*
    Rectangle2D myRect = new Rectangle2D.Double(100, 100, 200, 200);
AffineTransform at = AffineTransform.getRotateInstance(Math.PI / 4, 150, 150);
Shape rotatedRect = at.createTransformedShape(myRect);
     */

    public static boolean useRotations = false;
    public static float physicsPrecision = 1000;
    public static float minMotion = 0.000025f;

    public static void update() {
        if(EngineCore.physicsPaused) return;
        for(String l : EngineCore.currentScene.getLayers().keySet()) {
            for(GameObject2D obj : EngineCore.currentScene.getLayers().get(l)) {
                if(obj.isGravity()) {
                    if(obj.getMotion() == null) obj.setMotion(new Vector2f(0, 0));
                    obj.getMotion().y += gravity;
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

                    if(Math.abs(obj.getMotion().x) <= minMotion) obj.getMotion().x = 0;
                    if(Math.abs(obj.getMotion().y) <= minMotion) obj.getMotion().y = 0;
                }
            }
        }
    }

    public static boolean move(String layer, GameObject2D a, Vector2f newPosition) {
        boolean collided = false;
        Vector2f ac = a.getCollider();
        Rectangle r = new Rectangle((int)(newPosition.x*physicsPrecision-(ac.x*physicsPrecision/2f)), (int)(newPosition.y*physicsPrecision-(ac.y*physicsPrecision/2f)), (int)(ac.x*physicsPrecision), (int)(ac.y*physicsPrecision));
        //AffineTransform transform2 = new AffineTransform();
        //transform2.rotate(Math.toRadians(a.getRotation()), r.getX() + r.width/2, r.getY() + r.height/2);
        //Shape rotated = transform2.createTransformedShape(r);

        if(a.getColliderOffset() != null) {
            r.setLocation(r.getLocation().x+(int)(a.getColliderOffset().x*physicsPrecision), r.getLocation().y+(int)(a.getColliderOffset().y*physicsPrecision));
        }
        for(GameObject2D b : EngineCore.currentScene.getLayers().get(layer)) {
            if (b.isCanCollide()) {
                Vector2f bc = b.getCollider();
                Rectangle r2 = new Rectangle((int) (b.getPosition().x * physicsPrecision - (bc.x * physicsPrecision / 2f)), (int) (b.getPosition().y * physicsPrecision - (bc.y * physicsPrecision / 2f)), (int) (bc.x * physicsPrecision), (int) (bc.y * physicsPrecision));
                if (b.getColliderOffset() != null) {
                    r2.setLocation(r2.getLocation().x + (int) (b.getColliderOffset().x * physicsPrecision), r2.getLocation().y + (int) (b.getColliderOffset().y * physicsPrecision));
                }
                if (PositionUtils.distance(newPosition, b.getPosition()) < a.getCollider().x + a.getCollider().y + b.getCollider().x + b.getCollider().y) {
                    if (useRotations) {
                        AffineTransform transform = new AffineTransform();
                        transform.rotate(Math.toRadians(b.getRotation()), r2.getX() + r2.width / 2, r2.getY() + r2.height / 2);
                        Shape rotatedRect = transform.createTransformedShape(r2);
                        if (rotatedRect.intersects(r) && b != a) {
                            CollisionEvent e = new CollisionEvent(a, b);
                            EventManager.runEvent(e);
                            if(e.isCancelled() && collided != true) {
                                collided = false;
                            } else {
                                collided = true;
                                float yA = ((float) r.getLocation().y - (float) r.getHeight() / 2) / physicsPrecision;
                                float yB = ((float) r2.getLocation().y + (float) r2.getHeight() / 2) / physicsPrecision;
                                //System.out.println(yA + " " + yB + " " + (yB-yA) + " " + (yA+(ac.y/4)));
                                if (yB > yA && yA + (ac.y / 2) > yB) {
                                    if (newPosition.x < a.getPosition().x || newPosition.x > a.getPosition().x) {
                                        //System.out.println(yB + " " + yA + " " + (yB-yA));
                                        newPosition.y += (yB - yA) / physicsPrecision;
                                        return move(layer, a, newPosition);
                                    }
                                }
                            }
                        }
                    } else if (r.intersects(r2) && b != a) {
                        CollisionEvent e = new CollisionEvent(a, b);
                        EventManager.runEvent(e);
                        if(e.isCancelled() && collided != true) {
                            collided = false;
                        } else {
                            collided = true;
                            float yA = ((float) r.getLocation().y - (float) r.getHeight() / 2) / physicsPrecision;
                            float yB = ((float) r2.getLocation().y + (float) r2.getHeight() / 2) / physicsPrecision;
                            //System.out.println(yA + " " + yB + " " + (yB-yA) + " " + (yA+(ac.y/4)));
                            if (yB > yA && yA + (ac.y / 2) > yB) {
                                if (newPosition.x < a.getPosition().x || newPosition.x > a.getPosition().x) {
                                    //System.out.println(yB + " " + yA + " " + (yB-yA));
                                    newPosition.y += (yB - yA) / physicsPrecision;
                                    return move(layer, a, newPosition);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!collided) { a.setPosition(newPosition); }
        return !collided;
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
