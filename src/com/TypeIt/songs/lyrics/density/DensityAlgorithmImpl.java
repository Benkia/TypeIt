package com.TypeIt.songs.lyrics.density;

import com.TypeIt.songs.lyrics.density.WordComplexityCalculator.WordComplexityCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Character.isWhitespace;

/**
 * Created by admin on 12/05/2017.
 */
public class DensityAlgorithmImpl implements DensityAlgorithm {

    private boolean shouldAddWord(int wordRank) {
        return wordRank < 30000;
    }

    private WordComplexityCalculator calculator = new WordComplexityCalculator();

    @Override
    public DensityAlgorithmSolution filter(String rawLyrics, String lyricsString) {
        List<String> filteredWords = new ArrayList<>();

        // Initialize an array with false values
        Boolean[] wasIndexChosen = new Boolean[lyricsString.length()];
        Arrays.fill(wasIndexChosen, false);

        int currentIndexToModify = 0;

        String[] allSentences = rawLyrics.split("@");

        for (int i = 0; i < allSentences.length; ++i){
            String[] currSentenceWords = allSentences[i].split("!");

            for (int j = 0; j < currSentenceWords.length; ++j){
                boolean shouldAddWord = this.shouldAddWord(calculator.calculate(currSentenceWords[j]));

                String currentWordAsLyricsWord = currSentenceWords[j].replace(".", "")
                        .concat(j + 1 == currSentenceWords.length? "\n" : " ");

                for (int w = 0; w < currentWordAsLyricsWord.length(); ++w){
                    wasIndexChosen[currentIndexToModify] = shouldAddWord;
                    ++currentIndexToModify;
                }

                if (shouldAddWord)
                    filteredWords.add(currentWordAsLyricsWord);

            }
        }

        StringBuilder chosenLyrics = new StringBuilder();

        for (String word : filteredWords) {
            chosenLyrics.append(word);
        }

        return new DensityAlgorithmSolution(wasIndexChosen, chosenLyrics.toString());
    }
}