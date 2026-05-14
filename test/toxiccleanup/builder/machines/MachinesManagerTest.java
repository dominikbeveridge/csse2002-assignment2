package toxiccleanup.builder.machines;

import org.junit.Test;
import toxiccleanup.builder.entities.tiles.ToxicField;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.FixedTimer;
import toxiccleanup.engine.timing.TickTimer;

import static org.junit.Assert.*;

public class MachinesManagerTest {

    /**
     * Ensures the max power is 14
     */
    @Test
    public void maxPowerIs14() {
        Machines machines = new MachinesManager();
        assertEquals("The maximum power should be 14", 14, machines.getMaxPower());
    }
    /**
     * Ensures the power starts at the maximum
     */
    @Test
    public void powerStartsAtMaximum() {
        Machines machines = new MachinesManager();
        int expected = machines.getMaxPower();
        assertEquals("The maximum power should be equal to the starting power", expected, machines.getPower());
    }

    /**
     * Tests if MachinesManager setPower respects the minimum and maximum power constraints
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

    /**
     * Ensures that power cannot go above or below the maximum and 0.
     */
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

    /**
     * Ensures the power can be set correctly within the permissible range
     */
    @Test
    public void powerSetsToCorrectAmountWithinRange() {
        Machines machines = new MachinesManager();
        machines.setPower(machines.getMaxPower() - 1);
        int expected = machines.getMaxPower() - 1;
        assertEquals("Machine Managers power should be set to the correct amount", expected,
                machines.getPower());
    }

    /**
     *  Ensures that power adjusts correctly within the permissible range
     */
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

    /**
     * Ensures hasRequiredPower returns true in the correct situations
     */
    @Test
    public void hasRequiredPowerWhenValid() {
        int powerRequirement = 5;
        Machines machines = new MachinesManager();
        machines.setPower(powerRequirement);
        boolean expected = true;
        assertEquals("hasRequiredPower should return true when power is sufficient", expected,
                machines.hasRequiredPower(powerRequirement));
    }
    /**
     * Ensures hasRequiredPower returns false in the correct situations
     */
    @Test
    public void doesNotHaveRequiredPowerWhenInvalid() {
        int powerRequirement = 5;
        Machines machines = new MachinesManager();
        machines.setPower(powerRequirement - 1);
        boolean expected = false;
        assertEquals("hasRequiredPower should return false when power is sufficient", expected,
                machines.hasRequiredPower(powerRequirement));
    }

    /**
     * Ensures spawning a machine reduces the total power correctly
     */
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

    /**
     * Ensures machines do not spawn without enough power
     */
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

    /**
     * Ensures that if there is only one teleporter, it is returned
     */
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

    /**
     * Tests that the next teleporter excludes the current position
     */
    @Test
    public void getNextTeleporterPositionExcludesPositions() {
        TickTimer timer = new FixedTimer(1);
        Machines machines = new MachinesManager(timer);
        machines.setPower(machines.getMaxPower());
        Positionable position1 = new Position(0, 0);
        Positionable position2 = new Position(1, 1);
        timer.tick();
        machines.spawnTeleporter(position1);
        machines.spawnTeleporter(position2);
        assertEquals("getNextTeleporterPosition should exclude the excluded position", position2,
                machines.getNextTeleporterPosition(position1));
    }

    /**
     * Ensures that getting the next teleporter returns one of the available teleporters if there
     * are more than one
     */
    @Test
    public void getNextTeleporterDoesNotReturnNullIfTeleportersAvailable() {
        TickTimer timer = new FixedTimer(1);
        Machines machines = new MachinesManager(timer);
        machines.setPower(machines.getMaxPower());
        Positionable position1 = new Position(0, 0);
        Positionable position2 = new Position(1, 1);
        Positionable position3 = new Position(3, 3);
        timer.tick();
        machines.spawnTeleporter(position1);
        machines.spawnTeleporter(position2);
        Positionable nextTeleporter = machines.getNextTeleporterPosition(position3);
        assertTrue("getNextTeleporterPosition should return one of the available teleporters",
                (nextTeleporter.equals(position1) || nextTeleporter.equals(position2)) );

    }

    /**
     * Ensures that the excluded position is returned if there are no teleporters
     */
    @Test
    public void getNextTeleporterReturnsExcludedPositionForNoTeleporters() {
        TickTimer timer = new FixedTimer(1);
        Machines machines = new MachinesManager(timer);
        machines.setPower(machines.getMaxPower());
        Positionable position = new Position(0, 0);
        timer.tick();
        Positionable nextTeleporter = machines.getNextTeleporterPosition(position);
        assertEquals("getNextTeleporterPosition should return the excluded position if there are no teleporters",
               nextTeleporter, position  );

    }

}
