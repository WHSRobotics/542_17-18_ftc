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
    //TODO: Actually set these values
    private static final double LEFT_LIFT_DOWN_POSITION = 0;
    private static final double LEFT_LIFT_UP_POSITION = 1;
    private static final double RIGHT_LIFT_DOWN_POSITION = 1;
    private static final double RIGHT_LIFT_UP_POSITION = 0;

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
    public void operateLift(boolean gamepadInput) {
        if (gamepadInput) {
            leftLiftServo.setPosition(LEFT_LIFT_UP_POSITION);
            rightLiftServo.setPosition(RIGHT_LIFT_UP_POSITION);
        }
        else {
            leftLiftServo.setPosition(LEFT_LIFT_DOWN_POSITION);
            rightLiftServo.setPosition(RIGHT_LIFT_DOWN_POSITION);
        }
    }
}
