package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Error.EngineError;
import de.Bethibande.Engine.Fonts.fontMeshCreator.MasterFontRenderer;
import de.Bethibande.Engine.Input.Input;
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
import org.lwjgl.input.Keyboard;
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

    private static int wireframeLayer = -1;

    public static void startRendering() {
        int frames = 0;
        long lastFPS = Date.getTime()/1000;
        renderer.loadProjectionMatrix(projectionMatrix);
        MasterFontRenderer.init(EngineCore.loader);

        if(EngineCore.devMode) {
            InputManager.controlls.add(new Input(Keyboard.KEY_F5, Input.InputType.BUTTON, new Runnable() {
                @Override
                public void run() {
                    wireframeLayer++;
                    if(wireframeLayer >= EngineCore.currentScene.getLayers().size()) {
                        wireframeLayer = -1;
                    }
                }
            }));
        }

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
            float index = EngineCore.currentScene.getLayers().keySet().size()*0.01f+10;
            int i = 0;
            for(String l : EngineCore.currentScene.getLayers().keySet()) {
                index -= 0.01f;
                if(i == wireframeLayer) {
                    renderer.setWireframe(true);
                } else renderer.setWireframe(false);
                renderer.render(index, EngineCore.currentScene.getLayers().get(l));
                i++;
            }
        }
        if(!UIMaster.elements.isEmpty()) ui.render(UIMaster.elements);
    }

    private static void prepare() {
        Color c = EngineCore.cfg.clearColor;
        GL11.glClearColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1.0f);
    }

}
