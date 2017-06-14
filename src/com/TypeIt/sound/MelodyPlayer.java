package com.TypeIt.sound;

import javax.sound.midi.*;

/**
 * Created by Naveh on 03/01/2017.
 */
public class MelodyPlayer implements ISoundPlayer {
    private static MidiChannel[] mChannels;
    private static final int NOTES_IN_OCTAVE = 12;

    private static final int VELOCITY = 150;
    private static final int CHANNEL = 0;
    private static final int FAILED_NOTE_OCTAVE = -2;

    // Pitch-Bend is a 14-bit value - from 0 to 2^14-1 (= MAX_UP = 16383)
    // MAX_UP = 2^14-1 = 16383
    // CENTER = 2^14/2 = 2^13 = 8192
    // MAX_DOWN = 0
    private static final int CENTER = 8192;

    public MelodyPlayer(int instrument) {
        try {
            // Create a new Synthesizer and open it.
            Synthesizer midiSynth = MidiSystem.getSynthesizer();
            midiSynth.open();

            // Get and load default instrument and channel lists
            Instrument[] instr = midiSynth.getDefaultSoundbank().getInstruments();
            mChannels = midiSynth.getChannels();

            // Load the selected instrument
            midiSynth.loadInstrument(instr[instrument]);

            // Set the pitch-shift range to be a whole octave up or down.
            setupPitchChange(NOTES_IN_OCTAVE);
        }
        catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pitchRange - In half tones.
     */
    private void setupPitchChange(int pitchRange) {
        mChannels[CHANNEL].controlChange(100, 0);
        mChannels[CHANNEL].controlChange(101, 0);
        mChannels[CHANNEL].controlChange(6, pitchRange);
    }

    public void playNote(final int midiNote, int octavesFromStart) {
        // Calculate the note in the selected octave
        int newNote = midiNote + octavesFromStart*NOTES_IN_OCTAVE;

        // On channel 0, play the note with velocity = VELOCITY
        mChannels[CHANNEL].noteOn(newNote, VELOCITY);
    }

    public void setPitchBendForSpeed(float speed) {
        int oldPitchBend = mChannels[CHANNEL].getPitchBend();
        int newPitchBend = (int) (speed*CENTER);

        mChannels[CHANNEL].setPitchBend(newPitchBend);

        if (mChannels[CHANNEL].getPitchBend() == newPitchBend) {
            if (oldPitchBend != newPitchBend) {
                System.out.println("Bent pitch to " + newPitchBend);
            }
        }
    }

    @Override
    public void playFailedNote(int midiNote) {
        playNote(midiNote, FAILED_NOTE_OCTAVE);
    }

    public static void stopEverything() {
        if (mChannels != null) {
            mChannels[CHANNEL].allSoundOff();
        }
    }
}
