package org.whs542.subsys.vlift;

/**
 * Created by ivanm on 11/1/2017.
 */

public interface VLift {

    void operateLift(double leftPosition, double rightPosition);

    void operateLift(boolean gamepadInput);


}
