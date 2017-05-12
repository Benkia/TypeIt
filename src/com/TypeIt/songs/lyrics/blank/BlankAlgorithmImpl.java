package com.TypeIt.songs.lyrics.blank;

import java.util.*;

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
    public BlankAlgorithmSolution run(List<String> lyricsWords, String lyricsString) {
        StringBuilder sb = new StringBuilder(lyricsString);

        int overallStringIndex = 0;
        int wordIndex = 0;

        // Iterate over all the given words
        for (String current : lyricsWords) {
            // Iterate over all the expressions we want to make blank
            for (String expression : blankExpressions.keySet()) {
                // TODO: THINK OF A BETTER WAY
                if (random.nextBoolean()) {
                    // Get the index of the current expression in the current word
                    int index = current.indexOf(expression);

                    // If the expression exists
                    if (index != -1) {
                        StringBuilder blanks = new StringBuilder();

                        // Change every character in the expression to blank
                        for (int i=0; i<expression.length(); i++) {
                            // Change the lyrics string as well
                            sb.setCharAt(overallStringIndex+index+i, blankExpressions.get(expression).charAt(i));
                            blanks.append(BLANK);
                        }

                        // Change the word
                        current = current.replace(expression, blanks.toString());
                        lyricsWords.set(wordIndex, current);
                    }
                }
            }

            // Jump to the next word
            overallStringIndex += current.length();

            // Increment over the whitespaces
            while (overallStringIndex != lyricsString.length() &&
                    (lyricsString.charAt(overallStringIndex) == ' ' ||
                   lyricsString.charAt(overallStringIndex) == '\n')) {
                overallStringIndex++;
            }

            wordIndex++;
        }

        return new BlankAlgorithmSolution(lyricsWords, sb.toString());
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