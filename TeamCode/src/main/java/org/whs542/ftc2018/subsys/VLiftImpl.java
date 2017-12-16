package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.subsys.vlift.VLift;

/**
 * Created by ivanm on 11/1/2017.
 */

public class VLiftImpl implements VLift {

    private Servo leftLiftServo;
    private Servo rightLiftServo;
    //TODO: change phone configs and get actual gate positions
    //private Servo gateServo;
    public static final double[] LEFT_LIFT_POSITIONS = {0.28, 0.7, 0.82}; //Down, middle, up
    public static final double[] RIGHT_LIFT_POSITIONS = {0.67, 0.25, 0.13}; //Down, middle, up
    public static final double[] GATE_POSITIONS = {0.0, 1.0}; //Closed, open
    private static final double GAMEPAD_THRESHOLD = 0.05;


    public VLiftImpl(HardwareMap liftMap) {
        leftLiftServo = liftMap.servo.get("leftLiftServo");
        rightLiftServo = liftMap.servo.get("rightLiftServo");
    }

    @Override
    public void operateLift(double leftPosition, double rightPosition) {
        leftLiftServo.setPosition(leftPosition);
        rightLiftServo.setPosition(rightPosition);

    }

    @Override
    public void operateLift(boolean gamepadInput1, float gamepadInput2) {
        if (gamepadInput2 > GAMEPAD_THRESHOLD) {
            leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[2]);
            rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[2]);
        }
        else if (gamepadInput1) {
            leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[1]);
            rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[1]);
        }
        else {
            leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[0]);
            rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[0]);
        }
    }



}
