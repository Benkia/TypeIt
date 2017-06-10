package com.TypeIt.songs.lyrics.density.WordComplexityCalculator;

/**
 * Created by User on 10/06/2017.
 */
public class WordSyllablesComplexityCalculator {
    public int calculate(String rawWord){
        return rawWord.split(".").length;
    }
}
