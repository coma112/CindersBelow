package net.coma112;

import net.coma112.utils.FontUtils;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        FontUtils.initialize();

        JFrame window = new JFrame();

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true);
        window.setTitle("Cinders Below");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}