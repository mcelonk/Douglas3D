/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointcloud;

/**
 *
 * @author Marek
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Point3D;

public class write {

	//private static final String FILENAME = "C:\\Users\\Marek\\Desktop\\text.txt";

	public static void main(String[] args) {

		BufferedWriter bw = null;
		FileWriter fw = null;
                ArrayList<Point3D> in_points = GUI.out_points;
		try {

			
			fw = new FileWriter(GUI.w + ".txt");
			bw = new BufferedWriter(fw);
                        bw.write("X Y Z");
                        bw.newLine();
                        for (int i = 0; i<in_points.size();i++){
                            Point3D temp = in_points.get(i);
                            double x = temp.getX();
                            double y = temp.getY();
                            double z = temp.getZ();
                            String x2 = String.valueOf(x);
                            String y2 = String.valueOf(y);
                            String z2 = String.valueOf(z);
                            bw.write(x2+" "+y2+" "+z2);
                            bw.newLine();
                        }
			

			System.out.println("Done");

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}
