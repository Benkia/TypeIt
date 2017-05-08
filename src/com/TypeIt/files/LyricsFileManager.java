package com.TypeIt.files;

import com.TypeIt.songs.lyrics.LyricsConfiguration;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Asaf on 03/01/2017.
 */
public class LyricsFileManager {
    private final static String FILE_FORMAT_SUFFIX = ".txt";
    private final static String COMMENT = "//";
    private String fileName;

    public LyricsFileManager(String fileName) {
        this.fileName = fileName;
    }

    public String readLyrics() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName + FILE_FORMAT_SUFFIX), "UTF-8"));
        String everything = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                if (!line.startsWith(COMMENT)) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }

                line = br.readLine();
            }
            everything = sb.toString();

            // Replace line breaks with the custom line-end character.
            everything = everything.replaceAll("\n", LyricsConfiguration.LINE_END);
            everything = everything.replaceAll("\r", "");
        } finally {
            br.close();
        }

        return everything;
    }
}
