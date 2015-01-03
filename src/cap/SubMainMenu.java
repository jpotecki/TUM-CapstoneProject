/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;

/**
 *
 * @author jp
 */
public class SubMainMenu extends Window {
    
    
    
    public SubMainMenu(GUIScreen gui, Game game) {
        super("Ingame Menu");
        addComponent(new Label());
        
        addComponent(new Button("Quit Game and Return to MainMenu", new Action() {
            @Override
            public void doAction() {
                game.close();
                close();
            }

        }));
        addComponent(new Button("Return to Terminal", new Action() {
            @Override
            public void doAction() {
                close();
            }

        }));

    }
}
