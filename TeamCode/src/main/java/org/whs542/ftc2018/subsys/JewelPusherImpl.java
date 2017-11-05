package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.subsys.jewelpusher.JewelPusher;

/**
 * Created by Jason on 10/20/2017.
 */

public class JewelPusherImpl implements JewelPusher {

    private Servo extendServo;
    private Servo swivelServo;
    //TODO: change these to actual values
    private static final double UNFOLDED_POSITION = 1;
    private static final double FOLDED_POSITION = 0;
    private static final double SWIVEL_START_POSITION = 0;
    private static final double SWIVEL_LEFT_POSITION = 0.5;
    private static final double SWIVEL_MIDDLE_POSITION = 0.4;
    private static final double SWIVEL_RIGHT_POSITION = 0.6;
    private Color colorSensor;
    private static final int COLOR_SENSOR_THRESHOLD = 10;

    public JewelPusherImpl(HardwareMap jewelMap) {
        extendServo = jewelMap.servo.get("extendServo");
        swivelServo = jewelMap.servo.get("swivelServo");
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
    public void extendArm(double extendPosition) {
        extendServo.setPosition(extendPosition);
    }

    @Override
    public void extendArm(boolean unfoldedPosition) {
        if (unfoldedPosition) {
            extendServo.setPosition(UNFOLDED_POSITION);
        } else {
            extendServo.setPosition(FOLDED_POSITION);
        }
    }

    @Override
    public void operateSwivel(double swivelPosition) {
        extendServo.setPosition(swivelPosition);
    }

    @Override
    public void operateSwivel(boolean leftPosition, boolean rightPosition) {
        if (leftPosition) {
            swivelServo.setPosition(SWIVEL_LEFT_POSITION);
        } else if (rightPosition){
            swivelServo.setPosition(SWIVEL_RIGHT_POSITION);
        } else {
            swivelServo.setPosition(SWIVEL_MIDDLE_POSITION);
        }
    }
}
