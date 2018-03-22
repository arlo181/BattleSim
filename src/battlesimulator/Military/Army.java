/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.Military.UnitMetrics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class Army implements java.io.Serializable
{

    General general;
    ArrayList<Unit> units = new ArrayList<>();
    Color color;
    String allegiance;
    String startingPositonName;
    Point generalPosition;
    ArmyStats latestStats;

    public Army(Color color, String allegiance)
    {
        this.color = color;
        this.allegiance = allegiance;
        this.generalPosition = new Point(0, 0);

    }

    public Color getColor()
    {
        return color;
    }

    public String getAllegiance()
    {
        return allegiance;
    }

    public General getGeneral()
    {
        return general;
    }

    public ArrayList<Unit> getUnits()
    {
        return units;
    }

    public String getStartingPositionName()
    {
        return this.startingPositonName;
    }

    public void setStartingPosition(String spName, Point point)
    {
        this.startingPositonName = spName;
        this.generalPosition = point;
        if (this.general != null)
        {
            this.general.setCenterPoint(this.generalPosition);
        }
        int heightDisplacement = 0;

        //TODO:  smarter way of arranging units pre-strategy selection
        for (Unit unit : this.units)
        {
            unit.clearMetrics();
            unit.clearStateAlbum();
            Point pointForNextUnit = new Point(point.x, point.y + heightDisplacement);
            //TODO: maybe formations can have a height/widht.  then units can be arranged to not overlap
            unit.getDefaultFormation().setFormationTopLeft(pointForNextUnit);

            unit.getDefaultFormation().clearPositions();
            unit.getOfficer().setFormation(unit.getDefaultFormation());
            for (Soldier soldier : unit.getSoldiers())
            {
                soldier.setFormation(unit.getDefaultFormation());
                unit.incrementUnitMetrics(soldier);
            }
            heightDisplacement += unit.getDefaultFormation().getHeight();
        }
    }

    public Rectangle getBoundingRectangle()
    {
        Rectangle armyRect = null;
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = 0;
        int maxY = 0;
        for (Unit unit : this.getUnits())
        {
            Rectangle boundingRect = unit.getStateAlbum().getLatestMetrics().getBoundingRectangle();
            minX = Math.min(boundingRect.x, minX);
            minY = Math.min(boundingRect.y, minY);
            maxX = Math.max(boundingRect.x + boundingRect.width, maxX);
            maxY = Math.max(boundingRect.y + boundingRect.height, maxY);

        }

        int width = maxX - minX;
        int height = maxY - minY;
        armyRect = new Rectangle(new Point(minX, minY), new Dimension(width, height));
        return armyRect;
    }

    public Point getGeneralPoint()
    {
        return this.generalPosition;
    }

    public void setGeneral(General general)
    {
        this.general = general;
        this.general.setArmy(this);
        this.general.setColor(this.color);
        this.general.setCenterPoint(this.generalPosition);
    }
    
    public void setColor(Color color)
    {
        this.color = color;
        for (Unit unit : this.getUnits())
        {
            unit.setColor(color);
        }
        
        this.general.setColor(color);
    }

    public void addUnit(Unit unit)
    {
        this.units.add(unit);//todo check for null
        unit.setAllegiance(this.color, this.allegiance);
    }

    public ArrayList<Soldier> getAllSoldiers()
    {
        ArrayList<Soldier> allSoldiers = new ArrayList<>();
        if (general != null)
        {
            allSoldiers.add(general);
        }
        for (Unit unit : this.units)
        {
            if (unit.getOfficer() != null)
            {
                allSoldiers.add(unit.getOfficer());
            }
            allSoldiers.addAll(unit.getSoldiers());
        }

        return allSoldiers;
    }
    
    public ArmyStats getLatestStats()
    {
        return this.latestStats;
    }

    public ArmyStats generateArmyStats(int tickCount)
    {

        ArmyStats stats = new ArmyStats(tickCount, this.general, this.units, this.allegiance, this.color);

        this.latestStats = stats;
        return stats;
    }

    public class ArmyStats
    {

        int tickCount;
        String allegiance;
        Color color;
        int totalActiveTroops;
        int numUnits;
        int armyScore;
        //TODO:  map of troop class types with counts

        public ArmyStats(int tickCount, General general, ArrayList<Unit> units, String allegiance, Color color)
        {
            this.tickCount = tickCount;
            this.allegiance = allegiance;
            this.color = color;
            this.totalActiveTroops = 0;
            this.numUnits = 0;
            this.armyScore = 0;
            for (Unit unit : units)
            {
                UnitMetrics metric = unit.getStateAlbum().getLatestMetrics();

                this.totalActiveTroops += unit.getNumActive();
                if (metric != null)
                {
                    this.armyScore += metric.getClassScore();
                }
                numUnits++;
            }

        }

        public int getNumUnits()
        {
            return numUnits;
        }

        public int getTickCount()
        {
            return tickCount;
        }

        public int getArmyScore()
        {
            return armyScore;
        }

        public General getGeneral()
        {
            return general;
        }

        public String getAllegiance()
        {
            return allegiance;
        }

        public Color getColor()
        {
            return color;
        }

        public int getTotalActiveTroops()
        {
            return totalActiveTroops;
        }
    }
}
