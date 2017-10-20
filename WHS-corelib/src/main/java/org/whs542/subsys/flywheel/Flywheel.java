package org.whs542.subsys.flywheel;

/**
 * Created by Jason on 10/20/2017.
 */

public interface Flywheel {

    void operateFlywheelWithToggle(double gamepadInput);

    void operateFlywheelWithToggle(boolean gamepadInput);

    void operateFlywheel(double power);
}
