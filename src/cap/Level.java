/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

/**
 * Class to build the level
 *
 * @author jp
 */
public class Level {

    private Terminal terminal;
    private ArrayList<String> entryPoints = new ArrayList();
    private int[][] level;
    private int height;
    private int width;
    private int keys = 0;
    private final int unoccupiedField = 6;
    private Player player;
    private int firstCol;
    private int firstRow;

    public void setFirstCol(int firstCol) {
        this.firstCol = firstCol;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Player getPlayer() {
        return player;
    }

    public Level(String dateiName) {
        this.level = createLevel(dateiName);
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getCols() {
        return width;
    }

    public int getRows() {
        return height;
    }

    public char getValue(int x, int y) {
        // returns the value at the position   
        if (level[x][y] == unoccupiedField) {
            return 32;
        } else {
            return (char) ('0' + level[x][y]);
        }
    }

    public void setLevelValue(int x, int y, int value) {
        if (this.arrayExists(x, y)) {
            this.level[x][y] = value;
        }
    }

    public int getLevelValue(int x, int y) {
        if (this.arrayExists(x, y)) {
            return level[x][y];
        } else {
            return -1;
        }
    }

    public int[] getCharColor(int x, int y) {

        // return color for an unoccupied field
        if (level[x][y] == unoccupiedField) {
            return new int[]{0, 0, 0};
        } // wall
        else if (level[x][y] == 0) {
            return new int[]{175, 175, 175};
        } // entrace
        else if (level[x][y] == 1) {
            return new int[]{0, 0, 128};
        } // exit
        else if (level[x][y] == 2) {
            return new int[]{34, 139, 34};
        } // trap
        else if (level[x][y] == 3) {
            return new int[]{210, 105, 30};
        } // enemy
        else if (level[x][y] == 4) {
            return new int[]{255, 64, 64};
        } // key
        else if (level[x][y] == 5) {
            return new int[]{255, 215, 0};
        } else {
            return new int[]{255, 255, 255};
        }
    }

    public boolean arrayExists(int x, int y) {
        return this.arrayExists(level, x, y);
    }

    public void addEntryPoint(int x, int y) {
        entryPoints.add(x + "," + y);
    }

    public int[] getXY(String key) {
        int tmpH, tmpW, middle;
        // find the position of the seperator for the x,y coordinates
        middle = key.indexOf(',');

        // cp numbers bevor the middle char
        tmpH = Integer.parseInt(key.substring(0, middle));

        // cp numbers after the middle char
        tmpW = Integer.parseInt(key.substring(middle + 1, key.length()));

        return new int[]{tmpH, tmpW};
    }

    public boolean arrayExists(int[][] array, int x, int y) {
        try {
            return array[x][y] != 3231;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private int[][] createLevel(String dateiName) {

        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("src/cap/" + dateiName);

            // load a properties file
            prop.load(input);

            // get width and heigth of the level and create an array 
            this.width = Integer.parseInt(prop.getProperty("Width"));
            this.height = Integer.parseInt(prop.getProperty("Height"));

            level = new int[width][height];

            // fill array with unoccupiedField => the number for free fields
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    level[i][j] = unoccupiedField;
                }
            }

            // get the array positions and fill the values into the array
            int tmpH, tmpW;
            for (String key : prop.stringPropertyNames()) {

                String value = prop.getProperty(key);

                if (!key.equalsIgnoreCase("height") & !key.equalsIgnoreCase("width")) {

                    tmpH = this.getXY(key)[0];
                    tmpW = this.getXY(key)[1];
                    // cp the value into the array
                    this.level[tmpH][tmpW] = value.charAt(0) - '0';

                    // !missing: count all entraces and after that position player by chance
                    // check if value is an entrace
                    if (this.level[tmpH][tmpW] == 1) {
                        this.addEntryPoint(tmpH, tmpW);

                    }

                }
            }
            // set playerposition to a entry point
            playerToRandomEntryPoint();

            return level;

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void printChar(int x, int y) {
        printChar(firstCol + x, firstRow + y, x, y);
    }

    public int getFirstCol() {
        return firstCol;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void printChar(int cursorX, int cursorY, int x, int y) {
        terminal.moveCursor(cursorX, cursorY);
        terminal.applyForegroundColor(
                this.getCharColor(x, y)[0],
                this.getCharColor(x, y)[1],
                this.getCharColor(x, y)[2]);
        terminal.putCharacter(this.getValue(x, y));
    }

    public boolean playerSurvives(int x, int y) {
        return this.arrayExists(x, y)
                && this.level[x][y] != 3
                && this.level[x][y] != 4;
    }

    private boolean isStartingPosition(int x, int y) {
        return this.arrayExists(x, y)
                && this.level[x][y] == this.unoccupiedField;

    }

    private void playerToRandomEntryPoint() {
        boolean positionSet = false;
        while (!positionSet) {

            Random rand = new Random();
            int r = rand.nextInt(this.entryPoints.size());
            int tmpH = getXY(entryPoints.get(r))[0];
            int tmpW = getXY(entryPoints.get(r))[1];

            // check if surrounding fields are free and set Playerposition     
            if (this.isStartingPosition(tmpH + 1, tmpW)) {
                //this.playerPosition = new int[]{tmpH + 1, tmpW};
                player = new Player(tmpH + 1, tmpW);
                player.setLevel(this);
                positionSet = true;
            } else if (this.isStartingPosition(tmpH, tmpW + 1)) {
                //this.playerPosition = new int[]{tmpH, tmpW + 1};
                player = new Player(tmpH, tmpW + 1);
                player.setLevel(this);
                positionSet = true;

            } else if (this.isStartingPosition(tmpH - 1, tmpW)) {
                //this.playerPosition = new int[]{tmpH - 1, tmpW};
                player = new Player(tmpH - 1, tmpW);
                player.setLevel(this);
                positionSet = true;

            } else if (this.isStartingPosition(tmpH, tmpW - 1)) {
                //this.playerPosition = new int[]{tmpH, tmpW - 1};
                player = new Player(tmpH, tmpW - 1);
                player.setLevel(this);
                positionSet = true;

            }

        }
        // write the player position into the level array
        this.level[player.getX()][this.player.getY()] = player.getValue();

    }

}
