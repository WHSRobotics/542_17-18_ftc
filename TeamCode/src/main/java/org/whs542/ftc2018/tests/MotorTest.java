package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Amar2 on 11/4/2017.
 */

public class MotorTest extends OpMode {

    DcMotor motor;

    @Override
    public void init() {
        motor = hardwareMap.dcMotor.get("motor1");
    }

    @Override
    public void loop() {
        if(gamepad1.a){
            motor.setPower(1.0);
        }
        else {
            motor.setPower(0.0);
        }
    }
}
