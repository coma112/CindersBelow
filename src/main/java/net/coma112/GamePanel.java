package net.coma112;

import net.coma112.entity.impl.Player;
import net.coma112.handlers.KeyHandler;
import net.coma112.ui.DebugOverlay;
import net.coma112.ui.HotbarUI;
import net.coma112.ui.InventoryUI;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // kijelzo beallitasok
    final int ORIGINAL_TILE_SIZE = 16; // 16 x 16 tile
    final int SCALE = 3; // mivel a 16x16px nagyon kevés a mai monitorokon, ezért átméretezzük ami 48x48 lesz!
    public final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    // játékterület - dinamikus képernyőméret
    public final int SCREEN_WIDTH;
    public final int SCREEN_HEIGHT;
    public final int MAX_SCREEN_COL;
    public final int MAX_SCREEN_ROW;

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);

    private final HotbarUI hotbarUI;
    private final InventoryUI inventoryUI;
    private final DebugOverlay debugOverlay;

    final int FPS = 60;

    public GamePanel() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SCREEN_WIDTH = (int) screenSize.getWidth();
        SCREEN_HEIGHT = (int) screenSize.getHeight();

        MAX_SCREEN_COL = SCREEN_WIDTH / TILE_SIZE;
        MAX_SCREEN_ROW = SCREEN_HEIGHT / TILE_SIZE;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // jobb lesz a rendering teljesítmény
        this.addKeyListener(keyHandler);
        this.addMouseListener(keyHandler);
        this.setFocusable(true);

        // UI inicializálás
        this.hotbarUI = new HotbarUI(this);
        this.inventoryUI = new InventoryUI(this);
        this.debugOverlay = new DebugOverlay(this);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long drawInterval = 1_000_000_000 / FPS;
        long nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread.isAlive()) {
            // 1. Információk frissítése
            update();

            // 2. rajzolás, megjelenítés (grafika frissítése lényegében)
            repaint();

            try {
                long remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1_000_000;

                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep(remainingTime);

                nextDrawTime += drawInterval;
            } catch (InterruptedException exception) {
                System.err.println(exception.getMessage());
            }
        }
    }

    public void update() {
        if (keyHandler.exitPressed) {
            System.exit(0);
        }

        player.update();

        if (keyHandler.debugToggled) {
            debugOverlay.update();
        }

        removeBrokenItems();
    }

    private void removeBrokenItems() {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            var item = player.getInventory().getItem(i);
            if (item != null && item.isBroken()) {
                player.getInventory().removeItem(i);
            }
        }

        // Armor slotok ellenőrzése
        for (var armorSlot : net.coma112.inventory.ArmorSlot.values()) {
            var armorItem = player.getInventory().getArmorItem(armorSlot);
            if (armorItem != null && armorItem.isBroken()) {
                player.getInventory().removeArmorItem(armorSlot);
            }
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;

        // Játékos rajzolása
        player.draw(g2);

        // Hotbar UI rajzolása (mindig látható)
        hotbarUI.draw(g2, player.getInventory());

        // Inventory UI rajzolása (csak ha nyitva van)
        if (player.getInventory().isOpen()) {
            inventoryUI.draw(g2, player.getInventory());
        }

        // Debug overlay rajzolása
        if (keyHandler.debugToggled) {
            debugOverlay.draw(g2, player, player.getInventory());
        }

        g2.dispose();
    }
}