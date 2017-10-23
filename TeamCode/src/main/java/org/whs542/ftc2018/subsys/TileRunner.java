package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.subsys.drivetrain.TankDrivetrain;
import org.whs542.util.Toggler;

/**
 * Created by Jason on 10/20/2017.
 */

public class TileRunner implements TankDrivetrain {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private Toggler orientationSwitch = new Toggler(2);

    public TileRunner (HardwareMap driveMap) {

        frontLeft = driveMap.dcMotor.get("drive_fl");
        frontRight = driveMap.dcMotor.get("drive_fr");
        backLeft = driveMap.dcMotor.get("drive_bl");
        backRight = driveMap.dcMotor.get("drive_br");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        orientationSwitch.setState(1);

    }

    @Override
    public void operateWithOrientation(double leftPower, double rightPower) {
        switch (orientationSwitch.currentState()) {
            case 0:
                operateLeft(leftPower);
                operateRight(rightPower);
                break;
            case 1:
                operateLeft(-leftPower);
                operateRight(-rightPower);
                break;
        }
    }

    @Override
    public void operateWithOrientationScaled(double leftPower, double rightPower) {
        double rightScaledPower = Math.pow(rightPower, 3);
        double leftScaledPower = Math.pow(leftPower, 3);

        switch (orientationSwitch.currentState()) {
            case 0:
                operateLeft(rightScaledPower);
                operateRight(leftScaledPower);
                break;
            case 1:
                operateLeft(-rightScaledPower);
                operateRight(-leftScaledPower);
                break;
        }
    }

    @Override
    public void operate(double leftPower, double rightPower) {
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    @Override
    public void operateLeft(double leftPower) {
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
    }

    @Override
    public void operateRight(double rightPower) {
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    @Override
    public void switchOrientation(boolean gamepadInput) {
        orientationSwitch.changeState(gamepadInput);
    }

    @Override
    public String getOrientation() {
        return orientationSwitch.currentState() == 0 ? "reversed" : "normal";
    }

    @Override
    public double[] getEncoderDistance() {
        return new double[0];
    }

    @Override
    public double encToMM(double encoderTicks) {
        return 0;
    }

}
