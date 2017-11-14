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
    SimpleTimer armUpToMiddleTimer = new SimpleTimer();
    SimpleTimer armMiddleToDownTimer = new SimpleTimer();
    SimpleTimer armDownToMiddleTimer = new SimpleTimer();
    SimpleTimer armMiddleToUpTimer = new SimpleTimer();
    SimpleTimer jewelDeadmanTimer = new SimpleTimer();

    //Timing Constants
    //TODO: actually set these
    static final double SWIVEL_STORE_TO_MIDDLE_DELAY = 0.5;
    static final double SWIVEL_KNOCK_DELAY = 0.5;
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
        startingCoordinates[RED][CORNER] = new Coordinate(-1200, -1200, 150, 0); //lower right
        startingCoordinates[RED][OFF_CENTER] = new Coordinate(600, -1200, 150, 0); //upper right
        startingCoordinates[BLUE][CORNER] = new Coordinate(-1200, 1200, 150, 180); //lower left
        startingCoordinates[BLUE][OFF_CENTER] = new Coordinate(600, 1200, 150, 0); //upper left

        //safe zone positions array
        Position[][] safeZonePositions = new Position[2][2];
        safeZonePositions[BLUE][SAFEZONE_2] = new Position(-300, 1500, 150); //mid right
        safeZonePositions[BLUE][SAFEZONE_1] = new Position(1500, 900, 150); //upper right
        safeZonePositions[RED][SAFEZONE_2] = new Position(-300, -1500, 150); //mid left
        safeZonePositions[RED][SAFEZONE_1] = new Position(1500, -900, 150); //upper left

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
                if (performStateEntry) {
                    armUpToMiddleTimer.set(ARM_FOLD_DELAY);
                    performStateEntry = false;
                }
                if (!armUpToMiddleTimer.isExpired()) {
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.MIDDLE);
                    armMiddleToDownTimer.set(ARM_FOLD_DELAY);
                } else if (!armMiddleToDownTimer.isExpired()) {
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.DOWN);
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.MIDDLE);
                    jewelDeadmanTimer.set(JEWEL_DETECTION_DEADMAN);
                } else if (!jewelDeadmanTimer.isExpired()) {
                    /*checks if the color detected to the left of the swivel (assuming the color sensor is mounted on the left)
                    **matches the alliance, and if so, swings left, otherwise swings right
                     */
                    if (robot.jewelPusher.getJewelColor().ordinal() == ALLIANCE) {
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.LEFT);
                    } else {
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.RIGHT);
                    }
                    armDownToMiddleTimer.set(ARM_FOLD_DELAY);
                } else if (!armDownToMiddleTimer.isExpired()) {
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.MIDDLE);
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.MIDDLE);
                    armMiddleToUpTimer.set(ARM_FOLD_DELAY);
                } else if (!armMiddleToUpTimer.isExpired()) {
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.STORED);
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.UP);
                } else {
                    performStateExit = true;
                }
                if (performStateExit) {
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
                break;
            case DRIVE_INTO_SAFEZONE:
                currentStateDesc = "driving off platform into safe zone";
                //Change this

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
            default:
                break;
        }

        //Logging the current state
        telemetry.addData("Current State", currentStateDesc);
    }

    public void advanceState() {
        if (stateEnabled[(currentState + 1)]) {
            currentState = currentState + 1;
        } else {
            currentState = currentState + 1;
            advanceState();
        }
    }
}