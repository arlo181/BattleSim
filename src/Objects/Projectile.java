/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Flying;
import java.awt.Point;
/**
 *
 * @author Arlo
 */
public abstract class Projectile extends BattlefieldObject implements Flying {

    double accuracy;
    boolean inFlight;
    boolean rolls; //whether it rolls after landing
    boolean isRolling;
    short power; //this relates to how many people it can go through.
    short ticksStationary = 0;
    boolean staysOnField = false;
    boolean hasBeenFired = false;
    protected double directionOfFlight;
        short impactRadius;

    public Projectile(Point centerPoint, double accuracy, boolean rolls, short power, short speed) {
        super(centerPoint);
        this.accuracy = accuracy;
        this.rolls = rolls;
        this.power = power;
        this.speed = speed;
        this.inFlight = false;

    }

    @Override
    public boolean hasAllegiance() {
        return false;
    }

    @Override
    public String getAllegience() {
        return "";
    }

    public boolean hasBeenFired() {
        return hasBeenFired;
    }

    @Override
    public boolean inFlight()
    {
        return this.inFlight;
    }
    
    @Override
    public  void setFlight(boolean inFlight)
    {
        this.inFlight = inFlight;
        this.ticksStationary = 0;
    }
    public void land() {
        this.inFlight = false;
        if (this.rolls) {
            this.isRolling = true;
        } else {
        }
    }
    public int getImpactRadius() {
        return impactRadius;
    }
    @Override
    public boolean isActive() {
        return inFlight || isRolling || staysOnField;
    }

    public double getAccuracy() {
        return accuracy;
    }
    
    @Override
    public int getClassScore()
    {
        return 0;
    }
    
    public abstract void strikeTarget(BattlefieldObject bObject);


    public boolean isRolls() {
        return rolls;
    }

    public boolean isIsRolling() {
        return isRolling;
    }

    public int getPower() {
        return power;
    }

    public double getDirectionOfTravel() {
        return this.directionOfFlight;
    }
    
    
}
