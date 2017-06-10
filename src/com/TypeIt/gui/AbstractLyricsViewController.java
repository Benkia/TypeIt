package com.TypeIt.gui;

import com.TypeIt.gui.language.KeyCodeMap;
import com.TypeIt.gui.language.Language;
import com.TypeIt.songs.lyrics.LyricsConfiguration;
import com.TypeIt.main.Constants;
import com.TypeIt.songs.lyrics.blank.BlankAlgorithmImpl;
import com.TypeIt.songs.lyrics.blank.BlankAlgorithmSolution;
import com.TypeIt.songs.lyrics.density.DensityAlgorithmImpl;
import com.TypeIt.songs.lyrics.density.DensityAlgorithmSolution;
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

import java.util.List;

import static java.lang.Character.isWhitespace;

public abstract class AbstractLyricsViewController implements ILyricsViewController {
    protected static final float MIN_SPEED = 0.5f;
    protected static final float MAX_SPEED = 1f;
    protected static final float SPEED_TO_CHANGE_EVERY_KEY = 0.01f;
    protected boolean bendPitch;
    protected boolean challengeMode;
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
    protected Boolean[] chosenIndexes;

    protected boolean[] userTypedCorrect;

    protected int currentOctave = 0;
    protected int totalIndex = 0;
    protected int currentCharIndex = 0;
    protected int currentSyllableIndex = 0;

    private Language lang;

    public AbstractLyricsViewController(){
        Melody melody = new Melody();
        this.notes = melody.getNotes();

//        if (Constants.fileName.equals("Ha Tikva")) {
//            bgPlayer = new MidiBackgroundTrackPlayer(Constants.fileName, melody);
//        }
    }

    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;
        stage.centerOnScreen();

        stage.setFullScreen(true);

        Image cursor = new Image("file:assets/images/cursor.png");
        stage.getScene().setCursor(new ImageCursor(cursor));

        stage.setOnCloseRequest(windowEvent -> {
            BackgroundTrackPlayer.stopEverything();
            MidiBackgroundTrackPlayer.stopEverything();
            MelodyPlayer.stopEverything();
        });

        manageSizes();
    }

    protected abstract void manageSizes();

    public void setRawText(String rawText) {
        // Configure lyrics
        final LyricsConfiguration config = new LyricsConfiguration(rawText);

        config.getSyllables();

        // Run Density algorithm and split the string into a list of words
        //DensityAlgorithmSolution filter = new DensityAlgorithmImpl().filter(config.getLyricsText());
        DensityAlgorithmSolution filter = new DensityAlgorithmImpl().filter(rawText, config.getLyricsText());

        // TODO: This is just the lyrics again, isn't it? (config.getLyricsText())
        String filteredLyricsString = filter.getChosenLyricsString();
        chosenIndexes = filter.getChosenIndexes();

        if (this.challengeMode) {
            // Run the Blank algorithm and make some characters blank
            BlankAlgorithmSolution solution = new BlankAlgorithmImpl().run(filteredLyricsString, chosenIndexes);
            this.lyrics = solution.getLyricsString();
        }
        else {
            this.lyrics = filteredLyricsString;
        }

        this.syllables = config.getSyllables();
        this.notes = new Melody().getNotes();

        userTypedCorrect = new boolean[lyrics.length()];

        lang = config.getLanguage();
        setKeyboardLanguage(lang);

        jumpToNextChosenSyllable();
    }

    public void setBendPitch(boolean bendPitch) {
        this.bendPitch = bendPitch;
    }

    public void setChallengeMode(boolean challengeMode) {
        this.challengeMode = challengeMode;
    }

    protected void incrementCharacter(boolean userSuccess) {
        totalIndex++;

        char currentChar = lyrics.charAt(totalIndex);

        // Skip white-spaces
        while (isWhitespace(currentChar)) {
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

        jumpToNextChosenSyllable();
    }

    private void jumpToNextChosenSyllable() {
        // Keep incrementing characters until we reach the next chosen index
        while (totalIndex < lyrics.length() && !chosenIndexes[totalIndex]) {
            incrementCharacter(false);
        }
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

        if (challengeMode) {
            // Un-blank the current char
            unblank(currentCharToType);
        }

        // Get the english equivalent of the char to type
        currentCharToType = KeyCodeMap.getInstance().getEnglishCharacterFromHebrew(currentCharToType);
        typedChar = KeyCodeMap.getInstance().getEnglishCharacterFromHebrew(typedChar);

        // Transform both to upper-case characters
        typedChar = Character.toUpperCase(typedChar);
        currentCharToType = Character.toUpperCase(currentCharToType);

        // Check if the typed keys match - This is essentially like changing languages
        return typedChar == currentCharToType;
    }

    private void unblank(char currentCharToType) {
        // If the character is a blank one
        if (lyrics.charAt(totalIndex) == BlankAlgorithmImpl.BLANK_CHAR) {
            // Un-blank the character
            lyrics = lyrics.substring(0, totalIndex) + currentCharToType + lyrics.substring(totalIndex+1);
        }
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
