package com.TypeIt.main;

import com.TypeIt.sound.MelodyPlayer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Naveh on 6/1/2017.
 */
public class PlayPressedButtonRunnable implements Runnable {
    private final Timer timer = new Timer();
    private int add = 0;

    @Override
    public void run() {
        final MelodyPlayer s = new MelodyPlayer(0);
        final int startOctave = -2;

        for (int i = 0; i<=8; i++) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    s.playNote(60, startOctave+add);
                    add++;
                }
            };

            timer.schedule(task, i * 40);
        }
    }
}
