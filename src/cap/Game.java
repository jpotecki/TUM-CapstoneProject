/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.gui.GUIScreen;
import static com.googlecode.lanterna.gui.GUIScreen.Position.CENTER;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.ResizeListener;
import com.googlecode.lanterna.terminal.TerminalSize;

/**
 *
 * @author jp
 */
public class Game {

    private boolean wannaPlay;    // wannaPlay = false kills the game

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

                drawField(0, 0);
            }
        };
        terminal.addResizeListener(resize);
        // loops until user wants to quit playing
        drawField(0, 0);
        while (wannaPlay) {

            // print here the gamefield
            // read the input by the user 
            // !missing : movement of the monsters in the readkey function
            key = readKey(terminal);

            // moves the player on the field
            level.getPlayer().movePlayer(key);

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
                    drawField(0,0);
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

    private void drawField(int startCol, int startRow) {

        level.setFirstCol(3);
        level.setFirstRow(2);

        // draws the first gamefield
        Terminal terminal = gui.getScreen().getTerminal();
        terminal.clearScreen();

        // draw the players lives and collected keys
        terminal.moveCursor(level.getFirstCol(), 0);
        terminal.applyBackgroundColor(Terminal.Color.BLACK);
        terminal.applyForegroundColor(Terminal.Color.WHITE);
        terminal.putCharacter('L');
        terminal.putCharacter('i');
        terminal.putCharacter('v');
        terminal.putCharacter('e');
        terminal.putCharacter('s');
        terminal.putCharacter(':');
        terminal.putCharacter(' ');

        // print the lives
        //terminal.putCharacter((char) (level.getLive() + '0'));
        terminal.putCharacter(' ');
        terminal.putCharacter('K');
        terminal.putCharacter('e');
        terminal.putCharacter('y');
        terminal.putCharacter('s');
        terminal.putCharacter(':');
        terminal.putCharacter(' ');

        // print the keys
        terminal.putCharacter((char) (level.getKeys() + '0'));

        int x = terminal.getTerminalSize().getColumns() - level.getFirstCol() - startCol;
        if (x > level.getCols()) {
            x = level.getCols();
        }

        int y = terminal.getTerminalSize().getRows() - level.getFirstRow() - startRow;
        if (y > level.getRows()) {
            y = level.getRows();
        }

        // !missing check if y and x > 0
        // print the gamefield
        for (int i = startCol; i < x; i++) {
            for (int j = startRow; j < y; j++) {

                level.printChar(i, j);
            }
        }

    }
}
