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
public class EngageCombatAction extends Action {
    BattlefieldObject target;
    public EngageCombatAction(BattlefieldObject attacker, BattlefieldObject target )
    {
        super(0, attacker);
        this.target = target;
    }

    public BattlefieldObject getTarget() {
        return target;
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
         this.getTarget().addAttacker(this.battlefieldObject);
    }
    
    
    
}
