package com.TypeIt.songs.lyrics.density.WordComplexityCalculator;

/**
 * Created by User on 10/06/2017.
 */
public class WordComplexityCalculator {

    public int calculate(String rawWord){

        String wordWithConcatedSyllables = "";
        for (String currSyllable : rawWord.split(".")){
            wordWithConcatedSyllables += currSyllable;
        }

        if (wordWithConcatedSyllables == "")
            wordWithConcatedSyllables = rawWord;

        return new WordDistanceComplexityCalculator().calculate(wordWithConcatedSyllables) +
                new WordSyllablesComplexityCalculator().calculate((rawWord));
    }
}
