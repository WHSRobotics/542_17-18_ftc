package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


@TeleOp(name = "FourBar Debugging", group = "tests")
public class FourBarDebugging extends OpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;

    @Override
    public void init() {
        leftMotor = hardwareMap.dcMotor.get("leftFourBar");
        rightMotor = hardwareMap.dcMotor.get("rightFourBar");

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void loop() {
        leftMotor.setPower(0.0);
        rightMotor.setPower(0.0);
        telemetry.addData("Left Pos", leftMotor.getCurrentPosition());
        telemetry.addData("Right Pos", rightMotor.getCurrentPosition());
    }
}
