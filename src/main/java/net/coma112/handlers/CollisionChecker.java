package net.coma112.handlers;

import net.coma112.GamePanel;
import net.coma112.entity.Entity;
import org.jspecify.annotations.NonNull;

public class CollisionChecker {
    private final GamePanel gamePanel;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void checkTile(@NonNull Entity entity) {
        int entityLeftWorldX = entity.x + 8;
        int entityRightWorldX = entity.x + gamePanel.TILE_SIZE - 8;
        int entityTopWorldY = entity.y + 16;
        int entityBottomWorldY = entity.y + gamePanel.TILE_SIZE - 4;

        int entityLeftCol = entityLeftWorldX / gamePanel.TILE_SIZE;
        int entityRightCol = entityRightWorldX / gamePanel.TILE_SIZE;
        int entityTopRow = entityTopWorldY / gamePanel.TILE_SIZE;
        int entityBottomRow = entityBottomWorldY / gamePanel.TILE_SIZE;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gamePanel.TILE_SIZE;

                if (entityLeftCol < 0 || entityLeftCol >= gamePanel.getTileManager().MAX_WORLD_COL ||
                        entityRightCol < 0 || entityRightCol >= gamePanel.getTileManager().MAX_WORLD_COL ||
                        entityTopRow < 0 || entityTopRow >= gamePanel.getTileManager().MAX_WORLD_ROW) {
                    entity.collisionOn = true;
                    return;
                }

                tileNum1 = gamePanel.getTileManager().getMapTileNumbers()[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.getTileManager().getMapTileNumbers()[entityRightCol][entityTopRow];

                if (gamePanel.getTileManager().getTiles()[tileNum1].isCollision() ||
                        gamePanel.getTileManager().getTiles()[tileNum2].isCollision()) {
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / gamePanel.TILE_SIZE;

                if (entityLeftCol < 0 || entityLeftCol >= gamePanel.getTileManager().MAX_WORLD_COL ||
                        entityRightCol < 0 || entityRightCol >= gamePanel.getTileManager().MAX_WORLD_COL ||
                        entityBottomRow < 0 || entityBottomRow >= gamePanel.getTileManager().MAX_WORLD_ROW) {
                    entity.collisionOn = true;
                    return;
                }

                tileNum1 = gamePanel.getTileManager().getMapTileNumbers()[entityLeftCol][entityBottomRow];
                tileNum2 = gamePanel.getTileManager().getMapTileNumbers()[entityRightCol][entityBottomRow];

                if (gamePanel.getTileManager().getTiles()[tileNum1].isCollision() ||
                        gamePanel.getTileManager().getTiles()[tileNum2].isCollision()) {
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gamePanel.TILE_SIZE;

                if (entityLeftCol < 0 || entityLeftCol >= gamePanel.getTileManager().MAX_WORLD_COL ||
                        entityTopRow < 0 || entityTopRow >= gamePanel.getTileManager().MAX_WORLD_ROW ||
                        entityBottomRow < 0 || entityBottomRow >= gamePanel.getTileManager().MAX_WORLD_ROW) {
                    entity.collisionOn = true;
                    return;
                }

                tileNum1 = gamePanel.getTileManager().getMapTileNumbers()[entityLeftCol][entityTopRow];
                tileNum2 = gamePanel.getTileManager().getMapTileNumbers()[entityLeftCol][entityBottomRow];

                if (gamePanel.getTileManager().getTiles()[tileNum1].isCollision() ||
                        gamePanel.getTileManager().getTiles()[tileNum2].isCollision()) {
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gamePanel.TILE_SIZE;

                if (entityRightCol < 0 || entityRightCol >= gamePanel.getTileManager().MAX_WORLD_COL ||
                        entityTopRow < 0 || entityTopRow >= gamePanel.getTileManager().MAX_WORLD_ROW ||
                        entityBottomRow < 0 || entityBottomRow >= gamePanel.getTileManager().MAX_WORLD_ROW) {
                    entity.collisionOn = true;
                    return;
                }

                tileNum1 = gamePanel.getTileManager().getMapTileNumbers()[entityRightCol][entityTopRow];
                tileNum2 = gamePanel.getTileManager().getMapTileNumbers()[entityRightCol][entityBottomRow];

                if (gamePanel.getTileManager().getTiles()[tileNum1].isCollision() ||
                        gamePanel.getTileManager().getTiles()[tileNum2].isCollision()) {
                    entity.collisionOn = true;
                }
            }
        }
    }
}