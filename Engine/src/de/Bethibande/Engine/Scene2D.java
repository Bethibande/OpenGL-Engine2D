package de.Bethibande.Engine;

import de.Bethibande.Engine.Entities.FBO;
import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Lights.PointLight;
import de.Bethibande.Engine.UI.UIElement;
import de.Bethibande.Engine.utils.ArrayUtils;
import lombok.Getter;
import org.lwjgl.opengl.Display;

import java.io.Serializable;
import java.util.*;

public class Scene2D implements Serializable {

    @Getter
    private List<PointLight> lights = new ArrayList<>();
    @Getter
    private List<UIElement> uiElements = new ArrayList<>();
    @Getter
    private LinkedHashMap<String, List<GameObject2D>> layers = new LinkedHashMap<>();
    @Getter
    private transient LinkedHashMap<String, FBO> fbos = new LinkedHashMap<>();

    public void addObject(String layer, GameObject2D obj) { layers.get(layer).add(obj); }
    public void removeObject(String layer, GameObject2D obj) { layers.get(layer).remove(obj); }

    public void addLayer(String layer) {
        layers.put(layer, new ArrayList<>());
        fbos.put(layer, new FBO(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight()));
    }
    public void removeLayer(String layer) {
        layers.remove(layer);
        FBO fbo = fbos.get(layer);
        fbos.remove(fbo);
        EngineCore.destroyFBO(fbo);
    }

}
