package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.subsys.MotorSubsystem;
import org.whs542.subsys.fourbar.FourBar;
import org.whs542.util.Toggler;

/**
 * Created by Jason on 10/20/2017.
 */

public class FourBarImpl implements FourBar, MotorSubsystem{

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private static final double FOUR_BAR_POWER = 0.3;
    private static final int[] FOUR_BAR_LEVEL_POSITIONS = {0, 220, 440}; //In encoder ticks
    private String fourBarLevel = "";
    private static final double FOUR_BAR_DEADBAND = 10.0;

    //private Toggler fourBarToggler = new Toggler(2);

    public FourBarImpl(HardwareMap fourBarMap) {
        leftMotor = fourBarMap.dcMotor.get("leftFourBar");
        rightMotor = fourBarMap.dcMotor.get("rightFourBar");

        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        try {
            Thread.sleep(42);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void operate(boolean level0GamepadInput, boolean level1GamepadInput, boolean level2GamepadInput) {
        if (level0GamepadInput){
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[0]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[0]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
//            if (Math.abs(leftMotor.getCurrentPosition() - FOUR_BAR_LEVEL_POSITIONS[0]) < FOUR_BAR_DEADBAND) {
//                leftMotor.setPower(0.0);
//            }
//            if (Math.abs(rightMotor.getCurrentPosition() - FOUR_BAR_LEVEL_POSITIONS[0]) < FOUR_BAR_DEADBAND) {
//                rightMotor.setPower(0.0);
//            }
            fourBarLevel = "Scoring on top of 0 glyphs";
        }
        else if (level1GamepadInput){
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[1]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[1]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
//            if (Math.abs(leftMotor.getCurrentPosition() - FOUR_BAR_LEVEL_POSITIONS[1]) < FOUR_BAR_DEADBAND) {
//                leftMotor.setPower(0.0);
//            }
//            if (Math.abs(rightMotor.getCurrentPosition() - FOUR_BAR_LEVEL_POSITIONS[1]) < FOUR_BAR_DEADBAND) {
//                rightMotor.setPower(0.0);
//            }
            fourBarLevel = "Scoring on top of 1 glyph";
        }
        else if (level2GamepadInput){
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[2]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[2]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
//            if (Math.abs(leftMotor.getCurrentPosition() - FOUR_BAR_LEVEL_POSITIONS[2]) < FOUR_BAR_DEADBAND) {
//                leftMotor.setPower(0.0);
//            }
//            if (Math.abs(rightMotor.getCurrentPosition() - FOUR_BAR_LEVEL_POSITIONS[2]) < FOUR_BAR_DEADBAND) {
//                rightMotor.setPower(0.0);
//            }
            fourBarLevel = "Scoring on top of 2 glyphs";
//        } else {
//            leftMotor.setPower(0.0);
//            rightMotor.setPower(0.0);
        }
    }

    @Override
    public void operate(double power) {
        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }

    @Override
    public String getFourBarLevel() {
        return fourBarLevel;
    }

    @Override
    public double[] fourBarEncoderPositions() {
        double[] arrayPositions = {leftMotor.getCurrentPosition(), rightMotor.getCurrentPosition()};
        return arrayPositions;
    }

    @Override
    public double[] fourBarTargetPositions() {
        double[] targetPositions = {leftMotor.getTargetPosition(), rightMotor.getTargetPosition()};
        return  targetPositions;
    }

    @Override
    public void setRunMode(DcMotor.RunMode runMode) {
        leftMotor.setMode(runMode);
        rightMotor.setMode(runMode);
    }

    @Override
    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        leftMotor.setZeroPowerBehavior(zeroPowerBehavior);
        rightMotor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public double getAbsPowerAverage() {
        return (Math.abs(leftMotor.getPower()) + Math.abs(rightMotor.getPower()))/2;
    }
}
