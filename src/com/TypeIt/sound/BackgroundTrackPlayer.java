package com.TypeIt.sound;

/**
 * Created by Naveh on 03/01/2017.
 */
public class BackgroundTrackPlayer implements BGPlayer {
    private static final String FILE_FORMAT_SUFFIX = ".wav";
    private static MusicRunnable musicRunnable;
    private static MusicRunnable previewRunnable;
    private static boolean playing = false;

    public BackgroundTrackPlayer(String fileName, float speedToChangeEveryKey) {
        this.musicRunnable = new MusicRunnable(fileName + FILE_FORMAT_SUFFIX, speedToChangeEveryKey);
    }

    public void setMusicSpeed(float speed) {
        if (musicRunnable != null) {
            musicRunnable.setSpeed(speed);
        }
    }

    public float getSpeed() {
        if (musicRunnable != null) {
            return musicRunnable.getSpeed();
        }
        return 0;
    }

    public void play() {
        if (!playing) {
            new Thread(musicRunnable).start();
            playing = true;
        }
    }

    public static void preview(String songName) {
        stopRunnable(previewRunnable);

        previewRunnable = new MusicRunnable(songName + FILE_FORMAT_SUFFIX, 0);
        new Thread(previewRunnable).start();
    }

    private static void stopRunnable(MusicRunnable runnable) {
        if (runnable != null && (!runnable.isStopped())) {
            runnable.stop();
        }
    }

    public static void stopEverything() {
        stopRunnable(musicRunnable);
        stopRunnable(previewRunnable);

        playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }
}
