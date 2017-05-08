package com.TypeIt.songs.melody;

import com.TypeIt.songs.Song;

import java.util.*;

/**
 * Created by Naveh on 03/01/2017.
 */
public class Melody {
    /**
     * HashMap of all song melodies, the key being the Song (example: { Song:{"The Scientist", "Coldplay"}, int[] notes})
     */
    private static final Map<Song, int[]> melodies = new HashMap<>();

    /**
     * Map of half-tone distances from original (example: "Ha-Tikva"'s notes are 5 half-tones above the backing-track, so { "ha_tikva", -5" })
     */
    private static final Map<String, Integer> distancesFromOriginal = new HashMap<>();

    /**
     * Notes
     */
    private static final int NOTES_IN_OCTAVE = 12;
    private static final int C = 60;
    private static final int Cs = 61;
    private static final int D = 62;
    private static final int Ds = 63;
    private static final int E = 64;
    private static final int F = 65;
    private static final int Fs = 66;
    private static final int G = 67;
    private static final int Gs = 68;
    private static final int A = 69;
    private static final int As = 70;
    private static final int B = 71;

    /**
     * This function returns the given note at a higher octave.
     */
    private static int h(int note) {
        return note + NOTES_IN_OCTAVE;
    }

    /**
     * This function returns the given note at a lower octave.
     */
    private static int l(int note) {
        return note - NOTES_IN_OCTAVE;
    }

    private static int[] ha_tikva =
            {
                    D, E, F, G, A, A, As, A, As, h(D), A,
                    G, G, G, F, F, E, D, E, F, D,
                    l(A), D, E, F, G, A, A, As, A, As, h(D), A,
                    G, G, G, F, F, E, D, E, F, D,

                    D, h(D), h(D), h(D), h(C), h(D), h(C), As, A,
                    D, h(D), h(D), h(D), h(C), h(D), h(C), As, A,
                    h(C), h(C), h(C), F, F, G, A, As, h(C), A, G, F,
                    G, G, F, F, F, E, D, E, F, D,
                    G, G, G, F, F, G, A, As, h(C), A, G, F,
                    G, G, F, F, F, E, D, E, F, D
            };

    /**
     * The scientist
     */
    private static final int[] the_scientist =
            {
                    F, G, F, h(C), A, F, G, F, h(C), A, F, F, G, F, A, A, A, A, G, F,
                    F, G, F, h(C), A, F, G, F, h(C), A, F, G, F, A, A, A, A, G, F,

                    F, G, F, h(C), A, F, F, G, F, h(C), A, F, G, F, A, A, A, A, G, F,
                    F, G, F, h(C), A, F, G, F, h(C), A, F, G, F, A, A, A, A, G, F
            };

    /**
     * Hide & seek
     */
    private static final int[] hide_and_seek =
            {
                    A, E, Gs, Gs, Fs, A, B,
                    A, E, Gs, A, Gs, A, Gs, Fs,
                    h(E), h(E), h(B), h(Gs), h(E), h(Cs),
                    h(A), h(Fs), h(E), h(D), h(Cs), h(D), h(Cs), A,
                    B, h(E), Gs, h(Cs),

                    A, E, A, B, A, Gs, A, Fs, A, A,
                    E, B, A, Gs, A, B, A,
                    h(E), h(E), h(B), h(Gs), h(E), h(Cs), h(A),
                    h(Fs), h(E), h(D), h(D), h(Cs), A,
                    B, h(Fs), Gs, h(Cs)
            };

    /**
     * With or without you
     */
    private static final int[] with_or_without_you =
            {
                    D, D, E, D, D, l(B), l(B), l(A),
                    D, D, E, D, D, l(B), l(B), l(A),
                    D, D, E, D, D, E,
                    D, D, E, D, D, l(B), l(B), l(A),

                    D, D, D, E, Fs,
                    E, E, A, G, Fs, E, D,
                    D, E, Fs,
                    E, E, Fs, E, D,

                    A, G, Fs, E,
                    A, G, Fs, D,
                    A, G, Fs, E,
                    E, D
            };

    /**
     * One love
     */
    private static final int[] one_love =
            {
                    D, D,
                    C, C,
                    Ds, D, C, As, C, As, D, C, As,

                    D, D,
                    C, C,
                    D, Ds, Ds, Ds, D, C, As, C, As, C, D, C, As,

                    D, C, As, G, F
            };

    /**
     * Could u be loved
     */
    private static final int[] could_you_be_loved =
    {
            D, E, Fs, Fs,
            D, Fs, E, D,

            D, E, Fs, Fs,
            D, Fs, E, D,

            D, E, Fs, G, Fs,
            Fs, Fs, D, E, G, Fs, Fs,
            G, E,
    };

    /**
     * Comfortably numb
     */
    private static final int[] comfortable_numb =
            {
                    Cs, D, D, D, D, D, D, E, E, Cs,
                    Cs, D, D, Cs, D, D, Cs, E,

                    C, C, B, C, B, C, B, C, D, D,
                    D, D, D, D, D, D, D, D, C, C, l(B), l(G),
                    B, C, B, C, D, D,

                    E,D,D, D, E, D, B, A, A, A
            };

    /**
     * Radiohead - No surprises
     */
    private static final int[] no_surprises =
            {
                    // This is ...
                    Gs, Gs, Gs, Gs, Gs, A, B,
                    Gs, Gs, Gs, Gs, A, Gs,

                    A, Gs, Fs, E, Ds, Ds, E, Fs,
                    A, Gs, Fs, E, Ds, Ds, E, Fs,
                    A, Gs, Fs, E, A, A, Ds, E, Fs, E,


                    // Such a pretty ...
                    Gs, Gs, Gs, Gs, A, B,
                    Gs, Gs, Gs, Gs, A, Gs,

                    A, Gs, Fs, E, Ds, Ds, E, Fs,
                    A, Gs, Fs, E, Ds, Ds, E, Fs,
                    A, Gs, Fs, E, A, A, Ds, E, Fs, E
            };

    private static int[] notes;
    private static Map<String, Song> songs = new HashMap<>();

    public static void init() {
        melodies.put(new Song("Ha Tikva", "Naftali Herz Imber"), ha_tikva);
        distancesFromOriginal.put("Ha Tikva", 0);

        melodies.put(new Song("The Scientist", "Coldplay"), the_scientist);
        distancesFromOriginal.put("The Scientist", 0);

        melodies.put(new Song("Hide & Seek", "Imogen Heap"), hide_and_seek);
        distancesFromOriginal.put("Hide & Seek", 0);

        melodies.put(new Song("With Or Without You", "U2"), with_or_without_you);
        distancesFromOriginal.put("With Or Without You", 0);

        melodies.put(new Song("One Love", "Bob Marley"), one_love);
        distancesFromOriginal.put("One Love", 0);

        melodies.put(new Song("Could You Be Loved", "Bob Marley"), could_you_be_loved);
        distancesFromOriginal.put("Could You Be Loved", 0);

        melodies.put(new Song("Comfortably Numb", "Pink Floyd"), comfortable_numb);
        distancesFromOriginal.put("Comfortably Numb", 0);

        melodies.put(new Song("No Surprises", "Radiohead"), no_surprises);
        distancesFromOriginal.put("No Surprises", 0);
    }

    public static void chooseSong(String songName) {
        Song chosenSong = null;

        for (Song song : melodies.keySet()) {
            if (songName.equals(song.getSongName())) {
                chosenSong = song;
                break;
            }
        }

        notes = melodies.get(chosenSong);

        final int distanceFromOriginal = distancesFromOriginal.get(songName);

        for (int i = 0; i < notes.length; i++) {
            notes[i] += distanceFromOriginal;
        }
    }

    public static Collection<Song> getListOfAllSongs() {
        List<Song> songs = new ArrayList<>();
        songs.addAll(melodies.keySet());

        return songs;
    }

    public int[] getNotes() {
        return notes;
    }

    public static String getSongTitle(String fileName) {
        String songTitle = "";

        for (Song song : getListOfAllSongs()) {
            String songName = song.getSongName();

            if (songName.equals(fileName)) {
                String bandName = song.getBandName();

                if (bandName != null && bandName != "") {
                    songTitle += bandName + " - ";
                }

                songTitle += songName;

                break;
            }
        }

        return songTitle;
    }
}
