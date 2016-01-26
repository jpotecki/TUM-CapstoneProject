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
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.dialog.DialogButtons;
import com.googlecode.lanterna.gui.dialog.DialogResult;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jp
 */
public class SubMainMenu extends Window {

    private class SaveNewFile extends Window {

        public SaveNewFile(GUIScreen gui, Game game) {
            super("Enter the Filename");

            // get the date
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();
            TextBox input = new TextBox(dateFormat.format(date), 30);

            // Save Button
            Button save = new Button("Save", new Action() {
                @Override
                public void doAction() {
                    if (input.getText().isEmpty()) {
                        DialogResult result = MessageBox.showMessageBox(gui, "Error", "empty filename", DialogButtons.OK);
                    } else {
                        String filename = "saved/" + input.getText();
                        boolean success = game.getLevel().saveGameNewFile(filename);
                        close();
                    }
                }
            });

            Button cancel = new Button("Cancel", new Action() {
                @Override
                public void doAction() {
                    close();
                }
            });

            // add buttons etc
            addComponent(input);
            addComponent(new Label());
            addComponent(save);
            addComponent(cancel);

        }

    }

    public SubMainMenu(GUIScreen gui, Game game) {

        super("Ingame Menu");
        addComponent(new Label());

        addComponent(new Button("Quit Game and Return to MainMenu", new Action() {
            @Override
            public void doAction() {
                game.closeGame();
                close();
            }

        }));

        addComponent(new Button("Save Game in a new File", new Action() {
            @Override
            public void doAction() {
                gui.showWindow(new SaveNewFile(gui, game), GUIScreen.Position.CENTER);
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
