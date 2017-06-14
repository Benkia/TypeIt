package com.TypeIt.sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Naveh on 9/1/2017.
 */
public class MusicRunnable implements Runnable {
    private String fileName;
    private float speed = 1f;
    private boolean stopped = false;
    private float deltaSpeed;

    public MusicRunnable(String fileName, float speedToChangeEveryKey) {
        this.fileName = fileName;
        this.deltaSpeed = speedToChangeEveryKey;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return this.speed;
    }

    @Override
    public void run() {
        testPlay(fileName);
    }

    public void testPlay(String filename)
    {
        try {
            File file = new File(filename);
            AudioInputStream in= AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            // Play now.
            rawplay(decodedFormat, din);
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
    {
        int frameSize = 512;

        if (deltaSpeed != 0) {
            // Choose a number that is enough to represent small changes in music
            frameSize *= (0.01f / deltaSpeed);
        }

        System.out.println("FrameSize = " + frameSize);

        byte[] data = new byte[frameSize];
        SourceDataLine line = getLine(targetFormat);
        if (line != null)
        {
            // Start
            line.start();
            int nBytesRead = 0;

            while (!stopped && nBytesRead != -1)
            {
                nBytesRead = din.read(data, 0, data.length);

                // Jump back
                // Example:
                // frameSize = 1000, speed = 0.5:
                // Every 1000 bytes, we jump back 500 bytes,
                // which means we play 500 bytes at a time instead of 1000 (A.K.A. Slow motion)
                din.skip((long) (-frameSize*(1-speed)));

                if (nBytesRead != -1) {
                    line.write(data, 0, nBytesRead);
                }
            }

            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }

        stopped = true;
    }

    public void stop() {
        stopped = true;
    }
    public boolean isStopped() { return stopped; }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}
