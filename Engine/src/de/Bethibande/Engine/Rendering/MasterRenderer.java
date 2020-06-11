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
    //public static final UIRenderer ui = new UIRenderer();
    public static final List<Runnable> gameLogic = new ArrayList<>();

    private static final Matrix4f projectionMatrix = Maths.createProjectionMatrix();

    @Getter
    private static final LinkedHashMap<FBO, Float> fbos = new LinkedHashMap<>();
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
            InputManager.controlls.add(new Input(Keyboard.KEY_F5, Input.InputType.BUTTON, () -> {
                wireframeLayer++;
                if(wireframeLayer >= EngineCore.currentScene.getLayers().size()) {
                    wireframeLayer = -1;
                }
            }));
        }

        try {
            while (!Display.isCloseRequested()) {
                //-----------------------------------------------
                // delta time
                long startTime = System.nanoTime();

                //-----------------------------------------------
                // game logic
                if(EngineCore.isInitializedDiscord()) {
                    DiscordRPC.discordRunCallbacks();
                }
                InputManager.update();
                TimerManager.update();
                int i = 0;
                while(i < EngineCore.getListenToControllers().size()) {
                    Controller con = EngineCore.getListenToControllers().get(i);
                    con.poll();
                    i++;
                }
                i = 0;
                while(i < gameLogic.size()) {
                    gameLogic.get(i).run();
                    i++;
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
                //-----------------------------------------------
                // delta time
                long endTime = System.nanoTime();
                long time = endTime-startTime;
                EngineCore.deltaTime = time/1000000f;
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
        //-----------------------------------------------
        // delete old fbos
        if(!fbos.isEmpty()) {
            //fbos.keySet().forEach(EngineCore::destroyFBO);
            fbos.clear();
        }
        //-----------------------------------------------
        // render each layer to its own fbo
        if(EngineCore.currentScene != null) {
            float index = EngineCore.currentScene.getLayers().keySet().size()*0.01f+1;
            int i = 0;
            Iterator<String> it = EngineCore.currentScene.getLayers().keySet().iterator();
            while(it.hasNext()) {
                String l = it.next();
                index -= 0.01f;
                renderer.setWireframe(i == wireframeLayer);
                FBO fbo = EngineCore.currentScene.getFbos().get(l);
                fbos.put(fbo, index);
                renderer.render(EngineCore.currentScene.getLayers().get(l), fbo);
                i++;
            }
        }
        //-----------------------------------------------
        // merge fbos to one final fbo and apply post processing
        merger.prepare();
        Iterator<String> names = EngineCore.currentScene.getLayers().keySet().iterator();
        Iterator<String> it = EngineCore.currentScene.getFbos().keySet().iterator();
        while(it.hasNext()) {
            FBO fbo = EngineCore.currentScene.getFbos().get(it.next());
            float index = fbos.get(fbo);
            merger.render(index, PostProcessing.postProcessing(names.next(), fbo));
        }
        merger.stop();

        //-----------------------------------------------
        // apply final post processing and render final fbo to screen
        finalFBO = PostProcessing.finalPostProcessing(finalFBO);
        fRenderer.prepare();
        fRenderer.render(finalFBO);
    }

    private static void prepare() {
        Color c = EngineCore.cfg.clearColor;
        GL11.glClearColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f, 1.0f);
    }

}
