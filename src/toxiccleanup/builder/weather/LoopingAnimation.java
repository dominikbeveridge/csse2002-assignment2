package toxiccleanup.builder.weather;

import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.art.sprites.SpriteGroup;

public class LoopingAnimation implements Animation {
    private int currentFrame = 1;
    private final int maxFrames;
    private final SpriteGroup art;

    public LoopingAnimation(SpriteGroup art) {
        this.art = art;
        maxFrames = art.getSprites().size();
    }

    @Override
    public void animate() {
       currentFrame += 1;
        if (currentFrame > maxFrames) {
            currentFrame = 1;
        }
    }

    @Override
    public Sprite getCurrentSprite() {
        return art.getSprite(String.valueOf(currentFrame));
    }

    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }


}
