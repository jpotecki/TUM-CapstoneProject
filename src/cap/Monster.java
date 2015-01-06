/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

public class Monster extends MovingObjects {

    private long lastMove;
    private int movingInterval = 1000; // in millisec

    private boolean isAllowedToMove() {
        return System.currentTimeMillis() >= lastMove + movingInterval;
    }

    public void setMovingInterval(int movingInterval) {
        this.movingInterval = movingInterval;
    }

    public Monster(int x, int y, int value) {
        super(value);
    }

    @Override
    public boolean isFieldMoveable(int x, int y) {
        return level.arrayExists(x, y)
                && (level.getLevelValue(x, y) == level.getUnoccupiedField()
                || level.getLevelValue(x, y) == 7);
    }

    private int sign(int i) {
        if (i < 0) {
            return -1;

        } else if (i > 0) {
            return 1;

        } else {
            return 0;
        }

    }

    void move() {
        if (this.isAllowedToMove()) {

            if (this.x > level.getStartCol() && this.x < level.getStartCol() + level.getTerminal().getTerminalSize().getColumns()
                    && this.y > level.getStartRow() && this.y < level.getStartRow() + level.getTerminal().getTerminalSize().getRows()) {
                int xd = level.getPlayer().getX() - this.x;
                int yd = level.getPlayer().getY() - this.y;

                if (xd <= yd) {
                    if (this.isFieldMoveable(this.x, this.y + this.sign(yd))) {
                        this.move(this.x, this.y + this.sign(yd));
                    } else if (this.isFieldMoveable(this.x + sign(xd), this.y)) {
                        
                    }

                } else if (xd > yd && this.isFieldMoveable(this.x + this.sign(xd), this.y)) {
                    this.move(this.x + this.sign(xd), this.y);
                }

            }
        }
    }

    @Override
    void move(int newx, int newy) {

        if (this.isFieldMoveable(newx, newy)) {
            // safe the value of the field to be moved at newx,newy
            int newvalue = level.getLevelValue(newx, newy);

            // apply the the value to my current field and print it
            level.setLevelValue(this.x, this.y, newvalue);
            level.printChar(this.x, this.y);

            // apply my value to the field at newx, newy and print
            level.setLevelValue(newx, newy, this.value);
            level.printChar(newx, newy);

            // update my location
            this.x = newx;
            this.y = newy;

            // update the time I moved the last time
            this.lastMove = System.currentTimeMillis();
        }

    }

}
