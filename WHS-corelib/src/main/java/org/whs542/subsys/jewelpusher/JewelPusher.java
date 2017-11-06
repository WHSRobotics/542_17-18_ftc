package org.whs542.subsys.jewelpusher;


/**
 * Created by Jason on 10/20/2017.
 */

public interface JewelPusher {

    enum JewelColor  {
        RED, BLUE, ERROR
    }

    enum SwivelPosition {
        STORED, LEFT, MIDDLE, RIGHT;
    }

    enum ArmPosition {
        UP, MIDDLE, DOWN
    }

    JewelColor getJewelColor();

    void operateArm(double armPosition);

    void operateArm(ArmPosition armPosition);

    void operateSwivel(double pushPosition);

    void operateSwivel(SwivelPosition swivelPosition);


}
