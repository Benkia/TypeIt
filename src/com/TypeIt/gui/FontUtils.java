package com.TypeIt.gui;

import com.TypeIt.main.Constants;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by admin on 21/06/2017.
 */
public class FontUtils {
    private static Font font;

    protected static Font getDefaultFont(double fontSize) {
        Font f = null;

        try {
            // Load a custom font from a specific location
            f = Font.loadFont(new FileInputStream(new File("assets/fonts/NeonTech.ttf")), fontSize);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return f;
    }

    protected static Font getDefaultFont() {
        if (font == null) {
            font = getDefaultFont((Constants.DEFAULT_FONT_SIZE * 1.5d));
        }

        return font;
    }
}
