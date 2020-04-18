package de.Bethibande.Engine.Rendering;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.FBO;
import de.Bethibande.Engine.Error.EngineError;
import de.Bethibande.Engine.Fonts.fontMeshCreator.MasterFontRenderer;
import de.Bethibande.Engine.Input.Input;
import de.Bethibande.Engine.Input.InputManager;
import de.Bethibande.Engine.Physics.PhysicsEngine;
import de.Bethibande.Engine.Rendering.postProcessing.PostProcessing;
import de.Bethibande.Engine.TimerManager;
import de.Bethibande.Engine.UI.UIMaster;
import de.Bethibande.Engine.UI.rendering.UIRenderer;
import de.Bethibande.Engine.utils.Date;
import de.Bethibande.Engine.utils.DisplayManager;
import de.Bethibande.Engine.utils.Log;
import de.Bethibande.Engine.utils.Maths;
import lombok.Getter;
import net.arikia.dev.drpc.DiscordRPC;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MasterRenderer {

    public static int last_FPS = 0;

    public static final DefaultRenderer renderer = new DefaultRenderer();
    private static final FBOMergeRenderer merger = new FBOMergeRenderer();
    private static final FinalRenderer fRenderer = new FinalRenderer();
    public static final UIRenderer ui = new UIRenderer();
    public static final List<Runnable> gameLogic = new ArrayList<>();

    private static Matrix4f projectionMatrix = Maths.createProjectionMatrix();

    @Getter
    private static LinkedHashMap<FBO, Float> fbos = new LinkedHashMap<>();
    @Getter
    private static FBO finalFBO = new FBO(Display.getWidth(), Display.getHeight());

    private static int wireframeLayer = -1;

    public static void startRendering() {
        int frames = 0;
        long lastFPS = Date.getTime()/1000;
        renderer.loadProjectionMatrix(projectionMatrix);
        fRenderer.loadProjectionMatrix(projectionMatrix);
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
                for(Controller con : EngineCore.getListenToControllers()) {
                    con.poll();
                }
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
        PostProcessing.update();

        prepare();
        renderer.prepare();
        if(!fbos.isEmpty()) {
            fbos.keySet().forEach(EngineCore::destroyFBO);
            fbos.clear();
        }
        if(EngineCore.currentScene != null) {
            float index = EngineCore.currentScene.getLayers().keySet().size()*0.01f+1;
            int i = 0;
            for(String l : EngineCore.currentScene.getLayers().keySet()) {
                index -= 0.01f;
                if(i == wireframeLayer) {
                    renderer.setWireframe(true);
                } else renderer.setWireframe(false);
                FBO fbo = renderer.render(EngineCore.currentScene.getLayers().get(l));
                fbos.put(fbo, index);
                i++;
            }
        }

        merger.prepare();
        Iterator<String> names = EngineCore.currentScene.getLayers().keySet().iterator();
        for(FBO fbo : fbos.keySet()) {
            float index = fbos.get(fbo);
            merger.render(index, PostProcessing.postProcessing(names.next(), fbo));
        }

        finalFBO = PostProcessing.finalPostProcessing(finalFBO);
        fRenderer.prepare();
        fRenderer.render(finalFBO);

        if(!UIMaster.elements.isEmpty()) ui.render(UIMaster.elements);
    }

    private static void prepare() {
        Color c = EngineCore.cfg.clearColor;
        GL11.glClearColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1.0f);
    }

}
