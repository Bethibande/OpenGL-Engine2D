package de.Bethibande.Engine.Input;

import lombok.Getter;
import lombok.Setter;
import org.lwjgl.input.Keyboard;

public class Input {

    @Getter
    private final int key;
    @Getter
    @Setter
    private InputType type;
    @Getter
    @Setter
    private Runnable update;
    @Getter
    private boolean pressed = false;

    public Input(int key, InputType type, Runnable update) {
        this.key = key;
        this.type = type;
        this.update = update;
    }

    public void update() {
        if(type == InputType.HOLD) {
            if(Keyboard.isKeyDown(key)) {
                update.run();
                pressed = true;
            } else pressed = false;
        }
        if(type == InputType.BUTTON) {
            if(pressed && !Keyboard.isKeyDown(key)) {
                update.run();
                pressed = false;
            } else pressed = Keyboard.isKeyDown(key);
        }
    }

    public enum InputType {
        BUTTON, HOLD
    }

}
