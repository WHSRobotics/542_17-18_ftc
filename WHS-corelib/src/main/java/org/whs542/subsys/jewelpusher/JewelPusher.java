package org.whs542.subsys.jewelpusher;

/**
 * Created by Jason on 10/20/2017.
 */

public interface JewelPusher {

    JewelColor getJewelColor();

    enum JewelColor {
        RED, BLUE, ERROR
    }

    void extendArm(double armPosition);

    void extendArm(boolean downPosition);

    void operateSwivel(double pushPosition);

    void operateSwivel(boolean leftPosition, boolean rightPosition);

}
