package org.whs542.subsys.intake;

/**
 * Created by Jason on 10/20/2017.
 */

public interface Intake {

    void operateWithToggle(boolean intakeGamepadInput, double outtakeGamepadInput);

    //void operateWithToggle(boolean gamepadInput);

    void operate(double power);
}
