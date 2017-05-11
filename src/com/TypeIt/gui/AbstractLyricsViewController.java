package com.TypeIt.gui;

import com.TypeIt.gui.language.KeyCodeMap;
import com.TypeIt.gui.language.Language;
import com.TypeIt.songs.lyrics.LyricsConfiguration;
import com.TypeIt.main.Constants;
import com.TypeIt.songs.melody.Melody;
import com.TypeIt.sound.BGPlayer;
import com.TypeIt.sound.BackgroundTrackPlayer;
import com.TypeIt.sound.MelodyPlayer;
import com.TypeIt.sound.MidiBackgroundTrackPlayer;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class AbstractLyricsViewController implements ILyricsViewController {
    protected static final float MIN_SPEED = 0.5f;
    protected static final float MAX_SPEED = 1f;
    protected static final float SPEED_TO_CHANGE_EVERY_KEY = 0.01f;
    protected boolean bendPitch;
    protected Rectangle2D screenBounds;

    @FXML
    protected Slider slider;

    // Music players
    protected final MelodyPlayer melodyPlayer = new MelodyPlayer(0);
    protected BGPlayer bgPlayer = new BackgroundTrackPlayer(Constants.fileName, SPEED_TO_CHANGE_EVERY_KEY);

    protected Stage stage;
    protected String lyrics;
    protected String[] syllables;
    protected Integer[] notes;

    protected boolean[] userTypedCorrect;

    protected int currentOctave = 0;
    protected int totalIndex = 0;
    protected int currentCharIndex = 0;
    protected int currentSyllableIndex = 0;

    private Language lang;

    public AbstractLyricsViewController(){
        Melody melody = new Melody();
        this.notes = melody.getNotes();

        if (Constants.fileName.equals("Ha Tikva")) {
            bgPlayer = new MidiBackgroundTrackPlayer(Constants.fileName, melody);
        }
    }

    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;
        stage.centerOnScreen();

        stage.setFullScreen(true);

        Image cursor = new Image("file:cursor.png");
        stage.getScene().setCursor(new ImageCursor(cursor));

        stage.setOnCloseRequest(windowEvent -> {
            BackgroundTrackPlayer.stopEverything();
            MidiBackgroundTrackPlayer.stopEverything();
            MelodyPlayer.stopEverything();
        });

        manageSizes();
    }

    protected abstract void manageSizes();

    public void setRawText(String rawText){
        LyricsConfiguration config = new LyricsConfiguration(rawText);

        this.lyrics = config.getLyricsText();
        this.syllables = config.getSyllables();
        this.notes = new Melody().getNotes();

        userTypedCorrect = new boolean[lyrics.length()];

        lang = config.getLanguage();
        setKeyboardLanguage(lang);
    }

    public void setBendPitch(boolean bendPitch) {
        this.bendPitch = bendPitch;
    }

    protected void incrementCharacter(boolean userSuccess) {
        totalIndex++;

        char currentChar = lyrics.charAt(totalIndex);

        // Skip white-spaces
        while (isWhiteSpace(currentChar)) {
            totalIndex++;

            currentChar = lyrics.charAt(totalIndex);
        }

        if (currentCharIndex + 1 < syllables[currentSyllableIndex].length()) {
            // Still didn't finish the whole syllable
            currentCharIndex++;

            if (userSuccess) {
                // Character success
                currentOctave++;
            }
        }
        else {
            // Syllable is completed
            incrementSyllable();
        }
    }

    protected boolean isWhiteSpace(char currentChar) {
        return (currentChar == ' ' || currentChar == '\n' || currentChar == '\r');
    }

    protected void incrementSyllable() {
        currentSyllableIndex++;
        currentCharIndex=0;
        currentOctave = 0;
    }

    @Override
    public boolean didUserTypeCorrectly(char typedChar) {
        String currentSyllable = syllables[currentSyllableIndex];
        char currentCharToType = currentSyllable.charAt(currentCharIndex);

        // Get the english equivalent of the char to type
        currentCharToType = KeyCodeMap.getInstance().getEnglishCharacterFromHebrew(currentCharToType);
        typedChar = KeyCodeMap.getInstance().getEnglishCharacterFromHebrew(typedChar);

        // Transform both to upper-case characters
        typedChar = Character.toUpperCase(typedChar);
        currentCharToType = Character.toUpperCase(currentCharToType);

        // Check if the typed keys match - This is essentially like changing languages
        return typedChar == currentCharToType;
    }

    @Override
    public void userIsCorrect(String currentSyllable) {
        userTypedCorrect[totalIndex] = true;

        if (bendPitch) {
            addSpeed(SPEED_TO_CHANGE_EVERY_KEY);
        }

        // If syllable just started, currentOctave would be 0.
        melodyPlayer.playNote(notes[currentSyllableIndex], currentOctave);
        incrementCharacter(true);
    }

    @Override
    public void userIsIncorrect(String currentSyllable) {
        // User failed
        userTypedCorrect[totalIndex] = false;
        melodyPlayer.playFailedNote(notes[currentSyllableIndex]);
        incrementCharacter(false);

        if (bendPitch) {
            addSpeed(-SPEED_TO_CHANGE_EVERY_KEY);
        }
    }

    @Override
    public void playBackroundTrack() {
        bgPlayer.play();
        slider.setValue(MAX_SPEED);
    }

    protected void setSpeed(float newSpeed) {
        if (bgPlayer.isPlaying()) {
            boolean midi = (bgPlayer instanceof MidiBackgroundTrackPlayer);

            bgPlayer.setMusicSpeed(newSpeed);

            if (!midi) {
                melodyPlayer.setPitchBendForSpeed(newSpeed);
            }

            System.out.println("Set Speed to " + newSpeed);
        }
    }

    protected void addSpeed(float speedToAdd) {
        if (bgPlayer.isPlaying()) {
            float currentSpeed = bgPlayer.getSpeed();
            float newSpeed = currentSpeed + speedToAdd;

            boolean midi = (bgPlayer instanceof MidiBackgroundTrackPlayer);

            if (newSpeed < MIN_SPEED) {
                newSpeed = MIN_SPEED;
            }
            if (!midi && newSpeed > MAX_SPEED) {
                newSpeed = MAX_SPEED;
            }
            else if (midi && newSpeed > slider.getMax()) {
                newSpeed = (float) slider.getMax();
            }

            bgPlayer.setMusicSpeed(newSpeed);

            if (!midi) {
                melodyPlayer.setPitchBendForSpeed(newSpeed);
            }

            slider.setValue(newSpeed);

            if (currentSpeed != newSpeed) {
                System.out.println("Speed = " + newSpeed);
            }
        }
    }
}
