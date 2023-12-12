package tbplatformer.animation;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CharacterAnimation {

    public enum Posture {
        IDLE, WALKING, JUMPING, FALLING;
    }

    private Animation animation;
    private Map<String, FrameSet> frameSets;

    public CharacterAnimation(int width, int height) throws IOException {
        frameSets = new HashMap<>();

        frameSets.put(Posture.IDLE.name(),
                new FrameSet(1, "graphics/player/kirbyidle.gif", width, height));
        frameSets.put(Posture.JUMPING.name(),
                new FrameSet(1, "graphics/player/kirbyjump.gif", width, height));
        frameSets.put(Posture.FALLING.name(),
                new FrameSet(1, "graphics/player/kirbyfall.gif", width, height));
        frameSets.put(Posture.WALKING.name(),
                new FrameSet(6, "graphics/player/kirbywalk.gif", width, height));

        animation = new Animation();
    }

    public void setPosture(Posture posture, int delay) {
        animation.setFrames(frameSets.get(posture.name()));
        animation.setDelay(delay);
    }

    public void update() {
        animation.update();
    }

    public Image getImage() {
        return animation.getImage();
    }

}
