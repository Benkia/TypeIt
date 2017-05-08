package com.TypeIt.sound;

import com.TypeIt.songs.melody.Melody;

/**
 * Created by Naveh on 21/1/2017.
 */
public class MidiBackgroundTrackPlayer implements BGPlayer {
    private static final MidiTiming timing = new MidiTiming();
    private static MidiRunnable midiRunnable;
    private String songName;
    private Melody melody;

    public MidiBackgroundTrackPlayer(String songName, Melody melody) {
        this.songName = songName;
        this.melody = melody;
    }

    @Override
    public void play() {
        midiRunnable = new MidiRunnable(melody.getNotes(), timing.getDurations(songName));
        new Thread(midiRunnable).start();
    }

    public static void stopEverything() {
        if (midiRunnable != null) {
            midiRunnable.stop();
        }
    }

    @Override
    public void setMusicSpeed(float speed) {
        midiRunnable.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        return midiRunnable.getSpeed();
    }

    @Override
    public boolean isPlaying() {
        return midiRunnable != null && midiRunnable.isRunning();
    }
}
