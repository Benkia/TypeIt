package com.TypeIt.gui;

import com.TypeIt.gui.language.Language;

/**
 * Created by Asaf on 03/01/2017.
 */
public interface ILyricsViewController {
    void setKeyboardLanguage(Language language);
    void playBackroundTrack();
    void drawNote();

    void manageKeyTyped(char typedChar);
    boolean didUserTypeCorrectly(char typedChar);
    void userIsCorrect(String currentSyllable);
    void userIsIncorrect(String currentSyllable);

    // After key typed is managed
    void refreshScreen();
}
