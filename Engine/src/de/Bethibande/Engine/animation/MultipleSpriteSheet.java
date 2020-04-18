package de.Bethibande.Engine.animation;

import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.Texture;
import de.Bethibande.Engine.Rendering.SpriteLoader;
import de.Bethibande.Engine.Timer;
import de.Bethibande.Engine.TimerCode;
import de.Bethibande.Engine.TimerManager;
import de.Bethibande.Engine.utils.ArrayUtils;
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

    /*
        spriteSizeX -> the x-size of one sprite on the sprite sheet
        spriteSizeY -> the y-size of one sprite on the sprite sheet
        spriteDistance -> distance between the sprites on the sprite sheet
     */

public class MultipleSpriteSheet {
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

    private String[] animations;
    private int spritesX = 0;

    public MultipleSpriteSheet(File sheet, int spritesX, int animationsY, String... animationNames) {
        try {
            this.animations = animationNames;
            this.spritesX = spritesX;
            this.sheet = sheet;
            BufferedImage img = ImageIO.read(sheet);
            //List<BufferedImage> sprites = new ArrayList<>();
            int num = 0;
            int stepX = img.getWidth() / spritesX;
            int stepY = img.getHeight() / animationsY;
            int i = 0;
            for (int y = 0; y < img.getHeight() - stepY; y += stepY) {
                int i2 = 0;
                for (int x = 0; x < img.getWidth() - stepX; x += stepX) {
                    BufferedImage img2 = img.getSubimage(x, y, stepX, stepY);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(img2, "PNG", baos);
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    SpriteLoader.loadTexture(bais, sheet.getName() + "$" + animationNames[i] + "$" + i2);
                    i2++;
                }
                i++;
            }
        } catch (IOException e) {
            Log.logError("Couldn't load spritesheet '" + sheet + "'!");
        }
    }

    public void play(int framesPerSprite, String animation, GameObject2D obj) {
        if (playing.containsKey(obj)) return;
        if(!ArrayUtils.contains(animations, animation)) {
            Log.logError("The animation '" + animation + "' was not found on this spritesheet!");
            return;
        }
        if (!rText.containsKey(obj) && resetToStartingTexture) {
            rText.put(obj, obj.getModel().getId());
        }
        Timer t = new Timer(new TimerCode() {
            @Override
            public void run(float value) {
                int i = (int) value;
                try {
                    //System.out.println(i);
                    obj.setModel(new Texture(SpriteLoader.textures.get(sheet.getName() + "$" + animation + "$" + i)));
                } catch(NullPointerException e) {
                    //System.out.println(sheet.getPath() + "$" + animation + "$" + i + " not found!");
                }
            }
        }, new TimerCode() {
            @Override
            public void run(float value) {
                try {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        //System.out.println("loop!");
                        playing.remove(obj);
                        play(framesPerSprite, animation, obj);
                    } else {
                        if (resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                        playing.remove(obj);
                    }
                    //rText.remove(obj);
                    return;
                } catch (StackOverflowError e) {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        playing.remove(obj);
                        play(framesPerSprite, animation, obj);
                    } else {
                        playing.remove(obj);
                        if (resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                    }
                    //rText.remove(obj);
                }
            }
        }, 0, spritesX, framesPerSprite * spritesX);
        //System.out.println(spritesX);
        if (!playing.containsKey(obj)) {
            playing.put(obj, t);
        } else {
            playing.remove(obj);
            playing.put(obj, t);
        }
    }

    public void play(int framesPerSprite, String animation, GameObject2D obj, int firstFrame, int lastFrame) {
        if (playing.containsKey(obj)) return;
        if(!ArrayUtils.contains(animations, animation)) {
            Log.logError("The animation '" + animation + "' was not found on this spritesheet!");
            return;
        }
        if (!rText.containsKey(obj) && resetToStartingTexture) {
            rText.put(obj, obj.getModel().getId());
        }
        Timer t = new Timer(new TimerCode() {
            @Override
            public void run(float value) {
                int i = (int) value;
                try {
                    //System.out.println(i);
                    obj.setModel(new Texture(SpriteLoader.textures.get(sheet.getName() + "$" + animation + "$" + i)));
                } catch(NullPointerException e) {
                    //System.out.println(sheet.getPath() + "$" + animation + "$" + i + " not found!");
                }
            }
        }, new TimerCode() {
            @Override
            public void run(float value) {
                try {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        //System.out.println("loop!");
                        playing.remove(obj);
                        play(framesPerSprite, animation, obj, firstFrame, lastFrame);
                    } else {
                        if (resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                        playing.remove(obj);
                    }
                    //rText.remove(obj);
                    return;
                } catch (StackOverflowError e) {
                    if (loopAnimation) {
                        //TimerManager.timers.remove(this);
                        playing.remove(obj);
                        play(framesPerSprite, animation, obj, firstFrame, lastFrame);
                    } else {
                        playing.remove(obj);
                        if (resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
                    }
                    //rText.remove(obj);
                }
            }
        }, firstFrame, lastFrame, framesPerSprite * spritesX);
        //System.out.println(spritesX);
        if (!playing.containsKey(obj)) {
            playing.put(obj, t);
        } else {
            playing.remove(obj);
            playing.put(obj, t);
        }
    }

    public void stop(GameObject2D obj) {
        if (playing.containsKey(obj)) {
            TimerManager.timers.remove(playing.get(obj));
            playing.remove(obj);
            if (resetToStartingTexture) obj.setModel(new Texture(rText.get(obj)));
            if (resetToStartingTexture) rText.remove(obj);
            //System.out.println("stopped animation 1/" + TimerManager.timers.size());
        }
    }

    public boolean isPlaying(GameObject2D obj) {
        return playing.containsKey(obj);
    }

}
