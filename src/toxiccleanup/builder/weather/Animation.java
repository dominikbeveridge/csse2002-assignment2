package toxiccleanup.builder.weather;

import toxiccleanup.engine.art.sprites.Sprite;

/**
 * An interface for providing common Animation behaviour to game machines, weather and sprites
 */
public interface Animation {
    /**
     * Animates the object by one step
     */
    void animate();

    /**
     * Gets the current sprite that should be shown for the current animation frame
     *
     * @return the current Sprite of the animation frame
     */
    Sprite getCurrentSprite();

    /**
     * Gets the current frame index of the animation
     *
     * @return the current frame index of the animation
     */
    int getCurrentFrame();
}
