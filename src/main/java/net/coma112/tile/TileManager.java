package net.coma112.tile;

import lombok.Getter;
import net.coma112.GamePanel;
import net.coma112.utils.LoggerUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    private final GamePanel gamePanel;
    // Getter metódusok a CollisionChecker számára
    @Getter
    private Tile[] tiles;
    @Getter
    private int[][] mapTileNumbers;

    // Pálya mérete tile-okban
    public final int MAX_WORLD_COL = 50;
    public final int MAX_WORLD_ROW = 50;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tiles = new Tile[10]; // Max 10 különböző tile típus
        this.mapTileNumbers = new int[MAX_WORLD_COL][MAX_WORLD_ROW];

        loadTileImages();
        loadMap("/maps/terrain.txt");
    }

    private void loadTileImages() {
        try {
            // 0 - Fű
            tiles[0] = new Tile(TileType.GRASS);
            tiles[0].setImage(loadImage("/tiles/grass.png"));

            // 1 - Víz (NEM járható)
            tiles[1] = new Tile(TileType.WATER);
            tiles[1].setImage(loadImage("/tiles/water.png"));

            // 2 - Fal (NEM járható)
            tiles[2] = new Tile(TileType.WALL);
            tiles[2].setImage(loadImage("/tiles/wall.png"));

            // 3 - Fa (NEM járható)
            tiles[3] = new Tile(TileType.TREE);
            tiles[3].setImage(loadImage("/tiles/tree.png"));

            // 4 - Út (járható)
            tiles[4] = new Tile(TileType.DIRT);
            tiles[4].setImage(loadImage("/tiles/dirt.png"));

            // 5 - Homok (járható)
            tiles[5] = new Tile(TileType.SAND);
            tiles[5].setImage(loadImage("/tiles/sand.png"));

        } catch (Exception e) {
            LoggerUtils.error("Hiba a tile képek betöltésekor: " + e.getMessage());
        }
    }

    private BufferedImage loadImage(String path) {
        try {
            InputStream stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                LoggerUtils.error("Nem található a tile: " + path);
                return createPlaceholderImage();
            }
            return ImageIO.read(stream);
        } catch (Exception e) {
            LoggerUtils.error("Hiba a " + path + " betöltésekor: " + e.getMessage());
            return createPlaceholderImage();
        }
    }

    private BufferedImage createPlaceholderImage() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.MAGENTA);
        g2.fillRect(0, 0, 16, 16);
        g2.dispose();
        return image;
    }

    public void loadMap(String mapPath) {
        try {
            InputStream stream = getClass().getResourceAsStream(mapPath);
            if (stream == null) {
                LoggerUtils.error("Nem található a map fájl: " + mapPath);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            int col = 0;
            int row = 0;

            while (col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
                String line = reader.readLine();

                if (line == null) break;

                String[] numbers = line.split(" ");

                col = 0;
                while (col < MAX_WORLD_COL && col < numbers.length) {
                    int num = Integer.parseInt(numbers[col]);
                    mapTileNumbers[col][row] = num;
                    col++;
                }

                row++;
            }

            reader.close();
            LoggerUtils.info("Map sikeresen betöltve: " + mapPath);

        } catch (Exception e) {
            LoggerUtils.error("Hiba a map betöltésekor: " + e.getMessage());
        }
    }

    public void draw(Graphics2D g2, int playerX, int playerY) {
        int col = 0;
        int row = 0;

        while (col < MAX_WORLD_COL && row < MAX_WORLD_ROW) {
            int tileNum = mapTileNumbers[col][row];

            // Tile pozíció a világban
            int worldX = col * gamePanel.TILE_SIZE;
            int worldY = row * gamePanel.TILE_SIZE;

            // Képernyőn való pozíció (játékos körül központosítva)
            int screenX = worldX - playerX + gamePanel.SCREEN_WIDTH / 2;
            int screenY = worldY - playerY + gamePanel.SCREEN_HEIGHT / 2;

            // Csak akkor rajzoljuk ki, ha látható a képernyőn (optimalizálás)
            if (worldX + gamePanel.TILE_SIZE > playerX - gamePanel.SCREEN_WIDTH / 2 &&
                    worldX - gamePanel.TILE_SIZE < playerX + gamePanel.SCREEN_WIDTH / 2 &&
                    worldY + gamePanel.TILE_SIZE > playerY - gamePanel.SCREEN_HEIGHT / 2 &&
                    worldY - gamePanel.TILE_SIZE < playerY + gamePanel.SCREEN_HEIGHT / 2) {

                if (tileNum >= 0 && tileNum < tiles.length && tiles[tileNum] != null) {
                    g2.drawImage(tiles[tileNum].getImage(), screenX, screenY,
                            gamePanel.TILE_SIZE, gamePanel.TILE_SIZE, null);
                }
            }

            col++;

            if (col == MAX_WORLD_COL) {
                col = 0;
                row++;
            }
        }
    }

    public boolean isTileCollision(int worldX, int worldY) {
        int col = worldX / gamePanel.TILE_SIZE;
        int row = worldY / gamePanel.TILE_SIZE;

        if (col < 0 || col >= MAX_WORLD_COL || row < 0 || row >= MAX_WORLD_ROW) {
            return true; // Pályán kívül = ütközés
        }

        int tileNum = mapTileNumbers[col][row];

        if (tileNum >= 0 && tileNum < tiles.length && tiles[tileNum] != null) {
            return tiles[tileNum].isCollision();
        }

        return false;
    }
}