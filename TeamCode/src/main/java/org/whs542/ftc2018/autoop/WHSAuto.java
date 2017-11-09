package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.util.Coordinate;
import org.whs542.util.Position;
import org.whs542.util.SimpleTimer;

/**
 * Created by Jason on 11/03/2017.
 */

@Autonomous(name = "WHSAuto", group = "auto")
public class WHSAuto extends OpMode {

    WHSRobotImpl robot;

    //coordinates and positions
    static Coordinate[][] startingCoordinateArray = new Coordinate[2][2];
    static Position[][] safeZonePositionsArray = new Position[2][2];

    static final int RED = 0;
    static final int BLUE = 1;
    static final int ALLIANCE = RED;
    static final int CORNER = 0;
    static final int OFF_CENTER = 1;
    static final int BALANCING_STONE = CORNER;
    static final int SAFEZONE_1 = 0;
    static final int SAFEZONE_2 = 1;

    //State Definitions
    static final int INIT = 0;
    static final int HIT_JEWEL = 1;
    static final int DRIVE_INTO_SAFEZONE = 2;
    static final int DRIVE_TO_BOX = 3;
    static final int PLACE_GLYPH = 4;
    static final int END = 5;

    static final int NUM_OF_STATES = 6;

    boolean[] stateEnabled = new boolean[NUM_OF_STATES];

    public void defineStateEnabledStatus() {
        stateEnabled[INIT] = true;
        stateEnabled[HIT_JEWEL] = true;
        stateEnabled[DRIVE_INTO_SAFEZONE] = true;
        stateEnabled[DRIVE_TO_BOX] = false;
        stateEnabled[PLACE_GLYPH] = false;
        stateEnabled[END] = true;
    }

    int currentState;
    String currentStateDesc;

    boolean performStateEntry;
    boolean performStateExit;

    //Timers
    SimpleTimer swivelTimer = new SimpleTimer();
    SimpleTimer armTimer = new SimpleTimer();
    SimpleTimer jewelDeadmanTimer = new SimpleTimer();

    //Timing Constants
    //TODO: actually set these
    static final double SWIVEL_STORE_TO_MIDDLE_DELAY = 1.0;
    static final double SWIVEL_KNOCK_DELAY = 0.5;
    static final double ARM_UNFOLD_DELAY = 0.75;
    static final double ARM_FOLD_DELAY = 0.75;
    static final double JEWEL_DETECTION_DEADMAN = 2.0;


    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        currentState = INIT;

        performStateEntry = true;
        performStateExit = false;

        //starting coordinate array
        Coordinate[][] startingCoordinates = new Coordinate[2][2];
        startingCoordinates[RED][CORNER] = new Coordinate(200, 200, 150, 90); //upper left
        startingCoordinates[RED][OFF_CENTER] = new Coordinate(200, -100, 150, 90); //lower left
        startingCoordinates[BLUE][CORNER] = new Coordinate(-200, 200, 150, 90); //upper right
        startingCoordinates[BLUE][OFF_CENTER] = new Coordinate(-200, -100, 150, 270); //lower right

        //safe zone positions array
        Position[][] safeZonePositions = new Position[2][2];
        safeZonePositions[RED][SAFEZONE_1] = new Position(-250, 50, 150); //mid left
        safeZonePositions[RED][SAFEZONE_2] = new Position(-150, -250, 150); //lower left
        safeZonePositions[BLUE][SAFEZONE_1] = new Position(250, 50, 150); //mid right
        safeZonePositions[BLUE][SAFEZONE_2] = new Position(150, -250, 150); //lower right

        //setting positions

        telemetry.setMsTransmissionInterval(50); //set driver station update frequency
        telemetry.log().setCapacity(6); //set max number of lines logged by telemetry
    }

    @Override
    public void loop() {

        robot.estimateHeading();
        robot.estimatePosition();

        //State Machine
        switch (currentState) {
            case INIT:
                currentStateDesc = "beginning AutoOp";
                robot.setInitialCoordinate(startingCoordinateArray[ALLIANCE][BALANCING_STONE]);
                advanceState();
                break;
            case HIT_JEWEL:
                //State Entry
                currentStateDesc = "hitting jewel";
                robot.jewelPusher.operateArm(1.0);
                if (robot.jewelPusher.getJewelColor().ordinal() == ALLIANCE) {
                    robot.jewelPusher.operateSwivel(1.0);
                }
                break;
            case DRIVE_INTO_SAFEZONE:
                currentStateDesc = "driving off platform into safe zone";
                if (ALLIANCE == RED) {
                    if (robot.getCoordinate() == startingCoordinateArray[RED][CORNER]) {
                        //drive to mid left
                        robot.driveToTarget(safeZonePositionsArray[RED][SAFEZONE_1]);
                    } else {
                        //drive to lower left
                        robot.driveToTarget(safeZonePositionsArray[RED][SAFEZONE_2]);
                    }
                } else if (ALLIANCE == BLUE) {
                    //drive to mid right
                    robot.driveToTarget(safeZonePositionsArray[RED][SAFEZONE_1]);
                }
                advanceState();
                break;
            case DRIVE_TO_BOX:
                currentStateDesc = "driving to box while scanning target";
                advanceState();
                break;
            case PLACE_GLYPH:
                currentStateDesc = "moving glyph";
                advanceState();
                break;
            case END:
                currentStateDesc = "hooray we made it!";
            default: break;
        }

        //Logging the current state
        telemetry.addData("Current State", currentStateDesc);
    }

    public void advanceState() {
        if(stateEnabled[(currentState + 1)]) {
            currentState = currentState + 1;
        }
        else {
            currentState = currentState + 1;
            advanceState();
        }
    }
}