package org.whs542.subsys.linearslides;

/**
 * Created by Jason on 10/20/2017.
 */

public interface LinearSlides {

    void operateWithToggle(double gamepadInput);

    void operateWithToggle(boolean gamepadInput);

    void operate(double power);
}
