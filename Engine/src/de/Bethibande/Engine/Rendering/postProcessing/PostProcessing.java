package de.Bethibande.Engine.Rendering.postProcessing;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Entities.FBO;
import de.Bethibande.Engine.Scene2D;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class PostProcessing {

    // String -> the name of the layer the postprocessing effect shall be added to
    private static LinkedHashMap<String, LinkedList<PostProcessingRenderer>> pc = new LinkedHashMap<>();
    // these postprocessing effects will be applied to the whole rendered scene and not only one layer
    // !!! please note, the ui won't be affected by this !!!
    private static LinkedList<PostProcessingRenderer> finalPP = new LinkedList<>();

    // because of the layers in the "pc" hashmap, they need to be correctly initialized for the current scene or the engine will crash
    // due to the not existing layers in the postprocessing called by the postprocessing
    private static Scene2D loadedScene;

    public static void addPostProcessing(String layer, PostProcessingRenderer ppr) {
        pc.get(layer).add(ppr);
    }
    public static void removePostProcessing(String layer, PostProcessingRenderer ppr) {
        pc.get(layer).remove(ppr);
    }

    public static void addPostProcessing(PostProcessingRenderer ppr) {
        finalPP.add(ppr);
    }
    public static void removePostProcessing(PostProcessingRenderer ppr) {
        finalPP.remove(ppr) ;
    }

    public static void update() {
        if(loadedScene != EngineCore.currentScene) {
            loadedScene = EngineCore.currentScene;
            pc = null;
            System.gc();
            pc = new LinkedHashMap<>();
            for(String layer : loadedScene.getLayers().keySet()) {
                pc.put(layer, new LinkedList<>());
            }
        }
    }

    public static FBO postProcessing(String name, FBO layer) {
        FBO fbo = layer;
        int i = 0;
        while(i < pc.get(name).size()) {
            PostProcessingRenderer ppr = pc.get(name).get(i);
            ppr.render(fbo);
            fbo = ppr.getFBO();
            i++;
        }
        return fbo;
    }

    public static FBO finalPostProcessing(FBO scene) {
        FBO fbo = scene;
        int i = 0;
        while(i < finalPP.size()) {
            PostProcessingRenderer ppr = finalPP.get(i);
            ppr.render(fbo);
            fbo = ppr.getFBO();
            i++;
        }
        return fbo;
    }

}
