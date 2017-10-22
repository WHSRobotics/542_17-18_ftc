package org.whs542.subsys.flywheel;

/**
 * Created by Jason on 10/20/2017.
 */

public interface Flywheel {

    void operateWithToggle(double gamepadInput);

    void operateWithToggle(boolean gamepadInput);

    void operate(double power);
}
