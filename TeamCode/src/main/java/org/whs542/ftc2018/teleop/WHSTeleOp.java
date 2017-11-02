package org.whs542.ftc2018.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.subsys.robot.WHSRobot;

/**
 * Created by Amar2 on 10/28/2017.
 */

public class WHSTeleOp extends OpMode {

    WHSRobotImpl robot;

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
    }

    @Override
    public void loop() {

        robot.intake.operateWithToggle(gamepad1.right_bumper);

        robot.drivetrain.operateWithOrientation(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.switchOrientation(gamepad1.a);

        robot.fourBar.operate(gamepad2.dpad_up, gamepad1.dpad_down);

        telemetry.addData("Drivetrain Orientation: ", robot.drivetrain.getOrientation());

        robot.lift.operateLift(gamepad2.right_bumper);
        robot.lift.operateLift(gamepad2.right_trigger);

    }

}
