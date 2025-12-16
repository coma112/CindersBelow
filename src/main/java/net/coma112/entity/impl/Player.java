package net.coma112.entity.impl;

import net.coma112.GamePanel;
import net.coma112.entity.Entity;
import net.coma112.handlers.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyHandler;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));  // FIX: _1 hozzáadva
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));  // FIX: _2 hozzáadva
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png")); // FIX: _1 hozzáadva
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png")); // FIX: _2 hozzáadva
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public void update() {
        if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
            if (keyHandler.upPressed) {
                direction = "up";
                y -= speed;
            }

            if (keyHandler.downPressed) {
                direction = "down";
                y += speed;
            }

            if (keyHandler.leftPressed) {
                direction = "left";
                x -= speed;
            }

            if (keyHandler.rightPressed) {
                direction = "right";
                x += speed;
            }

            spriteCounter++;

            if (spriteCounter > 16) {
                if (spriteNumber == 1) {
                    spriteNumber = 2;
                } else if (spriteNumber == 2) {
                    spriteNumber = 1;
                }

                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        switch (direction) {
            case "up" -> {
                if (spriteNumber == 1) {
                    image = up1;
                }

                if (spriteNumber == 2) {
                    image = up2;
                }
            }
            case "down" -> {
                if (spriteNumber == 1) {
                    image = down1;
                }

                if (spriteNumber == 2) {
                    image = down2;
                }
            }
            case "left" -> {
                if (spriteNumber == 1) {
                    image = left1;
                }

                if (spriteNumber == 2) {
                    image = left2;
                }
            }
            case "right" -> {
                if (spriteNumber == 1) {
                    image = right1;
                }

                if (spriteNumber == 2) {
                    image = right2;
                }
            }
        }

        g2.drawImage(image, x, y, gamePanel.TILE_SIZE, gamePanel.TILE_SIZE, null);
    }
}
