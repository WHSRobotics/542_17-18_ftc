package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by ivanm on 1/19/2018.
 */

public class Lighting {
    private DcMotor led;

    public Lighting(HardwareMap lightMap) {
        led = lightMap.dcMotor.get("led");
    }

    public void operateLED(double power) {
        led.setPower(power);
    }
}
