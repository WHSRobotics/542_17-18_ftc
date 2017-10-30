package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.subsys.fourbar.FourBar;
import org.whs542.util.Toggler;

/**
 * Created by Jason on 10/20/2017.
 */

public class FourBarImpl implements FourBar {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private static final double FOURBAR_POWER = 1.0;
    //private Toggler fourBarToggler = new Toggler(2);

    public FourBarImpl(HardwareMap fourBarMap) {
        leftMotor = fourBarMap.dcMotor.get("left_fb");
        rightMotor = fourBarMap.dcMotor.get("right_fb");
    }

    @Override
    public void operate(boolean up, boolean down) {
        if (up) { //lifting
            leftMotor.setPower(FOURBAR_POWER);
            rightMotor.setPower(FOURBAR_POWER);
        } else if (down) { //descending
            leftMotor.setPower(-FOURBAR_POWER);
            rightMotor.setPower(-FOURBAR_POWER);
        } else {
            leftMotor.setPower(0);
            rightMotor.setPower(0);
        }
    }

    @Override
    public void operate(double power) {
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }
}
