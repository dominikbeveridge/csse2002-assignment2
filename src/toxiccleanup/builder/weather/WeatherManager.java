package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.game.Entity;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.renderer.Renderable;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.machines.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The concrete implementation of {@link Weather} for the
 * {@link toxiccleanup.builder.ToxicCleanup} game. {@link WeatherManager} is responsible for:
 *
 * <ul>
 *   <li>Holding all {@link WeatherSpawnPoint}s for the game.</li>
 *   <li>Holding all Weather Phenomena {@link GameEntity}s for the game.</li>
 *   <li>Handling the interaction between {@link LightningRod} and {@link Lightning}.</li>
 *   <li>
 *       Answering requests about the overall state of the weather system by other systems
 *       <p>i.e</p>
 *       <ul>
 *           <li>> is a particular location obscured.</li>
 *           <li>> is a particular location currently receiving damage from weather phenomena.</li>
 *       </ul>
 *   </li>
 *   <li>Ticking forward the internal state of all {@link WeatherSpawnPoint}s. </li>
 *   <li>Ticking forward the internal state of all weather {@link GameEntity}s. </li>
 * </ul>
 */
public class WeatherManager implements Weather {
    private final List<WeatherSpawnPoint> spawnPoints = new ArrayList<>();
    private final List<GameEntity> phenomena = new ArrayList<>();

    public WeatherManager() {
    }

    /**
     * Adds the given spawnPoint to the WeatherManager to handle ticking and
     * other game logic.
     *
     * @param spawnPoint - the spawn point to add
     */
    public void addSpawnPoint(WeatherSpawnPoint spawnPoint) {
        spawnPoints.add(spawnPoint);
    }

    /**
     * Adds a GameEntity to be managed by the WeatherManager.
     *
     * @param weather - GameEntity instance of a weather Phenomenon.
     */
    public void addWeather(GameEntity weather) {
        phenomena.add(weather);
    }

    /**
     * Checks if the given tile location is currently obscured by the
     * internal weather system.
     *
     * @param dimensions - the dimensions of the game window, used to calculate tile
     *                   positions
     * @param position   - the position of the tile
     * @return true if the given title location is currently obscured by the
     * internal weather system and false otherwise.
     */
    @Override
    public boolean isObscuring(Dimensions dimensions, Positionable position) {
        int gridX = dimensions.pixelToTile(position.getX());
        int gridY = dimensions.pixelToTile(position.getY());

        for (GameEntity weather : phenomena) {
            final int weatherGridX = dimensions.pixelToTile(weather.getX());
            final int weatherGridY = dimensions.pixelToTile(weather.getY());

            if (gridX == weatherGridX && gridY == weatherGridY && weather instanceof Obscuring) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the {@link Damage} that the given tile location is currently experiencing.
     * If there is no damage it returns null.
     *
     * @param dimensions - the dimensions of the game window, used to calculate tile
     *                   positions.
     * @param position   - the position of the tile
     * @return {@link Damage} the given tile location is currently experiencing, or null if there
     * is no damage.
     */
    public Damage getDamage(Dimensions dimensions, Positionable position) {
        int gridX = dimensions.pixelToTile(position.getX());
        int gridY = dimensions.pixelToTile(position.getY());

        for (GameEntity weather : phenomena) {
            final int weatherGridX = dimensions.pixelToTile(weather.getX());
            final int weatherGridY = dimensions.pixelToTile(weather.getY());

            if (gridX == weatherGridX && gridY == weatherGridY && weather instanceof Damaging) {
                return ((Damaging) weather).getDamage();
            }
        }
        return null;
    }

    @Override
    public Damage getDamage() {
        return null;
    }

    /**
     * Checks if the given tile location is being damaged.
     *
     * @param dimensions - the dimensions of the game window, used to calculate tile
     *                        positions.
     * @param position   - the position of the tile
     * @return true if the given tile location is being damaged, false otherwise
     */
    @Override
    public boolean isDamaging(Dimensions dimensions, Positionable position) {
        int gridX = dimensions.pixelToTile(position.getX());
        int gridY = dimensions.pixelToTile(position.getY());

        for (GameEntity weather : phenomena) {
            final int weatherGridX = dimensions.pixelToTile(weather.getX());
            final int weatherGridY = dimensions.pixelToTile(weather.getY());

            if (gridX == weatherGridX && gridY == weatherGridY && weather instanceof Damaging) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adjusts the weather system according to the provided {@link LightningRod} position.
     * Moves any {@link Lightning} that are within the LightningRod radius {@value LightningRod#RADIUS}
     * to the given position.
     *
     * @param position - position of the LightningRod
     */
    @Override
    public void applyLightningRod(Positionable position) {
        for (GameEntity weather : phenomena) {
            if (weather instanceof Lightning) {
                final Lightning lightning = (Lightning) weather;
                int deltaX = position.getX() - lightning.getX();
                int deltaY = position.getY() - lightning.getY();
                final int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                if (distance <= LightningRod.RADIUS) {
                    lightning.setX(position.getX());
                    lightning.setY(position.getY());
                }
            }
        }

    }

    /**
     * Advances the state of the WeatherManager by one game tick
     *
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension.
     * @param game  The state of the game, including the player and world.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        for (WeatherSpawnPoint spawnPoint : spawnPoints) {
            spawnPoint.tick(state, game);
        }
        for (GameEntity weather : phenomena) {
            weather.tick(state, game);
        }
        cleanup();
    }

    /**
     * A collection of renderables that should each be displayed.
     *
     * @return A collection of renderables to display.
     */
    @Override
    public List<Renderable> render() {
        return new ArrayList<>(phenomena);
    }

    /**
     * Removes any weather phenomena that have been marked for removal.
     */
    private void cleanup() {
        phenomena.removeIf(Entity::isMarkedForRemoval);
    }

    /**
     * Generates a simplified {@link String} representation of the WeatherManagers internal state.
     *
     * @return a simplified {@link String} representation of the WeatherManagers internal state.
     */
    @Override
    public String toString() {
        return "WeatherManager:[\n"
                + "Phenomena:" + phenomena.size() + "\n"
                + "SpawnPoints:" + spawnPoints.size() + "\n"
                + "]\n";
    }

}
