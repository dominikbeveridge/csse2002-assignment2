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

     void addSpawnPoint(WeatherSpawnPoint spawnPoint);

     void addWeather(GameEntity weather);

     boolean isObscuring(Dimensions dimensions, Positionable position);

     boolean isDamaging(Dimensions dimensions, Positionable position);

    /**
     * Receives the position of a lightning rod and adjusts the weather system accordingly.
     *
     * @param position - position of the lightning rod that the weather should be adjusted for.
     */
     void applyLightningRod(Positionable position);
}
