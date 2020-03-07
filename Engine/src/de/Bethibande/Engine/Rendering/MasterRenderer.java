package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Error.EngineError;
import de.Bethibande.Engine.Fonts.fontMeshCreator.MasterFontRenderer;
import de.Bethibande.Engine.Input.InputManager;
import de.Bethibande.Engine.Physics.PhysicsEngine;
import de.Bethibande.Engine.TimerManager;
import de.Bethibande.Engine.UI.UIMaster;
import de.Bethibande.Engine.UI.rendering.UIRenderer;
import de.Bethibande.Engine.utils.Date;
import de.Bethibande.Engine.utils.DisplayManager;
import de.Bethibande.Engine.utils.Log;
import de.Bethibande.Engine.utils.Maths;
import net.arikia.dev.drpc.DiscordRPC;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MasterRenderer {

    public static int last_FPS = 0;

    public static final DefaultRenderer renderer = new DefaultRenderer();
    public static final UIRenderer ui = new UIRenderer();
    public static final List<Runnable> gameLogic = new ArrayList<>();

    private static Matrix4f projectionMatrix = Maths.createProjectionMatrix();

    public static void startRendering() {
        int frames = 0;
        long lastFPS = Date.getTime()/1000;
        renderer.loadProjectionMatrix(projectionMatrix);
        MasterFontRenderer.init(EngineCore.loader);
        try {
            while (!Display.isCloseRequested()) {
                //-----------------------------------------------
                // game logic
                if(EngineCore.isInitializedDiscord()) {
                    DiscordRPC.discordRunCallbacks();
                }
                InputManager.update();
                TimerManager.update();
                for (Runnable r : gameLogic) {
                    r.run();
                }
                //-----------------------------------------------
                // render
                render();
                MasterFontRenderer.render();
                //-----------------------------------------------
                // update
                DisplayManager.update();
                frames++;
                if (Date.getTime() / 1000 > lastFPS) {
                    last_FPS = frames;
                    lastFPS = Date.getTime() / 1000;
                    frames = 0;
                    if (EngineCore.cfg.logFPS) {
                        Log.log("[FPS] " + last_FPS);
                    }
                }
            }
            EngineCore.stop();
        } catch(Exception e) {
            EngineError.openError(e);
        }
    }

    private static void render() {
        PhysicsEngine.update();

        prepare();
        renderer.prepare();
        if(EngineCore.currentScene != null) {
            for(String l : EngineCore.currentScene.getLayers().keySet()) {
                renderer.render(EngineCore.currentScene.getLayers().get(l));
            }
        }
        if(!UIMaster.elements.isEmpty()) ui.render(UIMaster.elements);
    }

    private static void prepare() {
        Color c = EngineCore.cfg.clearColor;
        GL11.glClearColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1.0f);
    }

}
