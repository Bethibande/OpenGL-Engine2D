package de.Bethibande.Engine.FileUtils;

import com.google.gson.*;
import de.Bethibande.Engine.*;
import de.Bethibande.Engine.Entities.GameObject2D;
import de.Bethibande.Engine.Entities.Prefab;
import de.Bethibande.Engine.Entities.PrefabFactory;
import de.Bethibande.Engine.Entities.PrefabManager;
import de.Bethibande.Engine.utils.Log;
import javafx.scene.Scene;
import org.lwjgl.util.vector.Vector2f;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> read(File f) {
        List<String> content = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String s;
            while((s = reader.readLine()) != null) {
                content.add(s);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void write(File f, String[] content) {
        try {
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)));
            for(String s : content) {
                w.println(s);
            }
            w.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void createProjectFiles() {
        File cfg = new File(EngineCore.project_root + "/engine_config.cfg");
        if(!createFile(cfg)) System.err.println("[!!!] Couldn't setup the project! [!!!]");
        EngineConfig config = new EngineConfig();
        config.defaultScene = EngineCore.project_root + "/DefaultScene.scene";
        saveJson(cfg, config);
        Prefab defaultPref = new Prefab("Default", "/res/B.png", true, new Vector2f(1.0f, 1.0f));
        //PrefabFactory.createPrefab(defaultPref, "default");
        File p = new File(EngineCore.project_root + "/Default.prefab");
        saveJson(p, defaultPref);
        Scene2D defaultScene = new Scene2D();
        defaultScene.addLayer("default");
        defaultScene.addObject("default", PrefabFactory.createObjectFromPrefab(defaultPref));
        File scene = new File(EngineCore.project_root + "/DefaultScene.scene");
        saveScene(defaultScene, scene);

        File test = new File(EngineCore.project_root + "/test/");
        File core = new File(test + "/Core.java");
        test.mkdirs();
        test.mkdir();
        createFile(core);
        String[] coreContent = {
          "package test;",
          "",
          "public class Core {",
          "    ",
          "    public static void main() {",
          "         System.out.println(\"It works!\");",
          "    }",
          "}"
        };
        write(core, coreContent);
        File run = new File(EngineCore.project_root + "/run.bat");
        String[] runBat = {"java -cp \"../../Engine.jar\" de.Bethibande.Engine.Boot.Bootstrap " + EngineCore.project_root.getName() + " --devMode:true", "PAUSE"};
        write(run, runBat);

    }

    public static void copyDirectory(File f, File to, List<String> excludeFileTypes) {
        if(f.isDirectory()) {
            System.out.println("#2" + f.getName());
            File newF = new File(to + "/" + f.getName() + "/");
            newF.mkdirs();
            newF.mkdir();
            for(File f2 : f.listFiles()) {
                copyDirectory(f2, newF);
            }
        } else {
            try {
                System.out.println("#" + f.getName());
                int i = 0;
                while(i < excludeFileTypes.size()) {
                    //System.out.println(f.getName() + " ." + excludeFileTypes.get(i));
                    if(f.getName().endsWith("." + excludeFileTypes.get(i))) return;
                    i++;
                }
                Files.copy(f.toPath(), new File(to + "/" + f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyDirectory(File f, File to) {
        if(f.isDirectory()) {
            File newF = new File(to + "/" + f.getName() + "/");
            newF.mkdirs();
            newF.mkdir();
            for(File f2 : f.listFiles()) {
                copyDirectory(f2, newF);
            }
        } else {
            try {
                Files.copy(f.toPath(), new File(to + "/" + f.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(File f, File to) {
        try {
            Files.copy(f.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory() && dir.exists()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        } else if(!dir.exists()) {
            return true;
        }
        return dir.delete();
    }
    public static void saveScene(Scene2D scene, File f) {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = g.toJsonTree(scene).getAsJsonObject();
        /*JsonArray a = jo.getAsJsonArray("layers");
        for(int i = 0; i < a.size(); i++) {
            JsonObject obj = a.get(i).getAsJsonObject();
            obj.remove("model");
            obj.remove("texture");
        }*/
        jo.addProperty("className", Scene2D.class + "");
        write(f, new String[]{ g.toJson(jo)});
    }

    public static Scene2D loadScene(File f) {
        String st = "";
        for(String str : read(f)) {
            st = st + str.replaceAll("\t", "");
        }
        JsonObject obj = new Gson().fromJson(st, JsonElement.class).getAsJsonObject();
        Scene2D s = new Scene2D();
        try {
            JsonArray a = obj.getAsJsonArray("layers");
            for (int i = 0; i < a.size(); i++) {
                JsonArray jo = a.get(i).getAsJsonArray();
                String l = jo.getAsString();
                s.addLayer(l);
                System.out.println("Layer name: " + l);
                for (int i2 = 0; i2 < jo.size(); i2++) {
                    JsonObject jo2 = jo.get(i2).getAsJsonObject();
                    Prefab p = PrefabManager.getByName(jo2.get("prefab").getAsString());
                    GameObject2D o = PrefabFactory.createObjectFromPrefab(p);
                    o.setRotation(jo2.get("rotation").getAsFloat());
                    o.setPosition(new Gson().fromJson(jo2.get("position"), Vector2f.class));
                    o.setSize(new Gson().fromJson(jo2.get("size"), Vector2f.class));
                    s.addObject(l, o);
                }
            }
        } catch(Exception e) {
            JsonObject a = obj.getAsJsonObject("layers");
                a.entrySet().stream().forEach(t -> {
                    String l = t.getKey();
                    s.addLayer(l);
                    System.out.println("Layer name: " + l);
                    JsonArray jo = a.getAsJsonArray(l);
                    for (int i2 = 0; i2 < jo.size(); i2++) {
                        JsonObject jo2 = jo.get(i2).getAsJsonObject();
                        Prefab p = PrefabManager.getByName(jo2.get("prefab").getAsString());
                        GameObject2D o = PrefabFactory.createObjectFromPrefab(p);
                        o.setRotation(jo2.get("rotation").getAsFloat());
                        o.setPosition(new Gson().fromJson(jo2.get("position"), Vector2f.class));
                        o.setSize(new Gson().fromJson(jo2.get("size"), Vector2f.class));
                        s.addObject(l, o);
                    }
                });
            }
        return s;
    }

    public static EngineConfig loadConfig(File f) {
        return (EngineConfig)loadJson(f);
    }

    public static void saveConfig(EngineConfig cfg, File f) {
        saveJson(f, cfg);
    }

    public static Object loadJson(File f) {
        String s = "";
        for(String str : read(f)) {
            s = s + str.replaceAll("\t", "");
        }
        try {
            Gson g = new Gson();
            JsonObject jo = g.fromJson(s, JsonObject.class);
            String clazz = jo.get("className").getAsString().substring(6);
            jo.remove("className");
            return g.fromJson(s, Class.forName(clazz));
        } catch(ClassNotFoundException e) {
            Log.logError("Couldn't load json from File: '" + f.getPath() + "'! \n");
        }
        return null;
    }

    public static void saveJson(File f, Object obj) {
        Gson g = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jo = g.toJsonTree(obj).getAsJsonObject();
        jo.addProperty("className", obj.getClass() + "");
        String[] c = { g.toJson(jo) };
        write(f, c);
    }

    public static boolean createFile(File f) {
        try {
            f.createNewFile();
            return true;
        } catch(IOException e) {
            return false;
        }
    }

}
