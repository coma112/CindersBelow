package net.coma112;

import net.coma112.entity.impl.Player;
import net.coma112.handlers.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // kijelzo beallitasok
    final int ORIGINAL_TILE_SIZE = 16; // 16 x 16 tile
    final int SCALE = 3; // mivel a 16x16px nagyon kevés a mai monitorokon, ezért átméretezzük ami 48x48 lesz!
    public final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE;

    // játékterület
    final int MAX_SCREEN_COL = 40; // 16 oszlop
    final double MAX_SCREEN_ROW = 22.5; // 12 oszlop
    final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    final int SCREEN_HEIGHT = (int) (TILE_SIZE * MAX_SCREEN_ROW);

    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyHandler);

    final int FPS = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // jobb lesz a rendering teljesítmény
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
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

            // 2. rajzolás, megjelenítés
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
        player.update();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics;

        player.draw(g2);
        g2.dispose();
    }
}
