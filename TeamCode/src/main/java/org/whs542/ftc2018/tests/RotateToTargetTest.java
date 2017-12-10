package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.util.Coordinate;
import org.whs542.util.Position;

/**
 * Created by Amar2 on 11/15/2017.
 */
@Autonomous(name = "RotateToTargetTest", group = "tests")
public class RotateToTargetTest extends OpMode {
    WHSRobotImpl robot;
    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        robot.setInitialCoordinate(new Coordinate(0, 0, 150, 0));
        telemetry.setMsTransmissionInterval(10);
    }

    @Override
    public void start(){
        robot.rotateToTarget(90);
    }

    @Override
    public void loop() {
        if(robot.rotateToTargetInProgress()) {
            robot.rotateToTarget(90);
        }
        robot.estimatePosition();
        robot.estimateHeading();

        telemetry.addData("DriveToTarget in progress: ", robot.driveToTargetInProgress());
        telemetry.addData("RotateToTarget in progress: ", robot.rotateToTargetInProgress());
        telemetry.addData("IMU", robot.imu.getHeading());
        telemetry.addData("X", robot.getCoordinate().getX());
        telemetry.addData("Y", robot.getCoordinate().getY());
        telemetry.addData("Z", robot.getCoordinate().getZ());
        telemetry.addData("Heading", robot.getCoordinate().getHeading());
        telemetry.addData("FL Power", robot.drivetrain.frontLeft.getPower());
        telemetry.addData("BL Power", robot.drivetrain.backLeft.getPower());
        telemetry.addData("FR Power", robot.drivetrain.frontRight.getPower());
        telemetry.addData("BR Power", robot.drivetrain.backRight.getPower());
        telemetry.addData("Distance to target", robot.distanceToTargetDebug);
        telemetry.addData("BLdelta", robot.drivetrain.backLeft.getCurrentPosition());
        telemetry.addData("BRdelta", robot.drivetrain.backRight.getCurrentPosition());
        telemetry.addData("FLdelta", robot.drivetrain.frontLeft.getCurrentPosition());
        telemetry.addData("FRdelta", robot.drivetrain.frontRight.getCurrentPosition());

    }
}
