package com.TypeIt.songs.lyrics.density.WordComplexityCalculator;

/**
 * Created by User on 10/06/2017.
 */
public class WordDistanceComplexityCalculator {
    private final String chars = "qwertyuiopasdfghjkl;zxcvbnm,.";

    public int calculate(String word){
        double complexity = 0;

        for (int i = 0; i < word.length() - 1; ++i)
            complexity += distance(word.charAt(i), word.charAt(i + 1));

        return (int)complexity;
    }

    private double distance(char c1, char c2) {
        return Math.sqrt(Math.pow(colOf(c2)-colOf(c1),2)+Math.pow(rowOf(c2)-rowOf(c1),2));
    }

    private int rowOf(char c) {
        return chars.indexOf(c) / 10;
    }

    private int colOf(char c) {
        return chars.indexOf(c) % 10;
    }
}
