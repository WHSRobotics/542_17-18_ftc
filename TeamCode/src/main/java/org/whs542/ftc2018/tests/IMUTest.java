package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.IMU;
import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.subsys.drivetrain.TankDrivetrain;

/**
 * Created by ivanm on 11/4/2017.
 */

public class IMUTest extends OpMode {

    IMU imu;
    TankDrivetrain drivetrain;

    @Override
    public void init() {
        imu = new IMU(hardwareMap);
        drivetrain = new TileRunner(hardwareMap);
    }

    @Override
    public void loop() {
        drivetrain.operate(0, 0);

        double heading = imu.getHeading();

        double[] threeHeading = imu.getThreeHeading();

        double accel = imu.getAccelerationMag();

        String headingValue = String.valueOf(heading);
        String accelValue = String.valueOf(accel);

        telemetry.addData("Heading: ", headingValue);
        telemetry.addData("Acceleration: ", accelValue);

        if(gamepad1.x){
            //imu.calibrateHeading();
        }

        telemetry.addData("x", threeHeading[0]);
        telemetry.addData("y", threeHeading[1]);
        telemetry.addData("z", threeHeading[2]);

    }
}
