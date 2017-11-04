package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.util.Timer;
import org.whs542.util.Position;
import org.whs542.util.Functions;
import org.whs542.util.Coordinate;
import org.whs542.util.Toggler;

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
        INIT, HIT_JEWEL, DRIVE_INTO_SAFEZONE, DRIVE_TO_BOX, PLACE_GLYPH, END;
        public static STATES[] vals = values();
        public STATES next() {
            return vals[(this.ordinal()+1) % vals.length];
        }
    }
    STATES currentState;
    String currentStateDesc;
    static final int NUM_OF_STATES = STATES.values().length;

    boolean[] stateEnabled = new boolean[NUM_OF_STATES + 1];

    public void defineStateEnabled() {
        stateEnabled[STATES.INIT.ordinal()] = true;
        stateEnabled[STATES.HIT_JEWEL.ordinal()] = true;
        stateEnabled[STATES.DRIVE_INTO_SAFEZONE.ordinal()] = true;
        stateEnabled[STATES.DRIVE_TO_BOX.ordinal()] = true;
        stateEnabled[STATES.PLACE_GLYPH.ordinal()] = true;
        stateEnabled[STATES.END.ordinal()] = true;
    }

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        currentState = STATES.INIT; //set the initial state

        telemetry.setMsTransmissionInterval(50); //set driver station update frequency
        telemetry.log().setCapacity(6); //set max number of lines logged by telemetry
    }

    @Override
    public void loop() {

        robot.estimateHeading();
        robot.estimatePosition();

        //State Machine
        switch (currentState) {
            case INIT:
                currentStateDesc = "beginning AutoOp";
                currentState = STATES.next();
                break;
            case HIT_JEWEL:
                currentStateDesc = "slapping jewel";
                currentState = STATES.DRIVE_INTO_SAFEZONE;
                break;
            case DRIVE_INTO_SAFEZONE:
                currentStateDesc = "driving off platform";
                currentState = STATES.DRIVE_TO_BOX;
                break;
            case DRIVE_TO_BOX:
                currentStateDesc = "driving to box while scanning target";
                currentState = STATES.PLACE_GLYPH;
                break;
            case PLACE_GLYPH:
                currentStateDesc = "moving glyph";
                currentState = STATES.END;
                break;
            case END:
                currentStateDesc = "hooray we made it!";
            default: break;
        }

        //Logging the current state
        telemetry.log().add(currentStateDesc);
    }
}