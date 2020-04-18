package de.Bethibande.Engine.animation;

import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.Texture;
import de.Bethibande.Engine.Rendering.SpriteLoader;
import de.Bethibande.Engine.Timer;
import de.Bethibande.Engine.TimerCode;
import de.Bethibande.Engine.TimerManager;
import de.Bethibande.Engine.utils.Log;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpriteSheet {
    /*
        spriteSizeX -> the x-size of one sprite on the sprite sheet
        spriteSizeY -> the y-size of one sprite on the sprite sheet
        spriteDistance -> distance between the sprites on the sprite sheet
     */
    private List<String> loadedSprites = new ArrayList<>();
    private File sheet;
    @Getter
    @Setter
    private boolean loopAnimation = false;
    @Getter
    @Setter
    private boolean resetToStartingTexture = true;
    private HashMap<GameObject2D, Timer> playing = new HashMap<>();
    private HashMap<GameObject2D, Integer> rText = new HashMap<>();
    public SpriteSheet(File sheet, int spriteSizeX, int spriteSizeY, int spriteDistance) {
        try {
            this.sheet = sheet;
            BufferedImage img = ImageIO.read(sheet);
            //List<BufferedImage> sprites = new ArrayList<>();
            int num = 0;
            for(int x = 0; x < img.getWidth(); ) {
                BufferedImage sprite = img.getSubimage(x, 0, spriteSizeX, spriteSizeY);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(sprite, "PNG", bos);
                SpriteLoader.loadTexture(new ByteArrayInputStream(bos.toByteArray()), sheet.getName() + "$" + num);
                loadedSprites.add(sheet.getName() + "$" + num);
                x+= spriteSizeX+spriteDistance;
                num++;
            }
        } catch(IOException e) {
            Log.logError("Couldn't load spritesheet '" + sheet + "'!");
        }
    }

    public void play(int framesPerSprite, GameObject2D obj) {
        if(playing.containsKey(obj)) return;
        if(!rText.containsKey(obj) && resetToStartingTexture) { rText.put(obj, obj.getModel().getId()); }
        Timer t = new Timer(new TimerCode() {
            @Override
            public void run(float value) {
                int i = (int) value;
                obj.setModel(new Texture(SpriteLoader.textures.get(sheet.getName() + "$" + i)));
            }
        }, new TimerCode() {
            @Override
            public void run(float value) {
                try {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        //System.out.println("loop!");
                        playing.remove(obj);
                        play(framesPerSprite, obj);
                    } else {
                        if(resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                        playing.remove(obj);
                    }
                    //rText.remove(obj);
                    return;
                } catch(StackOverflowError e) {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        playing.remove(obj);
                        play(framesPerSprite, obj);
                    } else {
                        playing.remove(obj);
                        if(resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                    }
                    //rText.remove(obj);
                }
            }
        }, 0, loadedSprites.size() - 1, framesPerSprite * (loadedSprites.size()));
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
            if(resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
            if(resetToStartingTexture) rText.remove(obj);
            //System.out.println("stopped animation 1/" + TimerManager.timers.size());
        }
    }

    public boolean isPlaying(GameObject2D obj) { return playing.containsKey(obj); }

}
