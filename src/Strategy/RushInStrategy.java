/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import UnitFormations.BaseUnitFormation;
import battlesimulator.Military.General;
import battlesimulator.Military.Unit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arlo
 */

public class RushInStrategy extends AbstractStrategy{

    @Override
    public boolean isValidStrategy(General general, ArrayList<Unit.UnitStateAlbum> friendlyUnits, ArrayList<Unit.UnitStateAlbum> enemyUnits) {
       this.friendlyUnits = friendlyUnits;
         this.enemyUnits = enemyUnits;
        return true;
    }

    @Override
    public Map<String, AbstractObjective> determineObjectives() {
        Map<String, AbstractObjective> objectives = new HashMap<>();
        
        for(Unit.UnitStateAlbum alliedUnits : this.friendlyUnits)
        {
            RushEnemyUnitObjective engageUnitObjective = new RushEnemyUnitObjective(StrategyStage.COMBAT);
            objectives.put(alliedUnits.getLatestMetrics().getUnitName(), engageUnitObjective);
        }
        
        return objectives;
    }

    @Override
    public Map<String, BaseUnitFormation> setInitialFormation()
    {
         Map<String, BaseUnitFormation> formations = new HashMap<>();
         return formations;
    }


    
}
