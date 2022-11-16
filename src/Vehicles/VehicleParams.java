package src.Vehicles;

import src.Highway.HighwayMain;

import java.awt.*;

public class VehicleParams {

    //Definition of parameter positions in txt-file
    private static final int maxSpeedPos = 1;
    private static final int accelerationPos = 2;
    private static final int lengthPos = 3;
    private static final int colorPos = 4;
    private static final int numberOfWheelsPos = 5;
    private static final int modelPos = 6;
    private static final int registrationNumberPos = 7;

    //Definition of parameters
    public int maxSpeed;
    public int acceleration;
    public float brakingFactor; //From Wikipedia - braking distance
    public int length;
    public Color color;
    public int numberOfWheels;
    public int currentSpeed;
    public String model;
    public String registrationNumber;
    public int laneOfTraffic;
    public int distanceToOvertaking; //From Wikipedia - braking distance
    public int position;

    public VehicleParams(String[] params) {

        parse(params);
        this.currentSpeed = 0;
        this.laneOfTraffic = HighwayMain.numberOfLanes;
        this.position = -1;
        this.brakingFactor = 0.2f;

    }

    /**
     * Parses string input to vehicle parameters.
     *
     */
    private void parse(String[] input){

        this.maxSpeed = Integer.parseInt(input[maxSpeedPos]);
        this.acceleration = Integer.parseInt(input[accelerationPos]);
        this.length = Integer.parseInt(input[lengthPos]);
        this.color = toColor(input[colorPos]);
        this.numberOfWheels = Integer.parseInt(input[numberOfWheelsPos]);
        this.model = input[modelPos];
        this.registrationNumber = input[registrationNumberPos];

    }

    /**
     * Converts string information to color.
     */
    private Color toColor(String string){

        if (string == null)
            return null;
        if ("1".equals(string))
            return Color.BLUE;
        if ("2".equals(string))
            return Color.GREEN;
        if ("3".equals(string))
            return Color.GRAY;
        if ("4".equals(string))
            return Color.YELLOW;
        if ("5".equals(string))
            return Color.LIGHT_GRAY;
        if ("6".equals(string))
            return Color.DARK_GRAY;
        if ("7".equals(string))
            return Color.RED;
        if ("8".equals(string))
            return Color.ORANGE;
        if ("9".equals(string))
            return Color.PINK;
        if ("10".equals(string))
            return Color.BLACK;

        return null;
    }

}

