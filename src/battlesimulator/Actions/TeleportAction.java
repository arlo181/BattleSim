/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Unit;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class TeleportAction extends Action{
    Point pointToTravelTo;

    public TeleportAction(BattlefieldObject bObject, Point pointToTravelTo) {
        super(0, bObject);
        this.pointToTravelTo = pointToTravelTo;
    }

    public Point getPointToTravelTo() {
        return pointToTravelTo;
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {

        if (!battlefield.isConflict(this.battlefieldObject, this.pointToTravelTo, this.battlefieldObject.getPersonalSpaceRadius()))
        {
            battlefield.moveObjectInDirection(this.battlefieldObject, this.pointToTravelTo, true);
        }  
    
    }
    
    
    
    
}
