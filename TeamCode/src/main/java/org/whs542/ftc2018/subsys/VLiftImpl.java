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
    private Servo gateServo;
    //TODO: Actually set these values
    private static final double LEFT_LIFT_DOWN_POSITION = 0;
    private static final double LEFT_LIFT_UP_POSITION = 1;
    private static final double RIGHT_LIFT_DOWN_POSITION = 1;
    private static final double RIGHT_LIFT_UP_POSITION = 0;
    private static final double GAMEPAD_THRESHOLD = 0.05;
    private static final double GATE_OPEN_POSITION = 1;
    private static final double GATE_CLOSED_POSITION = 0;

    public VLiftImpl(HardwareMap liftMap) {
        leftLiftServo = liftMap.servo.get("leftLiftServo");
        rightLiftServo = liftMap.servo.get("rightLiftServo");
        gateServo = liftMap.servo.get("gateServo");
    }

    @Override
    public void operateLift(double position) {
        leftLiftServo.setPosition(position);
        rightLiftServo.setPosition(position);
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

    @Override
    public void operateLift(float gamepadInput) {
        if (gamepadInput > GAMEPAD_THRESHOLD) {
            gateServo.setPosition(GATE_OPEN_POSITION);
        }
        else {
            gateServo.setPosition(GATE_CLOSED_POSITION);
        }
    }
}
