package com.TypeIt.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Created by Naveh on 11/1/2017.
 */
public abstract class AbstractScoreView extends AnchorPane implements Initializable {
    protected float percentage = 0f;

    @FXML
    protected TextFlow scoreLabel;

    protected void appendToPane(String msg, String color, boolean customFont) {
        Text t1 = new Text();
        t1.setStyle("-fx-fill: " + color + "; " + "-fx-stroke: black; -fx-stroke-width: 0.2;");

        Font font = FontUtils.getDefaultFont(getFontSize());

        if (customFont && font != null) {
            t1.setFont(font);
        }
        else {
            t1.setFont(Font.font("Courier New", FontWeight.BOLD, getFontSize()));
        }

        t1.setText(msg);

        scoreLabel.getChildren().add(t1);
    }

    protected String createColorString() {
        final double fraction = ((double)percentage/100);
        Color color = Color.color(1-(fraction/2), fraction, 0);
        String colorString = color.toString();
        colorString = colorString.replaceAll("0x", "#");

        return colorString;
    }

    abstract int getFontSize();
    abstract void refreshLabels();
}
