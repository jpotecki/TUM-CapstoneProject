/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

public class Monster extends MovingObjects {

    private long lastMove;
    private int movingInterval = 250; // in millisec
    private int lastX = 0;
    private int lastY = 0;

    private boolean isAllowedToMove() {
        return System.currentTimeMillis() >= lastMove + movingInterval;
    }

    public void setMovingInterval(int movingInterval) {
        this.movingInterval = movingInterval;
    }

    public Monster(int x, int y, Level level) {
        super(level.getMonsterNr());
        super.setXY(x, y);
        super.setLevel(level);
        this.lastMove = System.currentTimeMillis();
    }

    @Override
    public boolean isFieldMoveable(int x, int y) {
        return level.arrayExists(x, y)
             //   && (lastX != x && lastY != y)
                && (level.getLevelValue(x, y) == level.getUnoccupiedField()
                && (!level.isSecureStartingArea(x, y))
                || (level.getLevelValue(x, y) == 7 && !level.isSecureStartingArea(x, y)));
        // deny the area around the starting point
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

    boolean move() { 
        if (this.isAllowedToMove()) {

            if (this.x >= level.getStartCol() && this.x <= (level.getStartCol() + level.getTerminal().getTerminalSize().getColumns())
                    && this.y >= level.getStartRow() && this.y <= level.getStartRow() + level.getTerminal().getTerminalSize().getRows()) {

                int xd = level.getPlayer().getX() - this.x;
                int yd = level.getPlayer().getY() - this.y;

                if (sign(xd) == 0) { // both have the same x coordinate
                    if (this.isFieldMoveable(this.x, this.y + sign(yd))) { //try to move in the position direction
                        return this.move(this.x, this.y + sign(yd));
                    } else if (this.isFieldMoveable(this.x + 1, this.y)) {
                        return this.move(this.x + 1, this.y);
                    } else if (this.isFieldMoveable(this.x - 1, this.y)) {
                        return this.move(this.x - 1, this.y);
                    }
                } else if (sign(yd) == 0) { // both have the same y coordinate
                    if (this.isFieldMoveable(this.x + sign(xd), this.y)) { //try to move in the position direction
                        return this.move(this.x + sign(xd), this.y);
                    } else if (this.isFieldMoveable(this.x, this.y + 1)) {
                        return this.move(this.x, this.y + 1);

                    } else if (this.isFieldMoveable(this.x, this.y  -1)) {
                        return move(this.x, this.y -1);

                    }
                } else if (Math.abs(xd) > Math.abs(yd)) { // y distance is smaller than x distance, so try to move in the y direction
                    if (this.isFieldMoveable(this.x, this.y + sign(yd))) {
                        return this.move(this.x, this.y + sign(yd));

                    } else if (this.isFieldMoveable(this.x, this.y + sign(yd) * -1)) {
                        return move(this.x, this.y + sign(yd) * -1);

                    } else if (this.isFieldMoveable(this.x + sign(xd), this.y)) {
                        return this.move(this.x + sign(xd), this.y);
                    } else if (this.isFieldMoveable(this.x + (sign(xd) * -1), this.y)) {
                        return this.move(this.x + (sign(xd) * -1), this.y);
                    }
                } else if (Math.abs(xd) <= Math.abs(yd)) { // try to move in x direction
                    if (this.isFieldMoveable(this.x + sign(xd), this.y)) {
                        return this.move(this.x + sign(xd), this.y);

                    } else if (this.isFieldMoveable(this.x + (sign(xd) * -1), this.y)) {
                        return this.move(this.x + (sign(xd) * -1), this.y);
                    } else if (this.isFieldMoveable(this.x, this.y + sign(yd))) {
                        return this.move(this.x, this.y + sign(yd));

                    } else if (this.isFieldMoveable(this.x, this.y + sign(yd) * -1)) {
                        return move(this.x, this.y + sign(yd) * -1);

                    }

                }
            }
        }
        return true;
    }

    @Override
    boolean move(int newx, int newy) {

        boolean returnValue = true;

        if (this.isFieldMoveable(newx, newy)) {
            // safe the value of the field to be moved at newx,newy
            int newvalue = level.getLevelValue(newx, newy);

            if (newvalue == 7) {
                returnValue = false;
            }
            // apply the the value to my current field and print it

            if (returnValue == false) {
                level.setLevelValue(this.x, this.y, level.getUnoccupiedField());
            } else {
                level.setLevelValue(this.x, this.y, newvalue);
            }
            level.printChar(this.x, this.y);

            // apply my value to the field at newx, newy and print
            level.setLevelValue(newx, newy, this.value);
            level.printChar(newx, newy);

            // safe my last location
            lastX = this.x;
            lastY = this.y;

            // update my location
            this.x = newx;
            this.y = newy;

            // update the time I moved the last time
            this.lastMove = System.currentTimeMillis();

        }
        return returnValue;
    }

}
