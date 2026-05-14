package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.builder.Damage;

/**
 * Lightning Type Damage. Specifically does not deal damage to lightning rods.
 */
public class LightningDamage extends Damage {
    /**
     * Constructs an instance of LightningDamage at the specified position
     *
     * @param position the position to spawn the LightningDamage at
     */
    public LightningDamage(Positionable position) {
        super(position);
    }
}
