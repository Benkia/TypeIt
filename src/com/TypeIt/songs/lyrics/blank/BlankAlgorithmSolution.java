package com.TypeIt.songs.lyrics.blank;

import java.util.List;

/**
 * Created by admin on 12/05/2017.
 */
public class BlankAlgorithmSolution {
    private List<String> lyricsWords;
    private String lyricsString;

    public BlankAlgorithmSolution(List<String> lyricsWords, String lyricsString) {
        this.lyricsWords = lyricsWords;
        this.lyricsString = lyricsString;
    }

    public List<String> getWords() {
        return lyricsWords;
    }

    public String getLyricsString() {
        return lyricsString;
    }
}
