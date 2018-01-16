package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by jewik on 1/12/2018.
 */

public class BalancingStoneSensor {
    Color balancingStoneSensor;

    public BalancingStoneSensor(HardwareMap balanceMap) {

        balancingStoneSensor = new Color(balanceMap, "balancingStoneSensor");

    }

    public boolean balancingStoneDetected (int alliance) {
        if (alliance == 0 && (balancingStoneSensor.getR()/balancingStoneSensor.getB() > 1.4)) {
            return true;
        }
        //TODO: test threshold for blue balancing stone
        else if (alliance == 1 && (balancingStoneSensor.getB()/balancingStoneSensor.getR() > 1.4)) {
            return true;
        }
        else {
            return false;
        }
    }

}
