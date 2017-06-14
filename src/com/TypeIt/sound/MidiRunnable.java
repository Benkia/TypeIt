package com.TypeIt.sound;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Naveh on 21/1/2017.
 */
public class MidiRunnable implements Runnable {
    private static final int OCTAVES_FROM_START = -1;
    private static final MelodyPlayer melodyPlayer = new MelodyPlayer(0);
    private Integer[] notes;
    private long[] durations;
    private boolean isRunning;
    private int currentNoteIndex = 0;
    private float speed = 1f;

    private static Timer timer;

    public MidiRunnable(Integer[] notes, long[] durations) {
        this.notes = notes;
        this.durations = durations;
        isRunning = false;
    }

    @Override
    public void run() {
        isRunning = true;
        timer = new Timer();
        scheduleNextNote();
    }

    private void scheduleNextNote() {
        long delay = (long) (durations[currentNoteIndex] / speed);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                melodyPlayer.playNote(notes[currentNoteIndex], OCTAVES_FROM_START);
                currentNoteIndex++;

                if (isRunning && currentNoteIndex < notes.length && currentNoteIndex < durations.length) {
                    scheduleNextNote();
                }
                else {
                    isRunning = false;
                }
            }
        }, delay);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void stop() {
        isRunning = false;
        timer.cancel();
    }

    public void setSpeed(float newSpeed) {
        speed = newSpeed;
    }

    public float getSpeed() {
        return speed;
    }
}
