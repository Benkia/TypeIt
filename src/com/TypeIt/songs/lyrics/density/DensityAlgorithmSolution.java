package com.TypeIt.songs.lyrics.density;

/**
 * Created by admin on 12/05/2017.
 */
public class DensityAlgorithmSolution {
    private Boolean[] chosenIndexes;
    private String chosenLyricsString;

    public DensityAlgorithmSolution(Boolean[] chosenIndexes, String chosenLyricsString) {
        this.chosenIndexes = chosenIndexes;
        this.chosenLyricsString = chosenLyricsString;
    }

    /**
     * This array represents all the characters in the lyrics string, and whether each character was selected by the density algorithm.
     * @return
     */
    public Boolean[] getChosenIndexes() {
        return chosenIndexes;
    }
    public String getChosenLyricsString() {
        return chosenLyricsString;
    }
}
