package toxiccleanup.builder.weather;

import toxiccleanup.engine.art.sprites.Sprite;

public interface Animation {
    void animate();
    Sprite getCurrentSprite();
    int getCurrentFrame();
}
