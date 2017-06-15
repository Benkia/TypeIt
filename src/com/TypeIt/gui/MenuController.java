package com.TypeIt.gui;

import com.TypeIt.main.Constants;
import com.TypeIt.songs.Song;
import com.TypeIt.songs.melody.Melody;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Naveh on 9/1/2017.
 */
public class MenuController extends AnchorPane implements Initializable {
    private Stage stage;

    @FXML
    private ImageView logo;

    @FXML
    private Button play;

    @FXML
    private Button about;

    @FXML
    private VBox box;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert play != null : "fx:id=\"play\" was not injected: check your FXML file 'MenuView.fxml'.";

        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:assets/images/background.jpg"),
                                                              BackgroundRepeat.REPEAT,
                                                              BackgroundRepeat.NO_REPEAT,
                                                              BackgroundPosition.DEFAULT,
                                                              BackgroundSize.DEFAULT);

        box.setBackground(new Background(backgroundImage));

        logo.setImage(new Image("file:assets/images/typeit.png"));

        box.setAlignment(Pos.TOP_CENTER);
        play.setTextAlignment(TextAlignment.CENTER);
        about.setTextAlignment(TextAlignment.CENTER);

        play.setFont(Font.font("Courier New", FontWeight.BOLD, Constants.DEFAULT_FONT_SIZE));
        about.setFont(Font.font("Courier New", FontWeight.BOLD, Constants.DEFAULT_FONT_SIZE));
        CustomButton.setCustomStyle(play);
        CustomButton.setCustomStyle(about);

        play.requestFocus();

        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Melody.init();
                ObservableList<Song> listOfSongs = FXCollections.observableArrayList(Melody.getListOfAllSongs());

                // Create the first controller
                ChooseSongController chooseSongController = new ChooseSongController(stage, listOfSongs);
            }
        });

        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                dialog.setTitle("About TypeIt");
                dialog.setResizable(false);
                VBox dialogVbox = new VBox(20);
                dialogVbox.setBackground(new Background(backgroundImage));
                dialogVbox.setAlignment(Pos.CENTER);

                Text text = new Text("TypeIt is a keyboard-based karaoke,\n developed by Asaf Fadida & Naveh Ohana as a MusicTech project.");
                text.setFont(Font.font("Courier New", FontWeight.BOLD, Constants.DEFAULT_FONT_SIZE));
                text.setFill(Paint.valueOf("white"));
                text.setTextAlignment(TextAlignment.CENTER);

                CustomButton confirm = new CustomButton("Ok");
                confirm.setFont(Font.font("Courier New", FontWeight.BOLD, Constants.DEFAULT_FONT_SIZE));
                CustomButton.setCustomStyle(confirm);

                confirm.setOnAction(event1 -> dialog.close());

                dialogVbox.getChildren().add(text);
                dialogVbox.getChildren().add(confirm);

                Scene dialogScene = new Scene(dialogVbox, 1250, 200);

                Image cursor = new Image("file:cursor.png");
                dialogScene.setCursor(new ImageCursor(cursor));

                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        // Disable the "Press ESC to exit fullscreen" message
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        stage.centerOnScreen();
    }
}
