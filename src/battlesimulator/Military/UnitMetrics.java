/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.Utils;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class UnitMetrics implements java.io.Serializable
{

    int orientation;
    int distanceToClosestEnemy;
    ArrayList<Point> points;
    int tickCount;
    String allegiance;
    String unitName;
    Color color;
    Point centerMass;
    Rectangle boundingRectangle;
    int sumX;
    int sumY;
    int maxX;
    int minX;
    int maxY;
    int minY;
    int activeNumber;
    int activeCounter;
    int numMelee;
    int numRanged;
    int numMounted;
    boolean mostlyRanged;
    boolean mostlyMelee;
    boolean mostlyMounted;
    int numEngaged;
    double percentEngaged;
    boolean inFormation;
    int maxNumInUnit;
    Point corners[];
    double width;
    double height;
    int classScore;

    public UnitMetrics(int tickCount, String allegiance, Color color, String unitName, int maxNumInUnit)
    {
        this.orientation = 0;
        this.distanceToClosestEnemy = Short.MAX_VALUE;
        this.points = new ArrayList<>();
        this.corners = new Point[4];
        for (int i = 0; i < corners.length; i++)
        {
            this.corners[i] = new Point(0, 0);
        }
        this.centerMass = new Point(0, 0);
        this.boundingRectangle = new Rectangle();
        this.tickCount = tickCount;
        this.allegiance = allegiance;
        this.color = color;
        this.sumX = 0;
        this.sumY = 0;
        this.maxX = 0;
        this.minX = Integer.MAX_VALUE;
        this.maxY = 0;
        this.minY = Integer.MAX_VALUE;
        this.activeNumber = 0;
        this.activeCounter = 0;
        this.numMelee = 0;
        this.numRanged = 0;
        this.numMounted = 0;
        this.classScore = 0;
        this.unitName = unitName;
        this.mostlyRanged = false;
        this.mostlyMelee = false;
        this.mostlyMounted = false;
        this.numEngaged = 0;
        this.percentEngaged = 0.0;
        this.inFormation = false;
        this.maxNumInUnit = maxNumInUnit;
    }

    public UnitMetrics(int tickCount, String allegiance, String unitName, Color color, Point centerMass, Rectangle boundingRectangle, int activeNumber, boolean mostlyRanged, boolean mostlyMelee, boolean mostlyMounted, double percentEngaged, boolean inFormation, int maxNumInUnit, int distanceToEnemy, int classScore)
    {
        this.tickCount = tickCount;
        this.allegiance = allegiance;
        this.classScore = classScore;
        this.unitName = unitName;
        this.color = color;
        this.centerMass = centerMass;
        this.boundingRectangle = boundingRectangle;
        this.activeNumber = activeNumber;
        this.mostlyRanged = mostlyRanged;
        this.mostlyMelee = mostlyMelee;
        this.mostlyMounted = mostlyMounted;
        this.percentEngaged = percentEngaged;
        this.inFormation = inFormation;
        this.maxNumInUnit = maxNumInUnit;
        this.distanceToClosestEnemy = distanceToEnemy;
    }

    @Override
    public UnitMetrics clone()
    {
        UnitMetrics copy = new UnitMetrics(this.tickCount, this.allegiance, this.unitName, this.color, (Point) this.centerMass.clone(), (Rectangle) this.boundingRectangle.clone(), this.activeNumber, this.mostlyRanged, this.mostlyMelee, this.mostlyMounted, this.percentEngaged, this.inFormation, this.maxNumInUnit, this.distanceToClosestEnemy, this.classScore);
        copy.setOrientation(this.orientation);
        copy.setWidth(this.width);
        copy.setHeight(this.height);
        return copy;
    }

    public void setWidth(double width)
    {
        this.width = width;
    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public void setOrientation(int orientation)
    {
        this.orientation = orientation;
    }

    public int getOrientation()
    {
        return orientation;
    }

    public int getClassScore()
    {
        return this.classScore;
    }

    public Rectangle getBoundingRectangle()
    {
        return boundingRectangle;
    }

    public void finalizeUnitMetrics(int tickCount)
    {
        this.activeNumber = this.activeCounter;
        if (this.activeNumber > 0)
        {
            this.centerMass = new Point(sumX / activeNumber, sumY / activeNumber);
            this.percentEngaged = (double) this.numEngaged / this.activeNumber;
        } else
        {
            this.centerMass = new Point(0, 0);
            this.percentEngaged = 0.0;
        }
        this.boundingRectangle = new Rectangle(minX, minY, (maxX - minX), (maxY - minY));
        this.tickCount = tickCount;
        this.mostlyRanged = this.numRanged > this.activeNumber / 2;
        this.mostlyMelee = this.numMelee > this.activeNumber / 2;
        this.mostlyMounted = this.numMounted > this.activeNumber / 2;

//        this.findSmallestBoundingRectangle();
    }

    public void findSmallestBoundingRectangle()
    {

        //this uses the least square method to calculate the line of best fit
        int sumNumerator = 0; //top
        int sumDenominator = 0; //bottom
        int yIntercept;
        double slope;
        Point leftOfLine;
        Point rightOfLine;
        if(this.centerMass != null)
        {
        for (Point point : this.points)
        {
            int xPortion = (point.x - this.centerMass.x);
            sumNumerator += (xPortion * (point.y - this.centerMass.y));
            sumDenominator += xPortion * xPortion;
        }
        slope = 1.0 * sumNumerator / sumDenominator;

        yIntercept = (int) (this.centerMass.y - slope * this.centerMass.x);
        leftOfLine = new Point(this.minX, (int) (this.minX * slope + yIntercept)); //min x plugged into y = mx+b
        rightOfLine = new Point(this.maxX, (int) (this.maxX * slope + yIntercept));
        double bearingFromLeftToRight = Utils.findBearing(leftOfLine, rightOfLine);
        double bearingToTopCorners = Utils.adjustAngleValue(bearingFromLeftToRight - 90);
        double bearingToBottomCorners = Utils.adjustAngleValue(bearingFromLeftToRight + 90);
        Line2D.Double lineOfBestFit = new Line2D.Double(leftOfLine, rightOfLine);

        double maxAboveDistance = 0;
        double maxBelowDistance = 0;
        //determine if points are above/below the line and their distance from the line segment.  
        //Get an "above line max distance" and a "below line max distance"
        for (Point point : this.points)
        {
            int testY = (int) (slope * point.x + yIntercept);
            double distanceFromLine = lineOfBestFit.ptLineDist(point);

            boolean aboveLine = point.y > testY;
            if (aboveLine && distanceFromLine > maxAboveDistance)
            {
                maxAboveDistance = distanceFromLine;
            } else if (distanceFromLine < maxBelowDistance)
            {
                maxBelowDistance = distanceFromLine;
            }
        }
        this.corners[0] = Utils.getPointAtDistanceInDirection(leftOfLine, bearingToTopCorners, maxAboveDistance);
        this.corners[1] = Utils.getPointAtDistanceInDirection(leftOfLine, bearingToBottomCorners, maxBelowDistance);
        this.corners[2] = Utils.getPointAtDistanceInDirection(rightOfLine, bearingToBottomCorners, maxBelowDistance);
        this.corners[3] = Utils.getPointAtDistanceInDirection(rightOfLine, bearingToTopCorners, maxAboveDistance);
        this.width = maxAboveDistance + maxBelowDistance;
        this.height = leftOfLine.distance(rightOfLine);
        //find the four corners
        }//if centerMass isnt' null
    }

    public Point[] getCorners()
    {
        return corners;
    }

    public double getWidth()
    {
        return width;
    }

    public double getHeight()
    {
        return height;
    }

    public boolean isInFormation()
    {
        return this.inFormation;
    }

    public void setInFormation(boolean inFormation)
    {
        this.inFormation = inFormation;
    }

    public Point getCenterMass()
    {
        return this.centerMass;
    }

    public String getUnitName()
    {
        return unitName;
    }

    public double getPercentEngaged()
    {
        return percentEngaged;
    }

    public void setMaxNumInUnit(int maxNumInUnit)
    {
        this.maxNumInUnit = maxNumInUnit;
    }

    public void incrementUnitMetrics(Soldier soldier)
    {
        if (soldier != null && soldier.isActive())
        {
            this.classScore += soldier.getClassScore();
            this.activeCounter++;
            Point centerPoint = soldier.getCenterPoint();
            this.sumX += centerPoint.x;
            this.sumY += centerPoint.y;
            this.minX = Math.min(this.minX, centerPoint.x);
            this.maxX = Math.max(this.maxX, centerPoint.x);
            this.minY = Math.min(this.minY, centerPoint.y);
            this.maxY = Math.max(this.maxY, centerPoint.y);
            if (soldier.isMelee)
            {
                this.numMelee++;
            }
            if (soldier.isRanged)
            {
                this.numRanged++;
            }
            if (soldier.isMounted)
            {
                this.numMounted++;
            }

            if (soldier.getState() == SoldierConditions.IN_COMBAT)
            {
                numEngaged++;
            }

            
            this.points.add(soldier.getCenterPoint());
            if (this.distanceToClosestEnemy > soldier.getDistanceToEnemy())
            {
                //System.out.println(soldier.getDistanceToEnemy() + " less than "+ this.distanceToClosestEnemy + ". Setting to former");
                this.distanceToClosestEnemy = soldier.getDistanceToEnemy();
            }
        }
    }

    public int getTickCount()
    {
        return tickCount;
    }

    public int getActiveNumber()
    {
        return activeNumber;
    }

    public void clearMetrics()
    {
        this.classScore = 0;
        this.sumX = 0;
        this.sumY = 0;
        this.maxX = 0;
        this.minX = Integer.MAX_VALUE;
        this.maxY = 0;
        this.minY = Integer.MAX_VALUE;
        this.activeNumber = 0;
        this.activeCounter = 0;
        this.numMelee = 0;
        this.numRanged = 0;
        this.numMounted = 0;
        this.mostlyRanged = false;
        this.mostlyMelee = false;
        this.mostlyMounted = false;
        this.numEngaged = 0;
        this.percentEngaged = 0.0;
        this.points.clear();
        //this.distanceToClosestEnemy = Short.MAX_VALUE;
    }

    public boolean mostlyRanged()
    {
        return this.mostlyRanged;
    }

    public boolean mostlyMelee()
    {
        return this.mostlyMelee;
    }
    

    public boolean mostlyMounted()
    {
        return this.mostlyMounted;
    }

    public int getMaxNumInUnit()
    {
        return maxNumInUnit;
    }

    public int getDistanceToClosestEnemy()
    {
        return distanceToClosestEnemy;
    }

}//unit metrics class
