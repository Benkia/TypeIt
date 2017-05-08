package com.TypeIt.gui;

import com.TypeIt.main.Constants;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Naveh on 9/1/2017.
 */
public class ChooseSongTableCellController extends AbstractScoreView {
    private static final int FONT_SIZE = Constants.DEFAULT_FONT_SIZE*2/3;

    @FXML
    private Text songNameLabel;

    @FXML
    private Text bandNameLabel;

    @FXML
    private VBox box;

    public VBox getBox() {
        return box;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert scoreLabel != null : "fx:id=\"scoreLabel\" was not injected: check your FXML file 'ScoreView.fxml'.";

        box.setStyle(
                "-fx-border-width: 0.2;" +
                "-fx-border-radius: 20;" +
                "-fx-border-style: dashed;" +
                "-fx-border-color: black;"
        );
        box.setSpacing(1);

        box.setAlignment(Pos.TOP_CENTER);
        songNameLabel.setTextAlignment(TextAlignment.CENTER);
        scoreLabel.setTextAlignment(TextAlignment.CENTER);
        bandNameLabel.setTextAlignment(TextAlignment.LEFT);

        songNameLabel.setFont(Font.font("Guttman Yad Brush", FontWeight.SEMI_BOLD, getFontSize()*1.05));
        bandNameLabel.setFont(Font.font("Guttman Yad Brush", FontWeight.LIGHT, getFontSize()*0.7));

        refreshLabels();
    }

    public void setUserPerformanceData(String songName, String bandName, int percentage) {
        this.songNameLabel.setText(songName);
        this.bandNameLabel.setText(bandName);
        this.percentage = percentage;

        refreshLabels();
    }

    @Override
    protected void refreshLabels() {
        String colorString = createColorString();

        songNameLabel.setStyle("-fx-fill: black; -fx-stroke: "+colorString+"; -fx-stroke-width: 0.3;");
        scoreLabel.getChildren().removeAll(scoreLabel.getChildren());

        appendToPane("(", "black");
        appendToPane(String.valueOf((int)percentage), colorString);
        appendToPane("%)", "black");
    }

    @Override
    public int getFontSize() {
        return FONT_SIZE;
    }
}
