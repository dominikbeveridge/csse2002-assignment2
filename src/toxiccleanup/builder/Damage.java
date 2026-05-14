package toxiccleanup.builder;

import toxiccleanup.engine.game.Positionable;

/**
 * Represents a unit of Damage at a certain position
 */
public class Damage implements Positionable {
    private int posX;
    private int posY;

    /**
     * Constructs a new Damage instance at the provided position
     * @param position the position of the Damage instance
     */
    public Damage(Positionable position) {
        this.posX = position.getX();
        this.posY = position.getY();
    }



    /**
     * Returns the horizontal (x-axis) coordinate of the component.
     *
     * @return The horizontal (x-axis) coordinate.
     * @ensures \result >= 0
     */
    @Override
    public int getX() {
        return posX;
    }

    /**
     * Returns the vertical (y-axis) coordinate of the component.
     *
     * @return The vertical (y-axis) coordinate.
     * @ensures \result >= 0
     */
    @Override
    public int getY() {
        return posY;
    }

    /**
     * Set the horizontal (x-axis) coordinate of the component.
     *
     * @param x The new horizontal coordinate for this component.
     * @requires x >= 0
     * @ensures getX() == x
     */
    @Override
    public void setX(int x) {
        this.posX = x;
    }

    /**
     * Set the vertical (y-axis) coordinate of the component.
     *
     * @param y The new vertical coordinate for this component.
     * @requires y >= 0
     * @ensures getY() == y
     */
    @Override
    public void setY(int y) {
        this.posY = y;
    }
}
