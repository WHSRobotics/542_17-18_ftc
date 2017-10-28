package org.whs542.ftc2018.subsys;

import org.whs542.subsys.intake.Intake;
import org.whs542.util.Toggler;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Jason on 10/20/2017.
 */

public class RollerIntake implements Intake {

    private DcMotor intakeMotor;
    private static final double INTAKE_POWER =  1.0;
    private Toggler intakeToggler = new Toggler(2);

    public void IntakeImpl(HardwareMap intakeMap) {
        intakeMotor = intakeMap.dcMotor.get("intake");
    }

    @Override
    public void operateWithToggle(double gamepadInput) {
        intakeMotor.setPower(INTAKE_POWER);
    }

    @Override
    public void operateWithToggle(boolean gamepadInput) {
        intakeToggler.changeState(gamepadInput);

        if (intakeToggler.currentState() == 0) {
            intakeMotor.setPower(INTAKE_POWER);
        } else if (intakeToggler.currentState() == 1) {
            intakeMotor.setPower(0);
        }
    }

    @Override
    public void operate(double power) {
        intakeMotor.setPower(power);
    }

}
