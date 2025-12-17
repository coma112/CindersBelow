package net.coma112.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.awt.*;
import java.io.InputStream;

@UtilityClass
public class FontUtils {
    private Font starCrushBase;
    @Getter private boolean starCrushLoaded = false;

    public void initialize() {
        loadStarCrush();
    }

    private void loadStarCrush() {
        try {
            InputStream stream = FontUtils.class.getResourceAsStream("/fonts/StarCrush.ttf");

            if (stream == null) {
                LoggerUtils.error("Nem talalhato a fonts/StarCrush.ttf fajl!");
                starCrushLoaded = false;
                return;
            }

            starCrushBase = Font.createFont(Font.TRUETYPE_FONT, stream);
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            environment.registerFont(starCrushBase);

            stream.close();
            starCrushLoaded = true;

            LoggerUtils.info("StarCrush font sikeresen betoltve!");
        } catch (Exception exception) {
            LoggerUtils.error("Hiba a StarCrush font betoltesenel: " + exception.getMessage());
            starCrushLoaded = false;
        }
    }

    public Font getStarCrush(int style, float size) {
        if (starCrushLoaded && starCrushBase != null) {
            return starCrushBase.deriveFont(style, size);
        }
        return new Font("Arial", style, (int) size);
    }

    public Font getStarCrush(float size) {
        return getStarCrush(Font.PLAIN, size);
    }

}