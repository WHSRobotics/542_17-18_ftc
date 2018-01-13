package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by jewik on 1/12/2018.
 */

public class BalancingStoneSensor {
    ColorSensor balancingStoneSensor;
    boolean ledOn = true;
    String state = "";

    public BalancingStoneSensor(HardwareMap colorMap) {

        balancingStoneSensor = colorMap.colorSensor.get("jewelSensor");

        // Set the LED in the beginning
        balancingStoneSensor.enableLed(ledOn);
    }

        public int getR() {return balancingStoneSensor.red();}
        public int getG() {return balancingStoneSensor.green();}
        public int getB() {return balancingStoneSensor.blue();}
        public int getA() {return balancingStoneSensor.alpha();}

}
