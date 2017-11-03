package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.util.Timer;
import org.whs542.util.Position;
import org.whs542.util.Functions;
import org.whs542.util.Coordinate;
import org.whs542.util.Toggler

/**
 * Created by Jason on 11/03/2017.
 */

@Autonomous(name = "WHSAuto", group = "auto")
public class WHSAuto extends OpMode {

    WHSRobotImpl robot;

    //Starting Coordinates
    static Coordinate[][] startingCoordinateArray = new Coordinate[2][2];
    static final int RED = 0;
    static final int BLUE = 1;
    public static final int ALLIANCE = RED;

    //States
    enum STATES {
        EXTEND_JEWEL_ARM, WAIT_FOR_COLOR, HIT_JEWEL, RETRACT_JEWEL_ARM, DRIVE_OFF_PLATFORM, DRIVE_TO_BOX, PLACE_GLYPH, PARK
    }
    STATES currentState;
    String currentStateDesc;

    @Override
    public void init() {

    }

    @Override
    public void loop() {

        //State Machine
        switch (currentState) {
            case EXTEND_JEWEL_ARM:
                currentState = STATES.WAIT_FOR_COLOR;
                currentStateDesc = "extending jewel arm";
                break;
            case WAIT_FOR_COLOR:
                currentState = STATES.HIT_JEWEL;
                currentStateDesc = "waiting for color sensor";
                break;
            case HIT_JEWEL:
                currentState = STATES.RETRACT_JEWEL_ARM;
                currentStateDesc = "slapping jewel";
                break;
            case RETRACT_JEWEL_ARM:
                currentState = STATES.DRIVE_OFF_PLATFORM;
                currentStateDesc = "retracting jewel arm";
                break;
            case DRIVE_OFF_PLATFORM:
                currentState = STATES.DRIVE_TO_BOX;
                currentStateDesc = "driving off platform";
                break;
            case DRIVE_TO_BOX:
                currentState = STATES.PLACE_GLYPH;
                currentStateDesc = "driving to box while scanning target";
                break;
            case PLACE_GLYPH:
                currentState = STATES.PARK;
                currentStateDesc = "moving glyph";
                break;
            case PARK:
                currentStateDesc = "parking, hooray we made it!";
            default: break;
        }

        //Logging the current state
        telemetry.log().add(currentStateDesc);
    }

    @Override
    public void stop() {

    }

}