package com.TypeIt.gui;

import com.TypeIt.gui.language.Language;
import com.TypeIt.main.Constants;
import com.TypeIt.main.UserDataManager;
import com.TypeIt.songs.melody.Melody;
import com.TypeIt.sound.MidiBackgroundTrackPlayer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;

import java.awt.peer.FontPeer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Character.isWhitespace;
import static javafx.stage.Screen.getPrimary;

/**
 * Created by Asaf on 03/01/2017.
 */
public class LyricsViewController extends AbstractLyricsViewController {
    private static final int SCORE_WINDOW_WIDTH = 1000;
    private static final int SCORE_WINDOW_HEIGHT = 200;
    private static final int FONT_SIZE = Constants.DEFAULT_FONT_SIZE*4/3;
    private static final long MILLIS_TO_NORMAL_SPEED = 1000;
    private int whiteSpacesNum = 0;

    @FXML
    private VBox box;

    @FXML
    private Label songTitle;

    @FXML
    private TextFlow lyricsTextArea;

    @FXML
    private TextArea userTextArea;

    @FXML
    void initialize() {
        assert lyricsTextArea != null : "fx:id=\"lyricsTextArea\" was not injected: check your FXML file 'LyricsViewController.fxml'.";

        // Initialize the font
        Font font = getDefaultFont();

        screenBounds = Screen.getPrimary().getVisualBounds();

        this.userTextArea.requestFocus();

        box.setAlignment(Pos.TOP_CENTER);
        songTitle.setTextAlignment(TextAlignment.CENTER);

        songTitle.setFont(font);
        songTitle.setText(Melody.getSongTitle(Constants.fileName));

        lyricsTextArea.setPrefSize(FONT_SIZE*1d, FONT_SIZE*1d);
        lyricsTextArea.setTextAlignment(TextAlignment.CENTER);

        Platform.runLater(() -> {
            this.userTextArea.getScene().setOnKeyReleased(event -> {

                if (event.getCode() == KeyCode.ENTER) {
                    playBackroundTrack();
                    return;
                }

                if (!event.getText().isEmpty()) {
                    // Make sure we haven't finished the song
                    if (syllables.length >= currentSyllableIndex) {
                        char typedChar = event.getText().charAt(0);

                        try {
                            manageKeyTyped(typedChar);
                        }
                        catch (StringIndexOutOfBoundsException e) {}
                        finally {
                            // Check if we finished the song
                            if (totalIndex == lyrics.length()) {
                                done();
                            }
                        }
                    }
                }
            });

            this.refreshScreen();
        });

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);

        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:assets/images/lyrics_background.png"),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        box.setBackground(new Background(backgroundImage));

        slider.setMin(MIN_SPEED);
        slider.setMax(MAX_SPEED);
        slider.setValue(MAX_SPEED);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.1d);
        slider.setMinorTickCount(10);
        slider.setBlockIncrement(0.1d);
        slider.setOnMouseDragged(mouseEvent -> setSpeed((float) slider.getValue()));
        slider.valueProperty().addListener((observable, oldValue, newValue) -> setSpeed((float) slider.getValue()));

        if (Constants.fileName.equals("Ha Tikva")) {
            slider.setMax(MAX_SPEED*2);
        }

        // Hide the crappy slider!!!!!
        slider.setVisible(false);
    }

    @Override
    public void manageKeyTyped(char typedChar) {
        String currentSyllable = syllables[currentSyllableIndex];

        if (didUserTypeCorrectly(typedChar)) {
            // User is correct
            userIsCorrect(currentSyllable);
        }
        else {
            // User is incorrect
            userIsIncorrect(currentSyllable);
        }

        // If there are any syllables left
        if (syllables.length >= currentSyllableIndex) {
            refreshScreen();
        }
    }

    @Override
    protected void manageSizes() {
        // 1300 / 1920
        double boxWidth = screenBounds.getWidth()*0.65f;
        setWidth(slider, boxWidth/2);
        setWidth(box, boxWidth);
        setWidth(lyricsTextArea, boxWidth*0.95f);
//        lyricsTextArea.setPrefSize(FONT_SIZE);
        setWidthAndFontSize(songTitle, boxWidth*1.2);
    }

    private void setWidthAndFontSize(Label l, double labelWidth) {
        setWidth(l, labelWidth);

        int textLength = l.getText().length();
        double fontSize = labelWidth / textLength;

        Font newFont = getDefaultFont(fontSize);

        if (newFont != null) {
            l.setFont(newFont);
        }
        else {
            l.setFont(Font.font("Courier New", FontWeight.BOLD, fontSize));
        }

        l.setAlignment(Pos.CENTER);
        l.setTextAlignment(TextAlignment.CENTER);
    }

    private void setWidth(Region r, double width) {
        r.setMinWidth(width);
        r.setPrefWidth(width);
        r.setMaxWidth(width);
    }

    @Override
    protected void incrementSyllable() {
        super.incrementSyllable();
        this.userTextArea.setText("");
    }

    @Override
    public void userIsCorrect(String currentSyllable) {
        super.userIsCorrect(currentSyllable);
    }

    @Override
    public void userIsIncorrect(String currentSyllable) {
        // User failed
        super.userIsIncorrect(currentSyllable);

        // Update the hidden text field so that it's like the user was correct
        userTextArea.setText(currentSyllable.substring(0, currentCharIndex+1));
    }

    private int normalize(int n) {
        if (n < 0) {
            return 0;
        }
        else if (n > 255) {
            return 255;
        }

        return n;
    }

    @Override
    public void refreshScreen() {
        int characters = lyrics.length();
        int r = mistakes*255 / characters;
        int g = (totalIndex-mistakes)*255 / characters;

        normalize(r);
        normalize(g);

        // Make the song title go red/green as the song progresses
        songTitle.setTextFill(Color.rgb(r, g, 0));

        lyricsTextArea.getChildren().removeAll(lyricsTextArea.getChildren());

        try {
            for (int i = 0; i < totalIndex; i++) {
                String currentChar = lyrics.substring(i, i+1);

                if (!currentChar.equals(" ") && !currentChar.equals("\n")) {
                    if (chosenIndexes[i]) {
                        if (userTypedCorrect[i]) {
                            appendToPane(currentChar, "green", false);
                        } else {
                            appendToPane(currentChar, "red", false);
                        }
                    }
                    else {
                        appendToPane(currentChar, "gray", false);
                    }
                }
                else {
                    appendToPane(currentChar, "black",false);
                }
            }

            int resOfLyricsIndex = totalIndex + syllables[currentSyllableIndex].length() - currentCharIndex;

            // Append the current syllable
            String currentSyllable = lyrics.substring(totalIndex, resOfLyricsIndex);
            appendToPane(currentSyllable/*.substring(currentCharIndex)*/, "blue", true);

            // Append the rest of the lyrics, which the user hasn't touched yet.
            String restOfLyrics = lyrics.substring(resOfLyricsIndex);

            // Ite
            for (int i=0; i<restOfLyrics.length(); i++) {
                String currentChar = restOfLyrics.substring(i, i+1);

                if (chosenIndexes[resOfLyricsIndex + i]) {
                    appendToPane(currentChar, "black", false);
                }
                else {
                    appendToPane(currentChar, "gray", false);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendToPane(String msg, String color, boolean isCurrentCharacter) {
        int size = (int) (FONT_SIZE*0.8f);
        if (isCurrentCharacter) {
            size *= 2;
        }

        Text t1 = new Text();
        t1.setStyle("-fx-fill: " + color + ";" +
                    "-fx-font-size: " + (size*2) + ";");

        // Set the font
        if (font != null) {
            t1.setFont(font);
        }
        else {
            t1.setFont(Font.font("Courier New", FontWeight.BOLD, size));
        }

        t1.setText(msg);

        // If the user was wrong, blur the character
        if (color.equals("red")) {
            GaussianBlur blur = new GaussianBlur();
            blur.setRadius(FONT_SIZE * 8 / 100);
            t1.setEffect(blur);
        }

        lyricsTextArea.getChildren().add(t1);
    }

    @Override
    public void setKeyboardLanguage(Language language) {
        try {
            if (language.equals(Language.HEBREW)) {
//                System.out.println(sun.awt.im.InputContext.getInstance().getLocale());
//
//                Locale[] a = Locale.getAvailableLocales();
//
//                for (Locale o : a) {
//                    if (o.getCountry().equals("IL") || o.getCountry().equals("iw")) {
//                        Locale.setDefault(o);
//                        sun.awt.im.InputContext.getInstance().selectInputMethod(o);
//
//
//                        System.out.println(sun.awt.im.InputContext.getInstance().getLocale());
//                    }
//                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createScoreScene() {
        final int successes = calculateUserSuccesses();
        final int charsNum = lyrics.length() - whiteSpacesNum;
        final float percentage = ((100f*successes) / charsNum);

        // Continue to ScoreController
        FXMLLoader loader = new FXMLLoader(ScoreController.class.getResource("ScoreView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        loader.<ScoreController>getController().setUserPerformanceData(percentage, charsNum);

        stage.setTitle("TypeIt - Your score");
        stage.setScene(new Scene(root, SCORE_WINDOW_WIDTH, SCORE_WINDOW_HEIGHT));
        stage.show();

        loader.<ScoreController>getController().setStage(stage);

        // Save the score to the user data file
        UserDataManager.scored(Constants.fileName, percentage/100);
    }

    private int calculateUserSuccesses() {
        int successes = 0;

        for (int i=0; i<userTypedCorrect.length; i++) {
            boolean b = userTypedCorrect[i];

            if (!isWhitespace(lyrics.charAt(i)) &&
                    // Treat an un-chosen character as a whitespace: Don't count it
                chosenIndexes[i]) {
                if (b) {
                    successes++;
                }
            }
            else {
                whiteSpacesNum++;
            }
        }

        return successes;
    }

    private void done() {
        createScoreScene();

//        if (bendPitch) {
            // Start a new thread, because we use Thread.sleep()
            new Thread(() -> {
                // Make sure we don't divide by 0
//                if (bgPlayer.getSpeed() != MAX_SPEED) {
                if (!closeEnoughToOriginalSpeed()) {
                    final float iterations = (Math.abs(MAX_SPEED - bgPlayer.getSpeed()) / SPEED_TO_CHANGE_EVERY_KEY);

                    // Example:
                    // speed = 0.5f
                    // millis = 3000 / (((1-0.5)/0.01)) = 3000/ (0.5/0.01) = 3000/50 = 60
                    final long millisToSleep = (long) ((float) MILLIS_TO_NORMAL_SPEED / iterations);

                    // As long as we haven't reached original speed
                    while (bgPlayer.isPlaying() && !closeEnoughToOriginalSpeed()) {
                        int sign = 1;
                        if (bgPlayer.getSpeed() > MAX_SPEED) {
                            sign = -1;
                        }
                        // Keep adding speed
                        addSpeed(SPEED_TO_CHANGE_EVERY_KEY*sign);

                        try {
                            Thread.sleep(millisToSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
//        }
    }

    private static final float delta = SPEED_TO_CHANGE_EVERY_KEY*0.99f;

    private boolean closeEnoughToOriginalSpeed() {
        return bgPlayer.getSpeed() >= MAX_SPEED - delta
               &&
               bgPlayer.getSpeed() <= MAX_SPEED + delta;
    }
}
