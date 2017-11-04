package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.subsys.jewelpusher.JewelPusher;
import org.whs542.util.Toggler;

/**
 * Created by Jason on 10/20/2017.
 */

public class JewelPusherImpl implements JewelPusher {

    private Servo armServo;
    private Servo pushServo;
    //TODO: change these to actual values
    private static final double ARM_DOWN_POSITION = 1;
    private static final double ARM_UP_POSITION = 0;
    private static final double PUSH_LEFT_POSITION = 0;
    private static final double PUSH_MIDDLE_POSITION = 0.15;
    private static final double PUSH_RIGHT_POSITION = 0.3;
    private static final double JEWEL_POWER = 1.0;
    Toggler jewelToggler = new Toggler(2);
    private Color colorSensor;
    private static final int COLOR_SENSOR_THRESHOLD = 10;

    public JewelPusherImpl(HardwareMap jewelMap) {
        armServo = jewelMap.servo.get("armServo");
        pushServo = jewelMap.servo.get("pushServo");
        colorSensor = new Color(jewelMap);
    }

    @Override
    public JewelColor getJewelColor() {
        if ((colorSensor.getR() - colorSensor.getB()) > COLOR_SENSOR_THRESHOLD) {
            return JewelColor.RED;
        } else if ((colorSensor.getB() - colorSensor.getR()) > COLOR_SENSOR_THRESHOLD) {
            return JewelColor.BLUE;
        } else {
            return JewelColor.ERROR;
        }
    }

    @Override
    public void operateArm(double armPosition) {
        armServo.setPosition(armPosition);
    }

    @Override
    public void operateArm(boolean downPosition) {
        if (downPosition) {
            armServo.setPosition(ARM_DOWN_POSITION);
        } else {
            armServo.setPosition(ARM_UP_POSITION);
        }
    }

    @Override
    public void operatePusher(double pushPosition) {
        armServo.setPosition(pushPosition);
    }

    @Override
    public void operatePusher(boolean leftPosition, boolean rightPosition) {
        if (leftPosition) {
            pushServo.setPosition(PUSH_LEFT_POSITION);
        } else if (rightPosition){
            pushServo.setPosition(PUSH_RIGHT_POSITION);
        } else {
            pushServo.setPosition(PUSH_MIDDLE_POSITION);
        }
    }
}
