package com.TypeIt.gui;

import com.TypeIt.main.Constants;
import com.TypeIt.songs.Song;
import com.TypeIt.songs.melody.Melody;
import com.TypeIt.sound.BackgroundTrackPlayer;
import com.TypeIt.sound.MidiBackgroundTrackPlayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Naveh on 9/1/2017.
 */
public class ScoreController extends AbstractScoreView {
    private static final int FONT_SIZE = Constants.DEFAULT_FONT_SIZE*4/3;

    private Stage stage;
    private int correctCharacters = 0;
    private int charactersInLyrics = 1;

    @FXML
    private Button confirmButton;

    @FXML
    private VBox box;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert scoreLabel != null : "fx:id=\"scoreLabel\" was not injected: check your FXML file 'ScoreView.fxml'.";

        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:background.jpg"),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        box.setAlignment(Pos.TOP_CENTER);
        box.setBackground(new Background(backgroundImage));

        scoreLabel.setTextAlignment(TextAlignment.CENTER);

        confirmButton.setFont(Font.font("Courier New", FontWeight.BOLD, Constants.DEFAULT_FONT_SIZE));
        CustomButton.setCustomStyle(confirmButton);
        confirmButton.requestFocus();

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BackgroundTrackPlayer.stopEverything();
                MidiBackgroundTrackPlayer.stopEverything();

                // Go back to Main menu (Choose song)
                ObservableList<Song> listOfSongs = FXCollections.observableArrayList(Melody.getListOfAllSongs());

                // Create the first controller
                ChooseSongController chooseSongController = new ChooseSongController(ScoreController.this.stage, listOfSongs);
            }
        });

        refreshLabels();
    }

    public void setUserPerformanceData(float percentage, int charactersInLyrics) {
        this.percentage = percentage;
        this.charactersInLyrics = charactersInLyrics;

        this.correctCharacters = (int)((percentage/100)*charactersInLyrics);

        refreshLabels();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.centerOnScreen();

        Image cursor = new Image("file:cursor.png");
        stage.getScene().setCursor(new ImageCursor(cursor));
    }

    @Override
    protected void refreshLabels() {
        scoreLabel.getChildren().removeAll(scoreLabel.getChildren());

        String colorString = createColorString();

        appendToPane("Your score: ", "white");
        appendToPane(String.valueOf(correctCharacters), colorString);
        appendToPane("/" + charactersInLyrics + " (", "white");
        appendToPane(String.valueOf((int)percentage), colorString);
        appendToPane("%)", "white");
    }

    @Override
    public int getFontSize() {
        return FONT_SIZE;
    }
}
