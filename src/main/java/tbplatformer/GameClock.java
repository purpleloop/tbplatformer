package tbplatformer;

import java.util.logging.Logger;

/** Manages the game clock for a realistic update & rendering time. */
public class GameClock {
    
    /** One thousand (double). */
    public static final double THOUSAND_DOUBLE = 1000.0;

    /** One million (long). */
    public static final int MILLION_LONG = 1000000;

    /** One million (double). */
    private static final double MILLION_DOUBLE = 1000000.0;

    /** Class logger. */
    private static final Logger LOG = Logger.getLogger(GameClock.class.getName());

    /** The targeted rendering rate in frames par second. */
    public static final int TARGET_FPS = 30;

    /** Targeted rendering time in ms. */
    public static final long TARGET_TIME = 1000 / TARGET_FPS;

    /** The start time of the current loop, in ns. */
    private long startTime;

    /** Total time of the current render sequence. */
    private int totalTime = 0;

    /** Average frames per second for the last sequence. */
    private double averageFPS;

    /** Rendered frames of the current sequence. */
    private int frameCount = 0;

    /** Max frames, used to compose a sequence. */
    private int maxFrameCount = 30;

    /** Initialize the game clock at the start of the loop. */
    public void beginLoop() {
        startTime = System.nanoTime();
    }

    /** @return the time to wait to adapt to the targeted FPS, in ms */
    public long computeWaitTime() {

        // Compute the time elapsed for update and rendering, in ms.
        long urdTime = (System.nanoTime() - startTime) / MILLION_LONG;

        // Compute the time left
        long waitTime = TARGET_TIME - urdTime;

        if (waitTime <= 0) {

            // There was no more time left, assuming a minimum wait time.
            waitTime = 5;
            
            LOG.info(() -> "gameLoop targetTime : " + TARGET_TIME);
            LOG.info(() -> "gameLoop URDTimeMillis : " + urdTime);
        }

        return waitTime;
    }

    /** Handles the end of the loop and updates the sequence. */
    public void endLoop() {

        // Update the total elapsed time
        totalTime += System.nanoTime() - startTime;

        // Count the frame as rendered
        frameCount++;

        // If all expected frames of the sequence have been rendered
        if (frameCount == maxFrameCount) {

            // Compute the average FPS
            averageFPS = THOUSAND_DOUBLE / (((double) totalTime / frameCount) / MILLION_DOUBLE);

            // Reset the sequence
            frameCount = 0;
            totalTime = 0;
        }

    }

    /** @return the average fps */
    public double getAverageFPS() {
        return averageFPS;
    }
}
