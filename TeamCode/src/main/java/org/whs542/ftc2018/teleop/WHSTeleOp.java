package org.whs542.ftc2018.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2018.subsys.WHSRobotSimple;
import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.subsys.vlift.VLift;

/**
 * Main TeleOp Class for 2017-18
 */
@TeleOp(name = "WHSTeleOp", group = "a")
public class WHSTeleOp extends OpMode {

    WHSRobotSimple robot;
    int fourBarResetState = 0;

    @Override
    public void init() {
        robot = new WHSRobotSimple(hardwareMap);
    }

    @Override
    public void loop() {

        robot.jewelPusher.operateArm(JewelPusher.ArmPosition.UP);

        robot.intake.operateWithToggle(gamepad1.right_bumper, gamepad1.right_trigger);

        /* DRIVETRAIN */
        //Precision driving mode
        if (gamepad1.left_bumper || gamepad2.b) {
            robot.drivetrain.operateWithOrientation(gamepad1.left_stick_y / 2.54, gamepad1.right_stick_y / 2.54);
        } else {
            robot.drivetrain.operateWithOrientation(gamepad1.left_stick_y, gamepad1.right_stick_y);
        }

        robot.drivetrain.switchOrientation(gamepad1.a);

        if (robot.drivetrain.getOrientation() == "normal") {
            robot.lighting.operateLED(1.0);
        } else {
            robot.lighting.operateLED(0.0);
        }

        /* FOUR BAR */
        robot.fourBar.operate(gamepad2.a, gamepad2.x, gamepad2.y);

        //Resets four bar
        if (gamepad2.start && gamepad2.dpad_down) {
            robot.fourBar.operate(0.0);
            robot.fourBar.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            fourBarResetState = 1;
        } else if (fourBarResetState == 1) {
            robot.fourBar.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            fourBarResetState = 2;
        } else if (fourBarResetState == 2) {
            robot.fourBar.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
            fourBarResetState = 0;
        }

        /* VLIFT */
        //If the four bar is at highest level, the Vlift will go up slightly
        if (gamepad2.right_bumper || gamepad2.right_trigger > 0.01 || gamepad2.left_bumper) {
            robot.lift.operateLift(gamepad2.right_bumper, gamepad2.right_trigger);
        } else if (robot.fourBar.getFourBarLevel().equals("Scoring on top of 2 glyphs")) {  //Jank
            robot.lift.operateLift(VLift.LiftPosition.MIDDLE_DOWN);
        } else {
            robot.lift.operateLift(VLift.LiftPosition.DOWN);
        }

        //If the Vlift is up (flipped), the gate will go to middle
        if (gamepad2.left_trigger > 0.05) {
            robot.lift.operateGate(VLift.GatePosition.OPEN);
        } else if (robot.lift.getCurrentLiftPos() == "up") {
            robot.lift.operateGate(VLift.GatePosition.MIDDLE);
        } else {
            robot.lift.operateGate(VLift.GatePosition.CLOSED);
        }

        robot.lift.operateJiggle(gamepad2.left_bumper);

        telemetry.addData("Four Bar Level: ", robot.fourBar.getFourBarLevel());
        telemetry.addData("Drivetrain Orientation: ", robot.drivetrain.getOrientation());


    }

    @Override
    public void stop() {
        robot.drivetrain.operate(0.0, 0.0);
        robot.intake.operate(0.0);
    }


}
