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
    Coordinate[][] startingCoordinateArray = new Coordinate[2][2];
    Position[][] safeZonePositionsArray = new Position[2][2];
    Position[][] boxPositionsArray = new Position[2][2];

    static final int RED = 0;
    static final int BLUE = 1;
    static final int ALLIANCE = BLUE;
    static final int CORNER = 0;
    static final int OFF_CENTER = 1;
    static final int BALANCING_STONE = OFF_CENTER;
    static final int SAFEZONE_1 = 0;
    static final int SAFEZONE_2 = 1;
    static final int BOX_1 = 0;
    static final int BOX_2 =  1;

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
        stateEnabled[DRIVE_INTO_SAFEZONE] = false;
        stateEnabled[DRIVE_TO_BOX] = true;
        stateEnabled[PLACE_GLYPH] = false;
        stateEnabled[END] = true;
    }

    int currentState;
    String currentStateDesc;
    String subStateDesc = "";

    boolean performStateEntry;
    boolean performStateExit;

    //Timers
    SimpleTimer swivelStoreToMiddleTimer = new SimpleTimer();
    SimpleTimer swivelMiddleToStoreTimer = new SimpleTimer();
    SimpleTimer jewelKnockTimer = new SimpleTimer();
    SimpleTimer jewelKnockTimer2 = new SimpleTimer();
    SimpleTimer armUpToDown = new SimpleTimer();
    SimpleTimer armDownToUpTimer = new SimpleTimer();
    SimpleTimer jewelDeadmanTimer = new SimpleTimer();

    boolean isJewelAllianceColor;
    boolean hasJewelBeenDetected;

    //Timing Constants
    //TODO: actually set these
    static final double SWIVEL_STORING_DELAY = 0.75;
    static final double JEWEL_KNOCK_DELAY = 0.55;
    static final double JEWEL_KNOCK_DELAY2 = 0.4;
    static final double ARM_FOLD_DELAY = 0.9;
    static final double JEWEL_DETECTION_DEADMAN = 2.0;


    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        currentState = INIT;

        performStateEntry = true;
        performStateExit = false;

        //starting coordinate array
        startingCoordinateArray[RED][CORNER] = new Coordinate(-1200, -1200, 150, 0); //lower right
        startingCoordinateArray[RED][OFF_CENTER] = new Coordinate(600, -1200, 150, 0); //upper right
        startingCoordinateArray[BLUE][CORNER] = new Coordinate(-1200, 1200, 150, 180); //lower left
        startingCoordinateArray[BLUE][OFF_CENTER] = new Coordinate(600, 1200, 150, 0); //upper left

        //safe zone positions array
        safeZonePositionsArray[RED][SAFEZONE_1] = new Position(-300, -1200, 150); //mid right
        safeZonePositionsArray[RED][SAFEZONE_2] = new Position(1200, -900, 150); //upper right
        safeZonePositionsArray[BLUE][SAFEZONE_1] = new Position(-300, 1200, 150); //mid left
        safeZonePositionsArray[BLUE][SAFEZONE_2] = new Position(1200, 900, 150); //upper left

        //box positions array
        boxPositionsArray[RED][BOX_1] = new Position(-300, -1500, 150); //mid right
        boxPositionsArray[RED][BOX_2] = new Position(1500, -900, 150); //upper right
        boxPositionsArray[BLUE][BOX_1] = new Position(-300, 1500, 150); //mid left
        boxPositionsArray[BLUE][BOX_2] = new Position(1500, 900, 150); //upper left

        defineStateEnabledStatus();

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
                    swivelStoreToMiddleTimer.set(SWIVEL_STORING_DELAY);
                    performStateEntry = false;
                    hasJewelBeenDetected = false;
                    subStateDesc = "entry";
                }
                if(!swivelStoreToMiddleTimer.isExpired()){
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.MIDDLE);
                    armUpToDown.set(ARM_FOLD_DELAY);
                    subStateDesc = "swivel to middle";
                }
                else if (!armUpToDown.isExpired()) {
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.DOWN);
                    jewelDeadmanTimer.set(JEWEL_DETECTION_DEADMAN);
                    subStateDesc = "arm up to down";
                }
                else if (!hasJewelBeenDetected & !jewelDeadmanTimer.isExpired()) {
                    /*checks if the color detected to the left of the swivel (assuming the color sensor is mounted on the left)
                    **matches the alliance, and if so, swings left, otherwise swings right
                     */
                    subStateDesc = "detecting jewel color";
                    if(robot.jewelPusher.getJewelColor() == JewelPusher.JewelColor.ERROR){
                        //do nothing
                    }
                    else if (robot.jewelPusher.getJewelColor().ordinal() == ALLIANCE) {
                        isJewelAllianceColor = true;
                        hasJewelBeenDetected = true;
                    } else {
                        isJewelAllianceColor = false;
                        hasJewelBeenDetected = true;
                    }
                    jewelKnockTimer.set(JEWEL_KNOCK_DELAY);
                }
                else if (!jewelKnockTimer.isExpired()) {
                    subStateDesc = "knocking jewel";

                    if(isJewelAllianceColor){
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.RIGHT);
                    }
                    else if (!isJewelAllianceColor) {
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.LEFT);
                    } else {
                        //do nothing
                    }
                    jewelKnockTimer2.set(JEWEL_KNOCK_DELAY2);
                }
                else if (!jewelKnockTimer2.isExpired()) {
                    subStateDesc = "resetting swivel to middle";
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.MIDDLE);
                    armDownToUpTimer.set(ARM_FOLD_DELAY);
                }
                else if (!armDownToUpTimer.isExpired()) {
                    subStateDesc = "arm down to up";
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.UP);
                    swivelMiddleToStoreTimer.set(SWIVEL_STORING_DELAY);
                } else if (!swivelMiddleToStoreTimer.isExpired()){
                    subStateDesc = "swivel middle to store end";
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.END_STORED);
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
                if(performStateEntry){
                    robot.driveToTarget(safeZonePositionsArray[ALLIANCE][BALANCING_STONE]);
                    performStateEntry = false;
                }

                if(robot.driveToTargetInProgress()){
                    robot.driveToTarget(safeZonePositionsArray[ALLIANCE][BALANCING_STONE]);
                } else {
                    performStateExit = true;
                }

                if(performStateExit){
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
                break;
            case DRIVE_TO_BOX:
                currentStateDesc = "driving to box while scanning target";
                if (performStateEntry) {
                    robot.driveToTarget(boxPositionsArray[ALLIANCE][BALANCING_STONE]);
                }

                if (robot.driveToTargetInProgress()) {
                    robot.driveToTarget(boxPositionsArray[ALLIANCE][BALANCING_STONE]);
                } else {
                    performStateExit = true;
                }

                if (performStateExit) {
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
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
        telemetry.addData("Current State", currentStateDesc + ", " + subStateDesc);
        telemetry.addData("Current State Number:", currentState);
        telemetry.addData("Jewel Color:", robot.jewelPusher.getJewelColor());
        telemetry.addData("Jewel Matches Alliance?", robot.jewelPusher.getJewelColor().ordinal() == ALLIANCE);
        telemetry.addData("DriveToTarget in progress: ", robot.driveToTargetInProgress());
        telemetry.addData("RotateToTarget in progress: ", robot.rotateToTargetInProgress());
        telemetry.addData("IMU", robot.imu.getHeading());
        telemetry.addData("X", robot.getCoordinate().getX());
        telemetry.addData("Y", robot.getCoordinate().getY());
        telemetry.addData("Z", robot.getCoordinate().getZ());
        telemetry.addData("Heading", robot.getCoordinate().getHeading());
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