package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.subsys.robot.WHSRobot;
import org.whs542.util.Coordinate;
import org.whs542.util.Position;

/**
 * Created by Amar2 on 11/15/2017.
 */
@Autonomous(name = "DriveToTargetTest", group = "tests")
public class DriveToTargetTest extends OpMode {
    WHSRobotImpl robot;
    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        robot.setInitialCoordinate(new Coordinate(0, 0, 150, 0));
    }

    @Override
    public void start(){
        robot.driveToTarget(new Position(1200, 0, 150));
    }

    @Override
    public void loop() {
        if(robot.driveToTargetInProgress) {
            robot.driveToTarget(new Position(1200, 0, 150));
        }
    }
}