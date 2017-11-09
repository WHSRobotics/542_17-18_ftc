package org.whs542.ftc2018.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.subsys.fourbar.FourBar;
import org.whs542.util.Toggler;

/**
 * Created by Jason on 10/20/2017.
 */

public class FourBarImpl implements FourBar {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private static final double FOUR_BAR_POWER = 1.0;
    //TODO: Change these to actual values
    private static final int[] FOUR_BAR_LEVEL_POSITIONS = {0, 1000, 2000, 3000}; //In encoder ticks
    private String fourBarLevel;
    //private Toggler fourBarToggler = new Toggler(2);

    public FourBarImpl(HardwareMap fourBarMap) {
        leftMotor = fourBarMap.dcMotor.get("leftFourBar");
        rightMotor = fourBarMap.dcMotor.get("rightFourBar");
        //TODO: See if this is correct
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    }

    @Override
    public void operate(boolean level0GamepadInput, boolean level1GamepadInput, boolean level2GamepadInput, boolean level3GamepadInput) {
        if (level0GamepadInput){
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[0]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[0]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
            fourBarLevel = "Scoring on top of 0 glyphs";
        }
        else if (level1GamepadInput){
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[1]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[1]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
            fourBarLevel = "Scoring on top of 1 glyph";
        }
        else if (level2GamepadInput){
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[2]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[2]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
            fourBarLevel = "Scoring on top of 2 glyphs";
        }
        else {
            leftMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[3]);
            rightMotor.setTargetPosition(FOUR_BAR_LEVEL_POSITIONS[3]);
            leftMotor.setPower(FOUR_BAR_POWER);
            rightMotor.setPower(FOUR_BAR_POWER);
            fourBarLevel = "Scoring on top of 3 glyphs";
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
}
