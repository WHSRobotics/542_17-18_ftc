package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.subsys.robot.WHSRobot;
import org.whs542.util.Coordinate;
import org.whs542.util.Functions;
import org.whs542.util.Position;

/**
 * Created by Jason on 10/20/2017.
 */

public class WHSRobotImpl implements WHSRobot {

    public TileRunner drivetrain;
    public IMU imu;
    public VLiftImpl lift;
    public RollerIntake intake;
    public FourBarImpl fourBar;
    public JewelPusherImpl jewelPusher;
    public Vuforia vuforia;
    public BalancingStoneSensor balancingStoneSensor;
    public Lighting lighting;
    Coordinate currentCoord;
    public double targetHeading; //field frame
    public double angleToTargetDebug;
    private double lastKnownHeading = 0.1;
    private static final double DEADBAND_DRIVE_TO_TARGET = 110; //in mm
    private static final double DEADBAND_ROTATE_TO_TARGET = 2.5; //in degrees
    private static final double[] DRIVE_TO_TARGET_POWER_LEVEL = {0.37, 0.4, 0.43, 0.46}; //{0.33, 0.6, 0.7, 0.9};
    private static final double[] DRIVE_TO_TARGET_THRESHOLD = {DEADBAND_DRIVE_TO_TARGET, 300, 600, 1200};
    private static final double[] ROTATE_TO_TARGET_POWER_LEVEL = {0.30, 0.35, 0.5};
    private static final double[] ROTATE_TO_TARGET_THRESHOLD = {DEADBAND_ROTATE_TO_TARGET, 30, 60};
    private double rightMultiplier = 1.0;
    private int count = 0;
    private boolean driveBackwards;

    public boolean rotateToTargetInProgress;
    public boolean driveToTargetInProgress;

    public double distanceToTargetDebug = 0;
    public WHSRobotImpl (HardwareMap hardwareMap){
        drivetrain = new TileRunner(hardwareMap);
        intake = new RollerIntake(hardwareMap);
        fourBar = new FourBarImpl(hardwareMap);
        jewelPusher = new JewelPusherImpl(hardwareMap);
        currentCoord = new Coordinate(0.0, 0.0, 150.0, 0.0);
        imu = new IMU(hardwareMap);
        balancingStoneSensor = new BalancingStoneSensor(hardwareMap);
        lighting = new Lighting(hardwareMap);

        lift = new VLiftImpl(hardwareMap);
        vuforia = new Vuforia(hardwareMap);

    }

    @Override
    public void driveToTarget(Position targetPos, boolean backwards) {
        Position vectorToTarget = Functions.subtractPositions(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);
        distanceToTargetDebug = distanceToTarget;
        double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
        //double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        /*double*/ targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
        if(count == 0) {
            rotateToTarget(targetHeading, backwards);
            count++;
        }
        else if(rotateToTargetInProgress) {
            rotateToTarget(targetHeading, backwards);
        }

        if (rotateToTargetInProgress) {
            //if rotating, do nothing
        }
        else if (!driveBackwards && distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {

            if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[3]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[3]);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[3]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[2]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[1]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[0]);
                driveToTargetInProgress = true;
            }
        }
        else if (driveBackwards && distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {

            if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[3]) {
                drivetrain.operateRight(-DRIVE_TO_TARGET_POWER_LEVEL[3]);
                drivetrain.operateLeft(-DRIVE_TO_TARGET_POWER_LEVEL[3]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.operateRight(-DRIVE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.operateLeft(-DRIVE_TO_TARGET_POWER_LEVEL[2]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.operateRight(-DRIVE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.operateLeft(-DRIVE_TO_TARGET_POWER_LEVEL[1]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.operateRight(-DRIVE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.operateLeft(-DRIVE_TO_TARGET_POWER_LEVEL[0]);
                driveToTargetInProgress = true;
            }
        }
        else {
            drivetrain.operateRight(0.0);
            drivetrain.operateLeft(0.0);
            driveToTargetInProgress = false;
            rotateToTargetInProgress = false;
            count = 0;
        }
    }

    @Override
    public void rotateToTarget(double targetHeading, boolean backwards) {

        double angleToTarget = targetHeading - currentCoord.getHeading();
        angleToTarget = Functions.normalizeAngle(angleToTarget); //-180 to 180 deg
        if (backwards && angleToTarget > 90) {
            angleToTarget = angleToTarget - 180;
            driveBackwards = true;
        }
        else if (backwards && angleToTarget < -90) {
            angleToTarget = angleToTarget + 180;
            driveBackwards = true;
        }
        else {
            driveBackwards = false;
        }
        angleToTargetDebug = angleToTarget;

        //drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if (angleToTarget < -DEADBAND_ROTATE_TO_TARGET) {

            //targetQuadrant = 4;
            if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.operateLeft(ROTATE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.operateRight(-ROTATE_TO_TARGET_POWER_LEVEL[2]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.operateLeft(ROTATE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.operateRight(-ROTATE_TO_TARGET_POWER_LEVEL[1]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.operateLeft(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.operateRight(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }
        }
        else if (angleToTarget > DEADBAND_ROTATE_TO_TARGET) {

            //targetQuadrant = 1;
            if(angleToTarget > ROTATE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.operateLeft(-ROTATE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.operateRight(ROTATE_TO_TARGET_POWER_LEVEL[2]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget > ROTATE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.operateLeft(-ROTATE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.operateRight(ROTATE_TO_TARGET_POWER_LEVEL[1]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget > ROTATE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.operateLeft(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.operateRight(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }
        }
        else {
            drivetrain.operateLeft(0.0);
            drivetrain.operateRight(0.0);
            rotateToTargetInProgress = false;

        }
    }

    @Override
    public boolean driveToTargetInProgress() {
        return driveToTargetInProgress;
    }

    @Override
    public boolean rotateToTargetInProgress() {
        return rotateToTargetInProgress;
    }

    @Override
    public void estimatePosition() {

        Position estimatedPos;
        if(rotateToTargetInProgress) {
            //if rotating, do NOT update position and get rid of encoder values as it turns
            double[] encoderValues = drivetrain.getEncoderDelta();

            estimatedPos = currentCoord.getPos();
        }
        else {
            if (/*driveToTargetInProgress & */!rotateToTargetInProgress) {
                double[] encoderValues = drivetrain.getEncoderDelta();
                double encoderPosL = encoderValues[0];
                double encoderPosR = encoderValues[1];

                double encoderAvg = (encoderPosL + encoderPosR) * 0.5;
                double hdg = currentCoord.getHeading();
                double dist = drivetrain.encToMM(encoderAvg);

                double xPos = currentCoord.getX() + dist * Functions.cosd(hdg);
                double yPos = currentCoord.getY() + dist * Functions.sind(hdg);

                estimatedPos = new Position(xPos, yPos, currentCoord.getZ());

                currentCoord.setX(xPos);
                currentCoord.setY(yPos);
            } else if (rotateToTargetInProgress) {
                drivetrain.getEncoderDelta();
                estimatedPos = currentCoord.getPos();

            } else {
                estimatedPos = currentCoord.getPos();
            }
        }


    }

    @Override
    public void estimateHeading() {
        double currentHeading;
        currentHeading = Functions.normalizeAngle(imu.getHeading() + imu.getImuBias()); //-180 to 180 deg
        if (currentHeading != 0.0) {
            lastKnownHeading = currentHeading;
        }
        currentCoord.setHeading(lastKnownHeading); //updates global variable
    }

    @Override
    public void setInitialCoordinate(Coordinate initCoord) {
        currentCoord = initCoord;
        imu.setImuBias(currentCoord.getHeading());
    }

    @Override
    public void setCoordinate(Coordinate coord) {
        currentCoord = coord;
        imu.setImuBias(currentCoord.getHeading());
    }

    @Override
    public Coordinate getCoordinate() {
        return currentCoord;
    }


    public Position body2field(Position bodyVector)
    {
        Position fieldVector;
        double heading = currentCoord.getHeading();

        double[][] C_b2f = {{Functions.cosd(heading),  -Functions.sind(heading),  0},
                {Functions.sind(heading),   Functions.cosd(heading),  0},
                {0,                         0,                        1}};

        fieldVector = Functions.transformCoordinates(C_b2f,bodyVector);
        return fieldVector;

    }

    public Position field2body(Position fieldVector)
    {
        Position bodyVector;
        double heading = currentCoord.getHeading();

        double[][] C_f2b = {{ Functions.cosd(heading),   Functions.sind(heading),  0},
                {-Functions.sind(heading),   Functions.cosd(heading),  0},
                { 0,                         0,                        1}};

        bodyVector = Functions.transformCoordinates(C_f2b,fieldVector);
        return bodyVector;

    }

    public Position front2back(Position frontVector)
    {
        Position backVector;
        double heading = 180;

        double[][] C_f2b = {{ -1,  0, 0},
                {  0, -1, 0},
                {  0,  0, 1}};

        backVector = Functions.transformCoordinates(C_f2b,frontVector);
        return backVector;
    }
}
