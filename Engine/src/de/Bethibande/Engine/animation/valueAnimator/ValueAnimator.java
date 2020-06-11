package de.Bethibande.Engine.animation.valueAnimator;

import de.Bethibande.Engine.Timer;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class ValueAnimator {

    // frames = the amount of frames/updates it'll take to go from the "from" value to the "to" value
    @SuppressWarnings("unused")
    public static void animateInt(String field, Object obj, int from, int to, int frames) {
        Timer t = new Timer(value -> {
            try {
                Field f = obj.getClass().getDeclaredField(field);
                f.setAccessible(true);
                f.set(obj, (int)value);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }, from, to, frames);
    }

    // frames = the amount of frames/updates it'll take to go from the "from" value to the "to" value
    @SuppressWarnings("unused")
    public static void animateFloat(String field, Object obj, float from, float to, int frames) {
        Timer t = new Timer(value -> {
            try {
                Field f = obj.getClass().getDeclaredField(field);
                f.setAccessible(true);
                f.set(obj, value);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }, from, to, frames);
    }

}
