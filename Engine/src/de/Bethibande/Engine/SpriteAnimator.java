package de.Bethibande.Engine;

import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.Texture;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class SpriteAnimator {

    private int[] textures;
    @Getter
    @Setter
    private boolean loopAnimation = false;

    private HashMap<GameObject2D, Timer> playing = new HashMap<>();
    private HashMap<GameObject2D, Integer> rText = new HashMap<>();

    public SpriteAnimator(int... textures) {
        this.textures = textures;
    }

    public void play(int framesPerSprite, GameObject2D obj) {
        if(playing.containsKey(obj)) return;
        if(!rText.containsKey(obj)) { rText.put(obj, obj.getModel().getId()); }
        Timer t = new Timer(new TimerCode() {
            @Override
            public void run(float value) {
                int i = (int) value;
                obj.setModel(new Texture(textures[i]));
            }
        }, new TimerCode() {
            @Override
            public void run(float value) {
                try {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        play(framesPerSprite, obj);
                    } else obj.setModel(new Texture(rText.get(obj)));
                    playing.remove(obj);
                    //rText.remove(obj);
                    return;
                } catch(StackOverflowError e) {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        play(framesPerSprite, obj);
                    } else obj.setModel(new Texture(rText.get(obj)));
                    playing.remove(obj);
                    //rText.remove(obj);
                }
            }
        }, 0, textures.length - 1, framesPerSprite * (textures.length));
        if(!playing.containsKey(obj)) {
            playing.put(obj, t);
        } else {
            playing.remove(obj);
            playing.put(obj, t);
        }
    }

    public void stop(GameObject2D obj) {
        if(playing.containsKey(obj)) {
            TimerManager.timers.remove(playing.get(obj));
            playing.remove(obj);
            obj.setModel(new Texture(rText.get(obj)));
            rText.remove(obj);
            //System.out.println("stopped animation 1/" + TimerManager.timers.size());
        }
    }

    public boolean isPlaying(GameObject2D obj) { return playing.containsKey(obj); }

}
