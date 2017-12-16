package org.whs542.ftc2018.subsys;

import org.whs542.subsys.MotorSubsystem;
import org.whs542.subsys.intake.Intake;
import org.whs542.util.Toggler;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Jason on 10/20/2017.
 */

public class RollerIntake implements Intake, MotorSubsystem {

    private DcMotor intakeMotor;
    private static final double INTAKE_POWER = 1.0;
    private Toggler intakeToggler = new Toggler(2);
    private Toggler outtakeToggler = new Toggler(2);
    private static final double GAMEPAD_THRESHOLD = 0.05;

    public RollerIntake(HardwareMap intakeMap) {
        intakeMotor = intakeMap.dcMotor.get("intake");
    }

    public void operateWithToggle(double gamepadInput) {
        if (gamepadInput > GAMEPAD_THRESHOLD) {
            intakeToggler.changeState(true);
        } else {
            intakeToggler.changeState(false);
        }

        if (intakeToggler.currentState() == 1) {
            intakeMotor.setPower(INTAKE_POWER);
        } else if (intakeToggler.currentState() == 0) {
            intakeMotor.setPower(0);
        }
    }

    @Override
    public void operateWithToggle(boolean intakeGamepadInput, double outtakeGamepadInput) {
        intakeToggler.changeState(intakeGamepadInput);
        outtakeToggler.changeState(outtakeGamepadInput > GAMEPAD_THRESHOLD);

        //If only intake is toggled on, turn motor forwards (turn on intake)
        if (intakeToggler.currentState() == 1 && outtakeToggler.currentState() == 0) {
            intakeMotor.setPower(INTAKE_POWER);

        //If only outtake is toggled on, turn motor backwards (turn on outtake)
        } else if (intakeToggler.currentState() == 0 && outtakeToggler.currentState() == 1) {
            intakeMotor.setPower(-INTAKE_POWER);

        //If both are toggled off, turn motor off
        } else if (intakeToggler.currentState() == 0 && outtakeToggler.currentState() == 0) {
            intakeMotor.setPower(0);

        //If both are toggled on, reset both togglers to off
        } else {
            intakeToggler.setState(0);
            outtakeToggler.setState(0);
            intakeMotor.setPower(0);
        }
    }

    @Override
    public void operate(double power) {
        intakeMotor.setPower(power);
    }

    @Override
    public void setRunMode(DcMotor.RunMode runMode) {
        intakeMotor.setMode(runMode);
    }

}
