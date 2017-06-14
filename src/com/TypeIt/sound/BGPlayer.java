package com.TypeIt.sound;

/**
 * Created by Naveh on 21/1/2017.
 */
public interface BGPlayer {
    void setMusicSpeed(float speed);
    float getSpeed();
    void play();
    boolean isPlaying();
}
