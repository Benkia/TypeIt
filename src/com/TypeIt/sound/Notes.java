package com.TypeIt.sound;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 11/05/2017.
 */
public class Notes {
    private static final int NOTES_IN_OCTAVE = 12;
    private static final Map<String, Integer> notes = createMap();

    /**
     * Note names
     */
    private static final String C = "C";
    private static final String Csharp = "Cs";
    private static final String D = "D";
    private static final String Dsharp = "Ds";
    private static final String E = "E";
    private static final String F = "F";
    private static final String Fsharp = "Fs";
    private static final String G = "G";
    private static final String Gsharp = "Gs";
    private static final String A = "A";
    private static final String Asharp = "As";
    private static final String B = "B";

    /**
     * Note midi numbers
     */
    private static final int C_Note = 60;
    private static final int Csharp_Note = 61;
    private static final int D_Note = 62;
    private static final int Dsharp_Note = 63;
    private static final int E_Note = 64;
    private static final int F_Note = 65;
    private static final int Fsharp_Note = 66;
    private static final int G_Note = 67;
    private static final int Gsharp_Note = 68;
    private static final int A_Note = 69;
    private static final int Asharp_Note = 70;
    private static final int B_Note = 71;

    /**
     * Functions
     */
    private final static String HIGHER_OCTAVE_FUNC = "h";
    private final static String LOWER_OCTAVE_FUNC = "l";


    private static Map<String, Integer> createMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put(C, C_Note);
        map.put(Csharp, Csharp_Note);
        map.put(D, D_Note);
        map.put(Dsharp, Dsharp_Note);
        map.put(E, E_Note);
        map.put(F, F_Note);
        map.put(Fsharp, Fsharp_Note);
        map.put(G, G_Note);
        map.put(Gsharp, Gsharp_Note);
        map.put(A, A_Note);
        map.put(Asharp, Asharp_Note);
        map.put(B, B_Note);

        return map;
    }

    public static int resolveNote(String noteString) {
        int octaveOffset = 0;

        // Manage higher/lower octaves
        while (noteString.startsWith(HIGHER_OCTAVE_FUNC)) {
            // Increment and remove the "h()"
            noteString = noteString.substring(2, noteString.length()-1);
            octaveOffset++;
        }
        while (noteString.startsWith(LOWER_OCTAVE_FUNC)) {
            // Increment and remove the "l()"
            noteString = noteString.substring(2, noteString.length()-1);
            octaveOffset--;
        }

        // Now, we are left with only the core midi note string.
        Integer noteMidi = notes.get(noteString);

        if (noteMidi != null) {
            // Get the actual midi note in the correct octave
            noteMidi = changeOctave(noteMidi, octaveOffset);
            return noteMidi;
        }

        // Default:
        return 0;
    }

    /**
     * This function returns the given note at a higher or lower octave.
     */
    private static int changeOctave(int note, int octavesOffset) {
        return note + (octavesOffset*NOTES_IN_OCTAVE);
    }
}
