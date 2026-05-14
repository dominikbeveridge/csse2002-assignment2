package toxiccleanup.builder.machines;

import org.junit.Test;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.entities.tiles.Tile;
import toxiccleanup.builder.player.Player;
import toxiccleanup.builder.weather.Weather;
import toxiccleanup.builder.weather.WeatherSpawnPoint;
import toxiccleanup.builder.world.World;
import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.input.KeyState;
import toxiccleanup.engine.input.MouseState;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.renderer.Renderable;
import toxiccleanup.engine.renderer.TileGrid;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

public class PumpTest {

    public static final double testWeight = 5.0;

    private static final int ANIM_INTERVAL = 4;
    private static final int PUMP_INTERVAL = 100;

    private static final TileGrid TILE_GRID = new TileGrid(16, 800);
    private static final Position POSITION = new Position(100, 100);
    private static final EngineState BASE_STATE = makeEngineState(false);
    private static final EngineState E_KEY_STATE = makeEngineState(true);
    private TestAdjustable pumpTarget;
    private Pump pump;

    private static EngineState makeEngineState(boolean eKeyDown) {
        return new EngineState() {
            @Override
            public Dimensions getDimensions() {
                return TILE_GRID;
            }

            @Override
            public MouseState getMouse() {
                return new MouseState() {
                    @Override
                    public int getMouseX() {
                        return 0;
                    }

                    @Override
                    public int getMouseY() {
                        return 0;
                    }

                    @Override
                    public boolean isLeftPressed() {
                        return false;
                    }

                    @Override
                    public boolean isRightPressed() {
                        return false;
                    }

                    @Override
                    public boolean isMiddlePressed() {
                        return false;
                    }
                };
            }

            @Override
            public KeyState getKeys() {
                return new KeyState() {
                    @Override
                    public List<Character> getDown() {
                        return eKeyDown ? List.of('e') : List.of();
                    }

                    @Override
                    public boolean isDown(char c) {
                        return eKeyDown && c == 'e';
                    }
                };
            }

            @Override
            public int currentTick() {
                return 0;
            }
        };
    }

    private static GameState makeGameState(int power, Damage damage) {
        Machines machines = new MachinesManager(power);
        return new GameState() {
            @Override
            public World getWorld() {
                return new World() {
                    @Override
                    public List<Tile> tilesAtPosition(Positionable p, Dimensions d) {
                        return List.of();
                    }

                    @Override
                    public List<Tile> allTiles() {
                        return List.of();
                    }

                    @Override
                    public void place(Tile tile) {
                    }
                };
            }

            @Override
            public Player getPlayer() {
                return new Player() {
                    @Override
                    public Positionable getPosition() {
                        return new Position(0, 0);
                    }

                    @Override
                    public void setPosition(Positionable p) {
                    }

                    @Override
                    public int getHp() {
                        return 5;
                    }

                    @Override
                    public int getMaxHp() {
                        return 10;
                    }

                    @Override
                    public void adjust(int amount) {
                    }

                    @Override
                    public void tick(EngineState s, GameState g) {
                    }

                    @Override
                    public List<Renderable> render() {
                        return List.of();
                    }
                };
            }

            @Override
            public Machines getMachines() {
                return machines;
            }

            @Override
            public Weather getWeather() {
                return new Weather() {
                    @Override
                    public void addSpawnPoint(WeatherSpawnPoint sp) {
                    }

                    @Override
                    public void addWeather(GameEntity w) {
                    }

                    @Override
                    public boolean isObscuring(Dimensions d, Positionable p) {
                        return false;
                    }

                    @Override
                    public boolean isDamaging(Dimensions d, Positionable p) {
                        return damage != null;
                    }

                    @Override
                    public void applyLightningRod(Positionable p) {
                    }

                    @Override
                    public Damage getDamage(Dimensions d, Positionable p) {
                        return damage;
                    }

                    @Override
                    public Damage getDamage() {
                        return damage;
                    }

                    @Override
                    public void tick(EngineState s, GameState g) {
                    }

                    @Override
                    public List<Renderable> render() {
                        return List.of();
                    }
                };
            }
        };
    }

    @Before
    public void setUp() {
        pumpTarget = new TestAdjustable();
        pump = new Pump(POSITION, pumpTarget);
    }

    /**
     * Confirms the pump pumps every 100 ticks
     */
    @Test
    public void pumpsOnlyEvery100Ticks() {
        GameState gameState = makeGameState(pump.getPowerRequirement(), null);

        for (int i = 1; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, gameState);
            String message = String.format("Pump should not have pumped yet since only %d ticks have passed", i);
            assertFalse(message,
                    pumpTarget.adjustCalled());
        }

        pump.tick(BASE_STATE, gameState);
        assertTrue("Pump should have pumped after 100 ticks", pumpTarget.adjustCalled());
    }

    /**
     * Ensures the animation loops every 4 ticks
     */
    @Test
    public void animatesEvery4Ticks() {
        GameState gameState = makeGameState(pump.getPowerRequirement(), null);

        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);

        String not_expected = "1";
        assertNotEquals("Pump should have changed sprite after 4 ticks", not_expected, pump.getSprite().toString());
    }

    /**
     * Ensures the power requirement equals 2
     */
    @Test
    public void powerRequirementEqualsTwo() {
        assertEquals("Power requirement should be 2", 2, pump.getPowerRequirement());
    }

    /**
     * Confirms a newly constructed Pump renders the 'default' sprite.
     */
    @Test
    public void initialSpriteIsOne() {
        final Sprite expected = SpriteGallery.pump.getSprite("1");
        assertEquals("initial Pump sprite should be '1'", expected.toString(),
                pump.getSprite().toString());
    }

    /**
     * Confirms the Pump shows the 'damaged' sprite when its damage handler is set.
     */
    @Test
    public void spriteIsDamagedWhenDamaged() {
        Damage damage = new Damage(POSITION);
        GameState gameState = makeGameState(0, damage);
        pump.tick(BASE_STATE, gameState);
        final Sprite expected = SpriteGallery.pump.getSprite("damaged");
        assertEquals("Pump sprite should be 'damaged' when damage handler reports damage",
                expected.toString(), pump.getSprite().toString());
    }

    /**
     * Confirms pressing 'e' over a damaged Pump repairs it.
     */
    @Test
    public void repairWithEKeyWhenDamaged() {
        Damage damage = new Damage(POSITION);
        GameState gameState = makeGameState(pump.getPowerRequirement(), damage);
        pump.tick(BASE_STATE, gameState);
        pump.playerOver(E_KEY_STATE, gameState);
        // Ensure animation timer has time to finish and reset sprite
        gameState = makeGameState(pump.getPowerRequirement(), null);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);


        Sprite not_expected = SpriteGallery.pump.getSprite("damaged");
        assertNotEquals("Pump should not show 'damaged' sprite after being repaired with 'e'",
                not_expected.toString(), pump.getSprite().toString());
    }

    /**
     * Confirms pressing 'e' on an undamaged Pump does not cause errors or change state.
     */
    @Test
    public void pressEKeyWhenNotDamagedDoesNothing() {
        GameState gameState = makeGameState(0, null);
        pump.playerOver(E_KEY_STATE, gameState);
        Sprite notExpected = SpriteGallery.pump.getSprite("damaged");
        pump.tick(E_KEY_STATE, gameState);
        assertNotEquals("undamaged Pump should not show 'damaged' sprite after 'e' press",
                notExpected.toString(), pump.getSprite().toString());
    }

    /**
     * Confirms that the pump does not animate when there is not enough power
     */
    @Test
    public void pumpDoesNotAnimateWhenNotEnoughPower() {
        GameState gameState = makeGameState(pump.getPowerRequirement() - 1, null);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        pump.tick(BASE_STATE, gameState);
        Sprite expected = SpriteGallery.pump.getSprite("1");
        assertEquals("Pump should not animate when power is insufficient", expected.toString(),
                pump.getSprite().toString());
    }

    /**
     * Ensures pump adjusts the target when powered
     */
    @Test
    public void pumpAdjustsTargetWhenPowered() {
        GameState gameState = makeGameState(pump.getPowerRequirement(), null);
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, gameState);
        }
        assertTrue("Pump should adjust the target when power is sufficient",
                pumpTarget.adjustCalled());
    }

    /**
     * Ensures pump does not adjust the target when powered
     */
    @Test
    public void pumpDoesNotAdjustTargetWhenPowered() {
        GameState gameState = makeGameState(pump.getPowerRequirement() - 1, null);
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, gameState);
        }
        assertFalse("Pump should not adjust the target when power is sufficient",
                pumpTarget.adjustCalled());
    }

    /**
     * Ensures pump adjusts the target by the correct amount
     */
    @Test
    public void pumpReducesTargetByOne() {
        GameState gameState = makeGameState(pump.getPowerRequirement(), null);
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, gameState);
        }
        int expected = 1;
        assertEquals("Pump should adjust the target by one", expected, pumpTarget.getLastAmount());
    }

    private static class TestAdjustable implements Adjustable {
        private int callCount = 0;
        private int lastAmount = 0;

        @Override
        public void adjust(int amount) {
            callCount++;
            lastAmount = amount;
        }

        public boolean adjustCalled() {
            return callCount > 0;
        }

        public int getLastAmount() {
            return lastAmount;
        }
    }
}