package toxiccleanup.builder.machines;

import toxiccleanup.builder.Damage;

/**
 * Class for managing a damaged state, currently wraps a basic boolean
 * but other implementations could wrap around a more sophisticated health systems in the future.
 *
 * @provided
 */
public class DamageHandler implements Damageable {
    private boolean damaged = false;

    /**
     * Constructs a new DamageHandler
     */
    public DamageHandler() {
    }

    /**
     * Returns if this damageable Object is or is not in its damaged state.
     *
     * @return if this damageable Object is or is not in its damaged state.
     */
    @Override
    public boolean isDamaged() {
        return this.damaged;
    }

    /**
     * Sets the Damageable Object to its damaged state.
     *
     * @param dmg The damage being applied to the damageable object
     */
    @Override
    public void setDamage(Damage dmg) {
        this.damaged = true;
    }

    /**
     * Sets the Damageable Object to its undamaged state.
     */
    @Override
    public void repairDamage() {
        this.damaged = false;
    }
}
