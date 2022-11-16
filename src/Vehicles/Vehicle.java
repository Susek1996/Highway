package src.Vehicles;


import src.Constant;
import static src.Highway.HighwayMain.numberOfLanes;
import static src.Highway.HighwayMain.speedLimit;
import static java.util.Objects.isNull;

public class Vehicle {
    private int id;
    private Type type;
    private VehicleParams params;
    public boolean isOnRoad;

    private static int idCounter;

    public Vehicle(Type type, VehicleParams params) {

        this.id = ++idCounter;
        this.type = type;
        this.params = params;
        this.isOnRoad = false;

    }
    public VehicleParams getParams() {
        return this.params;
    }

    /**
     * Updates parameters of vehicle based on decision made inside the method
     */
    public void update(int updateTimeInMilisec, Vehicle front, Vehicle frontLeft, Vehicle frontRight, Vehicle backLeft, Vehicle backRight){

        //First update. Changing position from -1 to 0.
        this.getParams().position = (this.getParams().position == -1 ? 0 : this.getParams().position);

        //Make a decision depends on other vehicles on the road.
        Decision decisionSpeed = getDecisionSpeed(front, frontLeft, backLeft);
        Decision decisionLane = getDecisionLane(front, frontLeft, frontRight, backLeft, backRight);

        //Execute decision
        executeDecisionSpeed(decisionSpeed, updateTimeInMilisec);
        executeDecisionLane(decisionLane);

    }

    /**
     * Returns vehicle ID
     */
    public int getID() {
        return id;
    }

    /**
     * Returns decision about the speed depends on other vehicles on the road
     */
    private Decision getDecisionSpeed(Vehicle front, Vehicle frontLeft, Vehicle backLeft) {

        // 1. There is no vehicle in front
        if(isNull(front)){
            return Decision.ACCELERATING;
        }

        // 2. The vehicle has another vehicle in front of it.
        if (!isNull(front) && Math.abs(front.getParams().position - this.getParams().position) <= this.getParams().distanceToOvertaking * 100) {

            //Checking left lane.
            if (this.getParams().laneOfTraffic - 1 > 0) {
                //Checking front- and back left vehicle.
                if (isNull(frontLeft) && isNull(backLeft)) {
                    //The left lane is available. Accelerating before changing lane.
                    return Decision.ACCELERATING;
                } else if(!isNull(frontLeft) && Math.abs(front.getParams().position - this.getParams().position) <= this.getParams().distanceToOvertaking * 100){
                    //The left lane is occupied and the distance is too small to keep speed.
                    return Decision.REDUCING_SPEED;
                }
            }
        }
        return null;
    }

    /**
     * Returns decision about changing the lane depends on other vehicles on the road
     */
    private Decision getDecisionLane(Vehicle front, Vehicle frontLeft, Vehicle frontRight, Vehicle backLeft, Vehicle backRight) {

        // 1. There is no vehicle in front
        if(isNull(front)){
            //The right lane is available.
            if(this.getParams().laneOfTraffic < numberOfLanes && isNull(backRight) && isNull(frontRight))
                return Decision.BACK_TO_THE_RIGHT_LANE;
        }

        // 2. The vehicle has another vehicle in front of it.
        if (!isNull(front) && Math.abs(front.getParams().position - this.getParams().position) <= this.getParams().distanceToOvertaking * 100) {

            //Checking left lane
            if (this.getParams().laneOfTraffic - 1 > 0) {
                //Checking front left vehicle
                if (isNull(frontLeft) && isNull(backLeft)) {
                    return Decision.CHANGING_LANE;
                }
            }
        }

        return null;
    }

    /**
     * Executes speed decision
     */
    private void executeDecisionSpeed(Decision decisionSpeed, int updateTimeInMilisec) {

        //Accelerating
        if(decisionSpeed == Decision.ACCELERATING){

            //Calculating the current speed depend on the acceleration
            //The speed of the vehicle is lower than max speed of it (Acceleration)
            if(this.getParams().currentSpeed < this.getParams().maxSpeed)
                this.getParams().currentSpeed += this.getParams().acceleration * updateTimeInMilisec;
            //The speed of the vehicle cannot be greater than max available speed
            if(this.getParams().currentSpeed > this.getParams().maxSpeed)
                this.getParams().currentSpeed = this.getParams().maxSpeed;
            //The speed of the vehicle cannot be greater than max speed of the highway
            if(this.getParams().currentSpeed >= speedLimit)
                this.getParams().currentSpeed = speedLimit;

            //Calculating position
            this.getParams().position = (this.getParams().position + updateTimeInMilisec * this.getParams().currentSpeed) % Constant.PANEL_WIDTH;

        }
        //Reducing Speed
        else if (decisionSpeed == Decision.REDUCING_SPEED) {

            //Calculating the current speed depend on the acceleration
            this.getParams().currentSpeed -= this.getParams().acceleration * updateTimeInMilisec;

            //Calculating position
            this.getParams().position = (this.getParams().position + updateTimeInMilisec * this.getParams().currentSpeed) % Constant.PANEL_WIDTH;

        }

        //Calculating distance to overtaking
        this.getParams().distanceToOvertaking = (int) Math.ceil((Math.pow(this.getParams().currentSpeed, 2) * this.getParams().brakingFactor));

    }

    /**
     * Executes changing lane decision
     */
    private void executeDecisionLane(Decision decisionLane) {

        //Changing to the left lane
        if (decisionLane == Decision.CHANGING_LANE) {
            this.getParams().laneOfTraffic = this.getParams().laneOfTraffic - 1;
        }
        //Changing to the right lane
        else if (decisionLane == Decision.BACK_TO_THE_RIGHT_LANE) {
            this.getParams().laneOfTraffic = this.getParams().laneOfTraffic + 1;
        }

    }

    /**
     * Stores information about vehicle types
     */
    public enum Type {
        CAR,
        MOTORCYCLE,
        TRUCK,
        BUS
    }

    /**
     * Stores information about possible drivers decision
     */
    public enum Decision {
        REDUCING_SPEED,
        ACCELERATING,
        CHANGING_LANE,
        BACK_TO_THE_RIGHT_LANE
    }

}

