package de.Bethibande.Engine.Input;

import java.util.ArrayList;
import java.util.List;

public class InputManager {

    public static List<Input> controlls = new ArrayList<>();

    public static void update() {
        for (Input in : controlls) {
            if (in != null) {
                in.update();
            }
        }
    }

}
