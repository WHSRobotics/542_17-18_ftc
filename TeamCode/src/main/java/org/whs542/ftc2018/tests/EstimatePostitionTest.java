package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.util.Coordinate;

@Autonomous(name = "EstimatePositionTest", group = "tests")
public class EstimatePostitionTest extends OpMode {
    WHSRobotImpl robot;

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        robot.setInitialCoordinate(new Coordinate(0, 0, 0, 0));
        robot.drivetrain.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.drivetrain.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.drivetrain.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.drivetrain.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        robot.driveToTargetInProgress = true;
        robot.rotateToTargetInProgress = false;
        robot.estimatePosition();
        robot.estimateHeading();
        telemetry.addData("DriveToTarget in progress: ", robot.driveToTargetInProgress());
        telemetry.addData("RotateToTarget in progress: ", robot.rotateToTargetInProgress());
        telemetry.addData("IMU", robot.imu.getHeading());
        telemetry.addData("X", robot.getCoordinate().getX());
        telemetry.addData("Y", robot.getCoordinate().getY());
        telemetry.addData("Z", robot.getCoordinate().getZ());
        telemetry.addData("Heading", robot.getCoordinate().getHeading());
        telemetry.addData("FL Position", robot.drivetrain.frontLeft.getCurrentPosition());
        telemetry.addData("BL Position", robot.drivetrain.backLeft.getCurrentPosition());
        telemetry.addData("FR Position", robot.drivetrain.frontRight.getCurrentPosition());
        telemetry.addData("BR Position", robot.drivetrain.backRight.getCurrentPosition());
    }
}
