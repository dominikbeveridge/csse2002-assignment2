package toxiccleanup.builder.machines;

import org.junit.Test;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.Tickable;
import toxiccleanup.builder.ToxicCleanup;
import toxiccleanup.builder.ToxicCleanupGameState;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.entities.tiles.ToxicField;
import toxiccleanup.builder.util.MockMachines;
import toxiccleanup.builder.weather.Lightning;
import toxiccleanup.engine.core.headless.MockKeys;
import toxiccleanup.engine.core.headless.MockMouse;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.TileGrid;
import toxiccleanup.engine.timing.FixedTimer;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.engine.util.MockEngineState;
import toxiccleanup.engine.util.MockGameState;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MachinesManagerTest {
    private final  TileGrid tileGrid = new TileGrid(16, 800);
    private final MockMouse mockMouse = new MockMouse(2, 2, false, false, false);
    private final MockKeys mockKeys = new MockKeys(new ArrayList<>());
    private final  MockEngineState baseEngineState = new MockEngineState(tileGrid, mockMouse, mockKeys);
    private final  MockGameState baseGameState = new MockGameState();


    /**
     * Tests if MachinesManager respects the minimum and maximum power constraints
     */
    @Test
    public void setPowerIsGreaterThanZeroAndLessThanMaximum() {
        Machines machines = new MachinesManager();
        machines.setPower(machines.getMaxPower() + 1);
        int expected = machines.getMaxPower();
        assertEquals("Machine Managers power should not exceed its maximum", expected,
                machines.getPower());
        machines.setPower(-1);
        expected = 0;
        assertEquals("Machine Manager power should not be less than zero", expected,
                machines.getPower());
    }

    @Test
    public void adjustedPowerIsGreaterThanZeroAndLessThanMaximum() {
        Machines machines = new MachinesManager();
        machines.adjust(machines.getMaxPower() + 1);
        int expected = machines.getMaxPower();
        assertEquals("Machine Managers power should not exceed its maximum", expected,
                machines.getPower());
        machines.adjust(-(machines.getMaxPower() + 1));
        expected = 0;
        assertEquals("Machine Manager power should not be less than zero", expected,
                machines.getPower());
    }

    @Test
    public void powerSetsToCorrectAmountWithinRange() {
        Machines machines = new MachinesManager();
        machines.setPower(machines.getMaxPower() - 1);
        int expected = machines.getMaxPower() - 1;
        assertEquals("Machine Managers power should be set to the correct amount", expected,
                machines.getPower());
    }

    @Test
    public void powerAdjustsByCorrectAmountWithinRange() {
        Machines machines = new MachinesManager();
        machines.adjust(-1);
        int expected = machines.getMaxPower() - 1;
        assertEquals("Machine Managers power should decrease correctly within range", expected,
                machines.getPower());
        machines.adjust(1);
        expected = machines.getMaxPower();
        assertEquals("Machine Managers power should increase correctly within range", expected,
                machines.getPower());
    }

    @Test
    public void hasRequiredPowerWhenValid() {
        int powerRequirement = 5;
        Machines machines = new MachinesManager();
        machines.setPower(powerRequirement);
        boolean expected = true;
        assertEquals("hasRequiredpower should return true when power is sufficient", expected,
                machines.hasRequiredPower(powerRequirement));
    }

    @Test
    public void doesNotHaveRequiredPowerWhenInvalid() {
        int powerRequirement = 5;
        Machines machines = new MachinesManager();
        machines.setPower(powerRequirement - 1);
        boolean expected = false;
        assertEquals("hasRequiredpower should return false when power is sufficient", expected,
                machines.hasRequiredPower(powerRequirement));
    }

    @Test
    public void spawnedMachinesReducePower() {
        Machines machines = new MachinesManager();
        Positionable position = new Position(0, 0);
        machines.setPower(machines.getMaxPower());
        machines.spawnSolarPanel(position);
        int expected = machines.getMaxPower() - SolarPanel.COST;
        assertEquals("Spawning a solar panel should reduce power by the correct amount", expected,
                machines.getPower());

        machines.setPower(machines.getMaxPower());
        machines.spawnLightningRod(position);
        expected = machines.getMaxPower() - LightningRod.COST;
        assertEquals("Spawning a lightning rod should reduce power by the correct amount", expected,
                machines.getPower());

        machines.setPower(machines.getMaxPower());
        machines.spawnTeleporter(position);
        expected = machines.getMaxPower() - Teleporter.COST;
        assertEquals("Spawning a teleporter should reduce power by the correct amount", expected,
                machines.getPower());

        machines.setPower(machines.getMaxPower());
        ToxicField toxicField = new ToxicField(position);
        machines.spawnPump(position, toxicField);
        expected = machines.getMaxPower() - Pump.COST;
        assertEquals("Spawning a pump should reduce power by the correct amount", expected,
                machines.getPower());
    }

    @Test
    public void machinesDoNotSpawnIfInsufficientPower() {

        Machines machines = new MachinesManager();
        Positionable position = new Position(0, 0);


        machines.setPower(SolarPanel.COST - 1);
        assertNull("Solar panel should not spawn if insufficient power",
                machines.spawnSolarPanel(position));

        machines.setPower(LightningRod.COST - 1);
        assertNull("Lightning rod should not spawn if insufficient power",
                machines.spawnLightningRod(position));

        machines.setPower(Teleporter.COST - 1);
        assertNull("Teleporter should not spawn if insufficient power",
                machines.spawnTeleporter(position));

        machines.setPower(Pump.COST - 1);
        ToxicField toxicField = new ToxicField(position);
        assertNull("Pump should not spawn if insufficient power",
                machines.spawnPump(position, toxicField));

    }

    @Test
    public void getNextTeleporterPositionReturnsOnlyTeleporter() {

        Machines machines = new MachinesManager();
        Positionable position = new Position(0, 0);
        Teleporter teleporter = machines.spawnTeleporter(position);
        Positionable expected = teleporter.getPosition();

        assertEquals(
                "getNextTeleporterPosition should return the only teleporter if there is only one",
                expected, machines.getNextTeleporterPosition(position));

    }

    @Test
    public void getNextTeleporterPositionExcludesPositions() {
        TickTimer timer = new FixedTimer(1);
        Machines machines = new MachinesManager(timer);
        machines.setPower(machines.getMaxPower());
        Positionable position1 = new Position(0, 0);
        Positionable position2 = new Position(1, 1);
        Teleporter teleporter1 = machines.spawnTeleporter(position1);
        Teleporter teleporter2 = machines.spawnTeleporter(position2);
        Positionable expected = teleporter2.getPosition();
        machines.tick(baseEngineState, baseGameState);
        machines.tick(baseEngineState, baseGameState);
        assertEquals("getNextTeleporterPosition should exclude the excluded position", expected,
                machines.getNextTeleporterPosition(position1));
    }

    @Test
    public void getNextTeleporterPositionWaitsForCooldown() {
        TickTimer tickTimer = new FixedTimer(1);
        Machines machines = new MachinesManager(tickTimer);
        machines.setPower(machines.getMaxPower());
        Positionable position1 = new Position(0, 0);
        Positionable position2 = new Position(1, 1);
        Positionable excludedPosition = new Position(2, 2);
        machines.spawnTeleporter(position1);
        machines.spawnTeleporter(position2);
        assertEquals("getNextTeleporterPosition should wait for the cooldown before returning", excludedPosition, machines.getNextTeleporterPosition(excludedPosition));

        machines.tick(baseEngineState, baseGameState);
        machines.tick(baseEngineState, baseGameState);

        assertEquals("getNextTeleporterPosition should work on the cooldown expiry", position1, machines.getNextTeleporterPosition(position2));


    }


}
