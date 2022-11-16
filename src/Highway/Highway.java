package src.Highway;


import src.Constant;
import src.Vehicles.*;
import static src.Vehicles.Vehicle.Decision.*;

import java.net.SocketPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Highway {
    private int speedLimit = 120;
    private int numberOfLanes = 4;
    private float length = 500.00f;
    List<Vehicle> vehicles = new ArrayList<>();

    //Array with the ID of vehicles
    private int[][] occupied = new int[numberOfLanes][Constant.PANEL_WIDTH];

    public Highway() {
    }

    public Highway(int speedLimit, int numberOfLanes, float length) {

        this.speedLimit = speedLimit;
        this.numberOfLanes = numberOfLanes;
        this.length = length;
        this.occupied = new int[numberOfLanes][Constant.PANEL_WIDTH];
        //Initialization of occupied array
        for(int[] laneOfHighway : occupied){
            Arrays.fill(laneOfHighway, -1);
        }

    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    /**
     * Updates parameters of vehicles on the highway in the provided time in milisec.
     */
    public void update(int updateTimeInMilisec) {

        for (Vehicle vehicle : vehicles) {
            if (!vehicle.isOnRoad) {
                if (vehicle.getID() == 1 || isEnoughPlaceForInitVehicle(vehicle)) {
                    vehicle.isOnRoad = true;

                } else {
                    continue;
                }
            }

            vehicle.update(updateTimeInMilisec, getFrontCar(vehicle), getFrontLeftCar(vehicle), getFrontRightCar(vehicle), getBackLeftCar(vehicle), getBackRightCar(vehicle)) ; //, vehicle.getFrontVehicle(), vehicle.getFrontLeftVehicle(), vehicle.getFrontRightVehicle(),vehicle.getBackLeftVehicle(),vehicle.getBackRightVehicle());//Nie moga byc tutaj metody bo array jest caly czas na -1! getFrontLeftCar(vehicle), getFrontRightCar(vehicle), getBackLeftCar(vehicle), getBackRightCar(vehicle));
            this.updateOccupiedForVehicle(vehicle);
        }

        //Actualization of occupied
        for(int[] laneOfHighway : occupied){
            Arrays.fill(laneOfHighway, -1);
        }

        for (Vehicle vehicle : vehicles) {
            //Consider only the vehicles on road
            if(vehicle.isOnRoad) {
                this.updateOccupiedForVehicle(vehicle);
            }
        }
    }

    /**
     * Checks if the vehicle can start driving.
     */
    private boolean isEnoughPlaceForInitVehicle(Vehicle vehicle){
        for (int i = 0; i < (vehicle.getParams().length + Constant.DISTANCE_TO_MAKE_DECISION); i++){
            if(this.occupied[vehicle.getParams().laneOfTraffic - 1][i] != -1)
                return false;
        }
        return true;
    }

    /**
     * Updates occupied array which inform us about positions of all vehicle on the highway.
     */
    private void updateOccupiedForVehicle(Vehicle vehicle){
        for (int i = vehicle.getParams().position; i >= 0 && i < this.occupied[0].length && i < vehicle.getParams().position + vehicle.getParams().length; i++)
            this.occupied[vehicle.getParams().laneOfTraffic - 1][i] = vehicle.getID();
    }

    /**
     * Returns the vehicle located in front of provided vehicle.
     */
    private Vehicle getFrontCar(Vehicle vehicle){

        int position = vehicle.getParams().position;

        //The case that position is equal to -1 (first vehicle)
        if(position < 0)
            return null;

        if(position > Constant.PANEL_WIDTH - vehicle.getParams().length){
            for(int i = 0; i < Constant.DISTANCE_TO_MAKE_DECISION + vehicle.getParams().length - (Constant.PANEL_WIDTH - position); i++){
                if(occupied[vehicle.getParams().laneOfTraffic - 1][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 1][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 1][i] - 1);
            }
        }

        for(int i = position; i <= position + vehicle.getParams().length + Constant.DISTANCE_TO_MAKE_DECISION && i < Constant.PANEL_WIDTH; i++){
            if(occupied[vehicle.getParams().laneOfTraffic - 1][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 1][i] != vehicle.getID()) {
                return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 1][i] - 1);
            }
        }

        return null;
    }

    /**
     * Returns the vehicle located in front left of provided vehicle.
     */
    private Vehicle getFrontLeftCar(Vehicle vehicle){

        //On the first lane left car is always null.
        if(vehicle.getParams().laneOfTraffic == 1)
            return null;

        int position = vehicle.getParams().position;

        //The case that position is equal to -1 (first vehicle)
        if(position < 0)
            return null;

        if(position > Constant.PANEL_WIDTH - vehicle.getParams().length){
            for(int i = 0; i < Constant.DISTANCE_TO_MAKE_DECISION + vehicle.getParams().length - (Constant.PANEL_WIDTH - position); i++){
                if(occupied[vehicle.getParams().laneOfTraffic - 2][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 2][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 2][i] - 1);
            }
        }

        for(int i = position; i < position + vehicle.getParams().length + Constant.DISTANCE_TO_MAKE_DECISION && i < Constant.PANEL_WIDTH; i++){
            if(occupied[vehicle.getParams().laneOfTraffic - 2][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 2][i] != vehicle.getID())
                return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 2][i] - 1);
        }

        return null;
    }

    /**
     * Returns the vehicle located in front right of provided vehicle.
     */
    private Vehicle getFrontRightCar(Vehicle vehicle){

        //On the last lane right car is always null.
        if(vehicle.getParams().laneOfTraffic == this.numberOfLanes)
            return null;

        int position = vehicle.getParams().position;

        //The case that position is equal to -1 (first vehicle)
        if(position < 0)
            return null;

        if(position > Constant.PANEL_WIDTH - vehicle.getParams().length){
            for(int i = 0; i < Constant.DISTANCE_TO_MAKE_DECISION + vehicle.getParams().length - (Constant.PANEL_WIDTH - position); i++){
                if(occupied[vehicle.getParams().laneOfTraffic][i] != -1 && occupied[vehicle.getParams().laneOfTraffic][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic][i] - 1);
            }
        }

        for(int i = position; i < position + vehicle.getParams().length + Constant.DISTANCE_TO_MAKE_DECISION + 100 && i < Constant.PANEL_WIDTH; i++){
            if(occupied[vehicle.getParams().laneOfTraffic][i] != -1 && occupied[vehicle.getParams().laneOfTraffic][i] != vehicle.getID())
                return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic][i] - 1);
        }

        return null;
    }

    /**
     * Returns the vehicle located in back left of provided vehicle.
     */
    private Vehicle getBackLeftCar(Vehicle vehicle){

        //On the first lane left car is always null.
        if(vehicle.getParams().laneOfTraffic == 1)
            return null;

        int position = vehicle.getParams().position;

        //The case that position is equal to -1 (first vehicle)
        if(position < 0)
            return null;

        if(position < vehicle.getParams().length){
            for(int i = Constant.PANEL_WIDTH - Constant.DISTANCE_TO_MAKE_DECISION + position; i < Constant.PANEL_WIDTH; i++){
                if(occupied[vehicle.getParams().laneOfTraffic - 2][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 2][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 2][i] - 1);
            }
            for(int i = 0; i < position + vehicle.getParams().length; i++){
                if(occupied[vehicle.getParams().laneOfTraffic - 2][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 2][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 2][i] - 1);
            }
        }

        for(int i = position; i > position - Constant.DISTANCE_TO_MAKE_DECISION && i >= 0; i--){
            if(occupied[vehicle.getParams().laneOfTraffic - 2][i] != -1 && occupied[vehicle.getParams().laneOfTraffic - 2][i] != vehicle.getID())
                return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic - 2][i] - 1);
        }

        return null;
    }

    /**
     * Returns the vehicle located in back right of provided vehicle.
     */
    private Vehicle getBackRightCar(Vehicle vehicle){

        //On the last lane right car is always null.
        if(vehicle.getParams().laneOfTraffic == this.numberOfLanes)
            return null;

        int position = vehicle.getParams().position;

        //The case that position is equal to -1 (first vehicle)
        if(position < 0)
            return null;

        if(position < vehicle.getParams().length){
            for(int i = Constant.PANEL_WIDTH - Constant.DISTANCE_TO_MAKE_DECISION + position; i < Constant.PANEL_WIDTH; i++){
                if(occupied[vehicle.getParams().laneOfTraffic][i] != -1 && occupied[vehicle.getParams().laneOfTraffic][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic][i] - 1);
            }
            for(int i = 0; i < position + vehicle.getParams().length; i++){
                if(occupied[vehicle.getParams().laneOfTraffic][i] != -1 && occupied[vehicle.getParams().laneOfTraffic][i] != vehicle.getID())
                    return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic][i] - 1);
            }
        }

        for(int i = position; i > position - Constant.DISTANCE_TO_MAKE_DECISION && i >= 0; i--){
            if(occupied[vehicle.getParams().laneOfTraffic][i] != -1 && occupied[vehicle.getParams().laneOfTraffic][i] != vehicle.getID())
                return this.vehicles.get(occupied[vehicle.getParams().laneOfTraffic][i] - 1);
        }

        return null;

    }
}

