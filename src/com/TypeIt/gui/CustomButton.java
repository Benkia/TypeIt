package com.TypeIt.gui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Created by Naveh on 17/1/2017.
 */
public class CustomButton extends Button {
    private static Font font;

    public CustomButton() {
        super();
    }

    public CustomButton(String text) {
        super(text);

        this.setCustomStyle(this);
    }

    public static void setCustomStyle(Button button) {
        Font oldFont = button.getFont();

        button.setStyle("-fx-background-radius: 8,7,6; "+
                        "-fx-background-insets: 0,1,2; " +
                        "-fx-text-fill: black; "+
                        "-fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.6) , 5, 0.0 , 0 , 1 ); "
        );

        if (font == null) {
            font = FontUtils.getDefaultFont(oldFont.getSize());
            if (font == null) {
                font = Font.font("Comic Sans MS", FontWeight.BOLD, oldFont.getSize());
            }
        }

        button.setFont(font);
    }
}
