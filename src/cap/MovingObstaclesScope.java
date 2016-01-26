/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import java.util.ArrayList;

/**
 *
 * @author jp
 */
public class MovingObstaclesScope {

    private final Level level;
    private final ArrayList<MO> m;

    public MovingObstaclesScope(Level level) {
        this.level = level;
        this.m = new ArrayList<>();
    }

    public void add(int x, int y) {
        m.add(new MO(x, y, level));
    }

    public boolean move() {
        boolean tmp = true;
        for (MO n : m) {
            if (!n.move()) {
                tmp = false;
            }
        }
        return tmp;
    }

    public void setTargets() {
        for (MO n : m) {
            n.setTarget();
        }
    }

}
