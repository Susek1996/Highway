package src.Highway;

import src.Highway.Highway;
import src.Vehicles.Vehicle;
import src.Vehicles.VehicleParams;
import src.Vehicles.Vehicle;
import src.Vehicles.VehicleParams;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static src.GUI.HighwayGUI.HPanel.createAndShowGui;

public class HighwayMain {
    public static int numberOfLanes;
    public static int speedLimit;
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("-=-=-=-=-=-=Start-=-=-=-=-=-=");
        File file = new File("highway.txt");
        Highway highway = initHighwayFromTxtFile(file);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui(highway);
            }
        });

    }

    /**
     * Creates highway from the txt - file
     */
    private static Highway initHighwayFromTxtFile(File txtFile) throws FileNotFoundException {

        Scanner in = new Scanner(txtFile);
        Highway highway = new Highway();
        int counter = 0;
        while(in.hasNext()){
            counter++;
            //Initialisation of highway
            if(counter == 1) {
                String[] inHighway = in.nextLine().split(";");
                speedLimit = Integer.parseInt(inHighway[0]);
                numberOfLanes = Integer.parseInt(inHighway[1]);
                highway = new Highway(Integer.parseInt(inHighway[0]), Integer.parseInt(inHighway[1]), Float.parseFloat(inHighway[2]));
            }
            //Initialisation of vehicles
            String[] inVehicle = in.nextLine().split(";");
            highway.addVehicle(new Vehicle(toType(inVehicle[0]), new VehicleParams(inVehicle)));
        }
        return highway;
    }

    /**
     * Converts string input to Type.
     */
    private static Vehicle.Type toType(String input) {

        if (input == null)
            return null;
        if ("CAR".equals(input))
            return Vehicle.Type.CAR;
        if ("BUS".equals(input))
            return Vehicle.Type.BUS;
        if ("MOTORCYCLE".equals(input))
            return Vehicle.Type.MOTORCYCLE;
        if ("TRUCK".equals(input))
            return Vehicle.Type.TRUCK;

        return null;
    }
}
