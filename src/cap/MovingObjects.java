/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

/**
 *
 * @author jp
 */
public abstract class MovingObjects {

    int x;
    int y;
    int value;
    Level level;

    public void setLevel(Level level) {
        this.level = level;
    }

    public MovingObjects(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    /**
     *
     * @param x
     * @param y
     * @return returns true, if the MovingObject can go to the field at x,y
     */
    abstract boolean isFieldMoveable(int x, int y);

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    abstract void move(int newx, int newy);
}
