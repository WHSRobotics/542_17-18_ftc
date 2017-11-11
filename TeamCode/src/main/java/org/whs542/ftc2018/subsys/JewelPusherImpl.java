package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.subsys.jewelpusher.JewelPusher;

/**
 * Created by Jason on 10/20/2017.
 */

public class JewelPusherImpl implements JewelPusher {

    private Servo armServo;
    private Servo swivelServo;
    //TODO: change these to actual values

    static final double[] ARM_POSITONS = {0.625, 0.3, 0.005};               //UP, MIDDLE, DOWN
    static final double[] SWIVEL_POSITIONS = {0.995, 0.3, 0.445, 0.73};     //STORED,LEFT, MIDDLE, RIGHT

    public Color colorSensor;
    private static final int COLOR_SENSOR_THRESHOLD = 10;

    public JewelPusherImpl(HardwareMap jewelMap) {
        armServo = jewelMap.servo.get("armServo");
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
    public void operateArm(double armPosition) {
        armServo.setPosition(armPosition);
    }

    @Override
    public void operateArm(ArmPosition armPosition) {
        armServo.setPosition(ARM_POSITONS[armPosition.ordinal()]);
    }

    @Override
    public void operateSwivel(double swivelPosition) {
        swivelServo.setPosition(swivelPosition);
    }

    @Override
    public void operateSwivel(SwivelPosition swivelPosition) {
        swivelServo.setPosition(SWIVEL_POSITIONS[swivelPosition.ordinal()]);
    }
}
