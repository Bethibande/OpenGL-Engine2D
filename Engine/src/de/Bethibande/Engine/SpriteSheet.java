package de.Bethibande.Engine;

import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.Texture;
import de.Bethibande.Engine.Rendering.SpriteLoader;
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
    private HashMap<GameObject2D, Timer> playing = new HashMap<>();
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
        int realID2 = obj.getModel().getTexture().getId();
        Timer t = new Timer(new TimerCode() {
            @Override
            public void run(float value) {
                int i = (int) value;
                obj.getModel().setTexture(new Texture(SpriteLoader.textures.get(sheet.getName() + "$" + i)));
            }
        }, new TimerCode() {
            private int realID = realID2;
            @Override
            public void run(float value) {
                try {
                    if (loopAnimation) {
                        play(framesPerSprite, obj);
                    } else obj.getModel().setTexture(new Texture(realID));
                    playing.remove(obj);
                    return;
                } catch(StackOverflowError e) {
                    if (loopAnimation) {
                        play(framesPerSprite, obj);
                    } else obj.getModel().setTexture(new Texture(realID));
                    playing.remove(obj);
                }
            }
        }, 0, loadedSprites.size() - 1, framesPerSprite * loadedSprites.size());
        playing.put(obj, t);
    }

    public void stop(GameObject2D obj) {
        if(playing.containsKey(obj)) {
            TimerManager.timers.remove(playing.get(obj));
        }
    }

}
