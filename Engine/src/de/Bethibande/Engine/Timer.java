package de.Bethibande.Engine;

import lombok.Getter;

public class Timer {

    @Getter
    private float start;
    @Getter
    private float end;
    @Getter
    private int time;
    @Getter
    private float value;
    @Getter
    private TimerCode code = null;
    @Getter
    private TimerCode done = null;

    public Timer(TimerCode code, TimerCode done, float start, float end, int frames) {
        this.start = start;
        this.end = end;
        this.time = frames;
        this.code = code;
        this.done = done;
        this.value = this.start;
        TimerManager.timers.add(this);
    }

    public Timer(TimerCode update, float start, float end, int frames) {
        this.start = start;
        this.end = end;
        this.time = frames;
        this.code = update;
        this.value = start;
        TimerManager.timers.add(this);
    }

    public Timer(float start, float end, int frames) {
        this.start = start;
        this.end = end;
        this.time = frames;
        this.value = start;
        TimerManager.timers.add(this);
    }

    public void update() {
        float dist = start - end;
        float update = dist/time;
        //System.out.println("u " + dist + " " + update + " " + value);
        value += update*-1;

        if(code != null) {
            code.run(value);
        }

    }

    public void done() {
        if(this.done != null) {
            this.done.run(value);
        }
    }

}

