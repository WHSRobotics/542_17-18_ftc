package org.whs542.subsys.intake;

/**
 * Created by Jason on 10/20/2017.
 */

public interface Intake {

    void operateIntakeWithToggle(double gamepadInput);

    void operateIntakeWithToggle(boolean gamepadInput);

    void operateIntake(double power);
}
