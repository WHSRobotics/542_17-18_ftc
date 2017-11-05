package org.whs542.ftc2018.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2018.subsys.WHSRobotImpl;
import org.whs542.util.Coordinate;

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

    //State Definitions
    static final int INIT = 0;
    static final int HIT_JEWEL = 1;
    static final int DRIVE_INTO_SAFEZONE = 2;
    static final int DRIVE_TO_BOX = 3;
    static final int PLACE_GLYPH = 4;
    static final int END = 5;

    static final int NUM_OF_STATES = 6;

    boolean[] stateEnabled = new boolean[NUM_OF_STATES + 1];

    public void defineStateEnabledStatus() {
        stateEnabled[INIT] = true;
        stateEnabled[HIT_JEWEL] = true;
        stateEnabled[DRIVE_INTO_SAFEZONE] = true;
        stateEnabled[DRIVE_TO_BOX] = true;
        stateEnabled[PLACE_GLYPH] = true;
        stateEnabled[END] = true;
    }

    int currentState;
    String currentStateDesc;

    boolean performedStateEntry;
    boolean performStateExit;

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        currentState = INIT;

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
                advanceState();
                break;
            case HIT_JEWEL:
                currentStateDesc = "slapping jewel";
                currentState = States.DRIVE_INTO_SAFEZONE;
                break;
            case DRIVE_INTO_SAFEZONE:
                currentStateDesc = "driving off platform";
                currentState = States.DRIVE_TO_BOX;
                break;
            case DRIVE_TO_BOX:
                currentStateDesc = "driving to box while scanning target";
                currentState = States.PLACE_GLYPH;
                break;
            case PLACE_GLYPH:
                currentStateDesc = "moving glyph";
                currentState = States.END;
                break;
            case END:
                currentStateDesc = "hooray we made it!";
            default: break;
        }

        //Logging the current state
        telemetry.log().add(currentStateDesc);
    }

    public void advanceState() {
        if(stateEnabled[(currentState + 1)]) {
            currentState = currentState + 1;
        }
        else {
            currentState = currentState + 1;
            advanceState();
        }
    }
}