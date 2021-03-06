package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2018.subsys.IMU;
import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.subsys.drivetrain.TankDrivetrain;
import org.whs542.util.SimpleTimer;

/**
 * Created by Amar2 on 1/14/2018.
 */
@TeleOp(name = "IMUStabilityTest", group = "tests")
public class IMUStabilityTest extends OpMode{

    IMU imu;
    TankDrivetrain drivetrain;
    int i = 0;
    SimpleTimer timer = new SimpleTimer();
    boolean b = false;

    @Override
    public void init() {
        drivetrain = new TileRunner(hardwareMap);
        imu = new IMU(hardwareMap);
        imu.setImuBias(imu.getHeading());
    }

    @Override
    public void loop() {
        if(!b) {
            drivetrain.operateWithOrientationScaled(gamepad1.left_stick_y, gamepad1.right_stick_y);
            drivetrain.switchOrientation(gamepad1.a);
            telemetry.addData("IMU heading", imu.getHeading());
        }
        if (gamepad1.x && i==0){
            i++;
            imu = new IMU(hardwareMap);
            timer.set(5);
            b=true;
        }
        else if(timer.isExpired()){
            b=false;
        }
    }
}
