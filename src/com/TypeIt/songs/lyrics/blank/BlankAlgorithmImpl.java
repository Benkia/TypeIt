package com.TypeIt.songs.lyrics.blank;

import java.util.*;

import static java.lang.Character.isWhitespace;

/**
 * Created by admin on 12/05/2017.
 */
public class BlankAlgorithmImpl implements BlankAlgorithm {
    private static final String BLANK = "_";
    public static final char BLANK_CHAR = BLANK.charAt(0);

    /**
     * This map holds the expressions we want to make blank as keys, and the output blank expressions as values
     *
     * Example: When we see a "ou" we want to change it to "_u".
     *          Key: "ou", Value: "_u"
     */
    private final Map<String, String> blankExpressions = getExpressionsToBlank();

    // TODO: THIS IS SHIT
    private final Random random = new Random();

    @Override
    public BlankAlgorithmSolution run(String lyricsString, Boolean[] chosenIndexes) {
        StringBuilder sb = new StringBuilder(lyricsString);

        int lastWhiteSpaceIndex = -1;

        // Iterate over all the given words
        for (int charIndex=0; charIndex<lyricsString.length(); charIndex++) {
                char currentChar = lyricsString.charAt(charIndex);

                if (isWhitespace(currentChar)) {
                    // Get the current word by splitting from the last white space to this one
                    String currentWord = lyricsString.substring(lastWhiteSpaceIndex + 1, charIndex);
                    lastWhiteSpaceIndex = charIndex;

                    // Iterate over all the expressions we want to make blank
                    for (String expression : blankExpressions.keySet()) {
                        // TODO: THINK OF A BETTER WAY
                        if (random.nextBoolean()) {
                            // Get the index of the current expression in the current word
                            int expressionIndex = currentWord.indexOf(expression);

                            // If the expression exists
                            if (expressionIndex != -1) {
                                // Change every character in the expression to blank
                                for (int i = 0; i < expression.length(); i++) {
                                    int indexToBlank = charIndex - currentWord.length() + expressionIndex + i;

                                    // Perform the blank only for indexes that were chosen in the Density algorithm
                                    if (chosenIndexes[indexToBlank]) {
                                        // Change the lyrics string as well
                                        // The index we're in now (at the whitespace) - the word's length (to get to the start of the word) + the index where the expression starts + the current character
                                        sb.setCharAt(indexToBlank, blankExpressions.get(expression).charAt(i));
                                    }
                                }
                            }
                        }
                    }

                }
        }

        return new BlankAlgorithmSolution(sb.toString());
    }

    private Map<String, String> getExpressionsToBlank() {
        Map<String, String> expressions = new HashMap<>();
        String twoBlanks = BLANK + BLANK;

        expressions.put("ou", twoBlanks);
        expressions.put("io", twoBlanks);
        expressions.put("gh", twoBlanks);
        expressions.put("th", twoBlanks);
        expressions.put("ea", twoBlanks);
        expressions.put("am", BLANK_CHAR + "m");
        expressions.put("is", BLANK_CHAR + "s");

        return expressions;
    }
}