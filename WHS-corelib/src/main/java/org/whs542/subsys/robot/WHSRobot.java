package org.whs542.subsys.robot;

import org.whs542.util.Coordinate;
import org.whs542.util.Position;

/**
 * Created by Jason on 10/20/2017.
 */

public interface WHSRobot {

    public void driveToTarget(Position targetPos, boolean backwards);

    public void rotateToTarget(double targetHeading, boolean backwards); //-180 to 180 degrees

    public boolean driveToTargetInProgress();

    public boolean rotateToTargetInProgress();

    public void estimatePosition();

    public void estimateHeading();

    public void setInitialCoordinate(Coordinate initCoord);

    public void setCoordinate(Coordinate coord);

    void setPosition(Position pos);

    public Coordinate getCoordinate();

}
