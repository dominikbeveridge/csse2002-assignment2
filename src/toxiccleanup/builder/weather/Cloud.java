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
    final public static int MOVEMENT_TIME = 1;
    private static final SpriteGroup art = SpriteGallery.cloud;
    final private static int SPEED = 2;
    final private TickTimer timer;
    private final int maxFrames;
    private final TickTimer animTimer = new RepeatingTimer(12);
    private int currentArtFrame = 1;

    /**
     * Constructs a new Cloud at the specified position.
     *
     * @param position the position that the Cloud spawns on
     */
    public Cloud(Positionable position) {
        super(position);
        this.timer = new RepeatingTimer(Cloud.MOVEMENT_TIME);
        maxFrames = getArt().getSprites().size();
        updateArtFrame(currentArtFrame);
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
        this.animTimer.tick();

        if (this.animTimer.isFinished()) {
            currentArtFrame += 1;
        }
        updateArtFrame(currentArtFrame);
        this.timer.tick();
        if (this.timer.isFinished()) {
            final int movement = this.getX() - Cloud.SPEED;
            this.setX(movement);
            if (getX() < 0 || getX() > state.getDimensions().windowSize()) {
                this.markForRemoval();
            }
        }
    }

    public SpriteGroup getArt() {
        return art;
    }

    /**
     * Updates the current frame of the sprite while ensuring that the frame is within bounds
     * @param frame the current frame. Any values greater than the animation range are limited to the
     *              max number of frames in the sprite group
     */
    public void updateArtFrame(int frame) {

        frame = Integer.min(frame, maxFrames);
        setSprite(getArt().getSprite(String.valueOf(frame)));

    }
}
