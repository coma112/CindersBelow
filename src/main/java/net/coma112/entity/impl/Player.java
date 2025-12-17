package net.coma112.entity.impl;

import net.coma112.GamePanel;
import net.coma112.entity.Entity;
import net.coma112.handlers.KeyHandler;
import net.coma112.sprite.SpriteAnimation;
import net.coma112.sprite.SpriteLoader;
import net.coma112.sprite.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyHandler;

    @SpriteSheet(pattern = "player/boy_up_{n}.png")
    private BufferedImage[] upSprites;

    @SpriteSheet(pattern = "player/boy_down_{n}.png")
    private BufferedImage[] downSprites;

    @SpriteSheet(pattern = "player/boy_left_{n}.png")
    private BufferedImage[] leftSprites;

    @SpriteSheet(pattern = "player/boy_right_{n}.png")
    private BufferedImage[] rightSprites;

    private final Map<String, SpriteAnimation> animations = new HashMap<>();
    private SpriteAnimation currentAnimation;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;

        setDefaultValues();
        loadSprites();
        setupAnimations();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 3;
        direction = "down";
    }

    private void loadSprites() {
        SpriteLoader.loadSprites(this);
    }

    private void setupAnimations() {
        animations.put("up", new SpriteAnimation(upSprites, 16));
        animations.put("down", new SpriteAnimation(downSprites, 16));
        animations.put("left", new SpriteAnimation(leftSprites, 16));
        animations.put("right", new SpriteAnimation(rightSprites, 16));

        currentAnimation = animations.get(direction);
    }

    public void update() {
        boolean isMoving = keyHandler.upPressed || keyHandler.downPressed ||
                keyHandler.leftPressed || keyHandler.rightPressed;

        if (isMoving) {
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

            SpriteAnimation newAnimation = animations.get(direction);
            if (currentAnimation != newAnimation) {
                if (currentAnimation != null) {
                    currentAnimation.stop();
                    currentAnimation.reset();
                }
                currentAnimation = newAnimation;
            }

            if (!currentAnimation.isPlaying()) {
                currentAnimation.play();
            }

            currentAnimation.update();
        } else {
            if (currentAnimation != null && currentAnimation.isPlaying()) {
                currentAnimation.stop();
                currentAnimation.reset();
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        if (currentAnimation != null) {
            image = currentAnimation.getCurrentFrame();
        }

        if (image == null) {
            switch (direction) {
                case "up" -> image = upSprites != null && upSprites.length > 0 ? upSprites[0] : null;
                case "down" -> image = downSprites != null && downSprites.length > 0 ? downSprites[0] : null;
                case "left" -> image = leftSprites != null && leftSprites.length > 0 ? leftSprites[0] : null;
                case "right" -> image = rightSprites != null && rightSprites.length > 0 ? rightSprites[0] : null;
            }
        }

        if (image != null) {
            g2.drawImage(image, x, y, gamePanel.TILE_SIZE, gamePanel.TILE_SIZE, null);
        }
    }
}