package com.TypeIt.songs.lyrics.density;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 12/05/2017.
 */
public class DensityAlgorithmImpl implements DensityAlgorithm {
    @Override
    public List<String> chooseWords(String lyricsString) {
        return getWordArrayFromString(lyricsString);
    }

    private List<String> getWordArrayFromString(String lyricsString) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(lyricsString);
        int lastIndex = breakIterator.first();

        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();

            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(lyricsString.charAt(firstIndex))) {
                words.add(lyricsString.substring(firstIndex, lastIndex));
            }
        }

        return words;
    }
}
