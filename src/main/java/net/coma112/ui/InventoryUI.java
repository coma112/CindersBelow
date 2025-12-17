package net.coma112.ui;

import net.coma112.GamePanel;
import net.coma112.inventory.impl.PlayerInventory;
import net.coma112.item.Item;
import org.jspecify.annotations.NonNull;

import java.awt.*;

public class InventoryUI {
    private final GamePanel gamePanel;
    private final int SLOT_SIZE = 64;
    private final int PADDING = 10;
    private final int COLUMNS = 9;
    private final int ROWS = 4;

    public InventoryUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2, @NonNull PlayerInventory inventory) {
        if (!inventory.isOpen()) return;

        int panelWidth = (SLOT_SIZE * COLUMNS) + (PADDING * (COLUMNS + 1));
        int panelHeight = (SLOT_SIZE * ROWS) + (PADDING * (ROWS + 1)) + 40;
        int panelX = (gamePanel.SCREEN_WIDTH - panelWidth) / 2;
        int panelY = (gamePanel.SCREEN_HEIGHT - panelHeight) / 2;

        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Inventory", panelX + 20, panelY + 30);

        Item[] items = inventory.getItems();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int slotIndex = row * COLUMNS + col;
                if (slotIndex >= inventory.getSize()) break;

                int slotX = panelX + PADDING + (col * (SLOT_SIZE + PADDING));
                int slotY = panelY + 50 + (row * (SLOT_SIZE + PADDING));

                g2.setColor(new Color(60, 60, 60));
                g2.fillRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);

                g2.setColor(new Color(120, 120, 120));
                g2.drawRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);

                Item item = items[slotIndex];
                if (item != null) {
                    g2.setColor(getRarityColor(item));
                    g2.fillRect(slotX + 5, slotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10);

                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Arial", Font.PLAIN, 10));
                    drawCenteredString(g2, item.getName(), slotX, slotY, SLOT_SIZE, SLOT_SIZE);
                    drawCenteredString(g2, String.valueOf(item.getDurability()), slotX, slotY + 10, SLOT_SIZE, SLOT_SIZE);
                }

                g2.setColor(new Color(200, 200, 200));
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.drawString(String.valueOf(slotIndex + 1), slotX + 2, slotY + 12);
            }
        }

        g2.setColor(new Color(180, 180, 180));
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString("Press 'E' to close", panelX + 20, panelY + panelHeight - 10);
    }

    private void drawCenteredString(@NonNull Graphics2D g2, String text, int x, int y, int width, int height) {
        FontMetrics metrics = g2.getFontMetrics();
        int textX = x + (width - metrics.stringWidth(text)) / 2;
        int textY = y + ((height - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.drawString(text, textX, textY);
    }

    private @NonNull Color getRarityColor(@NonNull Item item) {
        return switch (item.getRarity()) {
            case COMMON -> new Color(150, 150, 150);
            case UNCOMMON -> new Color(30, 255, 0);
            case RARE -> new Color(0, 112, 221);
            case EPIC -> new Color(163, 53, 238);
            case LEGENDARY -> new Color(255, 128, 0);
        };
    }
}