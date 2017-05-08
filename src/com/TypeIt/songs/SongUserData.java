package com.TypeIt.songs;

/**
 * Created by Naveh on 11/1/2017.
 */
public class SongUserData {
    private Song song;
    private String fileName;

    // Percent
    private float userHighScore = 0;

    private SongUserData(Song song, String fileName) {
        this.song = song;
        this.fileName = fileName;
    }

    public SongUserData(Song song, String fileName, float userHighScore) {
        this(song, fileName);
        this.userHighScore = userHighScore;
    }

    // Getters & Setters
    public String getSongName() { return song.getSongName(); }
    public String getFileName() { return fileName; }
    public String getBandName() { return song.getBandName(); }
    public float getUserHighScore() { return userHighScore; }
    public void setUserHighScore(float newHighScore) {
        if (newHighScore > userHighScore) {
            userHighScore = newHighScore;
        }
    }

    public static String getCSVHeader() {
        return "songName,bandName,fileName,userHighScore";
    }

    public String toString(String delimiter) {
        return song.toString(delimiter) + delimiter + fileName + delimiter + userHighScore;
    }
}
