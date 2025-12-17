package net.coma112.handlers;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener, MouseListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean inventoryToggled = false;
    public boolean debugToggled = false;
    public boolean settingsToggled = false;
    public boolean shiftPressed = false;
    public boolean exitPressed = false;
    public boolean attackPressed = false;

    public int hotbarSlotPressed = -1; // -1 = nincs megnyomva, 0-8 = hotbar slot

    private boolean f3Pressed = false;
    private boolean f11Pressed = false;
    private boolean ePressed = false;
    private boolean escPressed = false;

    @Override
    public void keyPressed(@NonNull KeyEvent event) {
        int code = event.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_SHIFT -> shiftPressed = true;
            case KeyEvent.VK_E -> {
                if (!ePressed) {
                    inventoryToggled = !inventoryToggled;
                    ePressed = true;
                }
            }
            case KeyEvent.VK_F3 -> {
                if (!f3Pressed) {
                    debugToggled = !debugToggled;
                    f3Pressed = true;
                }
            }
            case KeyEvent.VK_F11 -> {
                if (!f11Pressed) {
                    exitPressed = true;
                    f11Pressed = true;
                }
            }
            case KeyEvent.VK_ESCAPE -> {
                if (!escPressed) {
                    settingsToggled = !settingsToggled;
                    escPressed = true;
                }
            }

            case KeyEvent.VK_1 -> hotbarSlotPressed = 0;
            case KeyEvent.VK_2 -> hotbarSlotPressed = 1;
            case KeyEvent.VK_3 -> hotbarSlotPressed = 2;
            case KeyEvent.VK_4 -> hotbarSlotPressed = 3;
            case KeyEvent.VK_5 -> hotbarSlotPressed = 4;
            case KeyEvent.VK_6 -> hotbarSlotPressed = 5;
            case KeyEvent.VK_7 -> hotbarSlotPressed = 6;
            case KeyEvent.VK_8 -> hotbarSlotPressed = 7;
            case KeyEvent.VK_9 -> hotbarSlotPressed = 8;
        }
    }

    @Override
    public void keyReleased(@NonNull KeyEvent event) {
        int code = event.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_SHIFT -> shiftPressed = false;
            case KeyEvent.VK_E -> ePressed = false;
            case KeyEvent.VK_F3 -> f3Pressed = false;
            case KeyEvent.VK_F11 -> f11Pressed = false;
            case KeyEvent.VK_ESCAPE -> escPressed = false;
        }
    }

    @Override
    public void mousePressed(@NotNull MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            attackPressed = true;
        }
    }

    @Override
    public void mouseReleased(@NotNull MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            attackPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {}

    @Override
    public void mouseEntered(MouseEvent event) {}

    @Override
    public void mouseExited(MouseEvent event) {}

    @Override
    public void mouseClicked(MouseEvent event) {}
}