package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "ColorSensorAcquireTest", group = "tests")
public class ColorSensorAcquireTimeTest extends OpMode{

	ColorSensor color;
	boolean jewelDetected = false;
	String jewelColor = "red";
	double millisIndex = 0;
	double timeToAcquire = 0;
	private static final double COLOR_SENSOR_THRESHOLD = 1.5;


	public void init(){
		color = hardwareMap.colorSensor.get("color");
		color.enableLed(true);
		telemetry.log().add("Controls: b/x|red/blue");
		telemetry.log().add("A: Restart timer");
	}

	public void loop(){

		if (gamepad1.b){
			jewelColor = "red";
			jewelDetected = false;
		} else if (gamepad1.x) {
			jewelColor = "blue";
			jewelDetected = false;
		}

		if (gamepad1.x){
			jewelDetected = false;
			millisIndex = getRuntime();
		}

		if (jewelColor=="red"){
			if ((color.red() / (color.blue()+0.001)) > COLOR_SENSOR_THRESHOLD) {
				jewelDetected = true;
				timeToAcquire = getRuntime()-millisIndex;
			}
		}
		if (jewelColor=="blue"){
			if ((color.blue() / (color.red()+0.001)) > COLOR_SENSOR_THRESHOLD) {
				jewelDetected = true;
				timeToAcquire = getRuntime()-millisIndex;
			}
		}

		telemetry.addData("Time:", jewelDetected ? timeToAcquire : getRuntime()-millisIndex);
		telemetry.addData("jewelDetected?", jewelDetected);

	}
}