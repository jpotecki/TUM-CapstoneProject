/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to build the level
 *
 * @author jp
 */
public class Level {

    private int[][] level;
    private int height;
    private int width;
    private int live = 2;
    private int keys = 0;

    public Level(String dateiName) {

        this.level = createLevel(dateiName);
    }

    public int getKeys() {
        return keys;
    }

    public void setKeys(int keys) {
        this.keys = keys;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public int getCols() {
        return width;
    }

    public int getRows() {
        return height;
    }

    public char getValue(int x, int y) {
        // returns the value at the position   
        if (level[x][y] != 32) {
            return (char) ('0' + level[x][y]);
        } else {
            return 32;
        }
    }

    private int[][] createLevel(String dateiName) {

        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("src/capstoneproject/" + dateiName);
            
            // load a properties file
            prop.load(input);
            
            // get width and heigth of the level and create an array 
            this.width = Integer.parseInt(prop.getProperty("Width"));
            this.height = Integer.parseInt(prop.getProperty("Height"));

            int[][] level = new int[width][height];
            
            // fill array with number 6 => the number for free fields
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    level[i][j] = 6;
                }
            }

            // get the array positions and fill the values into the array
            int tmpH, tmpW, middle;
            
            for (String key : prop.stringPropertyNames()) {
                String value = prop.getProperty(key);

                if (!key.equalsIgnoreCase("height") & !key.equalsIgnoreCase("width")) {
                    
                    // find the position of the seperator for the x,y coordinates
                    middle = key.indexOf(',');
                    
                    // cp numbers bevor the middle char
                    tmpH = Integer.parseInt(key.substring(0, middle)); 
                    
                    // cp numbers after the middle char
                    tmpW = Integer.parseInt(key.substring(middle + 1, key.length())); 
                    
                    // cp the value into the array
                    level[tmpH][tmpW] = value.charAt(0) - '0';
                }
            }
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

}
