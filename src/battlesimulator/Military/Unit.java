/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import UnitFormations.BaseUnitFormation;
import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class Unit implements java.io.Serializable
{

    Officer officer;
    ArrayList<Soldier> soldiers = new ArrayList<>();
    ArrayList<UnitMetrics> unitStatistics = new ArrayList<>();
    Color color;
    String allegiance;
    BaseUnitFormation defaultFormation;
    String role; //string describing the unit's role in the army -- this can(will) be used for use in strategy
    //to link a unit with their position in a formation...
    int positionInFormation;
    String name;
    UnitMetrics metricsInProgress;

    int tickCount;
    UnitStateAlbum stateAlbum;

    public Unit(Officer officer, BaseUnitFormation formation, Color color, String allegiance, String name)
    {
        this.officer = officer;
        this.defaultFormation = formation;
        this.color = color;
        this.allegiance = allegiance;
        this.officer.setUnit(this);
        this.officer.setUnitFormation(formation);
        this.officer.setColor(this.color);
        this.name = name;
        this.tickCount = 0;
        this.metricsInProgress = new UnitMetrics(this.tickCount, allegiance, color, name, this.soldiers.size());
        this.stateAlbum = new UnitStateAlbum();
    }

    public Officer getOfficer()
    {
        return officer;
    }

    public Point getCenterMass()
    {
        return this.stateAlbum.getLatestMetrics().getCenterMass();
    }

    public UnitStateAlbum getStateAlbum()
    {
        return this.stateAlbum;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public int getPositionInFormation()
    {
        return positionInFormation;
    }

    public void setPositionInFormation(int positionInFormation)
    {
        this.positionInFormation = positionInFormation;
    }

    public void clearSoldiers()
    {
        this.soldiers.clear();
        this.metricsInProgress.clearMetrics();
    }

    public ArrayList<Soldier> getSoldiers()
    {
        return soldiers;
    }

    public void setDefaultFormation(BaseUnitFormation defaultFormation)
    {
        this.defaultFormation = defaultFormation;
        for (Soldier soldier : this.soldiers)
        {
            soldier.setFormation(this.defaultFormation);
        }
    }

    public int getNumActive()
    {
        int active = this.officer.isActive() ? 1 : 0;
        UnitMetrics metric = this.getStateAlbum().getLatestMetrics();
        if(metric != null)
        {
            active = metric.getActiveNumber();
        }
        
        return active;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Color getColor()
    {
        return color;
    }

    public BaseUnitFormation getDefaultFormation()
    {
        return defaultFormation;
    }

    public void setColor(Color color)
    {
        this.color = color;
        for(Soldier soldier : this.getSoldiers())
        {
            soldier.setColor(color);
        }
        
        if(this.officer != null)
        {
            this.officer.setColor(color);
        }
    }

    public String getAllegiance()
    {
        return allegiance;
    }

    public void setAllegiance(String allegiance)
    {
        this.allegiance = allegiance;
    }

    public void clearMetrics()
    {
        this.metricsInProgress.clearMetrics();
    }

    public void clearStateAlbum()
    {
        this.stateAlbum.clearAll();
    }

    public void finalizeUnitMetrics(int tickCount)
    {
        this.metricsInProgress.finalizeUnitMetrics(tickCount);
        this.stateAlbum.addMetrics(this.metricsInProgress.clone());
    }

    public synchronized void incrementUnitMetrics(Soldier soldier)
    {
        this.metricsInProgress.incrementUnitMetrics(soldier);
    }

    public UnitMetrics generateUnitMetrics(int tickCount)
    {
        this.metricsInProgress.clearMetrics();

        for (Soldier soldier : this.getSoldiers())
        {
            this.metricsInProgress.incrementUnitMetrics(soldier);
        }
        this.incrementUnitMetrics(this.officer);
        this.metricsInProgress.finalizeUnitMetrics(tickCount);
        this.stateAlbum.addMetrics(this.metricsInProgress);
        return this.stateAlbum.getLatestMetrics();

    }

    public String getName()
    {
        return name;
    }

    public void addSoldier(Soldier soldier)
    {
        this.soldiers.add(soldier); //TODO check for null
        soldier.setColor(this.color);
        soldier.setFormation(this.defaultFormation);
        soldier.setUnitName(this.name);
        this.metricsInProgress.setMaxNumInUnit(this.soldiers.size());
        this.metricsInProgress.incrementUnitMetrics(soldier);
    }

    public void setOfficer(Officer officer)
    {
        this.officer = officer;
        this.officer.setUnit(this);
        officer.setColor(this.color);
        this.officer.setUnitName(this.name);
    }

    public void setAllegiance(Color color, String allegiance)
    {
        this.color = color;
        this.allegiance = allegiance;
        for (Soldier soldier : this.soldiers)
        {
            soldier.setColor(this.color);
            soldier.setAllegiance(this.allegiance);
        }

    }

    public void setInFormation(boolean inFormation)
    {
        this.metricsInProgress.setInFormation(this.officer.inFormation);
        this.stateAlbum.getLatestMetrics().setInFormation(this.officer.inFormation);
    }

    public class UnitStateAlbum implements Serializable
    {

        ArrayList<UnitMetrics> finalizedMetrics;

        public UnitStateAlbum()
        {
            this.finalizedMetrics = new ArrayList<>();
        }

        public UnitMetrics getMetrics(int sceneNumber)
        {
            return finalizedMetrics.get(sceneNumber);
        }

        public UnitMetrics getLatestMetrics()
        {
            UnitMetrics metric = null;
            if (!this.finalizedMetrics.isEmpty())
            {
                metric = finalizedMetrics.get(this.finalizedMetrics.size() - 1);
            }
            return metric;
        }

        public void addMetrics(UnitMetrics metrics)
        {
            this.finalizedMetrics.add(metrics);
        }

        public void clearAll()
        {
            this.finalizedMetrics.clear();
        }
    }
}
