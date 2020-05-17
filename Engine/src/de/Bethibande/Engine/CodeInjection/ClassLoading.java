package de.Bethibande.Engine.CodeInjection;

import de.Bethibande.Engine.Boot.ArgumentParser;
import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.FileUtils.FileUtils;
import de.Bethibande.Engine.utils.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassLoading {

    private static List<File> classes = new ArrayList<>();

    public static List<Class> injected = new ArrayList<>();

    private static java.lang.ClassLoader l = ClassLoading.class.getClassLoader();
    private static ClassLoader loader = new ClassLoader(l);

    private static File build;

    public static void start() {
        build = new File(EngineCore.engine_root + "/build/" + EngineCore.project_root.getName());
        if(!FileUtils.deleteDirectory(build)) {
            Log.logError("Couldn't delete old compiled files!");
            EngineCore.stop();
        }
        build.mkdir();
        collectClasses(EngineCore.project_root);
        compile();
        inject();
    }

    private static void collectClasses(File root) {
        for(File f : root.listFiles()) {
            if(!f.isDirectory()) {
                if(f.getName().endsWith(".java")) {
                    classes.add(f);
                }
            } else collectClasses(f);
        }
    }

    private static void compile() {
        build.mkdirs();
        build.mkdir();
        StringBuilder b = new StringBuilder();
        for(File clazz : classes) {
            b.append(" " + clazz.getPath());
        }
        try {
            String cmd = EngineCore.cfg.javacPath + b + " -d " + build + " -cp C:\\GameEngine\\Engine.jar";
            if(ArgumentParser.args.containsKey("--cp")) {
                cmd = cmd +";" + ArgumentParser.args.get("--cp");
            }
            System.out.println(cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            //compiled.add(new File(dest + "/" + f.getName().substring(0, f.getName().length()-5) + ".class"));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void inject() {
        inject(new File(EngineCore.engine_root + "/build/" + EngineCore.project_root.getName() + "/"));
    }

    private static void inject(File f) {
        if(!f.isDirectory()) {
            String name = f.getPath().substring(build.getPath().length()+1, f.getPath().length()-6);
            name = name.replace("\\", ".");
            Class s = loader.loadClass(name, f);
            injected.add(s);
            Log.log("Injected Class '" + name + "'!");
        } else {
            for(File f2 : f.listFiles()) {
                inject(f2);
            }
        }
    }

}
