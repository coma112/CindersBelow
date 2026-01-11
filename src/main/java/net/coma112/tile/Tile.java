package net.coma112.tile;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.awt.image.BufferedImage;

@Getter
public class Tile {
    @Setter
    private BufferedImage image;
    private final TileType type;
    private final boolean collision;

    @Contract(pure = true)
    public Tile(@NonNull TileType type) {
        this.type = type;
        this.collision = type.isCollision();
    }
}
