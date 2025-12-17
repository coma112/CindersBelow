package net.coma112.ui;

import net.coma112.GamePanel;
import net.coma112.entity.impl.Player;
import net.coma112.inventory.impl.PlayerInventory;
import net.coma112.item.Item;
import org.jspecify.annotations.NonNull;

import java.awt.*;

public class DebugOverlay {
    private final GamePanel gamePanel;
    private long lastFpsTime;
    private int fps;
    private int frameCount;

    public DebugOverlay(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.lastFpsTime = System.currentTimeMillis();
    }

    public void update() {
        frameCount++;
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastFpsTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            lastFpsTime = currentTime;
        }
    }

    public void draw(@NonNull Graphics2D g2, @NonNull Player player, @NonNull PlayerInventory inventory) {
        // Háttér
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(10, 10, 300, 200);

        // Keret
        g2.setColor(new Color(0, 255, 0));
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(10, 10, 300, 200);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Monospaced", Font.PLAIN, 14));

        int y = 30;
        int lineHeight = 20;

        // FPS
        g2.drawString("FPS: " + fps, 20, y);
        y += lineHeight;

        // Pozíció
        g2.drawString("X: " + player.x, 20, y);
        y += lineHeight;
        g2.drawString("Y: " + player.y, 20, y);
        y += lineHeight;

        // Sebesség
        g2.drawString("Speed: " + player.speed, 20, y);
        y += lineHeight;

        // Irány
        g2.drawString("Direction: " + player.direction, 20, y);
        y += lineHeight;

        // Inventory info
        g2.drawString("Inventory Open: " + inventory.isOpen(), 20, y);
        y += lineHeight;
        g2.drawString("Items: " + inventory.getItemCount() + "/" + inventory.getSize(), 20, y);
        y += lineHeight;

        Item item = inventory.getItem(1);

        g2.drawString("1. slot: " + item.getName() + ", " + String.valueOf(item.getDurability()), 20, y);
        y += lineHeight;

        // csempe pozicio képernyőn
        int screenCol = player.x / gamePanel.TILE_SIZE;
        int screenRow = player.y / gamePanel.TILE_SIZE;
        g2.drawString("Tile: [" + screenCol + ", " + screenRow + "]", 20, y);
    }
}