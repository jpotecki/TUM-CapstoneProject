/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import static com.googlecode.lanterna.gui.GUIScreen.Position.CENTER;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author jp
 */
public class CapstoneProject {

    /**
     * @param args the command line arguments
     */
    static Screen screen;

    public static void main(String[] args) {     
        Terminal terminal = TerminalFacade.createSwingTerminal(100,30);
        GUIScreen gui = TerminalFacade.createGUIScreen(terminal);
        gui.getScreen().startScreen();
        MainMenu menue = new MainMenu(gui);
        gui.showWindow(menue, CENTER);
    }

}
