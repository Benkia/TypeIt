package com.TypeIt.files;

import com.TypeIt.sound.Notes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/05/2017.
 */
public class MelodyFileReader {
    private final static String FILE_FORMAT_SUFFIX = " Notes.txt";
    private final static String COMMENT = "//";
    private final static char DELIMITER = ',';

    public Integer[] readMelody(String fileName) {
        List<Integer> notes = new ArrayList<>();
        String fileContents = "";
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName + FILE_FORMAT_SUFFIX), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if (!line.contains(COMMENT)) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }

                line = br.readLine();
            }

            // Get the contents of the file
            fileContents = sb.toString();

            // Iterate over the numbers, and create an array of notes
            for (int i=0; i<fileContents.length();) {
                char currentChar = fileContents.charAt(i);
                int currentStringIndex = 0;

                while (currentChar != DELIMITER) {
                    currentStringIndex++;

                    if (i+currentStringIndex < fileContents.length()) {
                        // Increment to the next character
                        currentChar = fileContents.charAt(i + currentStringIndex);
                    }
                    else {
                        // We got to the end of the file. Exit the loop
                        break;
                    }
                }

                // Get the actual string, without the delimiter
                String currentNoteString = fileContents.substring(i, i+currentStringIndex);

                // Trim whitespaces
                currentNoteString = currentNoteString.trim();

                // Get the actual midi note from the string note
                int currentNoteMidi = Notes.resolveNote(currentNoteString);

                notes.add(currentNoteMidi);

                // Move on to the next note
                i = i+currentStringIndex+1;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (br != null) {
                    br.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create an array from the list
        return notes.toArray(new Integer[notes.size()]);
    }
}
