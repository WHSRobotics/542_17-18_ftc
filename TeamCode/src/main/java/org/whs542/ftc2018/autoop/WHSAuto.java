package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
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
    Position[][][] safeZonePositionsArray = new Position[2][2][3];
    Position[] waypointPositionsArray = new Position[2];
    Position[][] boxPositionsArray = new Position[2][2];

    static final int RED = 0;
    static final int BLUE = 1;
    static final int ALLIANCE = RED;
    static final int CORNER = 0;
    static final int OFF_CENTER = 1;
    static final int BALANCING_STONE = CORNER;
    static final int SAFEZONE_1 = 0;
    static final int SAFEZONE_2 = 1;
    static final int BOX_1 = 0;
    static final int BOX_2 =  1;
    static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;

    //State Definitions
    static final int INIT = 0;
    static final int HIT_JEWEL = 1;
    static final int DRIVE_TO_VUFORIA = 2;
    static final int DRIVE_INTO_SAFEZONE = 3;
    static final int DRIVE_TO_BOX = 4;
    static final int SECURE_GLYPH = 5;
    static final int END = 6;

    static final int NUM_OF_STATES = 7;

    boolean[] stateEnabled = new boolean[NUM_OF_STATES];

    public void defineStateEnabledStatus() {
        stateEnabled[INIT] = true;
        stateEnabled[HIT_JEWEL] = true;
        stateEnabled[DRIVE_TO_VUFORIA] = true;
        stateEnabled[DRIVE_INTO_SAFEZONE] = true;
        stateEnabled[DRIVE_TO_BOX] = true;
        stateEnabled[SECURE_GLYPH] = true;
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
    SimpleTimer operateLiftTimer = new SimpleTimer();
    SimpleTimer driveAwayTimer = new SimpleTimer();
    SimpleTimer driveInTimer = new SimpleTimer();
    SimpleTimer driveOutTimer = new SimpleTimer();
    SimpleTimer vuforiaDriveTimer = new SimpleTimer();
    SimpleTimer vuforiaDetectionDeadmanTimer = new SimpleTimer();

    //boolean isJewelAllianceColor;
    enum JewelDetection {
        MATCH, NOT_MATCH, ERROR
    }
    //double SWIVEL_OFFSET = 0;
    //double SWIVEL_OFFSET_MAX = 0.05;
    JewelDetection jewelDetection;
    boolean hasJewelBeenDetected;
    boolean rotateToBoxComplete = false;

    boolean hasTargetBeenDetected;
    RelicRecoveryVuMark vuforiaReading = RelicRecoveryVuMark.UNKNOWN;
    int column = 0;

    //Timing Constants
    static final double SWIVEL_STORING_DELAY = 0.75;
    static final double JEWEL_KNOCK_DELAY = 0.75;
    static final double JEWEL_KNOCK_DELAY2 = 0.4;
    static final double ARM_FOLD_DELAY = 0.9;
    static final double JEWEL_DETECTION_DEADMAN = 5.42;
    static final double OPERATE_LIFT_DELAY = 1.0;
    static final double DRIVE_AWAY_DURATION = 1.2;
    static final double VUFORIA_DRIVE_DURATION = 2.5;
    static final double VUFORIA_DETECTION_DEADMAN = 5.42;

    //jank variables ( ͡° ͜ʖ ͡°)
    Position p;
    Position q;
    Position s;
    private double x;
    private double y;
    boolean initializeDriveToSafezone = true;

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
        startingCoordinateArray[BLUE][OFF_CENTER] = new Coordinate(600, 1200, 150, 180); //upper left

        //safe zone positions array
        safeZonePositionsArray[RED][SAFEZONE_1][LEFT] = new Position(-145, -1200, 150);
        safeZonePositionsArray[RED][SAFEZONE_1][CENTER] = new Position(-300, -1200, 150); //mid right
        safeZonePositionsArray[RED][SAFEZONE_1][RIGHT] = new Position(-520, -1200, 150);

        safeZonePositionsArray[RED][SAFEZONE_2][LEFT] = new Position(1200, -700, 150);
        safeZonePositionsArray[RED][SAFEZONE_2][CENTER] = new Position(1200, -900, 150); //upper right
        safeZonePositionsArray[RED][SAFEZONE_2][RIGHT] = new Position(1200, -1100, 150);

        safeZonePositionsArray[BLUE][SAFEZONE_1][LEFT] = new Position(/*-442*/-520, 1200, 150); //mid left
        safeZonePositionsArray[BLUE][SAFEZONE_1][CENTER] = new Position(/*-250*/-300, 1200, 150); //mid left
        safeZonePositionsArray[BLUE][SAFEZONE_1][RIGHT] = new Position(/*-58*/-145, 1200, 150); //mid left

        safeZonePositionsArray[BLUE][SAFEZONE_2][LEFT] = new Position(1200, 1100, 150);
        safeZonePositionsArray[BLUE][SAFEZONE_2][CENTER] = new Position(1200, 900, 150); //upper left
        safeZonePositionsArray[BLUE][SAFEZONE_2][LEFT] = new Position(1200, 700, 150);

        //waypoint positions
        waypointPositionsArray[RED] = new Position(1200, -1200, 150);
        waypointPositionsArray[BLUE] = new Position(1200, 1200, 150);

        //box positions array
        boxPositionsArray[RED][BOX_1] = new Position(-350, -1400, 150); //mid right
        boxPositionsArray[RED][BOX_2] = new Position(1400, -900, 150); //upper right
        boxPositionsArray[BLUE][BOX_1] = new Position(-250, 1400, 150); //mid left
        boxPositionsArray[BLUE][BOX_2] = new Position(1400, 900, 150); //upper left

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
                        jewelDetection = JewelDetection.ERROR;
                        /*if (SWIVEL_OFFSET < SWIVEL_OFFSET_MAX) {
                            robot.jewelPusher.operateSwivel(robot.jewelPusher.SWIVEL_POSITIONS[CENTER] - SWIVEL_OFFSET);
                            SWIVEL_OFFSET += 0.005;
                        }*/
                    }
                    else if (robot.jewelPusher.getJewelColor().ordinal() == ALLIANCE) {
                        jewelDetection = JewelDetection.MATCH;
                        hasJewelBeenDetected = true;
                    } else {
                        jewelDetection = JewelDetection.NOT_MATCH;
                        hasJewelBeenDetected = true;
                    }
                    jewelKnockTimer.set(JEWEL_KNOCK_DELAY);
                }
                else if (!jewelKnockTimer.isExpired()) {
                    subStateDesc = "knocking jewel";

                    if(jewelDetection == JewelDetection.MATCH){
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.RIGHT);
                    }
                    else if (jewelDetection == JewelDetection.NOT_MATCH) {
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
            case DRIVE_TO_VUFORIA:
                currentStateDesc = "driving to and stopping to scan vuforia";
                if(performStateEntry){
                    vuforiaDriveTimer.set(VUFORIA_DRIVE_DURATION);
                    performStateEntry = false;
                    hasTargetBeenDetected = false;
                    robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }

                if(!vuforiaDriveTimer.isExpired()){
                    if(ALLIANCE == RED) {
                        robot.drivetrain.operate(0.15, 0.15);
                        //vuforiaReading = robot.vuforia.getVuforiaReading();
                        //if(vuforiaReading != RelicRecoveryVuMark.UNKNOWN){
                            //vuforiaReading = robot.vuforia.getVuforiaReading();
                            //hasTargetBeenDetected = true;
                        //}
                    }
                    else if(ALLIANCE == BLUE){
                        robot.drivetrain.operate(-0.15, -0.15);
                        vuforiaReading = robot.vuforia.getVuforiaReading();
                        if(vuforiaReading != RelicRecoveryVuMark.UNKNOWN){
                            vuforiaReading = robot.vuforia.getVuforiaReading();
                            hasTargetBeenDetected = true;
                        }
                    }
                    vuforiaDetectionDeadmanTimer.set(VUFORIA_DETECTION_DEADMAN);
                }
                else if (!hasTargetBeenDetected && !vuforiaDetectionDeadmanTimer.isExpired()){
                    robot.drivetrain.operate(0.0, 0.0);
                    vuforiaReading = robot.vuforia.getVuforiaReading();
                    if(vuforiaReading != RelicRecoveryVuMark.UNKNOWN){
                        vuforiaReading = robot.vuforia.getVuforiaReading();
                        hasTargetBeenDetected = true;
                    }
                }
                /*else if (robot.balancingStoneSensor.balancingStoneDetected(ALLIANCE)) {
                    if (ALLIANCE == RED) {
                        robot.drivetrain.operate(0.15, 0.15);
                    }
                    else if(ALLIANCE == BLUE) {
                        robot.drivetrain.operate(-0.15, -0.15);
                    }
                }*/
                else {
                    performStateExit = true;
                }


                if(performStateExit){
                    robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
                break;
            case DRIVE_INTO_SAFEZONE:
                currentStateDesc = "driving off platform into safe zone";
                if(performStateEntry){
                    /*switch (vuforiaReading){
                        case LEFT:
                            column = LEFT;
                            break;
                        case CENTER:
                            column = CENTER;
                            break;
                        case RIGHT:
                            column = RIGHT;
                            break;
                        case UNKNOWN:
                            column = CENTER;
                            break;
                        default:
                            column = CENTER;
                            break;
                    }*/
                    if (vuforiaReading == RelicRecoveryVuMark.LEFT) {
                        column = LEFT;
                    } else if (vuforiaReading == RelicRecoveryVuMark.RIGHT) {
                        column = RIGHT;
                    } else {
                        column = CENTER;
                    }

                    if(ALLIANCE == RED){
                        p = safeZonePositionsArray[RED][SAFEZONE_1][column];
                    }
                    if(ALLIANCE == BLUE){
                        if (BALANCING_STONE == CORNER) {
                            p = safeZonePositionsArray[BLUE][SAFEZONE_1][column];
                        }
                        else if (BALANCING_STONE == OFF_CENTER) {
                            p = waypointPositionsArray[BLUE];
                            s = safeZonePositionsArray[BLUE][SAFEZONE_2][column];
                        }
                    }
                    robot.driveToTarget(p);
                    performStateEntry = false;
                }

                if(robot.driveToTargetInProgress() || robot.rotateToTargetInProgress()) {
                    robot.driveToTarget(p);
                }
                else if (BALANCING_STONE == OFF_CENTER && initializeDriveToSafezone) {
                    robot.driveToTarget(s);
                    initializeDriveToSafezone = false;
                }
                else if (robot.driveToTargetInProgress() || robot.rotateToTargetInProgress()) {
                    robot.driveToTarget(s);
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
                currentStateDesc = "driving to box";
                if (performStateEntry) {
                    x = robot.getCoordinate().getX();
                    y = robot.getCoordinate().getY();
                    if(ALLIANCE == RED){
                        q = boxPositionsArray[RED][BOX_1];
                        robot.driveToTarget(new Position(x, q.getY(), 150));
                    }
                    if(ALLIANCE == BLUE){
                        if (BALANCING_STONE == CORNER) {
                            q = boxPositionsArray[BLUE][BOX_1];
                            robot.driveToTarget(new Position(x, q.getY(), 150));
                        }
                        else if (BALANCING_STONE == OFF_CENTER) {
                            q = boxPositionsArray[BLUE][BOX_2];
                            robot.driveToTarget(new Position(q.getX(), y, 150));
                        }
                    }
                    performStateEntry = false;
                }

                if (robot.driveToTargetInProgress() || robot.rotateToTargetInProgress()) {
                    robot.driveToTarget(new Position(x, q.getY(), 150));
                    operateLiftTimer.set(OPERATE_LIFT_DELAY);
                } else if (!operateLiftTimer.isExpired()) {
                    subStateDesc = "placing glyph";
                    robot.lift.operateLift(false, 1f);
                    driveAwayTimer.set(DRIVE_AWAY_DURATION*2.1);
                } else if (!driveAwayTimer.isExpired()) {
                    robot.lift.operateLift(false, 1f);
                    robot.drivetrain.operate(-0.15, -0.15);
                } else {
                    performStateExit = true;
                }

                if (performStateExit) {
                    robot.drivetrain.operate(0.0, 0.0);
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
                break;
            case SECURE_GLYPH:
                currentStateDesc = "securing glyph";
                if (performStateEntry) {
                    robot.lift.operateLift(false, 0f);
                    driveInTimer.set(DRIVE_AWAY_DURATION);
                    performStateEntry = false;
                }

                if (!driveInTimer.isExpired()) {
                    robot.drivetrain.operate(0.3, 0.3);
                    driveOutTimer.set(DRIVE_AWAY_DURATION/2.9);
                }
                else if (!driveOutTimer.isExpired()) {
                    robot.drivetrain.operate(-0.3, -0.3);
                }
                else {
                    robot.drivetrain.operate(0, 0);
                    performStateExit = true;
                }

                if (performStateExit) {
                    performStateExit = false;
                    performStateEntry = true;
                    advanceState();
                }
                break;
            case END:
                currentStateDesc = "we made it?!";
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
        telemetry.addData("Column: ", column);
        telemetry.addData("Vuforia Reading: ", vuforiaReading);
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