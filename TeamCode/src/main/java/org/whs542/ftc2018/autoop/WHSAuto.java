package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.whs542.ftc2018.subsys.IMU;
import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.subsys.vlift.VLift;
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
    static final int BOX_2 = 1;
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
    static final int SECOND_GLYPH = 6;
    static final int END = 7;

    static final int NUM_OF_STATES = 8;

    boolean[] stateEnabled = new boolean[NUM_OF_STATES];

    public void defineStateEnabledStatus() {
        stateEnabled[INIT] = true;
        stateEnabled[HIT_JEWEL] = true;
        stateEnabled[DRIVE_TO_VUFORIA] = true;
        stateEnabled[DRIVE_INTO_SAFEZONE] = true;
        stateEnabled[DRIVE_TO_BOX] = true;
        stateEnabled[SECURE_GLYPH] = true;
        stateEnabled[SECOND_GLYPH] = true;
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
    SimpleTimer imuResetTimer = new SimpleTimer();
    SimpleTimer driveToBoxTimer = new SimpleTimer();

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
    int column2 = 0;

    //Timing Constants
    static final double SWIVEL_STORING_DELAY = 0.25;
    static final double JEWEL_KNOCK_DELAY = 0.75;
    static final double JEWEL_KNOCK_DELAY2 = 0.4;
    static final double ARM_FOLD_DELAY = 0.9;
    static final double JEWEL_DETECTION_DEADMAN = 1.0;
    static final double OPERATE_LIFT_DELAY = 1.0;
    static final double DRIVE_AWAY_DURATION = 2.5;
    static final double DRIVE_IN_DURATION = 1.5;
    static final double DRIVE_OUT_DURATION = 0.6;
    static final double VUFORIA_DRIVE_DURATION = 1.25;
    static final double VUFORIA_DETECTION_DEADMAN = 2.0;
    static final double IMU_RESET_DURATION = 4.2;
    static final double DRIVE_TO_BOX_DURATION = 2.5;

    //jank variables ( ͡° ͜ʖ ͡°)
    Position p1;
    Position p2;
    Position p3;
    boolean drivingToP2;
    boolean drivingToGlyphPit;
    private double x;
    private double y;
    boolean initializeDriveToSafezone = false;
    boolean initializeDriveToBox = false;
    boolean initializeResetIMU = true;
    double lastImuReading = 0.0;
    boolean imuResetComplete = false;
    double[] driveToTargetPowerLevels = new double[4];

    private double finishTime;
    private double currentTime;

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
        safeZonePositionsArray[RED][SAFEZONE_1][LEFT] = new Position(-100, -1200, 150);
        safeZonePositionsArray[RED][SAFEZONE_1][CENTER] = new Position(-300, -1200, 150); //mid right
        safeZonePositionsArray[RED][SAFEZONE_1][RIGHT] = new Position(-475, -1200, 150);

        safeZonePositionsArray[RED][SAFEZONE_2][LEFT] = new Position(1265, -670, 150);
        safeZonePositionsArray[RED][SAFEZONE_2][CENTER] = new Position(1265, -850, 150); //upper right
        safeZonePositionsArray[RED][SAFEZONE_2][RIGHT] = new Position(1265, -1000, 150);

        safeZonePositionsArray[BLUE][SAFEZONE_1][LEFT] = new Position(/*-442*/-460, 1200, 150); //mid left
        safeZonePositionsArray[BLUE][SAFEZONE_1][CENTER] = new Position(/*-250*/-280, 1200, 150); //mid left
        safeZonePositionsArray[BLUE][SAFEZONE_1][RIGHT] = new Position(/*-58*/-100, 1200, 150); //mid left

        safeZonePositionsArray[BLUE][SAFEZONE_2][LEFT] = new Position(1265, 1000, 150);
        safeZonePositionsArray[BLUE][SAFEZONE_2][CENTER] = new Position(1265, 850, 150); //upper left
        safeZonePositionsArray[BLUE][SAFEZONE_2][RIGHT] = new Position(1265, 670, 150);

        //waypoint positions
        waypointPositionsArray[RED] = new Position(1265, -1200, 150);
        waypointPositionsArray[BLUE] = new Position(1265, 1200, 150);

        //box positions array
        boxPositionsArray[RED][BOX_1] = new Position(-350, -1400, 150); //mid right
        boxPositionsArray[RED][BOX_2] = new Position(1400, -900, 150); //upper right
        boxPositionsArray[BLUE][BOX_1] = new Position(-250, 1400, 150); //mid left
        boxPositionsArray[BLUE][BOX_2] = new Position(1400, 900, 150); //upper left

        driveToTargetPowerLevels[0] = 0.5;
        driveToTargetPowerLevels[1] = 0.65;
        driveToTargetPowerLevels[2] = 0.7;
        driveToTargetPowerLevels[3] = 0.85;

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
                robot.lift.operateGate(VLift.GatePosition.CLOSED);
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
                if (!swivelStoreToMiddleTimer.isExpired()) {
                    subStateDesc = "swivel to middle";
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.MIDDLE);
                    armUpToDown.set(ARM_FOLD_DELAY);
                } else if (!armUpToDown.isExpired()) {
                    subStateDesc = "arm up to down";
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.DOWN);
                    jewelDeadmanTimer.set(JEWEL_DETECTION_DEADMAN);
                } else if (!hasJewelBeenDetected & !jewelDeadmanTimer.isExpired()) {
                    /*checks if the color detected to the left of the swivel (assuming the color sensor is mounted on the left)
                    **matches the alliance, and if so, swings left, otherwise swings right
                     */
                    subStateDesc = "detecting jewel color";
                    if (robot.jewelPusher.getJewelColor() == JewelPusher.JewelColor.ERROR) {
                        jewelDetection = JewelDetection.ERROR;
                        /*if (SWIVEL_OFFSET < SWIVEL_OFFSET_MAX) {
                            robot.jewelPusher.operateSwivel(robot.jewelPusher.SWIVEL_POSITIONS[CENTER] - SWIVEL_OFFSET);
                            SWIVEL_OFFSET += 0.005;
                        }*/
                    } else if (robot.jewelPusher.getJewelColor().ordinal() == ALLIANCE) {
                        jewelDetection = JewelDetection.MATCH;
                        hasJewelBeenDetected = true;
                    } else {
                        jewelDetection = JewelDetection.NOT_MATCH;
                        hasJewelBeenDetected = true;
                    }
                    jewelKnockTimer.set(JEWEL_KNOCK_DELAY);
                } else if (!jewelKnockTimer.isExpired()) {
                    subStateDesc = "knocking jewel";
                    if (jewelDetection == JewelDetection.MATCH) {
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.RIGHT);
                    } else if (jewelDetection == JewelDetection.NOT_MATCH) {
                        robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.LEFT);
                    } else {
                        //do nothing
                    }
                    jewelKnockTimer2.set(JEWEL_KNOCK_DELAY2);
                } else if (!jewelKnockTimer2.isExpired()) {
                    subStateDesc = "resetting swivel to middle";
                    robot.jewelPusher.operateSwivel(JewelPusher.SwivelPosition.MIDDLE);
                    armDownToUpTimer.set(ARM_FOLD_DELAY);
                } else if (!armDownToUpTimer.isExpired()) {
                    subStateDesc = "arm down to up";
                    robot.jewelPusher.operateArm(JewelPusher.ArmPosition.UP);
                    swivelMiddleToStoreTimer.set(SWIVEL_STORING_DELAY);
                } else if (!swivelMiddleToStoreTimer.isExpired()) {
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
                if (performStateEntry) {
                    vuforiaDriveTimer.set(VUFORIA_DRIVE_DURATION);
                    performStateEntry = false;
                    hasTargetBeenDetected = false;
                    robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }

                if (!vuforiaDriveTimer.isExpired()) {
                    subStateDesc = "driving to vuforia";
                    robot.drivetrain.operate(0.3, 0.3);
                    //scan while driving in case the robot drives too far
                    vuforiaReading = robot.vuforia.getVuforiaReading();
                    if (vuforiaReading != RelicRecoveryVuMark.UNKNOWN) {
                        vuforiaReading = robot.vuforia.getVuforiaReading();
                        hasTargetBeenDetected = true;
                    }
                    vuforiaDetectionDeadmanTimer.set(VUFORIA_DETECTION_DEADMAN);
                } else if (!hasTargetBeenDetected && !vuforiaDetectionDeadmanTimer.isExpired()) {
                    subStateDesc = "scanning vuforia target";
                    robot.drivetrain.operate(0.0, 0.0);
                    vuforiaReading = robot.vuforia.getVuforiaReading();
                    if (vuforiaReading != RelicRecoveryVuMark.UNKNOWN) {
                        vuforiaReading = robot.vuforia.getVuforiaReading();
                        hasTargetBeenDetected = true;
                    }
                }
                /*else if (robot.balancingStoneSensor.balancingStoneDetected(ALLIANCE)) {
                    subStateDesc = "driving off balancing stone";
                    if (ALLIANCE == RED) {
                        robot.drivetrain.operate(0.15, 0.15);
                    } else if (ALLIANCE == BLUE) {
                        robot.drivetrain.operate(-0.15, -0.15);
                    }
                }*/
                else {
                    performStateExit = true;
                }

                if (performStateExit) {
                    robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
                break;
            case DRIVE_INTO_SAFEZONE:
                currentStateDesc = "driving off platform into safe zone";
                if (performStateEntry) {
                    drivingToP2 = false;
                    if (vuforiaReading == RelicRecoveryVuMark.LEFT) {
                        column = LEFT;
                    } else if (vuforiaReading == RelicRecoveryVuMark.RIGHT) {
                        column = RIGHT;
                    } else {
                        column = CENTER;
                    }

                    if (BALANCING_STONE == CORNER) {
                        subStateDesc = "driving to safezone";
                        p1 = safeZonePositionsArray[ALLIANCE][SAFEZONE_1][column];
                    } else if (BALANCING_STONE == OFF_CENTER) {
                        subStateDesc = "driving to waypoint";
                        p1 = waypointPositionsArray[ALLIANCE];
                        p2 = safeZonePositionsArray[ALLIANCE][SAFEZONE_2][column];
                    }

                    robot.driveToTarget(p1, true);
                    performStateEntry = false;
                }

                if ((robot.driveToTargetInProgress() || robot.rotateToTargetInProgress()) && !drivingToP2) {
                    robot.driveToTarget(p1, true);
                } else if (BALANCING_STONE == OFF_CENTER && initializeResetIMU) {
                    subStateDesc = "resetting IMU";
                    if (ALLIANCE == RED) {
                        lastImuReading = robot.imu.getHeading();
                    } else {
                        lastImuReading = robot.imu.getHeading() + 180.0;
                    }
                    robot.imu = new IMU(hardwareMap);
                    initializeDriveToSafezone = true;
                    initializeResetIMU = false;
                    imuResetTimer.set(IMU_RESET_DURATION);
                } else if (BALANCING_STONE == OFF_CENTER && imuResetTimer.isExpired() && initializeDriveToSafezone) {
                    subStateDesc = "driving to safezone";
                    /*imuResetComplete makes sure that it won't advance states during the 5 seconds
                    it takes to reset imu*/
                    imuResetComplete = true;
                    robot.imu.setImuBias(lastImuReading);
                    robot.driveToTarget(p2, false);
                    initializeDriveToSafezone = false;
                    drivingToP2 = true;
                } else if ((robot.driveToTargetInProgress() || robot.rotateToTargetInProgress()) && drivingToP2) {
                    robot.driveToTarget(p2, false);
                } else if (BALANCING_STONE == CORNER || imuResetComplete) {
                    performStateExit = true;
                }

                if (performStateExit) {
                    performStateEntry = true;
                    performStateExit = false;
                    advanceState();
                }
                break;
            case DRIVE_TO_BOX:
                currentStateDesc = "driving to box and placing glyph";
                if (performStateEntry) {
                    x = robot.getCoordinate().getX();
                    y = robot.getCoordinate().getY();

                    if (BALANCING_STONE == OFF_CENTER) {
                        p3 = boxPositionsArray[ALLIANCE][BOX_2];
                        robot.driveToTarget(new Position(p3.getX(), y, 150), false);
                    }
                    performStateEntry = false;
                }

                if (BALANCING_STONE == OFF_CENTER && (robot.driveToTargetInProgress() || robot.rotateToTargetInProgress())) {
                    subStateDesc = "driving to box";
                    robot.driveToTarget(new Position(p3.getX(), y, 150), false);
                    operateLiftTimer.set(OPERATE_LIFT_DELAY);
                } else if (BALANCING_STONE == CORNER && initializeResetIMU) {
                    subStateDesc = "resetting IMU";
                    if (ALLIANCE == RED) {
                        lastImuReading = robot.imu.getHeading();
                    } else {
                        lastImuReading = robot.imu.getHeading() + 180;
                    }
                    robot.imu = new IMU(hardwareMap);
                    initializeResetIMU = false;
                    initializeDriveToBox = true;
                    imuResetTimer.set(IMU_RESET_DURATION);
                } else if (BALANCING_STONE == CORNER && imuResetTimer.isExpired() && initializeDriveToBox) {
                    subStateDesc = "driving to box";
                    imuResetComplete = true;
                    robot.imu.setImuBias(lastImuReading);
                    p3 = boxPositionsArray[ALLIANCE][BOX_1];
                    robot.driveToTarget(new Position(x, p3.getY(), 150), false);
                    initializeDriveToBox = false;
                } else if (BALANCING_STONE == CORNER && (robot.driveToTargetInProgress() || robot.rotateToTargetInProgress())) {
                    subStateDesc = "driving to box";
                    robot.driveToTarget(new Position(x, p3.getY(), 150), false);
                    operateLiftTimer.set(OPERATE_LIFT_DELAY);
                } else if (!operateLiftTimer.isExpired()) {
                    subStateDesc = "placing glyph";
                    robot.lift.operateLift(VLift.LiftPosition.UP);
                    robot.lift.operateGate(VLift.GatePosition.OPEN);
                    driveAwayTimer.set(DRIVE_AWAY_DURATION);
                } else if (!driveAwayTimer.isExpired()) {
                    subStateDesc = "driving away";
                    robot.lift.operateLift(VLift.LiftPosition.UP);
                    robot.lift.operateGate(VLift.GatePosition.OPEN);
                    robot.drivetrain.operate(-0.15, -0.15);
                } else if (BALANCING_STONE == OFF_CENTER || imuResetComplete) {
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
                    robot.lift.operateLift(VLift.LiftPosition.DOWN);
                    robot.lift.operateGate(VLift.GatePosition.MIDDLE);
                    driveInTimer.set(DRIVE_IN_DURATION);
                    performStateEntry = false;
                }

                if (!driveInTimer.isExpired()) {
                    subStateDesc = "pushing in glyph";
                    robot.drivetrain.operate(0.3, 0.3);
                    driveOutTimer.set(DRIVE_OUT_DURATION);
                } else if (!driveOutTimer.isExpired()) {
                    subStateDesc = "backing out";
                    robot.drivetrain.operate(-0.3, -0.3);
                } else {
                    robot.drivetrain.operate(0, 0);
                    performStateExit = true;
                }

                if (performStateExit) {
                    performStateExit = false;
                    performStateEntry = true;
                    advanceState();
                }
                break;
            case SECOND_GLYPH:
                currentStateDesc = "scoring second glyph";
                if (performStateEntry) {
                    if ((ALLIANCE != RED) || (BALANCING_STONE != CORNER)) {
                        performStateExit = true;
                    }
                    robot.setPosition(safeZonePositionsArray[ALLIANCE][BALANCING_STONE][column]);
                    if (column == LEFT) {
                        column2 = CENTER;
                        robot.driveToTarget(new Position(-300, -150, 150), true, driveToTargetPowerLevels);
                    } else {
                        column2 = LEFT;
                        robot.driveToTarget(new Position(-100, -150, 150), true, driveToTargetPowerLevels);
                    }
                    subStateDesc = "driving to glyph pit and intaking";
                    robot.intake.operate(1.0);
                    robot.lift.operateGate(VLift.GatePosition.CLOSED);
                    drivingToGlyphPit = true;
                    performStateEntry = false;
                }

                if (drivingToGlyphPit && (robot.driveToTargetInProgress() || robot.rotateToTargetInProgress())) {
                    if (column == LEFT) {
                        robot.driveToTarget(new Position(-300, -150, 150), true, driveToTargetPowerLevels);
                    } else {
                        robot.driveToTarget(new Position(-100, -150, 150), true, driveToTargetPowerLevels);
                    }
                    initializeDriveToBox = true;
                } else if (initializeDriveToBox) {
                    subStateDesc = "driving to box";
                    drivingToGlyphPit = false;
//                    if (column == LEFT) {
//                        column2 = CENTER;
//                        robot.setPosition(new Position(-300, -150, 150));
//                    } else {
//                        column2 = LEFT;
//                        robot.setPosition(new Position(-100, -150, 150));
//                    }
//                    x = robot.getCoordinate().getX();
//                    robot.driveToTarget(new Position(x, p3.getY(), 150), false);
                    robot.rotateToTarget(-90, false);
                    initializeDriveToBox = false;
                } else if (!drivingToGlyphPit && (/*robot.driveToTargetInProgress() || */robot.rotateToTargetInProgress())) {
//                    robot.driveToTarget(new Position(x, p3.getY(), 150), false);
                    robot.rotateToTarget(-90, false);
                    driveToBoxTimer.set(DRIVE_TO_BOX_DURATION);
                } else if (!driveToBoxTimer.isExpired()) {
                    robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robot.drivetrain.operate(0.6, 0.6);
                    operateLiftTimer.set(OPERATE_LIFT_DELAY);
                } else if (!operateLiftTimer.isExpired()) {
                    robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    robot.drivetrain.operate(0.0, 0.0);
                    robot.intake.operate(0.0);
                    subStateDesc = "placing glyph";
                    robot.lift.operateLift(VLift.LiftPosition.UP);
                    robot.lift.operateGate(VLift.GatePosition.OPEN);
                    driveOutTimer.set(DRIVE_OUT_DURATION);
                } else if (!driveOutTimer.isExpired()) {
                    subStateDesc = "backing out";
                    robot.drivetrain.operate(-0.3, -0.3);
                } else {
                    robot.drivetrain.operate(0, 0);
                    performStateExit = true;
                }

                if (performStateExit) {
                    performStateExit = false;
                    performStateEntry = true;
                    advanceState();
                }
            case END:
                currentStateDesc = "we made it?!";
                if (performStateEntry) {
                    finishTime = System.currentTimeMillis();
                    performStateEntry = false;
                }

                // pulsing LEDs :D
                currentTime = (System.currentTimeMillis() - finishTime) / 1000;
                robot.lighting.operateLED(Math.sin(currentTime) * 0.5 + 0.5);

            default:
                break;
        }

        if (robot.drivetrain.getAbsPowerAverage() > 0.5) {
            robot.lighting.operateLED(1.0);
        } else if (currentState != END) {
            robot.lighting.operateLED(robot.drivetrain.getAbsPowerAverage() * 2.0);
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
    /*
          _____                _____                    _____                    _____                    _____
         /\    \              /\    \                  /\    \                  /\    \                  /\    \
        /::\    \            /::\    \                /::\    \                /::\    \                /::\____\
       /::::\    \           \:::\    \              /::::\    \              /::::\    \              /:::/    /
      /::::::\    \           \:::\    \            /::::::\    \            /::::::\    \            /:::/    /
     /:::/\:::\    \           \:::\    \          /:::/\:::\    \          /:::/\:::\    \          /:::/    /
    /:::/__\:::\    \           \:::\    \        /:::/__\:::\    \        /:::/  \:::\    \        /:::/____/
    \:::\   \:::\    \          /::::\    \      /::::\   \:::\    \      /:::/    \:::\    \      /::::\    \
  ___\:::\   \:::\    \        /::::::\    \    /::::::\   \:::\    \    /:::/    / \:::\    \    /::::::\____\________
 /\   \:::\   \:::\    \      /:::/\:::\    \  /:::/\:::\   \:::\    \  /:::/    /   \:::\    \  /:::/\:::::::::::\    \
/::\   \:::\   \:::\____\    /:::/  \:::\____\/:::/  \:::\   \:::\____\/:::/____/     \:::\____\/:::/  |:::::::::::\____\
\:::\   \:::\   \::/    /   /:::/    \::/    /\::/    \:::\  /:::/    /\:::\    \      \::/    /\::/   |::|~~~|~~~~~
 \:::\   \:::\   \/____/   /:::/    / \/____/  \/____/ \:::\/:::/    /  \:::\    \      \/____/  \/____|::|   |
  \:::\   \:::\    \      /:::/    /                    \::::::/    /    \:::\    \                    |::|   |
   \:::\   \:::\____\    /:::/    /                      \::::/    /      \:::\    \                   |::|   |
    \:::\  /:::/    /    \::/    /                       /:::/    /        \:::\    \                  |::|   |
     \:::\/:::/    /      \/____/                       /:::/    /          \:::\    \                 |::|   |
      \::::::/    /                                    /:::/    /            \:::\    \                |::|   |
       \::::/    /                                    /:::/    /              \:::\____\               \::|   |
        \::/    /                                     \::/    /                \::/    /                \:|   |
         \/____/                                       \/____/                  \/____/                  \|___|

                         _______                   _____                    _____                    _____                    _____                    _____           _______                   _____
                        /::\    \                 /\    \                  /\    \                  /\    \                  /\    \                  /\    \         /::\    \                 /\    \
                       /::::\    \               /::\____\                /::\    \                /::\    \                /::\    \                /::\____\       /::::\    \               /::\____\
                      /::::::\    \             /:::/    /               /::::\    \              /::::\    \              /::::\    \              /:::/    /      /::::::\    \             /:::/    /
                     /::::::::\    \           /:::/    /               /::::::\    \            /::::::\    \            /::::::\    \            /:::/    /      /::::::::\    \           /:::/   _/___
                    /:::/~~\:::\    \         /:::/    /               /:::/\:::\    \          /:::/\:::\    \          /:::/\:::\    \          /:::/    /      /:::/~~\:::\    \         /:::/   /\    \
                   /:::/    \:::\    \       /:::/____/               /:::/__\:::\    \        /:::/__\:::\    \        /:::/__\:::\    \        /:::/    /      /:::/    \:::\    \       /:::/   /::\____\
                  /:::/    / \:::\    \      |::|    |               /::::\   \:::\    \      /::::\   \:::\    \      /::::\   \:::\    \      /:::/    /      /:::/    / \:::\    \     /:::/   /:::/    /
                 /:::/____/   \:::\____\     |::|    |     _____    /::::::\   \:::\    \    /::::::\   \:::\    \    /::::::\   \:::\    \    /:::/    /      /:::/____/   \:::\____\   /:::/   /:::/   _/___
                |:::|    |     |:::|    |    |::|    |    /\    \  /:::/\:::\   \:::\    \  /:::/\:::\   \:::\____\  /:::/\:::\   \:::\    \  /:::/    /      |:::|    |     |:::|    | /:::/___/:::/   /\    \
                |:::|____|     |:::|    |    |::|    |   /::\____\/:::/__\:::\   \:::\____\/:::/  \:::\   \:::|    |/:::/  \:::\   \:::\____\/:::/____/       |:::|____|     |:::|    ||:::|   /:::/   /::\____\
                 \:::\    \   /:::/    /     |::|    |  /:::/    /\:::\   \:::\   \::/    /\::/   |::::\  /:::|____|\::/    \:::\   \::/    /\:::\    \        \:::\    \   /:::/    / |:::|__/:::/   /:::/    /
                  \:::\    \ /:::/    /      |::|    | /:::/    /  \:::\   \:::\   \/____/  \/____|:::::\/:::/    /  \/____/ \:::\   \/____/  \:::\    \        \:::\    \ /:::/    /   \:::\/:::/   /:::/    /
                   \:::\    /:::/    /       |::|____|/:::/    /    \:::\   \:::\    \            |:::::::::/    /            \:::\    \       \:::\    \        \:::\    /:::/    /     \::::::/   /:::/    /
                    \:::\__/:::/    /        |:::::::::::/    /      \:::\   \:::\____\           |::|\::::/    /              \:::\____\       \:::\    \        \:::\__/:::/    /       \::::/___/:::/    /
                     \::::::::/    /         \::::::::::/____/        \:::\   \::/    /           |::| \::/____/                \::/    /        \:::\    \        \::::::::/    /         \:::\__/:::/    /
                      \::::::/    /           ~~~~~~~~~~               \:::\   \/____/            |::|  ~|                       \/____/          \:::\    \        \::::::/    /           \::::::::/    /
                       \::::/    /                                      \:::\    \                |::|   |                                         \:::\    \        \::::/    /             \::::::/    /
                        \::/____/                                        \:::\____\               \::|   |                                          \:::\____\        \::/____/               \::::/    /
                         ~~                                               \::/    /                \:|   |                                           \::/    /         ~~                      \::/____/
                                                                           \/____/                  \|___|                                            \/____/                                   ~~
    */
}