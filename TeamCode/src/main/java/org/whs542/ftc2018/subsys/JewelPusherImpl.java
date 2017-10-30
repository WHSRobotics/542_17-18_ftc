package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.util.Toggler;

/**
 * Created by Jason on 10/20/2017.
 */

public class JewelPusherImpl implements JewelPusher {

    private Servo jewelServo;
    private static final double JEWEL_POWER = 1.0;
    Toggler jewelToggler = new Toggler(2);

    public JewelPusherImpl(HardwareMap jewelMap) {
        jewelServo = jewelMap.servo.get("jewel-pusher");
    }

    @Override
    public void autoPushJewel(double gamepadInput) {
        
    }

    @Override
    public void operate(double power) {

    }

}
