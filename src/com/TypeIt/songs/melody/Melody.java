package com.TypeIt.songs.melody;

import com.TypeIt.files.MelodyFileReader;
import com.TypeIt.songs.Song;

import java.util.*;

/**
 * Created by Naveh on 03/01/2017.
 */
public class Melody {
    private static final MelodyFileReader fileReader = new MelodyFileReader();
    /**
     * HashMap of all song melodies, the key being the Song (example: { Song:{"The Scientist", "Coldplay"}, Integer[] notes})
     */
    private static final Map<Song, Integer[]> melodies = new HashMap<>();

    /**
     * Map of half-tone distances from original (example: "Ha-Tikva"'s notes are 5 half-tones above the backing-track, so { "ha_tikva", -5" })
     */
    private static final Map<String, Integer> distancesFromOriginal = new HashMap<>();


    private static Integer[] notes;
    private static Map<String, Song> songs = new HashMap<>();

    public static void init() {
        melodies.put(new Song("Ha Tikva", "Naftali Herz Imber"), null);
        distancesFromOriginal.put("Ha Tikva", 0);

        melodies.put(new Song("The Scientist", "Coldplay"), null);
        distancesFromOriginal.put("The Scientist", 0);

        melodies.put(new Song("Hide & Seek", "Imogen Heap"), null);
        distancesFromOriginal.put("Hide & Seek", 0);

        melodies.put(new Song("With Or Without You", "U2"), null);
        distancesFromOriginal.put("With Or Without You", 0);

        melodies.put(new Song("One Love", "Bob Marley"), null);
        distancesFromOriginal.put("One Love", 0);

        melodies.put(new Song("Could You Be Loved", "Bob Marley"), null);
        distancesFromOriginal.put("Could You Be Loved", 0);

        melodies.put(new Song("Comfortably Numb", "Pink Floyd"), null);
        distancesFromOriginal.put("Comfortably Numb", 0);

        melodies.put(new Song("No Surprises", "Radiohead"), null);
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

        // If we haven't read it from the file yet
        if (notes == null) {
            notes = fileReader.readMelody(songName);
        }

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

    public Integer[] getNotes() {
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
