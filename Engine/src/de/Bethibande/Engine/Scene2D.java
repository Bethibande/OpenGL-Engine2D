package de.Bethibande.Engine;

import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Lights.PointLight;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Scene2D {

    @Getter
    private List<PointLight> lights = new ArrayList<>();
    @Getter
    private LinkedHashMap<String, List<GameObject2D>> layers = new LinkedHashMap<>();

    public void addObject(String layer, GameObject2D obj) { layers.get(layer).add(obj); }
    public void removeObject(String layer, GameObject2D obj) { layers.get(layer).remove(obj); }

    public void addLayer(String layer) { layers.put(layer, new ArrayList<>()); }
    public void removeLayer(String layer) { layers.remove(layer); }

}
