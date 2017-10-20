package org.whs542.subsys.robot;

import org.whs542.util.Coordinate;
import org.whs542.util.Position;

/**
 * Created by Jason on 10/20/2017.
 */

public interface WHSRobot {

    void driveToTarget(Position targetPos);

    void rotateToTarget(double targetHeading); //-180 to 180 degrees

    Position estimatePosition();

    double estimateHeading();

    void setInitialCoordinate(Coordinate initCoord);

    void setCoordinate(Coordinate coord);

    void getCoordinate();

}
