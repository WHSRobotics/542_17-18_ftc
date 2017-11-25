package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.subsys.drivetrain.TankDrivetrain;

/**
 * Created by Amar2 on 10/21/2017.
 */
@TeleOp(name = "drivetrainTest", group = "tests")
public class DrivetrainTest extends OpMode {

    TileRunner drivetrain;

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
        }*/
    }
}