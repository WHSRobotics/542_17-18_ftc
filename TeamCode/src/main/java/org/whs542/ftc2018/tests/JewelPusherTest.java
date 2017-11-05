package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.JewelPusherImpl;
import org.whs542.subsys.jewelpusher.JewelPusher;

/**
 * Created by ivanm on 11/4/2017.
 */

public class JewelPusherTest extends OpMode {

    JewelPusher jewelPusher;

    @Override
    public void init() {
        jewelPusher = new JewelPusherImpl(hardwareMap);
    }

    @Override
    public void loop() {
        jewelPusher.extendArm(gamepad1.left_trigger);
        jewelPusher.operateSwivel(gamepad1.right_trigger);
    }
}
