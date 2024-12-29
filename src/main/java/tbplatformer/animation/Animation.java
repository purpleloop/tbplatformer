package tbplatformer.animation;

import java.awt.image.BufferedImage;

import tbplatformer.GameClock;

/** Handles an animation. */
public class Animation {

    /** The delay value used to disable the animation. */
    private static final int DISABLE_DELAY = -1;

    /** The set of images (frames) to animate. */
    private BufferedImage[] frames;

    /** The current frame index. */
    private int currentFrame;

    /** Start time of the animation, in nano-seconds. */
    private long startTime;

    /** Delay between two frames, in milli-seconds. */
    private long delay;

    /** Animation constructor. */
    public Animation() {
        startTime = System.nanoTime();
    }

    /**
     * Changes the animation frames.
     * 
     * @param frameSet the animation frameSet
     */
    public void setFrames(FrameSet frameSet) {
        this.frames = frameSet.getFrames();
        if (currentFrame >= this.frames.length) {
            currentFrame = 0;
        }
    }

    /** @param delay the new animation delay, in milli-seconds. */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    /** Updates the animation. */
    public void update() {

        // Immediate return if animation is disabled
        if (delay == DISABLE_DELAY) {
            return;
        }

        // Computes the elapsed time since the startTime, in millis.
        long elapsed = (System.nanoTime() - startTime) / GameClock.MILLION_LONG;

        // Check if the delay has been reached
        if (elapsed > delay) {

            // Updates the frame and reset the timer
            currentFrame++;
            startTime = System.nanoTime();
        }
        if (currentFrame == frames.length) {
            currentFrame = 0;
        }
    }

    /** @return the image of the current frame */
    public BufferedImage getImage() {
        return frames[currentFrame];
    }

}
