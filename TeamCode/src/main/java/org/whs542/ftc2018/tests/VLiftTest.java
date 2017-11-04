package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.VLiftImpl;
import org.whs542.subsys.vlift.VLift;
import org.whs542.util.Toggler;

/**
 * Created by ivanm on 11/3/2017.
 */

@TeleOp (name = "VLiftTest", group = "yay")
public class VLiftTest extends OpMode {

    VLift lift;

    Toggler leftAngleTog;
    Toggler rightAngleTog;
    long i;
    @Override
    public void init() {
        rightAngleTog = new Toggler(200);
        leftAngleTog = new Toggler(200);
        lift = new VLiftImpl(hardwareMap);
    }

    @Override
    public void loop() {
        leftAngleTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        rightAngleTog.changeState(gamepad1.dpad_left, gamepad1.dpad_right);
        i++;
        if (i%50 == 0) {
            if (gamepad1.a) leftAngleTog.setState(leftAngleTog.currentState() + 1);
            if (gamepad1.b) rightAngleTog.setState(rightAngleTog.currentState() + 1);
        }
        lift.operateLift(leftAngleTog.currentState()/200f, rightAngleTog.currentState()/200f);
        telemetry.addData("Left: ", leftAngleTog.currentState()/200f);
        telemetry.addData("Right: ", rightAngleTog.currentState()/200f);

    }
}
