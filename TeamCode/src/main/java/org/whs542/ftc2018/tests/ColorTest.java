package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.Color;

/**
 * Created by Amar2 on 10/28/2017.
 */
@Autonomous(name = "ColorTest", group = "tests")
public class ColorTest extends OpMode{


    Color colorSensor;

    @Override
    public void init() {
        colorSensor = new Color(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Color Sensor Red: ", colorSensor.getR());
        telemetry.addData("Color Sensor Blue: ", colorSensor.getB());
        telemetry.addData("Color Sensor Green: ", colorSensor.getG());
    }
}
