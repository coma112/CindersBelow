package net.coma112.ui;

import net.coma112.GamePanel;
import net.coma112.inventory.impl.PlayerInventory;
import net.coma112.item.Item;
import net.coma112.utils.FontUtils;
import org.jspecify.annotations.NonNull;

import java.awt.*;

public class HotbarUI {
    private final GamePanel gamePanel;
    private final int SLOT_SIZE = 64;
    private final int PADDING = 4;

    public HotbarUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(@NonNull Graphics2D g2, @NonNull PlayerInventory inventory) {
        int hotbarSize = inventory.getHotbarSize();
        int totalWidth = (SLOT_SIZE * hotbarSize) + (PADDING * (hotbarSize - 1));
        int startX = (gamePanel.SCREEN_WIDTH - totalWidth) / 2;
        int startY = gamePanel.SCREEN_HEIGHT - SLOT_SIZE - 20;

        for (int i = 0; i < hotbarSize; i++) {
            int slotX = startX + (i * (SLOT_SIZE + PADDING));

            boolean isSelected = (i == inventory.getSelectedHotbarSlot());

            // Slot háttér
            if (isSelected) {
                g2.setColor(new Color(80, 80, 80));
                g2.fillRect(slotX - 2, startY - 2, SLOT_SIZE + 4, SLOT_SIZE + 4);
            }

            g2.setColor(new Color(60, 60, 60));
            g2.fillRect(slotX, startY, SLOT_SIZE, SLOT_SIZE);

            // Slot keret
            g2.setColor(isSelected ? new Color(255, 255, 0) : new Color(120, 120, 120));
            g2.setStroke(new BasicStroke(isSelected ? 3 : 2));
            g2.drawRect(slotX, startY, SLOT_SIZE, SLOT_SIZE);

            // Item megjelenítése
            Item item = inventory.getHotbarItem(i);
            if (item != null && !item.isBroken()) {
                // Item háttér (rarity szín)
                g2.setColor(getRarityColor(item));
                g2.fillRect(slotX + 5, startY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10);

                // Item név
                g2.setColor(Color.WHITE);
                g2.setFont(FontUtils.getStarCrush(10));
                drawCenteredString(g2, item.getName(), slotX, startY + 5, SLOT_SIZE, SLOT_SIZE / 2);

                // Durability bar
                drawDurabilityBar(g2, item, slotX, startY, SLOT_SIZE);
            }

            // Slot szám
            g2.setColor(new Color(200, 200, 200));
            g2.setFont(FontUtils.getStarCrush(12));
            g2.drawString(String.valueOf(i + 1), slotX + 2, startY + 12);
        }
    }

    private void drawDurabilityBar(@NonNull Graphics2D g2, @NonNull Item item, int x, int y, int slotSize) {
        int barWidth = slotSize - 10;
        int barHeight = 6;
        int barX = x + 5;
        int barY = y + slotSize - barHeight - 5;

        float durabilityPercent = item.getDurabilityPercentage();
        int fillWidth = (int) (barWidth * durabilityPercent);

        // Háttér
        g2.setColor(new Color(40, 40, 40));
        g2.fillRect(barX, barY, barWidth, barHeight);

        // Durability szín
        Color durabilityColor = getDurabilityColor(durabilityPercent);
        g2.setColor(durabilityColor);
        g2.fillRect(barX, barY, fillWidth, barHeight);

        // Keret
        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

    private @NonNull Color getDurabilityColor(float percent) {
        if (percent > 0.75f) {
            return new Color(0, 255, 0); // Zöld
        } else if (percent > 0.5f) {
            return new Color(255, 200, 0); // Citromsárga
        } else if (percent > 0.25f) {
            return new Color(255, 165, 0); // Narancssárga
        } else {
            return new Color(255, 0, 0); // Piros
        }
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