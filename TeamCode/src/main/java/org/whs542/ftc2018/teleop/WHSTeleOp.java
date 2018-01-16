package org.whs542.ftc2018.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2018.subsys.JewelPusherImpl;
import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.ftc2018.subsys.WHSRobotSimple;
import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.subsys.robot.WHSRobot;

/**
 * Created by Amar2 on 10/28/2017.
 */
@TeleOp(name = "WHSTeleOp", group = "a")
public class WHSTeleOp extends OpMode {

    WHSRobotSimple robot;

    @Override
    public void init() {
        robot = new WHSRobotSimple(hardwareMap);
    }

    @Override
    public void loop() {

        robot.jewelPusher.operateArm(JewelPusherImpl.ArmPosition.UP);
        
        robot.intake.operateWithToggle(gamepad1.right_bumper, gamepad1.right_trigger);

        robot.drivetrain.operateWithOrientation(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.switchOrientation(gamepad1.a);

        robot.fourBar.operate(gamepad2.a, gamepad2.x, gamepad2.y);

        robot.lift.operateLift(gamepad2.right_bumper, gamepad2.right_trigger);
        robot.lift.operateJiggle(gamepad2.left_bumper);

        telemetry.addData("Four Bar Level: ", robot.fourBar.getFourBarLevel());
        telemetry.addData("Drivetrain Orientation: ", robot.drivetrain.getOrientation());


    }

    @Override
    public void stop(){
        robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.intake.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.fourBar.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    }


}
