package org.whs542.subsys.jewelpusher;

/**
 * Created by Jason on 10/20/2017.
 */

public interface JewelPusher {

    JewelColor getJewelColor();

    enum JewelColor {
        RED, BLUE, ERROR
    }

    void operateArm(double armPosition);

    void operateArm(boolean downPosition);

    void operatePusher(double pushPosition);

    void operatePusher(boolean leftPosition, boolean rightPosition);

}
