package org.whs542.subsys.vlift;

/**
 * Created by ivanm on 11/1/2017.
 */

public interface VLift {

    enum LiftPosition {
        DOWN, MIDDLE_DOWN, MIDDLE_UP, UP
    }

    enum GatePosition {
        CLOSED, MIDDLE, OPEN
    }

    void operateLift(double leftPosition, double rightPosition);

    void operateLift(boolean gamepadInput1, float gamepadInput2);

    void operateLift(LiftPosition liftPosition);

    void operateJiggle(boolean gamepadInput1);

    void operateGate(double position);

    void operateGate(boolean gamepadInput);

    void operateGate(GatePosition gatePosition);

    String getCurrentLiftPos();

}
