package org.whs542.subsys.drivetrain;

/**
 * Created by Jason on 10/20/2017.
 */

public interface TankDrivetrain {

    void operateWithOrientation(double leftPower, double rightPower);

    void operateWithOrientationScaled(double leftPower, double rightPower);

    void operate(double leftPower, double rightPower);

    void operateLeft(double leftPower);

    void operateRight(double rightPower);

    void switchOrientation(boolean gamepadInput);

    String getOrientation();

    double[] getEncoderDistance();

    double encToMM(double encoderTicks);

}
