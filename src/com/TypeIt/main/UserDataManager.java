package com.TypeIt.main;

import com.TypeIt.files.UserDataFileManager;
import com.TypeIt.songs.Song;
import com.TypeIt.songs.SongUserData;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Naveh on 11/1/2017.
 */
public class UserDataManager {
    private static final UserDataFileManager fileManager = new UserDataFileManager();
    private static Map<String, SongUserData> songDataMap;

    public UserDataManager(List<Song> songs) {
        try {
            songDataMap = fileManager.readUserData();

            boolean needToWriteAgain = false;

            for (Song song : songs) {
                // Make sure every song in the melody list exists
                if (!songDataMap.containsKey(song.getSongName())) {
                    songDataMap.put(song.getSongName(), new SongUserData(song, song.getSongName(), 0));

                    needToWriteAgain = true;
                }
            }

            if (needToWriteAgain) {
                writeAndReload(songDataMap.values());
            }

            // After writing new records for new songs, remove the un-needed songs from the loaded songDataMap
            Map<String, SongUserData> tempSongDataMap = new HashMap<>();

            for (Song song : songs)
                tempSongDataMap.put(song.getSongName(), songDataMap.get(song.getSongName()));

            songDataMap = tempSongDataMap;
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found, trying to create it.");
            List<SongUserData> songUserDataList = new ArrayList<>();

            for (Song song : songs) {
                SongUserData newSongUserData = new SongUserData(song, song.getSongName(), 0);
                songUserDataList.add(newSongUserData);
            }

            writeAndReload(songUserDataList);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAndReload(Collection<SongUserData> newSongDataList) {
        fileManager.writeUserData(newSongDataList);

        // Immediately after writing it, try to read the file.
        try {
            songDataMap = fileManager.readUserData();
        }
        catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("Unable to read user data file.");
        }
    }

    public static void scored(String songName, float score) {
        String[] splited = songName.split("/");
        songDataMap.get(splited[splited.length - 1]).setUserHighScore(score);
        fileManager.writeUserData(songDataMap.values());
    }

    public Map<String, SongUserData> getSongDataMap() {
        return songDataMap;
    }
}
