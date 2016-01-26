/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.gui.GUIScreen;
import static com.googlecode.lanterna.gui.GUIScreen.Position.CENTER;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.ResizeListener;
import com.googlecode.lanterna.terminal.TerminalSize;
import java.util.logging.Logger;

/**
 *
 * @author jp
 */
public class Game {

    private boolean wannaPlay;    // wannaPlay = false kills the game
    private boolean changedSize = false;
    private Level level;
    boolean gameMenu = true;
    private final Terminal terminal;

    private final GUIScreen gui;

    public Game(GUIScreen gui) {

        this.gui = gui;
        this.terminal = gui.getScreen().getTerminal();
        wannaPlay = true;
    }

    public Level getLevel() {
        return level;
    }

    public void setMap(String dateiname) {
        this.level = new Level(dateiname);
        level.setTerminal(gui.getScreen().getTerminal());
    }

    public void playGame() {

        // close the menu screen
        gui.getScreen().stopScreen();

        // clear the screen and enter private mode
        terminal.clearScreen();
        terminal.enterPrivateMode();
        //   terminal.applyBackgroundColor(255,255,255);

        // Var for user input 
        Key key;

        // Ingame Menu
        SubMainMenu submenu = new SubMainMenu(gui, this);

        Terminal.ResizeListener resize = new ResizeListener() {

            @Override
            public void onResized(TerminalSize newSize) {

                drawField(true);
            }
        };
        terminal.addResizeListener(resize);
        // loops until user wants to quit playing

        drawField();
        while (wannaPlay) {

            key = readKey(terminal);

            if (level.getPlayer().movePlayer(key) == false) {
                abbruch();
            }

            if (level.getPlayer().isWon()) {
                this.won();
            }

            if (checkIfRedrawIsNecessary(level.getPlayer())) {
                drawField(false);
            }

            if (key != null && key.getKind() == Key.Kind.Escape) {

                // exit the terminalmode
                terminal.exitPrivateMode();

                // show the GUIScreen and the ingame menu
                gui.getScreen().startScreen();
                gui.showWindow(submenu, CENTER);
                if (gameMenu) {
                    gui.getScreen().stopScreen();
                    gui.getScreen().getTerminal().clearScreen();
                    gui.getScreen().getTerminal().enterPrivateMode();
                    drawField(false);
                }
            }
        }

        gui.getScreen().getTerminal().exitPrivateMode();
        gui.getScreen().startScreen();
    }

    public void close() {
        this.gameMenu = false;
    }

    public void closeGame() {
        this.wannaPlay = false;
    }

    private Key readKey(Terminal terminal) {
        Key key = terminal.readInput();

        while (key == null) {
            if (level.getMonsters().move() == false
                    || level.getMovingObst().move() == false) {
                this.abbruch();
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            key = terminal.readInput();
        }
        return key;
    }

    private void drawField() {
        drawField(true);
    }

    private void drawField(boolean position) {
        // draws the player, if postion true, player is centered
        // else he starts at the border

        if (position == true) {

            int windowCols = gui.getScreen().getTerminal().getTerminalSize().getColumns();
            int windowRows = gui.getScreen().getTerminal().getTerminalSize().getRows();
            int playerX = level.getPlayer().getX();
            int playerY = level.getPlayer().getY();
            int levelCols = level.getCols();
            int levelRows = level.getRows();

            // get the starting col to print
            if (windowCols < levelCols) {
                // if the player starts at the left windowboarder
                if (playerX == 0) {
                    level.setStartCol(0);

                    // if the player starts at the right windowboarder
                } else if (playerX == levelCols - 1) {
                    level.setStartCol(levelCols - 1 - windowCols);
                    if (level.getStartCol() < 0) {
                        level.setStartCol(0);
                    }
                } else {
                    level.setStartCol(playerX - (windowCols / 2));
                    if ((playerX - (windowCols / 2)) < 0) {
                        level.setStartCol(0);
                    }

                    if ((playerX + (windowCols / 2)) > levelCols) {
                        level.setStartCol(levelCols - 1 - windowCols);
                    }

                }
            }

            // get the starting row to print
            if (windowRows < levelRows) {
                // if the player starts at the top windowboarder
                if (playerY == 0) {
                    level.setStartRow(0);

                    // if the player starts at the bottom windowboarder
                } else if (playerY == levelRows - 1) {
                    level.setStartRow(levelRows - 1 - windowRows);
                    if (level.getStartRow() < 0) {
                        level.setStartRow(0);
                    }
                } else {
                    level.setStartRow(playerY - (windowRows / 2));

                    if ((playerY - (windowRows / 2)) < 0) {
                        level.setStartRow(0);
                    }

                    if ((playerY + (windowRows / 2)) > levelRows) {
                        level.setStartRow(levelRows - 1 - windowRows);
                    }

                }
            }
        }
        drawField(level.getStartCol(), level.getStartRow());
    }

    private void drawField(int startCol, int startRow) {

        // draws the first gamefield
        terminal.clearScreen();

        level.printHeadInformations();

        // set the right boarder
        int x = startCol + terminal.getTerminalSize().getColumns();
        if (x > level.getCols()) {
            x = level.getCols();
            //   startCol = level.getCols() - terminal.getTerminalSize().getColumns();
        }

        int y = startRow + terminal.getTerminalSize().getRows() - 1;
        if (y > level.getRows()) {
            y = level.getRows();
            //  startRow = level.getRows() - terminal.getTerminalSize().getRows();
        }

        // !missing check if y and x > 0
        // print the gamefield
        for (int i = startCol; i < x; i++) {
            for (int j = startRow; j < y; j++) {

                level.printChar(i - startCol, j - startRow, i, j);
            }
        }
        level.printChar(level.getPlayer().getX() - startCol, level.getPlayer().getY() - startRow, level.getPlayer().getX(), level.getPlayer().getY());
    }

    private boolean checkIfRedrawIsNecessary(Player player) {
        // check if player is < startCol
        if (level.getPlayer().getX() < level.getStartCol()) {
            level.setStartCol(level.getPlayer().getX() - gui.getScreen().getTerminal().getTerminalSize().getColumns() + 1);
            return true;
            // check if player is < startRow
        } else if (level.getPlayer().getY() < level.getStartRow()) {
            level.setStartRow(level.getPlayer().getY() - gui.getScreen().getTerminal().getTerminalSize().getRows() + level.getFirstRow() + 1);
            return true;
            // check if player > startCol + terminalSize    
        } else if (level.getPlayer().getX() >= level.getStartCol() + gui.getScreen().getTerminal().getTerminalSize().getColumns()) {
            level.setStartCol(level.getPlayer().getX());
            return true;
        } else if (level.getPlayer().getY() >= level.getStartRow() + gui.getScreen().getTerminal().getTerminalSize().getRows() - level.getFirstRow()) {
            level.setStartRow(level.getPlayer().getY());
            return true;
        } else {
            return false;
        }
    }

    private void abbruch() {
        terminal.clearScreen();
        terminal.moveCursor(0, 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter('U');
        terminal.putCharacter(' ');
        terminal.putCharacter('D');
        terminal.putCharacter('I');
        terminal.putCharacter('E');
        terminal.putCharacter('D');
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        level.getPlayer().setLive(level.getPlayer().getLive() - 1);

        if (level.getPlayer().getLive() < 1) {
            //         this.wannaPlay = false;
            lost();
        }

        //       level.setLevelValue(level.getPlayer().getX(), level.getPlayer().getY(), level.getUnoccupiedField());
        level.getPlayer().setXY(level.getStartPositionX(), level.getStartPositionY());
        level.setLevelValue(level.getPlayer().getX(), level.getPlayer().getY(), 7);

        drawField(true);

    }

    private void won() {
        terminal.clearScreen();
        terminal.moveCursor(0, 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter('U');
        terminal.putCharacter(' ');
        terminal.putCharacter('W');
        terminal.putCharacter('O');
        terminal.putCharacter('N');
        terminal.putCharacter('!');
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        this.closeGame();
    }

    private void lost() {
        terminal.clearScreen();
        terminal.moveCursor(0, 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter('U');
        terminal.putCharacter(' ');
        terminal.putCharacter('L');
        terminal.putCharacter('O');
        terminal.putCharacter('S');
        terminal.putCharacter('T');
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        this.closeGame();
    }

}
