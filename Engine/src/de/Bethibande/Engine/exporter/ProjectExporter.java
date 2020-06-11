package de.Bethibande.Engine.exporter;

import de.Bethibande.Engine.Boot.ArgumentParser;
import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.FileUtils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectExporter {

    private static File build;
    private static String javac;
    private static final List<File> classes = new ArrayList<>();

    //java -jar Engine.jar test-project C:\Program%%_%%Files%%_%%(x86)\Java\jdk1.8.0_191\bin\javac.exe --releaseProject:null

    public static void export(File project, File targetDir, String javac) {
        build = targetDir;
        ProjectExporter.javac = javac;
        collectClasses(project);
        compile();
        File run = new File(targetDir + "/run.bat");
        String[] runBat = {"java -cp \"Engine.jar\" de.Bethibande.Engine.Boot.Bootstrap --exportMode:true", "PAUSE"};
        FileUtils.write(run, runBat);
        FileUtils.copyFile(new File(EngineCore.engine_root + "/Engine.jar"), new File(targetDir + "/Engine.jar"));
        List<String> excludedTypes = new ArrayList<>();
        excludedTypes.add("iml");
        excludedTypes.add("java");
        excludedTypes.add("bat");
        FileUtils.copyDirectory(project, targetDir, excludedTypes);
    }

    private static void collectClasses(File root) {
        for(File f : Objects.requireNonNull(root.listFiles())) {
            if(!f.isDirectory()) {
                if(f.getName().endsWith(".java")) {
                    classes.add(f);
                }
            } else collectClasses(f);
        }
    }

    private static void compile() {
        build.mkdirs();
        //build.mkdir();
        StringBuilder b = new StringBuilder();
        for(File clazz : classes) {
            b.append(" ").append(clazz.getPath());
        }
        try {
            String cmd = javac + b + " -d " + build + " -cp C:\\GameEngine\\Engine.jar";
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

}
