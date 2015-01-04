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

/**
 *
 * @author jp
 */
public class Game {

    private boolean wannaPlay;    // wannaPlay = false kills the game
    private boolean changedSize = false;
    private Level level;

    private final GUIScreen gui;

    public Game(GUIScreen gui) {

        this.gui = gui;

        wannaPlay = true;
    }

    public void setMap(String dateiname) {
        this.level = new Level(dateiname);
        level.setTerminal(gui.getScreen().getTerminal());
    }

    public void playGame() {
        Terminal terminal = gui.getScreen().getTerminal();

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

            // print here the gamefield
            // read the input by the user 
            // !missing : movement of the monsters in the readkey function
            key = readKey(terminal);

            // moves the player on the field
            level.getPlayer().movePlayer(key);

            if (checkIfRedrawIsNecessary(level.getPlayer())) {
                drawField(false);
            }

            if (key.getKind() == Key.Kind.Escape) {

                // exit the terminalmode
                terminal.exitPrivateMode();

                // show the GUIScreen and the ingame menu
                gui.getScreen().startScreen();
                gui.showWindow(submenu, CENTER);
                if (wannaPlay) {
                    gui.getScreen().stopScreen();
                    gui.getScreen().getTerminal().clearScreen();
                    gui.getScreen().getTerminal().enterPrivateMode();
                    drawField(false);
                }
            }
        }
    }

    public void close() {
        this.wannaPlay = false;
    }

    private Key readKey(Terminal terminal) {
        Key key = terminal.readInput();

        while (key == null) {
            key = terminal.readInput();
        }
        return key;
    }

    private void drawField(){
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
        Terminal terminal = gui.getScreen().getTerminal();
        terminal.clearScreen();

        level.printHeadInformations();

        // set the right boarder
        int x = startCol + terminal.getTerminalSize().getColumns();
        if (x > level.getCols()) {
            x = level.getCols();
            //   startCol = level.getCols() - terminal.getTerminalSize().getColumns();
        }

        int y = startRow + terminal.getTerminalSize().getRows() -1 ;
        if (y > level.getRows()) {
            y = level.getRows();
            //  startRow = level.getRows() - terminal.getTerminalSize().getRows();
        }

        // !missing check if y and x > 0
        // print the gamefield
        for (int i = startCol; i < x; i++) {
            for (int j = startRow; j < y; j++) {

                level.printChar(i - startCol, j - startRow , i, j);
            }
        }
        level.printChar(level.getPlayer().getX() - startCol, level.getPlayer().getY() - startRow, level.getPlayer().getX(), level.getPlayer().getY());
    }

    private boolean checkIfRedrawIsNecessary(Player player) {
        // check if player is < startCol
        if (level.getPlayer().getX() < level.startCol) {
            level.setStartCol( level.getPlayer().getX() - gui.getScreen().getTerminal().getTerminalSize().getColumns() + 1 );
            return true;
            // check if player is < startRow
        } else if (level.getPlayer().getY() < level.startRow) {
            level.setStartRow( level.getPlayer().getY() - gui.getScreen().getTerminal().getTerminalSize().getRows() + level.getFirstRow() + 1);
            return true;
            // check if player > startCol + terminalSize    
        } else if (level.getPlayer().getX() >= level.startCol + gui.getScreen().getTerminal().getTerminalSize().getColumns() ) {
            level.setStartCol( level.getPlayer().getX()   );
            return true;
        } else if (level.getPlayer().getY() >= level.startRow + gui.getScreen().getTerminal().getTerminalSize().getRows() - level.getFirstRow()) {
            level.setStartRow( level.getPlayer().getY()  );
            return true;
        } else {
            return false;
        }
    }
}
