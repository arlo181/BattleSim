/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import battlesimulator.BattlefieldObject;
import java.awt.Point;

/**
 *
 * @author Arlo
 */
public class Utils
{

    public static double adjustAngleValue(double angle)
    {
        double adjustedAngle = angle;
        while (adjustedAngle < 0)
        {
            adjustedAngle += 360;
        }
        adjustedAngle %= 360;

        return adjustedAngle;
    }
    
    public static double convertCompassDirectionToJava(double compassDirection)
    {
        double newAngle = (compassDirection -90)*-1;
        
        
        return Utils.adjustAngleValue(newAngle);
    }

    public static boolean angleIsBetween(double startAngle, double endAngle, double angleToCheck)
    {
        endAngle = (endAngle - startAngle) < 0 ? endAngle - startAngle + 360 : endAngle - startAngle;
        angleToCheck = (angleToCheck - startAngle) < 0 ? angleToCheck - startAngle + 360 : angleToCheck - startAngle;
        return (angleToCheck <= endAngle);
    }

    public static double degreesToRadians(double degrees)
    {
        return degrees * Math.PI / 180;
    }

    public static Point getPointAtDistanceInDirection(Point origin, double cardinalDirection, double distance)
    {

        Point point = new Point(0, 0);
        double xDisplacement = 0;
        double yDisplacement = 0;
        //if horizontal to the right
        if (cardinalDirection == 90)
        {
            xDisplacement = distance;
        } //if horizontal to the left
        else if (cardinalDirection == 270)
        {
            xDisplacement = -distance;
        } //if vertical up
        else if (cardinalDirection == 0 || cardinalDirection == 360)
        {
            yDisplacement = -distance;
        } //if vertical down
        else if (cardinalDirection == 180)
        {
            yDisplacement = distance;
        } else
        {
            int yFactor = -1;
            double angle = Math.PI / 180 * (90 - cardinalDirection);

            yDisplacement = yFactor * (int) Math.round(distance * Math.sin(angle));
            xDisplacement = (int) Math.round(distance * Math.cos(angle));
        }
        point.setLocation(origin.x + xDisplacement, origin.y + yDisplacement);
        return point;
    }

    public static long getSquaredDistance(Point firstPoint, Point secondPoint)
    {
        //return x^2 + y^2
        long xDiff = (firstPoint.x - secondPoint.x);
        long yDiff = (firstPoint.y - secondPoint.y);
        return xDiff * xDiff + yDiff * yDiff;
    }

    public static double findBearing(Point firstPoint, Point secondPoint)
    {
        double bearing = 0;
        double x1 = firstPoint.getX();
        double y1 = firstPoint.getY();

        double x2 = secondPoint.getX();
        double y2 = secondPoint.getY();
        if (y2 == y1)
        {
            bearing = (x2 > x1) ? 90 : 270;
        } else if (x2 == x1)
        {
            bearing = (y2 > y1) ? 180 : 0;
        } else
        {
            bearing = 90 - (180 / Math.PI) * arcTan2_v3(y1 - y2, x2 - x1) % 360;
        }
        bearing = Utils.adjustAngleValue(bearing);

        return bearing;

    }

    //trying to use a precompiled arctan2 function found here:
    //http://guihaire.com/code/?p=1168
    static public double arcTan2_v3(double y, double x)
    {
        if (y > 0)
        {
            if (x >= 0)
            {
                return 0.78539816339744830961566084581988 - 0.78539816339744830961566084581988 * (x - y) / (x + y);
            } else
            {
                return 2.3561944901923449288469825374596 - 0.78539816339744830961566084581988 * (x + y) / (y - x);
            }
        } else
        {
            if (x >= 0)
            {
                return -0.78539816339744830961566084581988 + 0.78539816339744830961566084581988 * (x + y) / (x - y);
            }
        }
        return -2.3561944901923449288469825374596 - 0.78539816339744830961566084581988 * (x - y) / (y + x);
    }

    public static boolean targetInReach(BattlefieldObject origin, BattlefieldObject target, int reach)
    {
        boolean inReach = false;

        double distance = origin.getCenterPoint().distance(target.getCenterPoint());
        //subtract the "personal space"
        distance -= (origin.getPersonalSpaceRadius() + target.getPersonalSpaceRadius());
        //System.out.println("distance for combat: " + distance);
        return distance <= reach;

    }

    public static Point rotatePoint(Point point, Point pointToRotateAround, int degreesClockwise)
    {
        int x, y;
        double radians = Utils.degreesToRadians(degreesClockwise);
        double x1 = point.x - pointToRotateAround.x;
        double y1 = point.y - pointToRotateAround.y;

        double x2 = x1 * Math.cos(radians) - (y1 * Math.sin(radians));
        double y2 = x1 * Math.sin(radians) + (y1 * Math.cos(radians));

        x = (int) Math.rint(x2 + pointToRotateAround.x);
        y = (int) Math.rint(y2 + pointToRotateAround.y);

        return new Point(x, y);
    }

    public static boolean pointOutOfBounds(Point point, int widthPixels, int heightPixels)
    {
        boolean outOfBounds
                = (point.x > widthPixels)
                || (point.x < 0)
                || (point.y > heightPixels)
                || (point.y < 0);
        return outOfBounds;
    }
}
