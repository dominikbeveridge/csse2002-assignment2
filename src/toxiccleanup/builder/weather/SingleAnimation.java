package toxiccleanup.builder.weather;

import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.art.sprites.SpriteGroup;

public class SingleAnimation implements Animation {
    private int currentFrame = 1;
    private final int maxFrames;
    private final SpriteGroup art;

    public SingleAnimation(SpriteGroup art) {
        this.art = art;
        this.maxFrames = art.getSprites().size();
    }

    @Override
    public void animate() {
        currentFrame += 1;

    }

    /**
     * Updates the current frame of the sprite while ensuring that the frame is within bounds
     *              max number of frames in the sprite group
     */
    @Override
    public Sprite getCurrentFrame() {
        int frame = Integer.min(currentFrame, maxFrames);
        return art.getSprite(String.valueOf(frame));
    }
}
