package com.TypeIt.songs;

/**
 * Created by Naveh on 11/1/2017.
 */
public class Song {
    private String songName;
    private String bandName;

    public Song(String songName, String bandName) {
        this.songName = songName;
        this.bandName = bandName;
    }

    public String getSongName() { return songName; }
    public String getBandName() { return bandName; }

    public String toString(String delimiter) {
        return songName + delimiter + bandName;
    }
}
