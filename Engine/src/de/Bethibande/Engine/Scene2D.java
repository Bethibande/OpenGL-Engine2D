package de.Bethibande.Engine;

import de.Bethibande.Engine.Entities.GameObject2D;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Scene2D {

    @Getter
    private TreeMap<String, List<GameObject2D>> layers = new TreeMap<>();

    public void addObject(String layer, GameObject2D obj) { layers.get(layer).add(obj); }
    public void removeObject(String layer, GameObject2D obj) { layers.get(layer).remove(obj); }

    public void addLayer(String layer) { layers.put(layer, new ArrayList<>()); }
    public void removeLayer(String layer) { layers.remove(layer); }

}
