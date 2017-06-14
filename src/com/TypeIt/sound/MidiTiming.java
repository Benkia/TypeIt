package com.TypeIt.sound;

import java.util.*;

/**
 * Created by Naveh on 03/01/2017.
 */
public class MidiTiming {
    /**
     * HashMap of all song midi notes timings, the key being the Song (example: { Song:{"The Scientist", "Coldplay"}, long[] timings})
     */
    private static final Map<String, long[]> durationsMap = new HashMap<>();

    private static long[] ha_tikva =
            {
                    0,
                    400, 500, 500, 500, 1000, 1000, 500, 500, 500, 500, 2000,
//                    D, E, F, G, A, A, As, A, As, h(D), A,
                    1000, 500, 500, 1000, 1000, 500, 500, 500, 500, 1500,
//                    G, G, G, F, F, E, D, E, F, D,
                    500, 500, 500, 500, 500, 1000, 1000, 500, 500, 500, 500, 2000,
//                    l(A), D, E, F, G, A, A, As, A, As, h(D), A,
                    1000, 500, 500, 1000, 1000, 500, 500, 500, 500, 2000,
//                    G, G, G, F, F, E, D, E, F, D,
//
                    1000, 1000, 1000, 1000, 500, 500, 500, 500, 2000,
//                    D, h(D), h(D), h(D), h(C), h(D), h(C), As, A,
                    1000, 1000, 1000, 1000, 500, 500, 500, 500, 2000,
//                    D, h(D), h(D), h(D), h(C), h(D), h(C), As, A,
                    1000, 500, 500, 1000, 1000, 500, 500, 500, 500, 1000, 500, 500,
//                    h(C), h(C), h(C), F, F, G, A, As, h(C), A, G, F,

                    1000, 1000, 1000, 750, 250, 500, 500, 500, 500, 2000,
//                    G, G, F, F, F, E, D, E, F, D,
                    1000, 500, 500, 1000, 1000, 500, 500, 500, 500, 1000, 500, 500,
//                    G, G, G, F, F, G, A, As, h(C), A, G, F,
                    1000, 1000, 1000, 750, 250, 500, 500, 500, 500, 2000,
//                    G, G, F, F, F, E, D, E, F, D
            };

    public MidiTiming() {
        init();
    }

    private static int[] durations;

    private static void init() {
        durationsMap.put("Ha Tikva", ha_tikva);
    }

    public long[] getDurations(String songName) {
        return durationsMap.get(songName);
    }
}
