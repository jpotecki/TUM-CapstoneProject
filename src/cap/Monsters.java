/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import java.util.ArrayList;
import java.util.Random;

/**
 * Storage for all the Monsters in the game
 *
 * @author jp
 */
public class Monsters {

    private final Level level;
    private final ArrayList<Monster> monsters;

    public Monsters(Level level) {
        this.level = level;
        monsters = new ArrayList<>();
    }

    public boolean isEmpty() {
        return monsters.isEmpty();
    }

    public void add(int x, int y) {
        monsters.add(new Monster(x, y, level));
        level.setLevelValue(x, y, level.getMonsterNr());
    }

    public boolean move() {
        boolean tmp = true;
        for (Monster m : monsters) {
            if (!m.move()) {
                tmp = false;
            }
        }
        return tmp;
    }

    void createMonsters(int multiplicator) {

        int numberOfNewMonsters = (level.getCols() / multiplicator) + (level.getRows() / multiplicator);
        Random rand;
        int col, row;
        int maxCols = level.getCols();
        int maxRows = level.getRows();

        for (int i = 0; i < numberOfNewMonsters; i++) {

            do {
                rand = new Random();
                col = rand.nextInt(maxCols);
                rand = new Random();
                row = rand.nextInt(maxRows);

            } while (level.getLevelValue(col, row) != level.getUnoccupiedField());

            this.add(col, row);

        }

    }

}
