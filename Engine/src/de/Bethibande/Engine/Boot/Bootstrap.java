package de.Bethibande.Engine.Boot;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Error.EngineError;
import de.Bethibande.Engine.FileUtils.FileUtils;
import de.Bethibande.Engine.exporter.ProjectExporter;

import java.io.File;

public class Bootstrap {

    // args:
    // project-name --devMode:true --configOverride:PATH-TO-CONFIG(overrides the config being loaded on startup)
    // args for the future - not yet implemented):
    // --exportMode:true - only used when the project is exported)
    // --releaseProject:PATH - will make a release folder with all the files needed an ready to be released
    // if PATH given/not equals "null" it will create the folder in the directory

    public static void main(String args[]) throws Exception {
        ArgumentParser.parseArguments(args);
        if(args.length < 1) {
            System.err.println("[!!!] You have to specify a project name as a start argument! [!!!]");
            try {
                throw new Exception("You have to specify a project name as a start argument!");
            } catch(Exception e) {
                EngineError.openError(e);
            }
        } else {
            if(ArgumentParser.args.containsKey("--releaseProject")) {
                if(args.length <= 2) {
                    try {
                        throw new Exception("arg 1 -> project name, arg 2 -> javac.exe path (including \"/javac.exe\"), arg 3 -> --releaseProject:PATH/null!");
                    } catch(Exception e) {
                        EngineError.openError(e);
                    }
                }
                File project = new File(EngineCore.engine_root + "/projects/" + args[0] + "/");
                if(project.exists()) {
                    File target = new File(ArgumentParser.args.get("--releaseProject").equalsIgnoreCase("null") ? EngineCore.engine_root + "/export/" + args[0] + "/": ArgumentParser.args.get("--releaseProject"));
                    if(!target.exists()) {
                        target.mkdirs();
                        target.mkdir();
                    }
                    ProjectExporter.export(project, target, args[1].replaceAll("%%_%%", " "));
                    System.out.println("Project Exported!");
                } else {
                    try {
                        throw new Exception("The specified project doesn't exist!");
                    } catch(Exception e) {
                        EngineError.openError(e);
                    }
                }

                return;
            }
            if(ArgumentParser.args.containsKey("--exportMode") && ArgumentParser.args.get("--exportMode").equalsIgnoreCase("true")) {
                Bootscreen.open();
                EngineCore.startExport(new File(Bootstrap.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
                return;
            }
            Bootscreen.open();

            EngineCore.loadProject(args[0]);
        }
    }

}
