package de.Bethibande.Engine.Boot;

import java.util.HashMap;

public class ArgumentParser {

    // args
    // -> --arg:value

    public static HashMap<String, String> args = new HashMap<>();

    public static void parseArguments(String[] args) {
        for(String arg : args) {
            if(arg.contains(":")) {
                String[] split = arg.split(":", 2);
                ArgumentParser.args.put(split[0], arg.substring(split[0].length()+1));
            }
        }
    }

}