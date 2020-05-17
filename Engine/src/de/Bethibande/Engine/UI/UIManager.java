package de.Bethibande.Engine.UI;

import de.Bethibande.Engine.EngineCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UIManager {

    private static HashMap<Class<UIElement>, UIRenderer> elementRenderers = new HashMap();

    public static void registerRenderer(Class<UIElement> elementType, UIRenderer renderer) { elementRenderers.put(elementType, renderer); }
    public static void unregister(Class<UIElement> elementType) { elementRenderers.remove(elementType); }

    public static void render() {
        List<UIElement> elements = EngineCore.currentScene.getUiElements();
        if(!elements.isEmpty()) {
            for(Class<UIElement> type : elementRenderers.keySet()) {
                UIRenderer renderer = elementRenderers.get(type);
                List<UIElement> elementsToRender = new ArrayList<>();
                for(UIElement element : elements) {
                    if(element.getClass().getSuperclass() == type) {
                        elementsToRender.add(element);
                    }
                }
                renderer.render(elementsToRender);
            }
        }
    }

}
