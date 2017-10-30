package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.subsys.drivetrain.TankDrivetrain;
import org.whs542.subsys.fourbar.FourBar;
import org.whs542.subsys.intake.Intake;
import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.subsys.robot.WHSRobot;
import org.whs542.util.Coordinate;
import org.whs542.util.Functions;
import org.whs542.util.Position;

/**
 * Created by Jason on 10/20/2017.
 */

public class WHSRobotImpl extends WHSRobot {

    public TankDrivetrain drivetrain;
    public Intake intake;
    public FourBar fourBar;
    public JewelPusher jewelPusher;
    public IMU imu;
    public Color color;

    Coordinate currentCoord;
    public double targetHeading; //field frame

    private static final double RADIUS_TO_DRIVETRAIN = 365/2; //in mm
    private static final double DEADBAND_MAX_DRIVE_HEADING_DEVIATION = 10; //in degrees
    private static final double DEADBAND_MAX_DRIVE_POSITION_DEVIATION = 300; //in mm
    private static final double[] DRIVE_TO_TARGET_POWER_LEVEL = {0.33, 0.6, 0.7, 0.9};
    public static final double DEADBAND_DRIVE_TO_TARGET = 110; //in mm
    private static final double[] DRIVE_TO_TARGET_THRESHOLD = {DEADBAND_DRIVE_TO_TARGET, 300, 600, 1200};
    private static final double[] ROTATE_TO_TARGET_POWER_LEVEL = {0.35, 0.6, 0.75};
    private static final double DEADBAND_ROTATE_TO_TARGET = 3.5; //in degrees
    private static final double[] ROTATE_TO_TARGET_THRESHOLD = {DEADBAND_ROTATE_TO_TARGET, 45, 90};
    private static final double DRIVE_CORRECTION_GAIN = 0.0007;
    public double rightMultiplier = 1.0;

    public boolean rotateToTargetInProgress;
    public boolean driveToTargetInProgress;

    public WHSRobotImpl (HardwareMap hardwareMap){
        drivetrain = new TileRunner(hardwareMap);
        intake = new RollerIntake(hardwareMap);
        fourBar = new FourBarImpl(hardwareMap);
        jewelPusher = new JewelPusherImpl(hardwareMap);
        currentCoord = new Coordinate(0.0, 0.0, 0.0, 0.0);
        imu = new IMU(hardwareMap, 0);
        color = new Color(hardwareMap);
    }

    public TileRunner drivetrain;
    public RollerIntake intake;
    public JewelPusherImpl pusher;
    public FourBarImpl fourBar;
    public Color colorSensor;

    public boolean driveToTargetInProgress;
    public boolean rotateToTargetInProgress;
    public boolean vuforiaTargetDetected;

    public String currentRobotState;

    public void WHSRobotImpl(HardwareMap robotMap) {
        drivetrain = new TileRunner(robotMap);
        intake = new RollerIntake(robotMap);
        pusher = new JewelPusherImpl(robotMap);
        fourBar = new FourBarImpl(robotMap);
        colorSensor = new Color(robotMap);

        driveToTargetInProgress = false;
        rotateToTargetInProgress = false;
        vuforiaTargetDetected = false;
        currentRobotState = "";
    }

    @Override
    public void driveToTarget(Position targetPos) {
        Position vectorToTarget = Functions.subtractPositions(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
        //double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        /*double*/ targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg

        rotateToTarget(targetHeading);

        if (rotateToTargetInProgress) {
            //if rotating, do nothing
        }
        else {
            drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

            if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[3]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[3] * rightMultiplier);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[3]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[2] * rightMultiplier);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[2]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[1] * rightMultiplier);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[1]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.operateRight(DRIVE_TO_TARGET_POWER_LEVEL[0] * rightMultiplier);
                drivetrain.operateLeft(DRIVE_TO_TARGET_POWER_LEVEL[0]);
                driveToTargetInProgress = true;
            }
            else {
                drivetrain.operateRight(0.0);
                drivetrain.operateLeft(0.0);
                driveToTargetInProgress = false;
                rotateToTargetInProgress = false;

            }
        }
    }

    @Override
    public void rotateToTarget(double targetHeading) {
        double angleToTarget = targetHeading - currentCoord.getHeading();
        angleToTarget=Functions.normalizeAngle(angleToTarget); //-180 to 180 deg

        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(angleToTarget<-DEADBAND_ROTATE_TO_TARGET)
        {


            if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[2]){
                drivetrain.operateLeft(ROTATE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.operateRight(-ROTATE_TO_TARGET_POWER_LEVEL[2]);
                rotateToTargetInProgress = true;
            }
            else if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[1]){
                drivetrain.operateLeft(ROTATE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.operateRight(-ROTATE_TO_TARGET_POWER_LEVEL[1]);
                rotateToTargetInProgress = true;
            } else if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[0]){
                drivetrain.operateLeft(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.operateRight(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }

        }
        else if(angleToTarget>DEADBAND_ROTATE_TO_TARGET)
        {
            if(angleToTarget > ROTATE_TO_TARGET_THRESHOLD[2]){
                drivetrain.operateLeft(-ROTATE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.operateRight(ROTATE_TO_TARGET_POWER_LEVEL[2]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget > ROTATE_TO_TARGET_THRESHOLD[1]){
                drivetrain.operateLeft(-ROTATE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.operateRight(ROTATE_TO_TARGET_POWER_LEVEL[1]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget > ROTATE_TO_TARGET_THRESHOLD[0]){
                drivetrain.operateLeft(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.operateRight(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }
        }
        else{
            drivetrain.operateLeft(0.0);
            drivetrain.operateRight(0.0);
            rotateToTargetInProgress = false;

        }
    }

    @Override
    public Position estimatePosition() {

        Position estimatedPos;
        if(rotateToTargetInProgress) {
            //if rotating, do NOT update position and get rid of encoder values as it turns
            double[] encoderValues = drivetrain.getEncoderDistance();

            estimatedPos = currentCoord.getPos();
        }
        else {
            if (driveToTargetInProgress & !rotateToTargetInProgress) {
                double[] encoderValues = drivetrain.getEncoderDistance();
                double encoderPosL = encoderValues[0];
                double encoderPosR = encoderValues[1];

                double encoderAvg = (encoderPosL + encoderPosR) * 0.5;

                double hdg = currentCoord.getHeading();
                double dist = Functions.encToMM(encoderAvg);

                double xPos = currentCoord.getX() + dist * Functions.cosd(hdg);
                double yPos = currentCoord.getY() + dist * Functions.sind(hdg);

                estimatedPos = new Position(xPos, yPos, currentCoord.getZ());

                currentCoord.setX(xPos);
                currentCoord.setY(yPos);
            } else if (rotateToTargetInProgress) {
                drivetrain.getEncoderDistance();
                estimatedPos = currentCoord.getPos();

            } else {
                estimatedPos = currentCoord.getPos();
            }
        }


        return estimatedPos;
    }

    @Override
    public double estimateHeading() {
        double currentHeading;


            currentHeading = Functions.normalizeAngle(imu.getHeading() + imu.getImuBias()); //-180 to 180 deg
            currentCoord.setHeading(currentHeading); //updates global variable

        return currentHeading;
    }

    @Override
    public void setInitialCoordinate(Coordinate initCoord) {

    }

    @Override
    public void setCoordinate(Coordinate coord) {

    }

    @Override
    public void getCoordinate() {

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
