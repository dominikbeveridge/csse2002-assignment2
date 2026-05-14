package toxiccleanup.builder.weather;

import toxiccleanup.builder.Damage;
import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.art.sprites.SpriteGroup;

/**
 * LoopingAnimation tracks and renders an animation of a given SpriteGroup.
 * It starts at frame "1" and continues through all available frames before looping to the start.
 */
public class LoopingAnimation implements Animation {
    private int currentFrame = 1;
    private final int maxFrames;
    private final SpriteGroup art;

    /**
     * Constructs an instance of LoopingAnimation with the provided SpriteGroup
     *
     * @param art the SpriteGroup to be animated
     */
    public LoopingAnimation(SpriteGroup art) {
        this.art = art;
        maxFrames = art.getSprites().size();
    }

    /**
     * Progresses the current frame of the animation forward by 1, looping to the start when
     * the end
     */
    @Override
    public void animate() {
        currentFrame += 1;
        if (currentFrame > maxFrames) {
            currentFrame = 1;
        }
    }

    /**
     * Returns the current Sprite corresponding to the current animation frame
     *
     * @return the current Sprite of the animation frame
     */
    @Override
    public Sprite getCurrentSprite() {
        return art.getSprite(String.valueOf(currentFrame));
    }

    /**
     * Gets the current frame index of the animation
     *
     * @return the current frame index of the animation
     */
    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }


}
