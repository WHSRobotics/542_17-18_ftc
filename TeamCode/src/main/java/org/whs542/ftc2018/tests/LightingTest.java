package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Light;

import org.whs542.ftc2018.subsys.Lighting;
import org.whs542.util.Toggler;

/**
 * Created by Amar2 on 2/2/2018.
 */
@TeleOp(name = "LightingTest", group = "tests")
public class LightingTest extends OpMode{

    Lighting LED;
    Toggler toggler;
    int i;


    @Override
    public void init() {
        LED = new Lighting(hardwareMap);
        toggler = new Toggler(200);
    }

    @Override
    public void loop() {
        toggler.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        i++;
        if (i%6 == 0) {
            if (gamepad1.b) {
                toggler.setState(toggler.currentState() + 1);
            }
            if (gamepad1.a) {
                toggler.setState(toggler.currentState() - 1);
            }
        }
        LED.operateLED(toggler.currentState()/200f);
    }
}
