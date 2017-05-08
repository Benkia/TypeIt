package com.TypeIt.gui.language;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Naveh on 6/1/2017.
 */
public class KeyCodeMap {

    private static final Map<Character, Character> keyCodeMap = new HashMap<>();
    private static KeyCodeMap instance = null;

    private KeyCodeMap() {
        keyCodeMap.put('א', 't');
        keyCodeMap.put('ב', 'c');
        keyCodeMap.put('ג', 'd');
        keyCodeMap.put('ד', 's');
        keyCodeMap.put('ה', 'v');
        keyCodeMap.put('ו', 'u');
        keyCodeMap.put('ז', 'z');
        keyCodeMap.put('ח', 'j');
        keyCodeMap.put('ט', 'y');
        keyCodeMap.put('י', 'h');
        keyCodeMap.put('כ', 'f');
        keyCodeMap.put('ל', 'k');
        keyCodeMap.put('מ', 'n');
        keyCodeMap.put('נ', 'b');
        keyCodeMap.put('ס', 'x');
        keyCodeMap.put('ע', 'g');
        keyCodeMap.put('פ', 'p');
        keyCodeMap.put('צ', 'm');
        keyCodeMap.put('ק', 'e');
        keyCodeMap.put('ר', 'r');
        keyCodeMap.put('ש', 'a');
        keyCodeMap.put('ת', ',');
        keyCodeMap.put('/', 'q');
        keyCodeMap.put('\'', 'w');
        keyCodeMap.put('ץ', '.');
        keyCodeMap.put('ך', 'l');
        keyCodeMap.put('ף', ';');
        keyCodeMap.put('ן', 'i');
        keyCodeMap.put('ם', 'o');
    }

    public static KeyCodeMap getInstance() {
        if (instance == null) {
            instance = new KeyCodeMap();
        }

        return instance;
    }

    public char getEnglishCharacterFromHebrew(char hebrewChar) {
        if (keyCodeMap.containsKey(hebrewChar)) {
            return keyCodeMap.get(hebrewChar);
        }

        return hebrewChar;
    }
}
