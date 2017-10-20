package org.whs542.subsys.drivetrain;

/**
 * Created by Jason on 10/20/2017.
 */

public interface TankDrivetrain {

    void operateDriveTrainWithOrientation(double left, double right);

    void operateDriveTrain(double left, double right);
}
