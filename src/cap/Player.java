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
    
    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public Player(int x, int y) {
        super(x, y, 7);
    }

    @Override
    boolean isFieldMoveable(int x, int y) {
        return level.arrayExists(x, y)
                && level.getLevelValue(x, y) != 0;
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
            super.move(x1, y1);
        }
    }

}


