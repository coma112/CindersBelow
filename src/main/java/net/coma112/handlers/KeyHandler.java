package net.coma112.handlers;

import org.jspecify.annotations.NonNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean inventoryToggled = false;
    public boolean debugToggled = false;
    public boolean settingsToggled = false;

    private boolean f3Pressed = false;
    private boolean ePressed = false;
    private boolean escPressed = false;

    @Override
    public void keyTyped(KeyEvent event) {
        // in another life
    }

    @Override
    public void keyPressed(@NonNull KeyEvent event) {
        int code = event.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
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
            case KeyEvent.VK_ESCAPE -> {
                if (!escPressed) {
                    settingsToggled = !settingsToggled;
                    escPressed = true;
                }
            }
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
            case KeyEvent.VK_E -> ePressed = false;
            case KeyEvent.VK_F3 -> f3Pressed = false;
            case KeyEvent.VK_ESCAPE -> escPressed = false;
        }
    }
}