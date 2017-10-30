package org.whs542.subsys.robot;

import org.whs542.subsys.intake.Intake;
import org.whs542.util.Coordinate;
import org.whs542.util.Position;

/**
 * Created by Jason on 10/20/2017.
 */

public abstract class WHSRobot {

    public abstract void driveToTarget(Position targetPos);

    public abstract void rotateToTarget(double targetHeading); //-180 to 180 degrees

    public abstract Position estimatePosition();

    public abstract double estimateHeading();

    public abstract void setInitialCoordinate(Coordinate initCoord);

    public abstract void setCoordinate(Coordinate coord);

    public abstract Coordinate getCoordinate();

}
