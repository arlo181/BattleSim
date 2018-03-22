/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Unit;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 * @author Arlo
 */
public class MassMurder extends Action{
    Collection<BattlefieldObject> victims;
    public MassMurder(int delayTicks, BattlefieldObject bObject, Collection<BattlefieldObject> victims)
    {
        super(delayTicks, bObject);
        this.victims = victims;
    }

    public Collection<BattlefieldObject> getVictims() {
        return victims;
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
        for (BattlefieldObject victim : victims)
        {
            victim.markKilled();
        }
    }


   
    
    
}
