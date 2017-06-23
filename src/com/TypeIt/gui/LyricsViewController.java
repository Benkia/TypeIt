package com.TypeIt.gui;

import com.TypeIt.gui.language.Language;
import com.TypeIt.main.Constants;
import com.TypeIt.main.UserDataManager;
import com.TypeIt.songs.Song;
import com.TypeIt.songs.melody.Melody;
import com.TypeIt.sound.BackgroundTrackPlayer;
import com.TypeIt.sound.MidiBackgroundTrackPlayer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

import java.awt.peer.FontPeer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Character.isWhitespace;
import static javafx.stage.Screen.getPrimary;

/**
 * Created by Asaf on 03/01/2017.
 */
public class LyricsViewController extends AbstractLyricsViewController {
    private static final int FONT_SIZE = Constants.DEFAULT_FONT_SIZE*4/3;
    private static final long MILLIS_TO_NORMAL_SPEED = 1000;
    private int whiteSpacesNum = 0;
    private Boolean showing = false;

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

        screenBounds = Screen.getPrimary().getVisualBounds();
        canvas.setWidth(screenBounds.getWidth());
        canvas.setHeight(screenBounds.getHeight());
        canvas.setLayoutY(0);

        this.userTextArea.requestFocus();

        box.setAlignment(Pos.TOP_CENTER);
        songTitle.setTextAlignment(TextAlignment.CENTER);

        songTitle.setFont(font);
        songTitle.setText(Melody.getSongName(Constants.fileName));

        lyricsTextArea.setPrefSize(FONT_SIZE*1d, FONT_SIZE*1d);
        lyricsTextArea.setTextAlignment(TextAlignment.CENTER);

        Platform.runLater(() -> {
            this.userTextArea.getScene().setOnKeyReleased(event -> {
                LyricsViewController.this.onKeyReleased(event);
            });

            this.refreshScreen();
        });

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);

        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:assets/images/lyrics_background.jpg"),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);

        box.setBackground(new Background(backgroundImage));

//        slider.setMin(MIN_SPEED);
//        slider.setMax(MAX_SPEED);
//        slider.setValue(MAX_SPEED);
//        slider.setShowTickLabels(true);
//        slider.setShowTickMarks(true);
//        slider.setMajorTickUnit(0.1d);
//        slider.setMinorTickCount(10);
//        slider.setBlockIncrement(0.1d);
//        slider.setOnMouseDragged(mouseEvent -> setSpeed((float) slider.getValue()));
//        slider.valueProperty().addListener((observable, oldValue, newValue) -> setSpeed((float) slider.getValue()));
//
//        if (Constants.fileName.equals("Ha Tikva")) {
//            slider.setMax(MAX_SPEED*2);
//        }
//
//        // Hide the crappy slider!!!!!
//        slider.setVisible(false);

        running = true;
        showing = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final GraphicsContext gc = canvas.getGraphicsContext2D();

                while (showing) {
                    List<GuiNote> toRemove = new ArrayList<>();

                    // Before anything is drawn, clear the whole canvas.
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                                gc.clearRect(0, 0, screenBounds.getWidth(), screenBounds.getHeight());
                            }
                    });

                    synchronized (notes) {
                        for (GuiNote note : notes) {
                            Image image = images[note.getImageIndex()];

                            final double W = image.getWidth();
                            final double H = image.getHeight();

                            if (note.getY() < -H) {
                                // Save it and later remove it from the list
                                toRemove.add(note);
                            }
                            else {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        note.move();
                                        gc.drawImage(image, note.getX(), note.getY());
                                    }
                                });
                            }
                        }

                        try {
                            Thread.sleep(33l);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        notes.removeAll(toRemove);
                    }
                }
            }
        }).start();
    }

    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (running) {
                playBackroundTrack();
                return;
            }
            else {
                goBackToChooseSong();
            }
        }

        if (running) {
            // Trim to ignore spaces.
            if (!event.getText().trim().isEmpty()) {
                // Make sure we haven't finished the song
                if (syllables.length >= currentSyllableIndex) {
                    char typedChar = event.getText().charAt(0);

                    try {
                        manageKeyTyped(typedChar);
                    } catch (StringIndexOutOfBoundsException e) {
                    } finally {
                        // Check if we finished the song
                        if (totalIndex == lyrics.length()) {
                            done();
                        }
                    }
                }
            }
        }
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
//        setWidth(slider, boxWidth/2);
//        setWidth(box, boxWidth);
        setWidth(lyricsTextArea, boxWidth*0.95f);
//        lyricsTextArea.setPrefSize(FONT_SIZE);
        setWidthAndFontSize(songTitle, boxWidth);
    }

    private void setWidthAndFontSize(Label l, double labelWidth) {
        setWidth(l, labelWidth);

        int textLength = l.getText().length();
        double fontSize = labelWidth / textLength;

        if (fontSize > 100) {
            fontSize = 100;
        }

        Font newFont = FontUtils.getDefaultFont(fontSize);

        if (newFont != null) {
            l.setFont(newFont);
        }
        else {
            l.setFont(Font.font("Courier New", FontWeight.BOLD, fontSize));
        }

        l.setAlignment(Pos.CENTER);
        l.setTextAlignment(TextAlignment.CENTER);

        l.setTranslateY((fontSize/2.5f));
        l.translateYProperty();
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
        updateSongTitleColor();

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

    private void updateSongTitleColor() {
        int characters = lyrics.length();
        double red = Color.RED.getRed();
        double green = Color.GREEN.getGreen();

        int r = (int) (mistakes*(red*255) / characters);
        int g = (int) ((totalIndex-mistakes)*(green*255) / characters);

        r = normalize(r);
        g = normalize(g);

        // Make it more subtle
        r /= 2;
        g /= 2;

        // Make the song title go red/green as the song progresses
        songTitle.setTextFill(Color.rgb(r, g, 0));
    }

    private void appendToPane(String msg, String color, boolean isCurrentCharacter) {
        int size = (int) (FONT_SIZE*0.8f);
        if (isCurrentCharacter) {
            size *= 2;
        }

        Text t1 = new Text();
        t1.setStyle("-fx-fill: " + color + ";" +
                    "-fx-font-size: " + (size*1.5f) + ";");

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

    private static BackgroundImage scoreBG = new BackgroundImage(new Image("file:assets/images/background.jpg"),
            BackgroundRepeat.REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);

    public void createScoreScene() {
        final int successes = calculateUserSuccesses();
        final int charsNum = lyrics.length() - whiteSpacesNum;
        final double percentage = ((100d*successes) / charsNum);

        running = false;

        // Continue to ScoreController
        stage.setTitle("TypeIt - Your score");

        songTitle.setVisible(false);
        box.getChildren().remove(songTitle);


        box.setAlignment(Pos.TOP_CENTER);
        box.setBackground(new Background(scoreBG));

        refreshScoreLabels(successes, charsNum, percentage);
//        refreshScoreLabels(successes, charsNum, 100d);

        // Save the score in a different thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Save the score to the user data file
                UserDataManager.scored(Constants.fileName, (float) (percentage/100));
            }
        }).start();
    }

    private void goBackToChooseSong() {
        showing = false;

        BackgroundTrackPlayer.stopEverything();
        MidiBackgroundTrackPlayer.stopEverything();

        // Go back to Main menu (Choose song)
        ObservableList<Song> listOfSongs = FXCollections.observableArrayList(Melody.getListOfAllSongs());

        // Create the first controller
        ChooseSongController chooseSongController = new ChooseSongController(stage, listOfSongs);
    }

    private double normalize(double f) {
        if (f < 0) {
            return 0;
        }
        if (f > 1) {
            return 1;
        }

        return f;
    }

    protected String createColorString(double percentage) {
        final double fraction = (percentage/100);

        double r = normalize(1-(fraction/2));
        double g = normalize(fraction);

        Color color = Color.color(r, g, 0);
        String colorString = color.toString();
        colorString = colorString.replaceAll("0x", "#");

        return colorString;
    }

    private void refreshScoreLabels(int correctCharacters, int charactersInLyrics, double percentage) {
        lyricsTextArea.getChildren().clear();
        lyricsTextArea.setMaxWidth(screenBounds.getWidth());

       Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String colorString = createColorString(percentage);

                Font specialFont = FontUtils.getDefaultFont(Constants.DEFAULT_FONT_SIZE*3f);
                Font regularFont = Font.font("Courier New", FontWeight.BOLD, Constants.DEFAULT_FONT_SIZE*3f);

                // Special characters like ':', '/' or '%' look bad in the custom font.
                // Write them with the regular font.
                appendScoreTextToPane("Your score", "white", specialFont);
                appendScoreTextToPane(": ", "white", regularFont);
                appendScoreTextToPane(String.valueOf(correctCharacters), colorString, specialFont);

                appendScoreTextToPane("/","white",regularFont);
                appendScoreTextToPane(String.valueOf(charactersInLyrics),"white",specialFont);
                appendScoreTextToPane(" (", "white", regularFont);

                appendScoreTextToPane(String.valueOf((int)percentage), colorString, specialFont);
                appendScoreTextToPane("%)", "white", regularFont);

//                // Count up from 0%
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Text num = scoreTexts.get(0);
//                        Text perc = scoreTexts.get(1);
//
//                        double currentPerc = 0;
//                        int currentNum = 0;
//
//                        while (currentPerc < percentage) {
//                            currentPerc++;
//
//                            if (currentPerc >= percentage) {
//                                currentPerc = percentage;
//                                currentNum = correctCharacters;
//                            }
//                            else {
//                                currentNum = (int) (((float) currentPerc * charactersInLyrics) / 100f);
//                            }
//
//                            num.setText(String.valueOf(currentNum));
//                            perc.setText(String.valueOf((int)currentPerc));
//
////                            String colorString = createColorString(currentPerc);
////                            num.setStyle("-fx-fill: " + colorString + "; " + "-fx-stroke: black; -fx-stroke-width: 0.2;");
////                            perc.setStyle("-fx-fill: " + colorString + "; " + "-fx-stroke: black; -fx-stroke-width: 0.2;");
//
//                            try {
//                                Thread.sleep(15l);
//                            }
//                            catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }).start();
            }
        });
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

////        if (bendPitch) {
//            // Start a new thread, because we use Thread.sleep()
//            new Thread(() -> {
//                // Make sure we don't divide by 0
////                if (bgPlayer.getSpeed() != MAX_SPEED) {
//                if (!closeEnoughToOriginalSpeed()) {
//                    final float iterations = (Math.abs(MAX_SPEED - bgPlayer.getSpeed()) / SPEED_TO_CHANGE_EVERY_KEY);
//
//                    // Example:
//                    // speed = 0.5f
//                    // millis = 3000 / (((1-0.5)/0.01)) = 3000/ (0.5/0.01) = 3000/50 = 60
//                    final long millisToSleep = (long) ((float) MILLIS_TO_NORMAL_SPEED / iterations);
//
//                    // As long as we haven't reached original speed
//                    while (bgPlayer.isPlaying() && !closeEnoughToOriginalSpeed()) {
//                        int sign = 1;
//                        if (bgPlayer.getSpeed() > MAX_SPEED) {
//                            sign = -1;
//                        }
//                        // Keep adding speed
//                        addSpeed(SPEED_TO_CHANGE_EVERY_KEY*sign);
//
//                        try {
//                            Thread.sleep(millisToSleep);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();
////        }
    }

//    private static final float delta = SPEED_TO_CHANGE_EVERY_KEY*0.99f;
//
//    private boolean closeEnoughToOriginalSpeed() {
//        return bgPlayer.getSpeed() >= MAX_SPEED - delta
//               &&
//               bgPlayer.getSpeed() <= MAX_SPEED + delta;
//    }

    private Random rand = new Random();
    private final Image images[] = new Image[] {
            new Image("file:assets/images/note1.png"),
            new Image("file:assets/images/note2.png"),
            new Image("file:assets/images/note3.png"),
            new Image("file:assets/images/note4.png"),
            new Image("file:assets/images/note5.png")
    };

    private final List<GuiNote> notes = new ArrayList<>();

    @Override
    public void addGuiNote() {
        int num = rand.nextInt(5);
        Image image = images[num];

        final double W = image.getWidth();
        final double H = image.getHeight();

        double x = rand.nextDouble() * (screenBounds.getWidth() - W);
        double y = screenBounds.getHeight() - H;
        double velocity = screenBounds.getHeight()/33;

        synchronized (notes) {
            notes.add(new GuiNote(x, y, velocity, num));
        }
    }

    protected void appendScoreTextToPane(String msg, String color, Font font) {
        Text t1 = new Text();
        t1.setStyle("-fx-fill: " + color + "; " + "-fx-stroke: black; -fx-stroke-width: 0.2;");

        if (font != null) {
            t1.setFont(font);
        }

        t1.setText(msg);

        lyricsTextArea.getChildren().add(t1);
    }
}
