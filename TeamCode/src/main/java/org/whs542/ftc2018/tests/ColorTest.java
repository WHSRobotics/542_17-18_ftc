package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.BalancingStoneSensor;
import org.whs542.ftc2018.subsys.Color;

/**
 * Created by Amar2 on 10/28/2017.
 */
@Autonomous(name = "ColorTest", group = "tests")
public class ColorTest extends OpMode{


    Color jewelSensor;
    Color balancingStoneSensor;

    @Override
    public void init() {
        jewelSensor = new Color(hardwareMap, "jewelSensor");
        balancingStoneSensor = new Color(hardwareMap, "balancingStoneSensor");
    }

    @Override
    public void loop() {
        telemetry.addData("Jewel Sensor Red: ", jewelSensor.getR());
        telemetry.addData("Jewel Sensor Blue: ", jewelSensor.getB());
        telemetry.addData("Jewel Sensor Green: ", jewelSensor.getG());

        telemetry.addData("Balancing Stone Sensor Red: ", balancingStoneSensor.getR());
        telemetry.addData("Balancing Stone Sensor Blue: ", balancingStoneSensor.getB());
        telemetry.addData("Balancing Stone Sensor Green: ", balancingStoneSensor.getG());
    }
}
