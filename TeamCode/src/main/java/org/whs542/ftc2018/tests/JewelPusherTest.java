package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.JewelPusherImpl;
import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.util.Toggler;

/**
 * Created by ivanm on 11/4/2017.
 */
@TeleOp(name = "JewelPusher Test", group = "tests")
public class JewelPusherTest extends OpMode {

    JewelPusher jewelPusher;
    Toggler armTog;
    Toggler swivelTog;
    long i;

    @Override
    public void init() {
        jewelPusher = new JewelPusherImpl(hardwareMap);
        armTog = new Toggler(200);
        swivelTog = new Toggler(200);
    }

    @Override
    public void loop() {
        armTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        swivelTog.changeState(gamepad1.dpad_left, gamepad1.dpad_right);
        i++;
        if (i%10 == 0) {
            if (gamepad1.a) armTog.setState(armTog.currentState() + 1);
            if (gamepad1.b) swivelTog.setState(swivelTog.currentState() + 1);
        }
        jewelPusher.operateArm(armTog.currentState()/200f);
        jewelPusher.operateSwivel(swivelTog.currentState()/200f);
        telemetry.addData("Arm: ", armTog.currentState()/200f);
        telemetry.addData("Swivel: ", swivelTog.currentState()/200f);
        telemetry.addData("Color: ", jewelPusher.getJewelColor());
    }
}
