package net.coma112.ui;

import net.coma112.GamePanel;
import net.coma112.inventory.ArmorSlot;
import net.coma112.inventory.impl.PlayerInventory;
import net.coma112.item.Item;
import net.coma112.utils.FontUtils;
import org.jspecify.annotations.NonNull;

import java.awt.*;

public class InventoryUI {
    private final GamePanel gamePanel;
    private final int SLOT_SIZE = 64;
    private final int PADDING = 10;
    private final int COLUMNS = 9;
    private final int ROWS = 4; // 3 sor inventory + 1 sor hotbar

    private final int ARMOR_SLOT_SIZE = 64;
    private final int ARMOR_PADDING = 10;

    public InventoryUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void draw(Graphics2D g2, @NonNull PlayerInventory inventory) {
        if (!inventory.isOpen()) return;

        int panelWidth = (SLOT_SIZE * COLUMNS) + (PADDING * (COLUMNS + 1)) + 200; // +200 az armor slotoknak
        int panelHeight = (SLOT_SIZE * ROWS) + (PADDING * (ROWS + 2)) + 60; // +1 sor a hotbar elválasztáshoz
        int panelX = (gamePanel.SCREEN_WIDTH - panelWidth) / 2;
        int panelY = (gamePanel.SCREEN_HEIGHT - panelHeight) / 2;

        // Háttér
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        // Keret
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 10, 10);

        g2.setColor(Color.WHITE);
        g2.setFont(FontUtils.getStarCrush(24));
        g2.drawString("Inventory", panelX + 20, panelY + 30);

        int contentStartY = panelY + 50;

        // Inventory slotok (9-35 indexek, 3 sor)
        drawInventorySlots(g2, inventory, panelX, contentStartY);

        // Hotbar elválasztó vonal
        int separatorY = contentStartY + (3 * (SLOT_SIZE + PADDING));
        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(panelX + PADDING, separatorY, panelX + (SLOT_SIZE * COLUMNS) + (PADDING * COLUMNS), separatorY);

        // Hotbar felirat
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(FontUtils.getStarCrush(14));
        g2.drawString("Hotbar", panelX + PADDING, separatorY - 5);

        // Hotbar slotok (0-8 indexek)
        drawHotbarSlots(g2, inventory, panelX, separatorY + PADDING);

        // Armor slotok
        drawArmorSlots(g2, inventory, panelX + (SLOT_SIZE * COLUMNS) + (PADDING * (COLUMNS + 2)), contentStartY);
    }

    private void drawInventorySlots(@NonNull Graphics2D g2, @NonNull PlayerInventory inventory, int startX, int startY) {
        Item[] items = inventory.getItems();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                int slotIndex = 9 + (row * COLUMNS + col); // 9-től kezdődnek az inventory slotok
                if (slotIndex >= inventory.getSize()) break;

                int slotX = startX + PADDING + (col * (SLOT_SIZE + PADDING));
                int slotY = startY + (row * (SLOT_SIZE + PADDING));

                drawSlot(g2, items[slotIndex], slotX, slotY, slotIndex + 1, false);
            }
        }
    }

    private void drawHotbarSlots(@NonNull Graphics2D g2, @NonNull PlayerInventory inventory, int startX, int startY) {
        Item[] items = inventory.getItems();
        int selectedSlot = inventory.getSelectedHotbarSlot();

        for (int i = 0; i < 9; i++) {
            int slotX = startX + PADDING + (i * (SLOT_SIZE + PADDING));
            int slotY = startY;

            boolean isSelected = (i == selectedSlot);
            drawSlot(g2, items[i], slotX, slotY, i + 1, isSelected);
        }
    }

    private void drawArmorSlots(@NonNull Graphics2D g2, @NonNull PlayerInventory inventory, int startX, int startY) {
        g2.setColor(Color.WHITE);
        g2.setFont(FontUtils.getStarCrush(20));
        g2.drawString("Armor", startX, startY - 10);

        String[] armorNames = {"Sisak", "Mellvert", "Nadrag", "Csizma", "Gyuru"};
        ArmorSlot[] armorSlots = ArmorSlot.values();

        for (int i = 0; i < armorSlots.length; i++) {
            int slotY = startY + (i * (ARMOR_SLOT_SIZE + ARMOR_PADDING));

            Item armorItem = inventory.getArmorItem(armorSlots[i]);

            // Slot háttér
            g2.setColor(new Color(50, 50, 50));
            g2.fillRect(startX, slotY, ARMOR_SLOT_SIZE, ARMOR_SLOT_SIZE);

            // Slot keret
            g2.setColor(new Color(120, 120, 120));
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(startX, slotY, ARMOR_SLOT_SIZE, ARMOR_SLOT_SIZE);

            // Armor név
            g2.setColor(new Color(180, 180, 180));
            g2.setFont(FontUtils.getStarCrush(10));
            g2.drawString(armorNames[i], startX + 5, slotY - 5);

            // Item ha van
            if (armorItem != null && !armorItem.isBroken()) {
                g2.setColor(getRarityColor(armorItem));
                g2.fillRect(startX + 5, slotY + 5, ARMOR_SLOT_SIZE - 10, ARMOR_SLOT_SIZE - 10);

                g2.setColor(Color.WHITE);
                g2.setFont(FontUtils.getStarCrush(10));
                drawCenteredString(g2, armorItem.getName(), startX, slotY + 5, ARMOR_SLOT_SIZE, ARMOR_SLOT_SIZE / 2);

                // Durability bar
                drawDurabilityBar(g2, armorItem, startX, slotY, ARMOR_SLOT_SIZE);
            }
        }
    }

    private void drawSlot(@NonNull Graphics2D g2, Item item, int slotX, int slotY, int slotNumber, boolean isSelected) {
        if (isSelected) {
            g2.setColor(new Color(255, 255, 0, 100));
            g2.fillRect(slotX - 3, slotY - 3, SLOT_SIZE + 6, SLOT_SIZE + 6);
        }

        // Slot háttér
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);

        // Slot keret
        g2.setColor(isSelected ? new Color(255, 255, 0) : new Color(120, 120, 120));
        g2.setStroke(new BasicStroke(isSelected ? 3 : 2));
        g2.drawRect(slotX, slotY, SLOT_SIZE, SLOT_SIZE);

        // Item és info
        if (item != null && !item.isBroken()) {
            g2.setColor(getRarityColor(item));
            g2.fillRect(slotX + 5, slotY + 5, SLOT_SIZE - 10, SLOT_SIZE - 10);

            g2.setColor(Color.WHITE);
            g2.setFont(FontUtils.getStarCrush(10));

            drawCenteredString(g2, item.getName(), slotX, slotY + 5, SLOT_SIZE, SLOT_SIZE / 2);
            drawDurabilityBar(g2, item, slotX, slotY, SLOT_SIZE);
        }

        // Slot szám
        g2.setColor(new Color(200, 200, 200));
        g2.setFont(FontUtils.getStarCrush(16));
        g2.drawString(String.valueOf(slotNumber), slotX + 2, slotY + 12);
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