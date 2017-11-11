package org.whs542.subsys.fourbar;

/**
 * Created by Jason on 10/20/2017.
 */

public interface FourBar {

    void operate(boolean level0GamepadInput, boolean level1GamepadInput, boolean level2GamepadInput);

    void operate(double power);

    String getFourBarLevel();

    double[] fourBarEncoderPositions();

    double[] fourBarTargetPositions();


}
