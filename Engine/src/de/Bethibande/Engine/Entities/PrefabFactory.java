package de.Bethibande.Engine.Entities;

import de.Bethibande.Engine.Rendering.SpriteLoader;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

public class PrefabFactory {

    private static final HashMap<String, Integer> textures = new HashMap<>();

    @SuppressWarnings("unused")
    public static void createPrefab(String name, String texture, boolean resourceLoader, Vector2f size) {
        PrefabManager.addPrefab(new Prefab(name, texture, resourceLoader, size), name);
    }

    @SuppressWarnings("unused")
    public static void createPrefab(Prefab prefab, String name) {
        PrefabManager.addPrefab(prefab, name);
    }

    public static GameObject2D createObjectFromPrefab(Prefab p) {
        Texture texture;
        if(!textures.containsKey(p.getTexture())) {
            if(p.isResourceLoader()) {
                texture = new Texture(SpriteLoader.loadResourceTexture(p.getTexture(), p.getName()));
            } else {
                texture = new Texture(SpriteLoader.loadTexture(p.getTexture(), p.getName()));
            }
            textures.put(p.getTexture(), texture.getId());
        } else texture = new Texture(textures.get(p.getTexture()));
        return new GameObject2D(new Vector2f(0, 0), new Vector2f(p.getSize().x, p.getSize().y), texture, 0, p.getName());
    }

}
