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
        return wordRank < 30;
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

//package com.TypeIt.songs.lyrics.density;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static java.lang.Character.isWhitespace;
//
///**
// * Created by admin on 12/05/2017.
// */
//public class DensityAlgorithmImpl implements DensityAlgorithm {
//    private static boolean f = false;
//
//    @Override
//    public DensityAlgorithmSolution filter(String lyricsString, String rawString) {
//        List<String> filteredWords = new ArrayList<>();
//
//        // Initialize an array with false values
//        Boolean[] wasIndexChosen = new Boolean[lyricsString.length()];
//        for (int i=0; i<lyricsString.length(); i++) {
//            wasIndexChosen[i] = false;
//        }
//
//        int lastWhitespaceIndex = -1;
//
//        // Go over all the characters in the lyrics string
//        for (int i=0; i<lyricsString.length(); i++) {
//            char currentChar = lyricsString.charAt(i);
//
//            // If we hit a whitespace
//            if (isWhitespace(currentChar)) {
//                // Split from the last whitespace to this one.
//                String currentWord = lyricsString.substring(lastWhitespaceIndex+1, i);
//
//                // Check if we should add the word
//                if (shouldAddWord(currentWord)) {
//                    filteredWords.add(currentWord);
//
//                    // Add the whitespace as well
//                    filteredWords.add(String.valueOf(currentChar));
//
//                    // TODO: Test - change the number here from 1 to 3 to select every 3rd word
//                    if (filteredWords.size() % 1 == 0) {
//                        // Mark all characters in this word as chosen.
//                        // Go over all the characters indexes since the last whitespace, and until the current one.
//                        for (int charIndex=lastWhitespaceIndex+1; charIndex<i+1 && charIndex<lyricsString.length(); charIndex++) {
//                            wasIndexChosen[charIndex] = true;
//                        }
//                    }
//                }
//
//                lastWhitespaceIndex = i;
//            }
//        }
//
//        StringBuilder chosenLyrics = new StringBuilder();
//
//        for (String word : filteredWords) {
//            chosenLyrics.append(word);
//        }
//
//        return new DensityAlgorithmSolution(wasIndexChosen, chosenLyrics.toString());
//    }
//
//    private boolean shouldAddWord(String word) {
//        f = !f;
//        return f;
//    }
//}
