package de.Bethibande.Engine.utils;

import de.Bethibande.Engine.EngineCore;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.ImageIOImageData;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DisplayManager {

    public static void createDisplay(String title, boolean resizeable, boolean vsync, int width, int height) {
        try {
            Display.setTitle(title);
            Display.setResizable(resizeable);
            Display.setVSyncEnabled(vsync);
            Display.setDisplayMode(new DisplayMode(width, height));
        } catch(LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void createDisplay(String title, boolean resizeable, boolean vsync) {
        try {
            Display.setTitle(title);
            Display.setResizable(resizeable);
            Display.setVSyncEnabled(vsync);
            Display.setFullscreen(true);
        } catch(LWJGLException e) {
            e.printStackTrace();
        }
    }

    public static void update() {
        if(EngineCore.cfg.FPS != EngineCore.NO_FPS_CAP) Display.sync(EngineCore.cfg.FPS);
        if(EngineCore.cfg.FPS == EngineCore.NO_FPS_CAP && !Display.isActive()) {
            Display.sync(120);
        }
        Display.update();
    }

    public static void loadIcon(String path) {
        try {
            Display.setIcon(new ByteBuffer[]{
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(path)), false, false, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File(path)), false, false, null)
            });
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
