/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Unit;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public abstract class Action implements java.io.Serializable{
    int delayTicks;
    BattlefieldObject battlefieldObject;
    boolean remove;
    
    public Action(int delayTicks, BattlefieldObject battlefieldObject)
    {
        this.delayTicks = delayTicks;
        this.battlefieldObject = battlefieldObject;
        remove = false;
    }
    
    public void delay()
    {
        this.delayTicks --;
    }

    public int getDelayTicks() {
        return delayTicks;
    }

    public BattlefieldObject getBattlefieldObject() {
        return battlefieldObject;
    }
    
    public void setObject(BattlefieldObject object)
    {
        this.battlefieldObject = object;
    }

    public boolean isRemove()
    {
        return remove;
    }
    
    
    public abstract void processAction(ArrayList<Unit> unitList, Battlefield battlefield);
    
    
    
    @Override
    public String toString()
    {
        return this.getClass().getName();
    }
    
}
