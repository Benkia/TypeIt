package com.TypeIt.files;

import com.TypeIt.songs.Song;
import com.TypeIt.songs.SongUserData;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Naveh on 11/1/2017.
 */
public class UserDataFileManager {
    private final static String USER_DATA_FILE_PATH = "assets/user_data/" + "UserData.csv";

    // Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    // CSV file header
    private static final String FILE_HEADER = SongUserData.getCSVHeader();

    /**
     * @return Key - Song Name, Value - SongUserData
     */
    public Map<String, SongUserData> readUserData() throws IOException {
        Map<String, SongUserData> songDataMap = new HashMap<>();

        BufferedReader fileReader = null;
        try {
            String line;

            // Create the file reader
            fileReader = new BufferedReader(new FileReader(USER_DATA_FILE_PATH));

            // Read the CSV file header to skip it
            fileReader.readLine();

            int currentMemberIndex;

            // Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                // Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                    currentMemberIndex = 0;

                    // Create the song
                    Song currentSong = new Song(tokens[currentMemberIndex++], tokens[currentMemberIndex++]);

                    // Create a new user-data object and fill its data
                    SongUserData songUserData = new SongUserData(currentSong,
                                                                 tokens[currentMemberIndex++],
                                                                 Float.parseFloat(tokens[currentMemberIndex++]));

                    songDataMap.put(songUserData.getSongName(), songUserData);
                }
            }
        }
        finally {
            try {
                fileReader.close();
            }
            catch (Exception e) {}
        }

        return songDataMap;
    }

    public void writeUserData(Collection<SongUserData> songUserDataMap) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(USER_DATA_FILE_PATH);

            // Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            // Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            // Write a new student object list to the CSV file
            for (SongUserData songUserData : songUserDataMap) {
                fileWriter.append(songUserData.toString(COMMA_DELIMITER));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("User data file written successfully");

        }
        catch (Exception e) {
            System.out.println("Error while writing user data file");
            e.printStackTrace();
        }
        finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            }
            catch (IOException e) {
                System.out.println("Error while flushing/closing user data file");
                e.printStackTrace();
            }
        }
    }
}
