package net.coma112.sprite;

import java.awt.image.BufferedImage;

public class SpriteAnimation {
    private final BufferedImage[] frames;
    private final int frameDelay;
    private final boolean loop;
    private int currentFrame;
    private int frameCounter;
    private boolean playing;

    public SpriteAnimation(BufferedImage[] frames, int frameDelay) {
        this.frames = frames;
        this.frameDelay = frameDelay;
        this.currentFrame = 0;
        this.frameCounter = 0;
        this.loop = true;
        this.playing = false;
    }

    public void update() {
        if (!playing || frames == null || frames.length == 0) {
            return;
        }

        frameCounter++;

        if (frameCounter >= frameDelay) {
            currentFrame++;
            frameCounter = 0;

            if (currentFrame >= frames.length) {
                if (loop) {
                    currentFrame = 0;
                } else {
                    currentFrame = frames.length - 1;
                    playing = false;
                }
            }
        }
    }

    public BufferedImage getCurrentFrame() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[currentFrame];
    }

    public void play() {
        this.playing = true;
    }

    public void stop() {
        this.playing = false;
    }

    public void reset() {
        this.currentFrame = 0;
        this.frameCounter = 0;
    }

    public boolean isPlaying() {
        return playing;
    }
}
