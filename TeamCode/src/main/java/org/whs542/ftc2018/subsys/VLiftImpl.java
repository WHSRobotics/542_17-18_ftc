package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.subsys.vlift.VLift;
import org.whs542.util.SimpleTimer;

/**
 * Created by ivanm on 11/1/2017.
 */

public class VLiftImpl implements VLift {

    private Servo leftLiftServo;
    private Servo rightLiftServo;
    private Servo gateServo;
    public static final double[] LEFT_LIFT_POSITIONS = {0.28, 0.38, 0.7, 0.82}; //DOWN, MIDDLE_DOWN, MIDDLE_UP, UP
    public static final double[] RIGHT_LIFT_POSITIONS = {0.67, 0.57, 0.25, 0.13}; //DOWN, MIDDLE_DOWN, MIDDLE_UP, UP
    public static final double[] GATE_POSITIONS = {0.09, 0.32, 0.59}; //Closed, middle, open
    private static final double GAMEPAD_THRESHOLD = 0.05;
    private static final double JIGGLE_DURATION = 0.1;
    private String currentLiftPos = "";
    SimpleTimer jiggleTimer = new SimpleTimer();


    public VLiftImpl(HardwareMap liftMap) {
        leftLiftServo = liftMap.servo.get("leftLiftServo");
        rightLiftServo = liftMap.servo.get("rightLiftServo");
        gateServo =  liftMap.servo.get("gateServo");
    }

    @Override
    public void operateLift(double leftPosition, double rightPosition) {
        leftLiftServo.setPosition(leftPosition);
        rightLiftServo.setPosition(rightPosition);

    }

    @Override
    public void operateLift(boolean gamepadInput1, float gamepadInput2) {
        if (gamepadInput2 > GAMEPAD_THRESHOLD) {
            leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[3]);
            rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[3]);
            currentLiftPos = "up";
        }
        else if (gamepadInput1) {
            leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[2]);
            rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[2]);
            currentLiftPos = "middle_up";
        }
        else {
            leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[0]);
            rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[0]);
            currentLiftPos = "down";
        }
    }

    @Override
    public void operateLift(LiftPosition liftPosition) {
        leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[liftPosition.ordinal()]);
        rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[liftPosition.ordinal()]);

        switch (liftPosition) {
            case UP:
                currentLiftPos = "up";
            case MIDDLE_UP:
                currentLiftPos = "middle_up";
            case MIDDLE_DOWN:
                currentLiftPos = "middle_down";
            case DOWN:
                currentLiftPos = "down";
        }

    }

    @Override
    public void operateJiggle(boolean gamepadInput1) {
        if (gamepadInput1) {
            if (jiggleTimer.isExpired()) {
                jiggleTimer.set(JIGGLE_DURATION);
                leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[0]);
                rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[0]);
            } else {
                leftLiftServo.setPosition(LEFT_LIFT_POSITIONS[0] + 0.1);
                rightLiftServo.setPosition(RIGHT_LIFT_POSITIONS[0] - 0.1);
            }
        }
    }

    @Override
    public void operateGate(double position) {
        gateServo.setPosition(position);
    }

    @Override
    public void operateGate(boolean gamepadInput) {
        if (gamepadInput) {
            gateServo.setPosition(GATE_POSITIONS[2]);
        } else {
            gateServo.setPosition(GATE_POSITIONS[0]);
        }
    }

    @Override
    public void operateGate(GatePosition gatePosition) {
        gateServo.setPosition(GATE_POSITIONS[gatePosition.ordinal()]);
    }

    @Override
    public String getCurrentLiftPos() {
        return currentLiftPos;
    }

}