package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.builder.Damage;

/**
 * Indicates something can deal/generate 'damage'.
 */
public interface Damaging {
    /**
     * Returns a Damage instance when the context of game dimensions is necessary. This is
     * usually for tile-based lookups.
     *
     * @param dimensions the dimensions of the game
     * @param position   the position to check for damage
     * @return the damage at the provided position
     */
    Damage getDamage(Dimensions dimensions, Positionable position);

    /**
     * Returns a Damage instance
     *
     * @return the damage instance from the Damaging object
     */
    Damage getDamage();
}
