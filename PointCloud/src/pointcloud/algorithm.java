/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pointcloud;

import java.util.ArrayList;
import javafx.geometry.Point3D;

/**
 *
 * @author Marek
 */
public class algorithm {

    static int start, end;
//    static ArrayList<Point3D> left = new ArrayList<Point3D>();
//    static ArrayList<Point3D> right = new ArrayList<Point3D>();
    static ArrayList<Point3D> gen_array = new ArrayList<Point3D>();

    //nalezeni xmin,xmax,ymin,ymax,zmin,zmax
    //na pozice v poli [0,1,2,3,4,5] ulozit indexy v array
    //využito pro první nalezení
    public static int[] minmax(ArrayList<Point3D> array) {
        int[] minmax = new int[6];
        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int f = 0;
        Point3D A, B, C, D, E, F;
        for (int i = 1; i < array.size(); i++) {
            //Xmin position
            A = array.get(a);
            Point3D temp = array.get(i);
            if (temp.getX() < A.getX()) {
                a = i;
            }
            //Xmax position
            B = array.get(b);
            if (temp.getX() > B.getX()) {
                b = i;
            }
            //Ymin position
            C = array.get(c);
            if (temp.getY() < C.getY()) {
                c = i;
            }
            //Ymax position
            D = array.get(d);
            if (temp.getY() > D.getY()) {
                d = i;
            }
            //Zmin position
            E = array.get(e);
            if (temp.getZ() < E.getZ()) {
                e = i;
            }
            //Zmax position
            F = array.get(f);
            if (temp.getZ() > F.getZ()) {
                f = i;
            }
        }
        minmax[0] = a;
        minmax[1] = b;
        minmax[2] = c;
        minmax[3] = d;
        minmax[4] = e;
        minmax[5] = f;
        return minmax;
    }

    //nalezení Originu, potřebné poka
    public static Point3D origin(int[] args, ArrayList<Point3D> array) {
        Point3D origin = new Point3D(0, 0, 0);
//        Point3D xmin = array.get(args[0]);
//        Point3D xmax = array.get(args[1]);
//        Point3D ymin = array.get(args[2]);
//        Point3D ymax = array.get(args[3]);
//        Point3D zmin = array.get(args[4]);
//        Point3D zmax = array.get(args[5]);
        double origin_max = -1;
        Point3D[] origin_candidates = new Point3D[6];
        for (int i = 0; i < 6; i++) {
            origin_candidates[i] = array.get(args[i]);
        }
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i != j) {
                    double origin_x = origin_candidates[i].getY() * origin_candidates[j].getZ() - origin_candidates[j].getY() * origin_candidates[i].getZ();
                    double origin_y = origin_candidates[i].getZ() * origin_candidates[j].getX() - origin_candidates[j].getZ() * origin_candidates[i].getX();
                    double origin_z = origin_candidates[i].getX() * origin_candidates[j].getY() - origin_candidates[j].getX() * origin_candidates[i].getY();
                    double origin_last = Math.sqrt((origin_x * origin_x) + (origin_y * origin_y) + (origin_z * origin_z));

                    if (origin_last > origin_max) {
                        origin_max = origin_last;
                        origin = new Point3D(origin_x, origin_y, origin_z);
                        //začátek a konec, vstupuje do sort metody
                        start = args[i];
                        end = args[j];

                    }
                }
            }
        }

        return origin;
    }
// funkce pro uspořádání bodů
//není implementován loneliness index
    public static ArrayList<Point3D> sort(ArrayList<Point3D> array) {
        if (array.size() < 3) {
            return array;
        }
        Point3D nearest;
        Point3D first = array.get(start);
        Point3D last = array.get(end);
        array.remove(last);
        array.remove(first);
        ArrayList<Point3D> sorted_list = new ArrayList<Point3D>();
        sorted_list.add(first);

        Point3D first_nearest = array.get(0);

        while (!array.isEmpty()) {
            for (int i = 1; i < array.size(); i++) {

                nearest = array.get(i);
                double distance_1 = first_nearest.distance(first);
                double distance_2 = nearest.distance(first);
                if (distance_1 > distance_2) {
                    first_nearest = nearest;
                }

            }
            sorted_list.add(first_nearest);
            array.remove(first_nearest);
            first = first_nearest;
            if (!array.isEmpty()) {
                first_nearest = array.get(0);
            }

//        array.remove(nearest);
//        sorted_list.add(first_nearest);
//        first = first_nearest;
//        first_nearest = nearest;
        }

        sorted_list.add(last);

        return sorted_list;
    }
    //rekurzivní volání pomocí nového pole
    public static void douglas(ArrayList<Point3D> array, Point3D origin, double epsilon) {
        if (epsilon == 0) {
            gen_array = array;
            return;
        }

        Point3D first = array.get(0);
        Point3D last = array.get(array.size() - 1);
        if (array.size() < 3) {
            if (gen_array.contains(first) == false) {
                gen_array.add(first);
            }
            if (gen_array.contains(last) == false) {
                gen_array.add(last);
            }
            return;
        }
        
        Point3D u = new Point3D(last.getX() - first.getX(), last.getY() - first.getY(), last.getZ() - first.getZ());
        Point3D v = new Point3D(origin.getX() - first.getX(), origin.getY() - first.getY(), origin.getZ() - first.getZ());
//        Point3D w = u.crossProduct(v);
        Point3D w = new Point3D(u.getY() * v.getZ() - v.getY() * u.getZ(), u.getZ() * v.getX() - v.getZ() * u.getX(), u.getX() * v.getY() - v.getX() * u.getY());
        //obecna rovnice roviny ax + by + cz + d = 0
        double a = w.getX();
        double b = w.getY();
        double c = w.getZ();
        double d = -(a * first.getX()) - (b * first.getY()) - (c * first.getZ());
        //vzdálenost bodu od roviny
        int mid_position = -1;
        for (int i = 0; i < array.size(); i++) {
            Point3D temp = array.get(i);
            double numerator = Math.abs(a * temp.getX() + b * temp.getY() + c * temp.getZ() + d);
            double denominator = Math.sqrt(a * a + b * b + c * c);
            double distance = numerator / denominator;
            if (distance > epsilon) {
                mid_position = i;
            }
        }
        //musim nejak celkove opravit, protože mi to zvysuje pocet bodu, problem s mid pointem
        if (mid_position == -1) {

            if (first == last) {
                if (gen_array.contains(first) == false) {
                    gen_array.add(first);
                }
            } else {
                if (gen_array.contains(first) == false) {
                    gen_array.add(first);
                }
                if (gen_array.contains(last) == false) {
                    gen_array.add(last);
                }

            }

        } else {
            ArrayList<Point3D> left = new ArrayList<Point3D>();
            ArrayList<Point3D> right = new ArrayList<Point3D>();

            for (int i = 0; i <= mid_position; i++) {
                left.add(array.get(i));
            }
            for (int i = mid_position; i < array.size(); i++) {
                right.add(array.get(i));
            }

            //vypocitat predchozi funkce aby byly vytvořeny vstupy pro douglas, lze pak přepsat do jedné funkce
            if (left.isEmpty() == false) { 
                int[] minmax_left = minmax(left);
                Point3D origin_left = origin(minmax_left, left);
                douglas(left, origin_left, epsilon);
            }
            if (right.isEmpty() == false) {                
                int[] minmax_right = minmax(right);
                Point3D origin_right = origin(minmax_right, right);
                douglas(right, origin_right, epsilon);
            }

        }
    }
    
    //pokus číslo 2, ale musím předtím vypočítat jednou minmax a seřazení
    //odkazuje se na pozici v sorted_listu
    //při velkém množství problém s rekurzí
    public static void douglas2 (int fid,int eid, Point3D origin, double epsilon){
        if (epsilon == 0) {
            gen_array = GUI.sorted_list;
            return;
        }
        Point3D first = GUI.sorted_list.get(fid);
        Point3D last = GUI.sorted_list.get(eid);
        
        
        Point3D u = new Point3D(last.getX() - first.getX(), last.getY() - first.getY(), last.getZ() - first.getZ());
        Point3D v = new Point3D(origin.getX() - first.getX(), origin.getY() - first.getY(), origin.getZ() - first.getZ());
//        Point3D w = u.crossProduct(v);
        Point3D w = new Point3D(u.getY() * v.getZ() - v.getY() * u.getZ(), u.getZ() * v.getX() - v.getZ() * u.getX(), u.getX() * v.getY() - v.getX() * u.getY());
        //obecna rovnice roviny ax + by + cz + d = 0
        double a = w.getX();
        double b = w.getY();
        double c = w.getZ();
        double d = -(a * first.getX()) - (b * first.getY()) - (c * first.getZ());
        //vzdálenost bodu od roviny
        int mid_position = -1;
        for (int i = fid; i < eid+1; i++) {
            Point3D temp = GUI.sorted_list.get(i);
            double numerator = Math.abs(a * temp.getX() + b * temp.getY() + c * temp.getZ() + d);
            double denominator = Math.sqrt(a * a + b * b + c * c);
            double distance = numerator / denominator;
            if (distance > epsilon) {
                mid_position = i;
            }
        }
        if (mid_position < 0) {

            if (first == last) {
                if (gen_array.contains(first) == false) {
                    gen_array.add(first);
                }
            } else {
                if (gen_array.contains(first) == false) {
                    gen_array.add(first);
                }
                if (gen_array.contains(last) == false) {
                    gen_array.add(last);
                }

            }
            return;
        }
            //levá
            
             int[] left_minmax = minmax_sorted(fid, mid_position, GUI.sorted_list);
             Point3D left_origin = origin(left_minmax, GUI.sorted_list);
             
            
             //pravá
             int[] right_minmax = minmax_sorted(mid_position, eid, GUI.sorted_list);
             Point3D right_origin = origin(right_minmax, GUI.sorted_list);
             
             
            
             
        
        douglas2(fid, mid_position, left_origin, epsilon);
        douglas2(mid_position, eid, right_origin, epsilon);       
    }
    // minmax, na vstupu pozice začátku, pozice konce a pole
    //vstupuje již setřízené pole bodů
    public static int[] minmax_sorted(int start, int end,ArrayList<Point3D> array) {
        int[] minmax = new int[6];
        int a = start;
        int b = start;
        int c = start;
        int d = start;
        int e = start;
        int f = start;
        Point3D A, B, C, D, E, F;
        for (int i = start+1; i < end+1; i++) {
            //Xmin position
            A = array.get(a);
            Point3D temp = array.get(i);
            if (temp.getX() < A.getX()) {
                a = i;
            }
            //Xmax position
            B = array.get(b);
            if (temp.getX() > B.getX()) {
                b = i;
            }
            //Ymin position
            C = array.get(c);
            if (temp.getY() < C.getY()) {
                c = i;
            }
            //Ymax position
            D = array.get(d);
            if (temp.getY() > D.getY()) {
                d = i;
            }
            //Zmin position
            E = array.get(e);
            if (temp.getZ() < E.getZ()) {
                e = i;
            }
            //Zmax position
            F = array.get(f);
            if (temp.getZ() > F.getZ()) {
                f = i;
            }
        }
        minmax[0] = a;
        minmax[1] = b;
        minmax[2] = c;
        minmax[3] = d;
        minmax[4] = e;
        minmax[5] = f;
        return minmax;
    }
    
}
