package com.TypeIt.songs.lyrics;

import com.TypeIt.gui.language.Language;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naveh on 03/01/2017.
 */
public class LyricsConfiguration {
    public final static String LINE_END = "@";
    private final static char LINE_END_CHAR = '@';
    private final static char SYLLABLE_END = '.';
    private final static char WORD_END = '!';

    private String rawText;

    public LyricsConfiguration(String rawText) {
        this.rawText = rawText;
    }

    public String getLyricsText() {
        String lyrics = "";

        for (char c : rawText.toCharArray()) {
            switch (c) {
                case (WORD_END): {
                    lyrics += " ";

                    break;
                }
                case (LINE_END_CHAR): {
                    lyrics += "\n";

                    break;
                }
                case (SYLLABLE_END): {
                    break;
                }
                default: {
                    lyrics += c;
                }
            }
        }

        return lyrics;
    }

    public String[] getSyllables() {
        List<String> syllables = new ArrayList<String>();

        int lastSpecialCharIndex = 0;

        for (int i=0; i<rawText.length(); i++) {
            char c = rawText.charAt(i);

            if (c == SYLLABLE_END || c == WORD_END || c == LINE_END_CHAR) {
                syllables.add(rawText.substring(lastSpecialCharIndex, i));
                lastSpecialCharIndex = i+1;
            }
        }

        String[] toReturn = new String[syllables.size()];

        // Cast it to String[]
        for (int i=0; i<syllables.size(); i++) {
            toReturn[i] = syllables.get(i);
        }

        return toReturn;
    }

    public Language getLanguage() {
        int i = 0;
        char current = rawText.charAt(i);

        if (Character.getDirectionality(current) == Character.DIRECTIONALITY_RIGHT_TO_LEFT) {
            return Language.HEBREW;
        }

        return Language.ENGLISH;
    }
}