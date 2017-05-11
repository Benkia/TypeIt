package com.TypeIt.gui;

import com.TypeIt.files.LyricsFileManager;
import com.TypeIt.main.Constants;
import com.TypeIt.main.PlayPressedButtonRunnable;
import com.TypeIt.main.UserDataManager;
import com.TypeIt.songs.Song;
import com.TypeIt.songs.SongUserData;
import com.TypeIt.songs.melody.Melody;
import com.TypeIt.sound.BackgroundTrackPlayer;
import com.TypeIt.sound.MelodyPlayer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Naveh on 9/1/2017.
 */
public class ChooseSongController extends AnchorPane implements Initializable {
    private static final int THIS_WINDOW_WIDTH = 400;
    private static final int THIS_WINDOW_HEIGHT = 800;

    private static final int LYRICS_WINDOW_WIDTH = 1500;
    private static final int LYRICS_WINDOW_HEIGHT = 700;

    private static final Boolean PITCH_BEND_DEFAULT = false;

    private final ListView<SongUserData> list = new ListView<>();
    private final ObservableList<SongUserData> data;
    private final Label label = new Label();

    private final UserDataManager userDataManager;

    public ChooseSongController(Stage stage, ObservableList<Song> listOfSongs) {
        stage.setOnCloseRequest(windowEvent -> {
            BackgroundTrackPlayer.stopEverything();
            MelodyPlayer.stopEverything();
        });

        // Create the user data manager with the list of songs
        userDataManager = new UserDataManager(listOfSongs);

        // Read the song datas from the user data file
        data = FXCollections.observableArrayList(userDataManager.getSongDataMap().values());

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        Scene scene = new Scene(box, THIS_WINDOW_WIDTH, THIS_WINDOW_HEIGHT);

        CustomButton select = new CustomButton("Continue");
        select.setDisable(true);

        CheckBox bendPitchCheckBox = new CheckBox("Pitch Bend");
        bendPitchCheckBox.setSelected(PITCH_BEND_DEFAULT);

        stage.setScene(scene);
        stage.setTitle("Choose a song");
        box.getChildren().addAll(list, label);
        VBox.setVgrow(list, Priority.ALWAYS);

        label.setLayoutX(10);
        label.setLayoutY(115);
        label.setFont(Font.font("Verdana", Constants.DEFAULT_FONT_SIZE*2/3));

        list.setItems(data);
        list.getStyleClass().add("custom-align");

        list.setCellFactory(list -> new SongDataCell());

        list.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<SongUserData>() {
                    public void changed(ObservableValue<? extends SongUserData> ov,
                                        SongUserData old_val, SongUserData new_val) {
                        label.setText(new_val.getSongName());
                        select.setDisable(false);

                        BackgroundTrackPlayer.preview(new_val.getFileName());
                    }
                });

        select.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BackgroundTrackPlayer.stopEverything();
                new Thread(new PlayPressedButtonRunnable()).start();

                Constants.fileName = label.getText();

                // Choose the song
                Melody.chooseSong(Constants.fileName);
                System.out.println("Playing song: " + Constants.fileName);

                // Continue to LyricsViewController
                FXMLLoader loader = new FXMLLoader(LyricsViewController.class.getResource("LyricsView.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                String rawText = "";

                try {
                    rawText = new LyricsFileManager(Constants.fileName).readLyrics();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                stage.setTitle("TypeIt");
                stage.setScene(new Scene(root, LYRICS_WINDOW_WIDTH, LYRICS_WINDOW_HEIGHT));
                stage.show();
                loader.<LyricsViewController>getController().setStage(stage);
                loader.<LyricsViewController>getController().setRawText(rawText);

                boolean bendPitch = bendPitchCheckBox.isSelected();
                loader.<LyricsViewController>getController().setBendPitch(bendPitch);
            }
        });

        box.getChildren().add(select);
        box.getChildren().add(bendPitchCheckBox);

        box.setSpacing(5f);
        box.setPadding(new Insets(0, 0,10, 0));

        stage.show();
        stage.centerOnScreen();

        Image cursor = new Image("file:cursor.png");
        stage.getScene().setCursor(new ImageCursor(cursor));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    private static class SongDataCell extends ListCell<SongUserData> {
        @Override
        public void updateItem(SongUserData item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                ChooseSongTableCellController controller = createCellController(item);
                this.setGraphic(controller.getBox());

                this.setHover(true);
            }

            this.setAlignment(Pos.CENTER);
        }

        private ChooseSongTableCellController createCellController(SongUserData item) {
            FXMLLoader loader = new FXMLLoader(LyricsViewController.class.getResource("ChooseSongTableCell.fxml"));
            try {
                loader.load();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            loader.<ChooseSongTableCellController>getController().setUserPerformanceData(
                    item.getSongName(),
                    item.getBandName(),
                    (int)(item.getUserHighScore()*100)
            );

            return loader.getController();
        }
    }
}
