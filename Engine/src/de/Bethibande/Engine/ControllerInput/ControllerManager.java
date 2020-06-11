package de.Bethibande.Engine.ControllerInput;

import de.Bethibande.Engine.EngineCore;
import org.lwjgl.input.Controller;

@SuppressWarnings("unused")
public class ControllerManager {



    public static void update() {
        for(Controller con : EngineCore.getListenToControllers()) {
            con.poll();
        }
    }

}
