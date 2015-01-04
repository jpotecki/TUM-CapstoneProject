/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.input.Key;

/**
 *
 * @author jp
 */
public class Player extends MovingObjects {

    private int live;
    private int keys = 0;

    public int getKeys() {
        return keys;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public Player() {
        super(7);
    }

    @Override
    boolean isFieldMoveable(int x, int y) {
        return level.arrayExists(x, y)
                && level.getLevelValue(x, y) != 0
                && level.getLevelValue(x, y) != 1;
    }

    public void movePlayer(Key key) {
        int x1 = this.x;
        int y1 = this.y;

        if (key.getKind() == Key.Kind.ArrowDown) {
            y1++;
        } else if (key.getKind() == Key.Kind.ArrowLeft) {
            x1--;
        } else if (key.getKind() == Key.Kind.ArrowRight) {
            x1++;
        } else if (key.getKind() == Key.Kind.ArrowUp) {
            y1--;
        }
        if (this.isFieldMoveable(x1, y1)) {
            move(x1, y1);
        }
    }

    @Override
    void move(int newx, int newy) {

        //! MISSING: just flip the fields if field is free
        if (this.isFieldMoveable(newx, newy)) {
            if (level.arrayExists(newx, newy)
                    && level.arrayExists(this.x, this.y)) {

                // safe the value of the field to be moved at newx,newy
                int newvalue = level.getLevelValue(newx, newy);

                // check if field is a key
                if (newvalue == 5) {
                    newvalue = level.getUnoccupiedField();
                    this.keys++;
                    level.printKeys();
                }

                // apply the the value to my current field and print it
                level.setLevelValue(this.x, this.y, newvalue);
                level.printChar(this.x, this.y);

                // apply my value to the field at newx, newy and print
                level.setLevelValue(newx, newy, this.value);
                level.printChar(newx, newy);

                // update my location
                this.x = newx;
                this.y = newy;
            }
        }

    }

}
