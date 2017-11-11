package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.subsys.robot.WHSRobot;

/**
 * Created by ivanm on 11/11/2017.
 */
@Autonomous(name = "Jank is dank. It's 4:20AM", group = "a")
public class WHSJankAuto extends LinearOpMode {

    WHSRobotImpl robot;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new WHSRobotImpl(hardwareMap);
        robot.drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        robot.drivetrain.operate(0.3, 0.3);
        Thread.sleep(4000);
        robot.drivetrain.operate(0.0, 0.0);
    }
}
