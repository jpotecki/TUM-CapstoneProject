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
    private final ArrayList<String> entryPoints = new ArrayList();
    private int[][] level;
    private int height;
    private int width;
    private final int unoccupiedField = 6;
    private final Player player;
    private int firstRow = 1;
    private int standardLives = 2;
    private int startCol = 0;
    private int startRow = 0;
    
    
    public int getUnoccupiedField() {
        return unoccupiedField;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        if (startCol > 0) {
            this.startCol = startCol;
        } else {
            this.startCol = 0;
        }

    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        if (startRow > 0) {
            this.startRow = startRow;
        } else {
            this.startRow = 0;
        }

    }
    

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Player getPlayer() {
        return player;
    }

    public Level(String dateiName) {
        player = new Player();
        this.level = createLevel(dateiName);
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
            return new int[]{255, 255, 255};
        } // wall
        else if (level[x][y] == 0) {
            return new int[]{100, 100, 100};
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
        } // player
        else if (level[x][y] == 7) {
            return new int[]{0, 0, 0};
        } else {
            return new int[]{255, 255, 255};
        }
    }

    public int[] getBGCharColor(int x, int y) {

        // return color for an unoccupied field
        if (level[x][y] == unoccupiedField) {
            return new int[]{230, 230, 230};
        } // wall
        else if (level[x][y] == 0) {
            return new int[]{100, 100, 100};
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
        }// player
        else if (level[x][y] == 7) {
            return new int[]{230, 230, 230};
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

                if (!key.equalsIgnoreCase("height") & !key.equalsIgnoreCase("width") & !key.equalsIgnoreCase("live")) {

                    tmpH = this.getXY(key)[0];
                    tmpW = this.getXY(key)[1];
                    // cp the value into the array
                    this.level[tmpH][tmpW] = value.charAt(0) - '0';

                    // check if value is an entrace
                    if (this.level[tmpH][tmpW] == 1) {
                        this.addEntryPoint(tmpH, tmpW);
                    }

                    // set live of player
                } else if (key.equalsIgnoreCase("live")) {
                    this.standardLives = value.charAt(0) - '0';
                }
            }
            // set playerposition to a entry point
            playerToRandomEntryPoint();
            player.setLive(standardLives);
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
        printChar(x - startCol, y - startRow, x, y);
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void printChar(int cursorX, int cursorY, int x, int y) {
        terminal.moveCursor(cursorX, cursorY + this.getFirstRow());
        terminal.applyBackgroundColor(
                this.getBGCharColor(x, y)[0],
                this.getBGCharColor(x, y)[1],
                this.getBGCharColor(x, y)[2]);
        terminal.applyForegroundColor(
                this.getCharColor(x, y)[0],
                this.getCharColor(x, y)[1],
                this.getCharColor(x, y)[2]);

        terminal.putCharacter(this.getValue(x, y));
    }

    public void printHeadInformations() {
        // draw the players lives and collected keys
        terminal.moveCursor(0, 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter('L');
        terminal.putCharacter('i');
        terminal.putCharacter('v');
        terminal.putCharacter('e');
        terminal.putCharacter('s');
        terminal.putCharacter(':');

        // print the lives
        this.printLives();

        terminal.putCharacter(' ');
        terminal.putCharacter('K');
        terminal.putCharacter('e');
        terminal.putCharacter('y');
        terminal.putCharacter('s');
        terminal.putCharacter(':');
        terminal.putCharacter(' ');

        // print the keys
        this.printKeys();
        terminal.putCharacter(' ');        terminal.putCharacter(' ');
        terminal.putCharacter('P');
        terminal.putCharacter('r');
        terminal.putCharacter('e');
        terminal.putCharacter('s');
        terminal.putCharacter('s');

        terminal.putCharacter(' ');

        terminal.putCharacter('E');
        terminal.putCharacter('S');
        terminal.putCharacter('C');
        terminal.putCharacter(' ');
        terminal.putCharacter('f');
        terminal.putCharacter('o');
        terminal.putCharacter('r');
        terminal.putCharacter(' ');
        terminal.putCharacter('M');
        terminal.putCharacter('e');
        terminal.putCharacter('n');
        terminal.putCharacter('u');

    }

    public void printLives() {
        terminal.moveCursor(7, 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter((char) (this.getPlayer().getLive() + '0'));
    }

    public void printKeys() {
        terminal.moveCursor(15, 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter((char) (player.getKeys() + '0'));
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
        // !missing : what to do, if no entry point is found
        // or has a free field next to the entry point

        boolean positionSet = false;
        while (!positionSet) {

            Random rand = new Random();
            int r = rand.nextInt(this.entryPoints.size());
            int tmpH = getXY(entryPoints.get(r))[0];
            int tmpW = getXY(entryPoints.get(r))[1];

            // check if surrounding fields are free and set Playerposition     
            if (this.isStartingPosition(tmpH + 1, tmpW)) {
                //this.playerPosition = new int[]{tmpH + 1, tmpW};
                //player = new Player(tmpH + 1, tmpW);
                //player.setLevel(this);
                player.setXY(tmpH + 1, tmpW);
                positionSet = true;
            } else if (this.isStartingPosition(tmpH, tmpW + 1)) {
                //this.playerPosition = new int[]{tmpH, tmpW + 1};
                //player = new Player(tmpH, tmpW + 1);
                //player.setLevel(this);
                player.setXY(tmpH, tmpW + 1);
                positionSet = true;

            } else if (this.isStartingPosition(tmpH - 1, tmpW)) {
                //this.playerPosition = new int[]{tmpH - 1, tmpW};
                //player = new Player(tmpH - 1, tmpW);
                //player.setLevel(this);
                player.setXY(tmpH - 1, tmpW);
                positionSet = true;

            } else if (this.isStartingPosition(tmpH, tmpW - 1)) {
                //this.playerPosition = new int[]{tmpH, tmpW - 1};
                //player = new Player(tmpH, tmpW - 1);
                //player.setLevel(this);
                player.setXY(tmpH, tmpW - 1);
                positionSet = true;

            }

        }
        // write the player position into the level array
        player.setLevel(this);
        this.level[player.getX()][this.player.getY()] = player.getValue();

    }

}
