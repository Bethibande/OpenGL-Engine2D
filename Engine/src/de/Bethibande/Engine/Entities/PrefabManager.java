package de.Bethibande.Engine.Entities;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.FileUtils.FileUtils;
import de.Bethibande.Engine.Rendering.SpriteLoader;

import java.io.File;
import java.util.HashMap;

public class PrefabManager {

    public static HashMap<Prefab, String> prefabs = new HashMap<>();

    public static void addPrefab(Prefab p, String name) {
        prefabs.put(p, EngineCore.project_root + "/" + name + ".prefab");
    }

    public static void loadPrefabs(File file) {
        for(File f : file.listFiles()) {
            if(f.isDirectory()) {
                loadPrefabs(f);
            } else {
                if(f.getName().endsWith(".prefab")) {
                    Prefab p = (Prefab) FileUtils.loadJson(f);
                    prefabs.put(p, f.getPath());
                }
            }
        }
    }

    public static void savePrefabs() {
        for(Prefab f : prefabs.keySet()) {
            FileUtils.saveJson(new File(prefabs.get(f).toString()), f);
        }
    }

    public static Prefab reloadPrefab(File f) {
        if(f.getName().endsWith(".prefab")) {
            Prefab p = null;
            for(Prefab p2 : prefabs.keySet()) {
                if(prefabs.get(p2).equals(f.getPath())) {
                    p = p2;
                    break;
                }
            }
            if(p != null) {
                prefabs.remove(p);
                Prefab p2 = (Prefab)FileUtils.loadJson(f);
                prefabs.put(p2, f.getPath());
                if(p2.isResourceLoader()) {
                    SpriteLoader.reloadResourceTexture(p2.getName(), p2.getTexture());
                } else SpriteLoader.reloadTexture(p2.getName(), p2.getTexture());
                return p2;
            }
        }
        return null;
    }

    public static Prefab getByName(String name) {
        for(Prefab p : prefabs.keySet()) {
            if(p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

}
