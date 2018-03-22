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
public class FallOnYourSwordAction extends Action{
    public FallOnYourSwordAction(int delayTicks, BattlefieldObject battlefieldObject)
    {
        super(delayTicks, battlefieldObject);
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
         this.battlefieldObject.markKilled();
    }
    
}
