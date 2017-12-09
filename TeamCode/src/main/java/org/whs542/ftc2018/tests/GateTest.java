package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.VLiftImpl;
import org.whs542.subsys.vlift.VLift;
import org.whs542.util.Toggler;

/**
 * Created by ivanm on 12/8/2017.
 */

public class GateTest extends OpMode {

    VLift lift;
    Toggler toggler;
    long i;

    @Override
    public void init() {
        lift = new VLiftImpl(hardwareMap);
        toggler = new Toggler(200);
    }

    @Override
    public void loop() {
        toggler.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        i++;
        if (i%8 == 0) {
            if (gamepad1.b) toggler.setState(toggler.currentState() + 1);
            if (gamepad1.a) toggler.setState(toggler.currentState() - 1);
        }
        lift.operateGate(toggler.currentState()/200f);
        telemetry.addData("Position: ", toggler.currentState()/200f);
    }
}
