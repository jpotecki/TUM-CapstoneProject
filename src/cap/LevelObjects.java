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
public abstract class LevelObjects {

    private final Game game;
    private int x;
    private int y;

    public LevelObjects(Game game, int x, int y) {
        this.game = game;
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

}
