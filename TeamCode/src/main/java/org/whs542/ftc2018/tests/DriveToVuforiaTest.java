package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.subsys.robot.WHSRobot;
import org.whs542.util.Toggler;

/**
 * Created by Amar2 on 10/21/2017.
 */
@TeleOp(name = "drivetoVuforiaTest", group = "tests")
public class DriveToVuforiaTest extends OpMode {
    WHSRobotImpl robot;
    Toggler scaleTog = new Toggler(3);
    String mode = "";
    double[] power = new double[2];
    Toggler powerTog = new Toggler(50);

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        telemetry.log().add("mode (scaled/normal) switch : gamepad1-x");
        telemetry.log().add("orientation switch : gamepad1-a");
    }

    @Override
    public void loop() {

        scaleTog.changeState(gamepad1.x);
        robot.drivetrain.switchOrientation(gamepad1.a);
        powerTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);

        if (scaleTog.currentState() == 0){
            robot.drivetrain.operate(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Normal";
        } else if (scaleTog.currentState() == 1){
            robot.drivetrain.operateWithOrientationScaled(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Scaled";
        } else {
            if (gamepad1.b) {
                robot.drivetrain.operate(powerTog.currentState() * 0.02, powerTog.currentState() * 0.02);
            } else {
                robot.drivetrain.operate(0.0, 0.0);
            }
            mode = "Step";
        }


        telemetry.addData("Mode:", mode);
        telemetry.addData("Orientation:", robot.drivetrain.getOrientation());
        telemetry.addData("LeftStickY:", gamepad1.left_stick_y);
        telemetry.addData("RightStickY:", gamepad1.right_stick_y);
        telemetry.addData("Vuforia: ", robot.vuforia.getVuforiaReading());
        telemetry.addData("Power (step)", powerTog.currentState()*0.02);
    }
}