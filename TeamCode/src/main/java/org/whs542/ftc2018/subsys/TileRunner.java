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

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    private Toggler orientationSwitch = new Toggler(2);

    private static final double RADIUS_OF_WHEEL = 50;
    private static final double CIRC_OF_WHEEL = RADIUS_OF_WHEEL * 2 * Math.PI;
    private static final double ENCODER_TICKS_PER_REV = 1120;                                   //Neverest 40
    private static final double GEAR_RATIO = 1;
    private static final double ENCODER_TICKS_PER_MM = ENCODER_TICKS_PER_REV / (CIRC_OF_WHEEL * GEAR_RATIO);

    private double[] encoderValues = {0.0, 0.0};

    public TileRunner (HardwareMap driveMap) {

        frontLeft = driveMap.dcMotor.get("driveFL");
        frontRight = driveMap.dcMotor.get("driveFR");
        backLeft = driveMap.dcMotor.get("driveBL");
        backRight = driveMap.dcMotor.get("driveBR");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);



        orientationSwitch.setState(1);

    }

    @Override
    public void operateWithOrientation(double leftPower, double rightPower) {
        switch (orientationSwitch.currentState()) {
            case 0:
                operateLeft(-rightPower);
                operateRight(-leftPower);
                break;
            case 1:
                operateLeft(leftPower);
                operateRight(rightPower);
                break;
        }
    }

    @Override
    public void operateWithOrientationScaled(double leftPower, double rightPower) {
        double rightScaledPower = Math.pow(rightPower, 3);
        double leftScaledPower = Math.pow(leftPower, 3);

        operateWithOrientation(leftScaledPower, rightScaledPower);
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



    public double getRightEncoderPosition()
    {
        /*double rightTotal = backRight.getCurrentPosition() + frontRight.getCurrentPosition();
        return rightTotal * 0.5;*/
        return frontRight.getCurrentPosition();
    }

    public double getLeftEncoderPosition()
    {
        /*double leftTotal = backLeft.getCurrentPosition() +frontLeft.getCurrentPosition();
        return leftTotal * 0.5;*/
        return frontLeft.getCurrentPosition();
    }

    public double getEncoderPosition() {
        double position = frontRight.getCurrentPosition() + frontLeft.getCurrentPosition() + backRight.getCurrentPosition() + backLeft.getCurrentPosition();
        return position * 0.25;
    }
    public double[] getEncoderDelta()
    {
        double currentLeft = getLeftEncoderPosition();
        double currentRight = getRightEncoderPosition();

        double[] encoderDistances = {currentLeft - encoderValues[0], currentRight - encoderValues[1]};

        encoderValues[0] = currentLeft;
        encoderValues[1] = currentRight;

        return encoderDistances;
    }


    @Override
    public double encToMM(double encoderTicks) {
        return encoderTicks * (1/ENCODER_TICKS_PER_MM);
    }

    @Override
    public void setRunMode(DcMotor.RunMode runMode) {
        frontLeft.setMode(runMode);
        frontRight.setMode(runMode);
        backLeft.setMode(runMode);
        backRight.setMode(runMode);
    }
}
