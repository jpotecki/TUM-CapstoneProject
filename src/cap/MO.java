/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import java.util.Random;

/**
 *
 * @author jp
 */
public class MO extends MovingObjects {

    int targetX;
    int targetY;
    double lastMove;

    public MO(int x, int y, Level level) {
        super(4);
        this.setLevel(level);
        this.setXY(x, y);
        lastMove = System.currentTimeMillis();

    }

    @Override
    boolean isFieldMoveable(int x, int y) {
        return level.getLevelValue(x, y) == level.getUnoccupiedField() || level.getLevelValue(x, y) == 7
                && level.arrayExists(x, y);
    }

    private boolean isAllowedToMove() {
        return System.currentTimeMillis() >= lastMove + 1000;
    }

    boolean move() {
        if (this.isAllowedToMove() && this.inWindow()) {
            return move(0, 0);
        } else {
            return true;
        }
    }

    @Override
    boolean move(int newx, int newy) {
        newx = targetX;
        newy = targetY;
        boolean returnValue = true;
        
        if (this.isFieldMoveable(targetX, targetY)) {

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

            // apply my value to the field at newx, newy and print
            level.setLevelValue(newx, newy, this.value);

            // print both chars
            level.printChar(this.x, this.y);
            level.printChar(newx, newy);
            // safe my last location
            targetX = this.x;
            targetY = this.y;

            // update my location
            this.x = newx;
            this.y = newy;

            // update the time I moved the last time
            this.lastMove = System.currentTimeMillis();
        }
        return returnValue;
    }

    public void setTarget() {
        Random rand = new Random();
        int r = rand.nextInt(4);
        int counter = 0;
        boolean result[] = new boolean[4];

        result = possibleDirections();
        while (this.possibleDirections()[r] == false) {
            if (counter == 100) {
                break;
            }

            rand = new Random();
            r = rand.nextInt(4);
            counter++;
        }
        if (possibleDirections()[r] == true) {
            switch (r) {
                case 0:
                    targetX = this.x + 1;
                    targetY = this.y;
                    break;
                case 1:
                    targetX = this.x - 1;
                    targetY = this.y;
                    break;
                case 2:
                    targetX = this.x;
                    targetY = this.y - 1;
                    break;
                case 3:
                    targetX = this.x;
                    targetY = this.y + 1;
                    break;

            }

        } else {
            targetX = this.x;
            targetY = this.y;
        }
    }

}
