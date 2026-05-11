package toxiccleanup.builder.machines;

import org.junit.Before;
import org.junit.Test;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.entities.tiles.ToxicField;
import toxiccleanup.builder.weather.Weather;
import toxiccleanup.builder.weather.WeatherSpawnPoint;
import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.core.headless.MockKeys;
import toxiccleanup.engine.core.headless.MockMouse;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.renderer.Renderable;
import toxiccleanup.engine.renderer.TileGrid;
import toxiccleanup.engine.util.MockEngineState;
import toxiccleanup.engine.util.MockGameState;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PumpTest {
    private final TileGrid tileGrid = new TileGrid(16, 800);
    private final MockMouse mockMouse = new MockMouse(2, 2, false, false, false);
    private final MockKeys mockKeys = new MockKeys(new ArrayList<>());
    private final MockEngineState
            baseEngineState = new MockEngineState(tileGrid, mockMouse, mockKeys);
    private final MockGameState baseGameState = new MockGameState();
    private final Position position = new Position(100, 100);
    private Pump pump;
    private ToxicField toxicField;

    @Before
    public void setup() {
        toxicField = new ToxicField(position);
        pump = new Pump(position, toxicField);
    }

    private MockGameState makeGameStateWithWeather(final Weather weather) {
        return new MockGameState() {
            @Override
            public Weather getWeather() {
                return weather;
            }
        };
    }

    private Weather makeWeather(boolean obscuring, Damage damage) {
        return new Weather() {
            @Override public void addSpawnPoint(WeatherSpawnPoint sp) {}
            @Override public void addWeather(GameEntity w) {}
            @Override public boolean isObscuring(Dimensions d, Positionable p) { return obscuring; }
            @Override public boolean isDamaging(Dimensions d, Positionable p) { return damage != null; }
            @Override public void applyLightningRod(Positionable p) {}
            @Override public Damage getDamage(Dimensions d, Positionable p) { return damage; }
            @Override public Damage getDamage() { return damage; }
            @Override public void tick(EngineState state, GameState game) {}
            @Override public List<Renderable> render() { return List.of(); }
        };
    }

    /**
     * Confirms a newly constructed Pump renders the 'default' sprite.
     */
    @Test
    public void initialSpriteIsOne() {
        final Sprite expected = SpriteGallery.pump.getSprite("1");
        assertEquals("initial Pump sprite should be '1'",
                expected.toString(), pump.getSprite().toString());
    }


    /**
     * Confirms the Pump shows the 'damaged' sprite when its damage handler is set.
     */
    @Test
    public void spriteIsDamagedWhenDamaged() {
        toxicField = new ToxicField(position);
        pump = new Pump(position, toxicField);

        GameState gameState = makeGameStateWithWeather(makeWeather(false, new Damage(position)));
        pump.tick(baseEngineState, gameState);
        final Sprite expected = SpriteGallery.pump.getSprite("damaged");
        assertEquals("Pump sprite should be 'damaged' when damage handler reports damage",
                expected.toString(), pump.getSprite().toString());
    }

    /**
     * Confirms pressing 'e' over a damaged Pump repairs it.
     */
    @Test
    public void repairWithEKeyWhenDamaged() {
        toxicField = new ToxicField(position);
        pump = new Pump(position, toxicField);
        GameState gameState = makeGameStateWithWeather(makeWeather(false, new Damage(position)));
        pump.tick(baseEngineState, gameState);
        ArrayList<Character> keys = new ArrayList<>();
        keys.add('e');
        MockEngineState eState = new MockEngineState(tileGrid, mockMouse, new MockKeys(keys));
        pump.playerOver(eState, baseGameState);
        Sprite not_expected = SpriteGallery.pump.getSprite("damaged");
        // Ensure animation timer has time to finish and reset sprite
        pump.tick(eState, baseGameState);
        pump.tick(eState, baseGameState);
        pump.tick(eState, baseGameState);
        pump.tick(eState, baseGameState);
        assertNotEquals("Pump should not show 'damaged' sprite after being repaired with 'e'",
                not_expected.toString(), pump.getSprite().toString());
    }

    /**
     * Confirms pressing 'e' on an undamaged Pump does not cause errors or change state.
     */
    @Test
    public void pressEKeyWhenNotDamagedDoesNothing() {
        toxicField = new ToxicField(position);
        pump = new Pump(position, toxicField);
        ArrayList<Character> keys = new ArrayList<>();
        keys.add('e');
        MockEngineState eState = new MockEngineState(tileGrid, mockMouse, new MockKeys(keys));
        pump.playerOver(eState, baseGameState);
        Sprite notExpected = SpriteGallery.pump.getSprite("damaged");
        pump.tick(eState, baseGameState);
        assertNotEquals("undamaged Pump should not show 'damaged' sprite after 'e' press",
                notExpected.toString(), pump.getSprite().toString());
    }

    @Test
    public void pumpDoesNotAnimateWhenNotEnoughPower() {
        toxicField = new ToxicField(position);
        pump = new Pump(position, toxicField);
        GameState gameState = new MockGameState();
        gameState.getMachines().setPower(pump.getPowerRequirement() - 1);
        pump.tick(baseEngineState, gameState);
        Sprite expected = SpriteGallery.pump.getSprite("1");
        assertEquals("Pump should not animate when power is insufficient", expected.toString(), pump.getSprite().toString());
    }

}
