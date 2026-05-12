package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.RepeatingTimer;
import toxiccleanup.engine.timing.TickTimer;

/**
 * <p>Handles creating {@link WeatherSpawnPoint}s at a given position,
 * the kind of {@link WeatherSpawnPoint} constructed varies based on the symbol given.</p>
 * <ul>
 * <li>c and C are for clouds</li>
 * <li>a and A are for acidclouds</li>
 * <li>r and R are for rainclouds</li>
 * <li>l and L are for lightning</li>
 * </ul>
 * <p>
 * lower case letters use the static SPAWN_TIME for the relevant class
 * as the duration to give a {@link RepeatingTimer}
 * </p>
 * <p>
 *  upper case letters use the static SPAWN_TIME * 5.5 for the relevant class as the duration
 *  to give a {@link RepeatingTimer}
 * </p>
 */
public class SpawnerFactory {
    /**
     * Creates a WeatherSpawnPoint at the given position according to the symbol
     *
     * @param position The position to create the WeatherSpawnPoint at
     * @param symbol The symbol corresponding to the weather type
     * @return a WeatherSpawnPoint at the given position and type
     */
    private static final char CLOUD_SYMBOL = 'c';
    private static final char RAINCLOUD_SYMBOL = 'r';
    private static final char ACIDCLOUD_SYMBOL = 'a';
    private static final char LIGHTNING_SYMBOL = 'l';
    private static final char EMPTY_SYMBOL = '_';
    private static final double UPPER_MODIFIER = 5.5;
    private enum WeatherType  {
        CLOUD,
        RAINCLOUD,
        ACIDCLOUD,
        LIGHTNING,
    }
    private static WeatherType parseWeatherType(char symbol) {
        return switch (Character.toLowerCase(symbol)) {
            case CLOUD_SYMBOL -> WeatherType.CLOUD;
            case RAINCLOUD_SYMBOL -> WeatherType.RAINCLOUD;
            case ACIDCLOUD_SYMBOL -> WeatherType.ACIDCLOUD;
            case LIGHTNING_SYMBOL -> WeatherType.LIGHTNING;
            default -> throw new IllegalArgumentException("Symbol does not represent a tile.");
        };

    }
    public static WeatherSpawnPoint fromSymbol(Positionable position, char symbol) {
        if (symbol == EMPTY_SYMBOL) {
            return null;
        }
        WeatherType weatherType = parseWeatherType(symbol);

        Spawner spawner = switch (weatherType) {
            case CLOUD -> (Positionable pos) -> new Cloud(pos);
            case RAINCLOUD -> (Positionable pos) -> new RainCloud(pos);
            case ACIDCLOUD -> (Positionable pos) -> new AcidCloud(pos);
            case LIGHTNING -> (Positionable pos) -> new Lightning(pos);
        };

         double baseDuration = switch (weatherType) {
            case CLOUD -> Cloud.SPAWN_TIME;
            case RAINCLOUD -> RainCloud.SPAWN_TIME;
            case ACIDCLOUD -> AcidCloud.SPAWN_TIME;
            case LIGHTNING -> Lightning.SPAWN_TIME;
        };

        double modifier = 1;
        if (Character.isUpperCase(symbol)) {
            modifier = UPPER_MODIFIER;
        }
        TickTimer repeatingTimer = new RepeatingTimer((int) (baseDuration * modifier));

        return new WeatherSpawnPoint(position, repeatingTimer, spawner);

    }


}

