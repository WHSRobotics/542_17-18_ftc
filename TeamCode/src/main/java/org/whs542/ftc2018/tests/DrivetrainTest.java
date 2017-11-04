package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.TileRunner;
import org.whs542.subsys.drivetrain.TankDrivetrain;

/**
 * Created by Amar2 on 10/21/2017.
 */

public class DrivetrainTest extends OpMode {

    TankDrivetrain drivetrain;

    @Override
    public void init() {
        drivetrain = new TileRunner(hardwareMap);
    }

    @Override
    public void loop() {
        drivetrain.operateWithOrientation(gamepad1.left_stick_y, gamepad1.right_stick_y);
        drivetrain.switchOrientation(gamepad1.a);
    }
}