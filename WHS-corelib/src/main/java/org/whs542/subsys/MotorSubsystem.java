package org.whs542.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Amar2 on 12/15/2017.
 */

public interface MotorSubsystem {

    void setRunMode (DcMotor.RunMode runMode);

    void setZeroPowerBehavior (DcMotor.ZeroPowerBehavior zeroPowerBehavior);

    double getAbsPowerAverage();

}
