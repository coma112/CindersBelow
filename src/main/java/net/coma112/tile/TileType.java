package net.coma112.tile;

import lombok.Getter;

@Getter
public enum TileType {
    GRASS(false),
    DIRT(false),
    WATER(true),
    STONE(true),
    SAND(false),
    WALL(true),
    TREE(true),
    FLOOR(false);

    private final boolean collision;

    TileType(boolean collision) {
        this.collision = collision;
    }
}
