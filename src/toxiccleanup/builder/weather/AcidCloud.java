package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.SpriteGroup;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;

/**
 * <p> A {@link AcidCloud} is a weather phenomena that will move to the left, over time. </p>
 * <p> It damages any machine that it shares a tile with, changing them to their damaged state </p>
 * <p> Plays an animation loop endlessly using sprites from {@link SpriteGallery#acidcloud}. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 *
 * <p> Rendered using {@link SpriteGallery#acidcloud}. </p>
 *
 * @provided
 */
public class AcidCloud extends Cloud implements Damaging {
    private static final SpriteGroup art = SpriteGallery.acidcloud;
    final public static int SPAWN_TIME = 300;

    /**
     * Constructs an instance of the AcidCloud at the given position.
     * @param position the position to spawn the AcidCloud at
     */
    public AcidCloud(Positionable position) {
        super(position);
    }

    /**
     *
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
    }

    @Override
    public Damage getDamage(Dimensions dimensions, Positionable position) {
        return new Damage(this.getPosition());
    }

    @Override
    public Damage getDamage() {
        return new Damage(this.getPosition());
    }

    @Override
    public SpriteGroup getArt() {
        return art;
    }
}