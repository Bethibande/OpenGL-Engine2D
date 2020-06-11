package de.Bethibande.Engine;

import java.util.ArrayList;
import java.util.List;

public class TimerManager {

    public static List<Timer> timers = new ArrayList<>();

    public static void update() {
        List<Timer> done = new ArrayList<>();
        for (Timer t : timers) {
            if (t != null) {
                t.update();
                if (t.getStart() < t.getEnd()) {
                    if (t.getValue() >= t.getEnd()) {
                        done.add(t);
                    }
                }
                if (t.getStart() > t.getEnd()) {
                    if (t.getValue() <= t.getEnd()) {
                        done.add(t);
                    }
                }
                if (t.getStart() == t.getEnd()) {
                    done.add(t);
                }
            }
        }

        for(Timer t : done) {
            timers.remove(t);
            t.done();
            //System.out.println("timer expired");
        }
        done.clear();
    }

}
