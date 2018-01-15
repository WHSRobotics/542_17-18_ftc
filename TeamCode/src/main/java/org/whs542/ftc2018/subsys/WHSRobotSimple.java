package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.util.Coordinate;

/**
 * Created by Amar2 on 1/14/2018.
 */

public class WHSRobotSimple {

    public TileRunner drivetrain;
    public IMU imu;
    public VLiftImpl lift;
    public RollerIntake intake;
    public FourBarImpl fourBar;
    public JewelPusherImpl jewelPusher;

    public WHSRobotSimple (HardwareMap hardwareMap){
        drivetrain = new TileRunner(hardwareMap);
        intake = new RollerIntake(hardwareMap);
        fourBar = new FourBarImpl(hardwareMap);
        jewelPusher = new JewelPusherImpl(hardwareMap);
        imu = new IMU(hardwareMap);
        lift = new VLiftImpl(hardwareMap);
    }

}
