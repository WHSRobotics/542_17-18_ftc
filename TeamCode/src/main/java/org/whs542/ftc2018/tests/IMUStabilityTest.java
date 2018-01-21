package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.IMU;
import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.subsys.drivetrain.TankDrivetrain;

/**
 * Created by Amar2 on 1/14/2018.
 */
@TeleOp(name = "IMUStabilityTest", group = "tests")
public class IMUStabilityTest extends OpMode{

    IMU imu;
    TankDrivetrain drivetrain;

    @Override
    public void init() {
        drivetrain = new TileRunner(hardwareMap);
        imu = new IMU(hardwareMap);
        imu.setImuBias(imu.getHeading());
    }

    @Override
    public void loop() {
        drivetrain.operateWithOrientationScaled(gamepad1.left_stick_y, gamepad1.right_stick_y);
        drivetrain.switchOrientation(gamepad1.a);
        telemetry.addData("IMU heading", imu.getHeading());
    }
}
