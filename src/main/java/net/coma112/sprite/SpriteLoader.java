package net.coma112.sprite;

import lombok.extern.java.Log;
import net.coma112.utils.LoggerUtils;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class SpriteLoader {
    private static final ConcurrentHashMap<String, BufferedImage> SPRITE_CACHE = new ConcurrentHashMap<>();

    public static void loadSprites(@NonNull Object o) {
        Class<?> clazz = o.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                if (field.isAnnotationPresent(Sprite.class)) {
                    loadSingleSprite(o, field);
                }

                if (field.isAnnotationPresent(SpriteSheet.class)) {
                    loadSpriteSheet(o, field);
                }
            } catch (Exception exception) {
                System.err.println(exception.getMessage());
            }
        }
    }

    private static void loadSingleSprite(Object o, @NonNull Field field) throws IllegalAccessException {
        Sprite annotation = field.getAnnotation(Sprite.class);
        String path = annotation.value();

        BufferedImage image = loadImage(path);

        if (image != null) {
            field.set(o, image);
        }
    }

    private static void loadSpriteSheet(Object o, @NonNull Field field) throws IllegalAccessException {
        SpriteSheet annotation = field.getAnnotation(SpriteSheet.class);
        String pattern = annotation.pattern();
        int count = annotation.count();
        int startIndex = annotation.startIndex();

        if (field.getType().isArray() && field.getType().getComponentType() == BufferedImage.class) {
            BufferedImage[] images = new BufferedImage[count];

            for (int i = 0; i < count; i++) {
                String path = pattern.replace("{n}", String.valueOf(startIndex + i));
                images[i] = loadImage(path);
            }

            field.set(o, images);
        } else {
            System.out.println("BufferedImage[] not found");
        }
    }

    private static @Nullable BufferedImage loadImage(String path) {
        if (SPRITE_CACHE.containsKey(path)) {
            return SPRITE_CACHE.get(path);
        }

        try {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            InputStream stream = SpriteLoader.class.getResourceAsStream(path);

            if (stream == null) {
                LoggerUtils.error("Nem talalhato stream");
                return null;
            }

            BufferedImage image = ImageIO.read(stream);

            if (image != null) {
                SPRITE_CACHE.put(path, image);
                return image;
            } else {
                LoggerUtils.error("Nem sikerult betolteni spriteot!");
            }
        } catch (Exception exception) {
            LoggerUtils.error(exception.getMessage());
        }

        return null;
    }

    public static void clearCache() {
        SPRITE_CACHE.clear();
    }

    public static int getCacheSize() {
        return SPRITE_CACHE.size();
    }
}
