package de.Bethibande.Engine.Input;

import java.util.ArrayList;
import java.util.List;

public class InputManager {

    public static List<Input> controlls = new ArrayList<>();

    public static void update() {
        for(int i = 0; i < controlls.size(); i++) {
            Input in = controlls.get(i);
            if(in != null) {
                in.update();
            }
        }
    }

}
