package com.TypeIt.main;

/**
 * Created by Naveh on 6/1/2017.
 */
import com.TypeIt.gui.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    private static final int MENU_WIDTH  = 750;
    private static final int MENU_HEIGHT = 750;

    @Override
    public void start(Stage stage) {
        // Continue to LyricsViewController
        FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("MenuView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("TypeIt");
        Scene scene = new Scene(root, MENU_WIDTH, MENU_HEIGHT);
        stage.setScene(scene);

        Image cursor = new Image("file:assets/images/cursor.png");
        scene.setCursor(new ImageCursor(cursor));

        stage.show();
        loader.<MenuController>getController().setStage(stage);

        stage.centerOnScreen();
        stage.setFullScreen(true);
    }

    public static void main(String[] args) {
        launch(args);
    }
}