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

    public MovingObjects(int value) {
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

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
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

    abstract boolean move(int newx, int newy);

    /**
     *
     * @return true, if this object is in the visible window
     */
    public boolean inWindow() {
        return this.x >= level.getStartCol() && this.x <= (level.getStartCol() + level.getTerminal().getTerminalSize().getColumns())
                && this.y >= level.getStartRow() + level.getFirstRow() && this.y <= level.getStartRow() + level.getTerminal().getTerminalSize().getRows();
    }

    /**
     *
     * @return Returns a boolean[] Array <br>
     * [0] true, if the object can move to the right<br>
     * [1] true, if the object can move to the left<br>
     * [2] true, if the object can move up [3] true, if the object can move down
     */
    public boolean[] possibleDirections() {
        boolean[] result = new boolean[4];
        result[0] = isFieldMoveable(this.x + 1, this.y);
        result[1] = isFieldMoveable(this.x - 1, this.y);
        result[2] = isFieldMoveable(this.x, this.y - 1);
        result[3] = isFieldMoveable(this.x, this.y + 1);

        return result;

    }

}
