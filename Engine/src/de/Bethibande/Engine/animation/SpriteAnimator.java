package de.Bethibande.Engine.animation;

import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.Texture;
import de.Bethibande.Engine.Timer;
import de.Bethibande.Engine.TimerCode;
import de.Bethibande.Engine.TimerManager;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class SpriteAnimator {

    private final int[] textures;
    @Getter
    @Setter
    private boolean loopAnimation = false;
    @Getter
    @Setter
    private boolean resetToStartingTexture = true;

    private final HashMap<GameObject2D, Timer> playing = new HashMap<>();
    private final HashMap<GameObject2D, Integer> rText = new HashMap<>();

    public SpriteAnimator(int... textures) {
        this.textures = textures;
    }

    @SuppressWarnings("unused")
    public void play(int framesPerSprite, GameObject2D obj) {
        if(playing.containsKey(obj)) return;
        if(!rText.containsKey(obj) && resetToStartingTexture) { rText.put(obj, obj.getModel().getId()); }
        Timer t = new Timer(value -> {
            int i = (int) value;
            obj.setModel(new Texture(textures[i]));
        }, value -> {
            try {
                if (loopAnimation) {
                    //TimerManager.timers.remove(this);
                    play(framesPerSprite, obj);
                } else if(resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                playing.remove(obj);
                //rText.remove(obj);
            } catch(StackOverflowError e) {
                if (loopAnimation) {
                    //TimerManager.timers.remove(this);
                    play(framesPerSprite, obj);
                } else if(resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                playing.remove(obj);
                //rText.remove(obj);
            }
        }, 0, textures.length - 1, framesPerSprite * (textures.length));
        playing.remove(obj);
        playing.put(obj, t);
    }

    public void stop(GameObject2D obj) {
        if(playing.containsKey(obj)) {
            TimerManager.timers.remove(playing.get(obj));
            playing.remove(obj);
            if(resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
            if(resetToStartingTexture) rText.remove(obj);
            //System.out.println("stopped animation 1/" + TimerManager.timers.size());
        }
    }

    @SuppressWarnings("unused")
    public boolean isPlaying(GameObject2D obj) { return playing.containsKey(obj); }

}
