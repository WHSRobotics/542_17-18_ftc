package org.whs542.subsys.robot;

import org.whs542.subsys.intake.Intake;
import org.whs542.util.Coordinate;
import org.whs542.util.Position;

/**
 * Created by Jason on 10/20/2017.
 */

public interface WHSRobot {

    public void driveToTarget(Position targetPos);

    public void rotateToTarget(double targetHeading, boolean backwards); //-180 to 180 degrees

    public boolean driveToTargetInProgress();

    public boolean rotateToTargetInProgress();

    public Position estimatePosition();

    public double estimateHeading();

    public void setInitialCoordinate(Coordinate initCoord);

    public void setCoordinate(Coordinate coord);

    public Coordinate getCoordinate();

}
