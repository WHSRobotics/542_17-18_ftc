package org.whs542.ftc2018.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.RollerIntake;
import org.whs542.subsys.intake.Intake;


/**
 * Created by Amar2 on 11/3/2017.
 */

public class IntakeTest extends OpMode {

    Intake intake;

    @Override
    public void init() {
        intake = new RollerIntake(hardwareMap);
    }

    @Override
    public void loop() {
        intake.operateWithToggle();
    }
}
