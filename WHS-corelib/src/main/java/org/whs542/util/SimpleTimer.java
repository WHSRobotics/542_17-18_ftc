package org.whs542.util;

//import com.qualcomm.ftccommon.DbgLog;

/**
 * Simplified Timer Class
 *
 * @see Timer
 */

public class SimpleTimer
{

    public double expirationTime; //in seconds

    public SimpleTimer()
    {

    }

    public void set(double timerDuration)
    {
        double currentTime = (double) System.currentTimeMillis() / 1000; //time in seconds
        expirationTime = currentTime + timerDuration;
    }

    public boolean isExpired()
    {
        //DbgLog.msg("whs isExpired entered");
        double currentTime = (double) System.currentTimeMillis() / 1000; //time in seconds
        //DbgLog.msg("whs currentTime found");
        if(expirationTime < currentTime)
        {
            //DbgLog.msg("already expired");
        }
        return (currentTime > expirationTime);
    }

    /*public double getCurrentTime()
    {
        double currentTime = (double) System.currentTimeMillis() / 1000; //time in seconds
        return currentTime;
    }*/
}
