package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.SpriteGroup;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.RepeatingTimer;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;

/**
 * <p> A {@link Cloud} is a weather phenomena that will move to the left, over time. </p>
 * <p> It obscures {@link toxiccleanup.builder.machines.SolarPanel}s that
 * it is sharing a tile with. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 *
 * <p> Rendered using {@link SpriteGallery#cloud}. </p>
 *
 * @provided
 */
public class Cloud extends GameEntity implements Obscuring {
    final public static int SPAWN_TIME = 300;
    final private static int MOVEMENT_TIME = 1;
    private static final SpriteGroup art = SpriteGallery.cloud;
    final private static int SPEED = 2;
    final private TickTimer movementTimer;
    private final TickTimer animTimer = new RepeatingTimer(12);
    private final Animation animation;
    /**
     * Constructs a new Cloud at the specified position with the default animation.
     *
     * @param position the position that the Cloud spawns on
     */
    public Cloud(Positionable position, Animation animation) {
        super(position);
        this.animation = animation;
        this.movementTimer = new RepeatingTimer(Cloud.MOVEMENT_TIME);
        setSprite(this.animation.getCurrentSprite());
    }


    /**
     * Constructs a new Cloud at the specified position with the default animation.
     *
     * @param position the position that the Cloud spawns on
     */
    public Cloud(Positionable position) {
        this(position, new SingleAnimation(art));
    }

    /**
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
        animTimer.tick();

        if (animTimer.isFinished()) {
            this.animation.animate();
            setSprite(this.animation.getCurrentSprite());
        }
        this.movementTimer.tick();
        if (this.movementTimer.isFinished()) {
            final int movement = this.getX() - Cloud.SPEED;
            this.setX(movement);
            if (getX() < 0 || getX() > state.getDimensions().windowSize()) {
                this.markForRemoval();
            }
        }
    }

}
