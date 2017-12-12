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

    static final double[] ARM_POSITONS = {0.1, 0.71};               //UP, DOWN
    static final double[] SWIVEL_POSITIONS = {0.115, 0.24, 0.485, 0.72, 0.17}; //{0.165, 0.29, 0.535, 0.77, 0.22};     //STORED, LEFT, MIDDLE, RIGHT, END_STORED

    public Color colorSensor;
    private static final double COLOR_SENSOR_THRESHOLD = 1.5;

    public JewelPusherImpl(HardwareMap jewelMap) {
        armServo = jewelMap.servo.get("armServo");
        swivelServo = jewelMap.servo.get("swivelServo");
        colorSensor = new Color(jewelMap);
    }

    @Override
    public JewelColor getJewelColor() {
        if ((colorSensor.getR() / (colorSensor.getB()+0.001)) > COLOR_SENSOR_THRESHOLD) {
            return JewelColor.RED;
        } else if ((colorSensor.getB() / (colorSensor.getR()+0.001)) > COLOR_SENSOR_THRESHOLD) {
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
