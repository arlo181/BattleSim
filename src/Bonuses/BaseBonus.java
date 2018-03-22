/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Bonuses;

import battlesimulator.BattlefieldObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class BaseBonus implements Serializable{
    public enum Attribute
    {
        SPEED,
        STRENGTH,
        DEFENSE,
        REACH
    }
    
    protected ArrayList<Class> appliesTo;
    
    Attribute attributeAffected;
    int magnitude; //how big the bonus is (can be negative)
    int durationRemaining;  //the number of ticks the bonus lasts for
    String description;
    
    public BaseBonus(String description, Attribute attribute, int magnitude, int duration)
    {
        this.appliesTo = new ArrayList<>();
        this.appliesTo.add(BattlefieldObject.class);
        this.magnitude = magnitude;
        this.attributeAffected = attribute;
        this.durationRemaining = duration;
        this.description = description;
    }
    public boolean bonusApplies(Class classInQuestion)
    {
        boolean itApplies = false;
        
        for(Class applicableClass : this.appliesTo)
        {
            itApplies |= applicableClass.isAssignableFrom(classInQuestion);
        }
        
        return itApplies;
    }

    public Attribute getAttributeAffected() {
        return attributeAffected;
    }

    public int getMagnitude() {
        return magnitude;
    }

    public int getDurationRemaining() {
        return durationRemaining;
    }
    
    public void decrementDuration()
    {
        this.durationRemaining --;
    }
    public boolean stillActive()
    {
        return this.durationRemaining > 0;
    }
}
