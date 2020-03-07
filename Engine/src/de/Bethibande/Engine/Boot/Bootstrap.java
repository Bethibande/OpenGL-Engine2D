package de.Bethibande.Engine.Boot;

import de.Bethibande.Engine.EngineCore;
import de.Bethibande.Engine.Error.EngineError;

public class Bootstrap {

    // args
    // --devMode:true

    public static void main(String args[]) {
        ArgumentParser.parseArguments(args);
        if(args.length < 1) {
            System.err.println("[!!!] You have to specify a project name as a start argument! [!!!]");
            try {
                throw new Exception("You have to specify a project name as a start argument!");
            } catch(Exception e) {
                EngineError.openError(e);
            }
        } else {
            Bootscreen.open();

            EngineCore.loadProject(args[0]);
        }
    }

}
