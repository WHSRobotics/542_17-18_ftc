package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Container class for the color sensor
 */

public class Color {
    ColorSensor colorSensor;    // Hardware Device Object
    boolean ledOn = true;     //Determine whether the LED is on or not
    String state = "";


    public Color(HardwareMap colorMap) {

        colorSensor = colorMap.colorSensor.get("colorSensor");

        // Set the LED in the beginning
        colorSensor.enableLed(ledOn);

    }



    //Methods to get Red, Green, Blue, or Alpha values from the colorsensor
    public int getR() {return colorSensor.red();}
    public int getG() {return colorSensor.green();}
    public int getB() {return colorSensor.blue();}
    public int getA() {return colorSensor.alpha();}

}