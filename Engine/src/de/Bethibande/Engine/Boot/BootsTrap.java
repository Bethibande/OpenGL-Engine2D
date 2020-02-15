package de.Bethibande.Engine.Boot;

import de.Bethibande.Engine.EngineCore;

public class BootsTrap {

    // args
    // --devMode:true

    public static void main(String args[]) {
        ArgumentParser.parseArguments(args);
        if(args.length < 1) {
            System.err.println("[!!!] You have to specify a project name as a start argument! [!!!]");
        } else {
            Bootscreen.open();

            EngineCore.loadProject(args[0]);
        }
    }

}
