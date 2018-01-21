package org.whs542.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Amar2 on 12/15/2017.
 */

public interface MotorSubsystem {

    void setRunMode (DcMotor.RunMode runMode);

    double getAbsPowerAverage();

}
