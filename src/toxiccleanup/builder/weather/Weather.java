package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.builder.Tickable;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.ui.RenderableGroup;

/**
 * Interface for managing weather phenomena that exist in this game.
 *
 * @provided
 */
public interface Weather extends Tickable, RenderableGroup, Damaging {

    /**
     * Adds a weather spawn point to be managed by the weather system
     * @param spawnPoint the spawn point to be added
     */
    void addSpawnPoint(WeatherSpawnPoint spawnPoint);

    /**
     * Adds a specific instance of a GameEntity to be managed by the weather system
     * @param weather the GameEntity to add
     */
    void addWeather(GameEntity weather);

    /**
     * Checks if the Weather system is obscuring the provided tile according to the position and
     * dimensions
     * @param dimensions the dimensions of the world, used for calculating tiles
     * @param position the position to check the obscuring status of
     * @return true if the position is being obscured, false otherwise
     */
    boolean isObscuring(Dimensions dimensions, Positionable position);

    /**
     * Checks if the Weather system is damaging the provided tile according to the position and
     * dimensions
     * @param dimensions the dimensions of the world, used for calculating tiles
     * @param position the position to check the damaging status of
     * @return true if the position is being damaged, false otherwise
     */
    boolean isDamaging(Dimensions dimensions, Positionable position);

    /**
     * Receives the position of a lightning rod and adjusts the weather system accordingly.
     *
     * @param position - position of the lightning rod that the weather should be adjusted for.
     */
    void applyLightningRod(Positionable position);
}
