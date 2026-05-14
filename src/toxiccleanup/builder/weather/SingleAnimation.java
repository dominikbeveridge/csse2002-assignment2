package toxiccleanup.builder.weather;

import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.art.sprites.SpriteGroup;

/**
 * SingleAnimation tracks and renders an animation of a given SpriteGroup.
 * It starts at frame "1" and continues until the last frame, which is held
 */
public class SingleAnimation implements Animation {
    private final int maxFrames;
    private final SpriteGroup art;
    private int currentFrame = 1;

    /**
     * Constructs an instance of LoopingAnimation with the provided SpriteGroup
     *
     * @param art the SpriteGroup to be animated
     */
    public SingleAnimation(SpriteGroup art) {
        this.art = art;
        this.maxFrames = art.getSprites().size();
    }

    /**
     * Progresses the current frame of the animation forward by 1 up to the last frame of the
     * SpriteGroup
     */
    @Override
    public void animate() {
        currentFrame += 1;
        currentFrame = Integer.min(currentFrame, maxFrames);

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
