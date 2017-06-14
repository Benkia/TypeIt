package com.TypeIt.sound;

/**
 * Created by Naveh on 03/01/2017.
 */
public interface ISoundPlayer {
    void playNote(final int midiNote, int octavesFromStart);
    void playFailedNote(int midiNote);
}
