package pointcloud;

import java.awt.Polygon;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point3D;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Marek
 */
public class Reader {
    public static void main(String args[]) {
        retArray();
    }

    // method which reads data from .txt file and create Polygon array 
    public static ArrayList<Point3D> retArray() {
        try {

            // variables
            File file = GUI.f;
            Scanner fileScanner = new Scanner(file);
            

            // scanning of lines and values
            // variables got from .txt file
            String line = fileScanner.nextLine();
            Scanner lineScanner = new Scanner(line);
            String token;
            double x;
            double y;
            double z;
            
            ArrayList<Point3D> in_points = new ArrayList<Point3D>();
            // while cyclus which reads all lines in .txt 
            while (fileScanner.hasNextLine()) {
                line = fileScanner.nextLine();
                lineScanner = new Scanner(line);
                token = lineScanner.next();
                x = Double.parseDouble(token);
                token = lineScanner.next();
                y = Double.parseDouble(token);
                token = lineScanner.next();
                z = Double.parseDouble(token);
                Point3D temp_point = new Point3D(x,y,z);
                in_points.add(temp_point);
            }
            // close scanner function and return array of points
            fileScanner.close();
            return in_points;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
