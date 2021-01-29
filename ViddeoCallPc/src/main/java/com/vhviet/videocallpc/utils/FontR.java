package com.vhviet.videocallpc.utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class FontR {
    public static final Font HEAVY = loadFont("/Fonts/heavy.ttf").deriveFont(Font.BOLD);
    public static final Font BOLD = loadFont("/Fonts/bold.ttf").deriveFont(Font.BOLD);
    public static final Font LIGHT = loadFont("/Fonts/light.ttf").deriveFont(Font.PLAIN);
    public static final Font MEDIUM = loadFont("/Fonts/semibold.ttf").deriveFont(Font.PLAIN);
    public static final Font REGULAR = loadFont("/Fonts/regular.ttf").deriveFont(Font.PLAIN);

    private static Font loadFont(String resourceName) {
        try (InputStream inputStream = FontR.class.getResourceAsStream(resourceName)) {
            return Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException("Could not load " + resourceName, e);
        }
    }
}
