package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.subsys.drivetrain.TankDrivetrain;
import org.whs542.util.Toggler;

/**
 * Created by Amar2 on 10/21/2017.
 */
@TeleOp(name = "drivetrainTest", group = "tests")
public class DrivetrainTest extends OpMode {

    /*TileRunner drivetrain;

    @Override
    public void init() {
        drivetrain = new TileRunner(hardwareMap);
    }

    @Override
    public void loop() {

        if(gamepad1.x){
            drivetrain.operate(0.5, 0.5);
        }
        else {
            drivetrain.operate(gamepad1.left_stick_y, gamepad1.right_stick_y);
        }
        drivetrain.switchOrientation(gamepad1.a);
        telemetry.addData("FL Position", drivetrain.frontLeft.getCurrentPosition());
        telemetry.addData("BL Position", drivetrain.backLeft.getCurrentPosition());
        telemetry.addData("FR Position", drivetrain.frontRight.getCurrentPosition());
        telemetry.addData("BR Position", drivetrain.backRight.getCurrentPosition());
        /*if(gamepad1.y){
            drivetrain.frontLeft.setPower(0.5);
        }
        else if(gamepad1.b){
            drivetrain.frontRight.setPower(0.5);
        }
        else if(gamepad1.x){
            drivetrain.backLeft.setPower(0.5);
        }
        else if(gamepad1.a){
            drivetrain.backRight.setPower(0.5);
        }
    }*/

    TileRunner drivetrain;
    Toggler scaleTog = new Toggler(3);
    String mode = "";
    double[] power = new double[2];
    Toggler powerTog = new Toggler(50);

    @Override
    public void init() {
        drivetrain = new TileRunner(hardwareMap);
        telemetry.log().add("mode (scaled/normal) switch : gamepad1-x");
        telemetry.log().add("orientation switch : gamepad1-a");
    }

    @Override
    public void loop() {

        scaleTog.changeState(gamepad1.x);
        drivetrain.switchOrientation(gamepad1.a);
        powerTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);

        if (scaleTog.currentState() == 0){
            drivetrain.operate(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Normal";
        } else if (scaleTog.currentState() == 1){
            drivetrain.operateWithOrientationScaled(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Scaled";
        } else {
            if (gamepad1.b) {
                drivetrain.operate(powerTog.currentState() * 0.02, powerTog.currentState() * 0.02);
            } else {
                drivetrain.operate(0.0, 0.0);
            }
            mode = "Step";
        }


        telemetry.addData("Mode:", mode);
        telemetry.addData("Orientation:", drivetrain.getOrientation());
        telemetry.addData("LeftStickY:", gamepad1.left_stick_y);
        telemetry.addData("RightStickY:", gamepad1.right_stick_y);
        //telemetry.addData("Scaled L", drivetrain.getScaledPower(gamepad1.left_stick_y));
        //telemetry.addData("Scaled R", drivetrain.getScaledPower(gamepad1.right_stick_y));
        telemetry.addData("Power (step)", powerTog.currentState()*0.02);
    }
}