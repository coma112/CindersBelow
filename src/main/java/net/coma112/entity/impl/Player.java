package net.coma112.entity.impl;

import lombok.Getter;
import net.coma112.GamePanel;
import net.coma112.entity.Entity;
import net.coma112.handlers.KeyHandler;
import net.coma112.inventory.impl.PlayerInventory;
import net.coma112.item.impl.Sword;
import net.coma112.sprite.SpriteAnimation;
import net.coma112.sprite.SpriteLoader;
import net.coma112.sprite.SpriteSheet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ConcurrentHashMap;

public class Player extends Entity {
    GamePanel gamePanel;
    KeyHandler keyHandler;
    @Getter private final PlayerInventory inventory;

    @SpriteSheet(pattern = "player/boy_up_{n}.png")
    private BufferedImage[] upSprites;

    @SpriteSheet(pattern = "player/boy_down_{n}.png")
    private BufferedImage[] downSprites;

    @SpriteSheet(pattern = "player/boy_left_{n}.png")
    private BufferedImage[] leftSprites;

    @SpriteSheet(pattern = "player/boy_right_{n}.png")
    private BufferedImage[] rightSprites;

    private final ConcurrentHashMap<String, SpriteAnimation> animations = new ConcurrentHashMap<>();
    private SpriteAnimation currentAnimation;

    private int staminaRegenCounter = 0;
    private static final int STAMINA_REGEN_DELAY = 60;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        this.gamePanel = gamePanel;
        this.keyHandler = keyHandler;
        this.inventory = new PlayerInventory(36); // 9x4 slot (9 hotbar + 27 inventory)

        setDefaultValues();
        loadSprites();
        setupAnimations();
    }

    public void setDefaultValues() {
        x = gamePanel.TILE_SIZE * 25;
        y = gamePanel.TILE_SIZE * 25;
        speed = 4;
        direction = "down";

        maxHealth = 100;
        currentHealth = maxHealth;
        maxStamina = 100;
        currentStamina = maxStamina;

        Sword sword = new Sword();
        getInventory().setItem(1, sword);
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
        if (keyHandler.inventoryToggled) {
            if (inventory.isOpen()) {
                inventory.close(this);
            } else {
                inventory.open(this);
            }

            keyHandler.inventoryToggled = false;
        }

        if (keyHandler.hotbarSlotPressed >= 0) {
            inventory.setSelectedHotbarSlot(keyHandler.hotbarSlotPressed);
            keyHandler.hotbarSlotPressed = -1;
        }

        if (inventory.isOpen()) {
            if (currentAnimation != null && currentAnimation.isPlaying()) {
                currentAnimation.stop();
                currentAnimation.reset();
            }

            return;
        }

        if (keyHandler.attackPressed) {
            performAttack();
            keyHandler.attackPressed = false;
        }

        if (currentStamina < maxStamina) {
            staminaRegenCounter++;
            if (staminaRegenCounter >= STAMINA_REGEN_DELAY) {
                currentStamina = Math.min(currentStamina + 1, maxStamina);
                staminaRegenCounter = 0;
            }
        }

        boolean isMoving = keyHandler.upPressed || keyHandler.downPressed ||
                keyHandler.leftPressed || keyHandler.rightPressed;

        if (isMoving) {
            int currentSpeed = speed;
            if (keyHandler.shiftPressed && currentStamina > 0) {
                currentSpeed = speed * 2;
                currentStamina = Math.max(currentStamina - 1, 0);
                staminaRegenCounter = 0;
            }

            collisionOn = false;

            if (keyHandler.upPressed) {
                direction = "up";
            } else if (keyHandler.downPressed) {
                direction = "down";
            } else if (keyHandler.leftPressed) {
                direction = "left";
            } else {
                direction = "right";
            }

            gamePanel.getCollisionChecker().checkTile(this);

            if (!collisionOn) {
                switch (direction) {
                    case "up" -> y -= currentSpeed;
                    case "down" -> y += currentSpeed;
                    case "left" -> x -= currentSpeed;
                    case "right" -> x += currentSpeed;
                }
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

    private void performAttack() {
        var selectedItem = inventory.getSelectedHotbarItem();

        if (selectedItem == null || selectedItem.isBroken()) {
            return;
        }

        if (currentStamina < selectedItem.getStaminaCost()) {
            return;
        }

        consumeStamina(selectedItem.getStaminaCost());
        selectedItem.use();

        staminaRegenCounter = 0;
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

        int screenX = gamePanel.SCREEN_WIDTH / 2 - (gamePanel.TILE_SIZE / 2);
        int screenY = gamePanel.SCREEN_HEIGHT / 2 - (gamePanel.TILE_SIZE / 2);

        if (image != null) {
            g2.drawImage(image, screenX, screenY, gamePanel.TILE_SIZE, gamePanel.TILE_SIZE, null);
        }

        if (gamePanel.keyHandler.debugToggled) {
            g2.setColor(new Color(255, 0, 0, 100));
            int hitboxX = screenX + 8;
            int hitboxY = screenY + 16;
            int hitboxWidth = gamePanel.TILE_SIZE - 16;
            int hitboxHeight = gamePanel.TILE_SIZE - 20;
            g2.fillRect(hitboxX, hitboxY, hitboxWidth, hitboxHeight);
        }
    }

    public void takeDamage(int damage) {
        currentHealth = Math.max(currentHealth - damage, 0);
    }

    public void heal(int amount) {
        currentHealth = Math.min(currentHealth + amount, maxHealth);
    }

    public void consumeStamina(int amount) {
        currentStamina = Math.max(currentStamina - amount, 0);
    }

    public void restoreStamina(int amount) {
        currentStamina = Math.min(currentStamina + amount, maxStamina);
    }
}