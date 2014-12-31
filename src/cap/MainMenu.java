/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import static com.googlecode.lanterna.gui.GUIScreen.Position.CENTER;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import java.io.File;

/**
 *
 * @author jp
 */
public class MainMenu extends Window {

    private class ListAllAvailableMaps extends Window {

        public ListAllAvailableMaps(GUIScreen gui) {
            super(" New Game ");

            // read All files in the directory
            String[] files = readFiles("maps");

            addComponent(new Label("Please select a map to play"));
            addComponent(new Label());
            addComponent(new Label("Available Maps: "));

            // create for every string[] entry a button
            for (String s : files) {
                addComponent(new Button(" " + s + " ", new Action() {
                    @Override
                    public void doAction() {
                        close();
                        Game game = new Game(gui);
                        game.playGame();
                    }
                }));
            }
            addComponent(new Label());
            addComponent(new Button(" Return ", new Action() {
                @Override
                public void doAction() {
                    close();
                }
            }));
        }

    }

    public MainMenu(GUIScreen gui) {
        super(" Main Menu ");
        addComponent(new Label());
        addComponent(new Button("New Game", new Action() {
            @Override
            public void doAction() {
                gui.showWindow(new ListAllAvailableMaps(gui), CENTER);
            }

        }));
        addComponent(new Button("Quit App", new Action() {
            @Override
            public void doAction() {
                System.exit(0);
            }

        }));
    }

    private String[] readFiles(String foldername) {
        /* 
         Load Folder maps and safe files in array
         */
        File file = new File("src/cap/" + foldername);

        if (file.isDirectory() && file.list().length > 0) {
            return file.list();
        }
        String[] files = {"leer"};
        return files;
    }

}
