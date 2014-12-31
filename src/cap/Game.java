/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author jp
 */
public class Game {

    private boolean wannaPlay;    // wannaPlay = false kills the game

    private final GUIScreen gui;

    public Game(GUIScreen gui) {
        this.gui = gui;
        wannaPlay = true;
    }

    public void playGame() {

        // close the menu screen
        gui.getScreen().stopScreen();

        // clear the screen and enter private mode
        gui.getScreen().getTerminal().clearScreen();
        gui.getScreen().getTerminal().enterPrivateMode();
        //   terminal.applyBackgroundColor(255,255,255);

        // Var for user input 
        Key key;

        // Ingame Menu
        SubMainMenu submenu = new SubMainMenu(gui, this);

        // loops until user wants to quit playing
        while (wannaPlay) {

            // print here the gamefield
            //
            // read the 
            key = readKey(gui.getScreen().getTerminal());

            if (key.getKind() == Key.Kind.Escape) {

                // exit the terminalmode
                gui.getScreen().getTerminal().exitPrivateMode();

                // show the GUIScreen and the ingame menu
                gui.getScreen().startScreen();
                gui.showWindow(submenu);
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
}
