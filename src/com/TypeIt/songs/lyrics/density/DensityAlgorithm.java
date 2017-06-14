package com.TypeIt.songs.lyrics.density;

/**
 * Created by admin on 12/05/2017.
 */
public interface DensityAlgorithm {
    DensityAlgorithmSolution filter(String rawString, String lyricsString);
}
