package org.whs542.subsys.jewelpusher;

/**
 * Created by Jason on 10/20/2017.
 */

public interface JewelPusher {

    void operateJewelPusherWithToggle(double gamepadInput);

    void operateJewelPusherWithToggle(boolean gamepadInput);

    void operateJewelPusher(double power);
}
