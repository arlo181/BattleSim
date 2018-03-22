/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import UnitFormations.BaseUnitFormation;
import battlesimulator.Military.General;
import battlesimulator.Military.Unit;
import battlesimulator.Military.UnitMetrics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arlo
 */
public class ShootAndWaitStrategy extends AbstractStrategy{

    ArrayList<Unit.UnitStateAlbum> rangedUnits;
    ArrayList<Unit.UnitStateAlbum> meleeUnits;
    int timeSpentWaitingForApproach;
    
    public ShootAndWaitStrategy()
    {
        super();
        rangedUnits = new ArrayList<>();
        meleeUnits = new ArrayList<>();
        timeSpentWaitingForApproach = 0;
        
    }
    
    @Override
    public boolean isValidStrategy(General general, ArrayList<Unit.UnitStateAlbum> friendlyUnits, ArrayList<Unit.UnitStateAlbum> enemyUnits) {
        boolean atLeastOneRangedUnit = false;
        for(Unit.UnitStateAlbum unit : friendlyUnits)
        {
            if (unit.getLatestMetrics().mostlyRanged())
            {
                atLeastOneRangedUnit = true;
                this.rangedUnits.add(unit);
            }
        }
        
        this.friendlyUnits = friendlyUnits;
         this.enemyUnits = enemyUnits;
       return atLeastOneRangedUnit && friendlyUnits.size() > 1;
    }

    @Override
    public Map<String, AbstractObjective> determineObjectives() {
        Map<String, AbstractObjective> objectives = new HashMap<>();
        this.rangedUnits.clear();
        this.meleeUnits.clear();
        for(Unit.UnitStateAlbum alliedUnit : this.friendlyUnits)
        {
           if (alliedUnit.getLatestMetrics().mostlyRanged())
            {
                
                this.rangedUnits.add(alliedUnit);
            }
           else
           {
               this.meleeUnits.add(alliedUnit);
           }
           
        }
        if(!rangedUnits.isEmpty())
        {
        //assign a melee unit to protect a ranged unit while melee units last.
        //TODO:  do this more intelligently -- fitting melee units to nearest ranged unit, just make sure no ranged unit is left defenseless.
        int numActiveMelee = 0;
            for (int i = 0; i < this.meleeUnits.size(); i++)
        {
           
            Unit.UnitStateAlbum protector = this.meleeUnits.get(i);
            numActiveMelee += protector.getLatestMetrics().getActiveNumber();
            UnitMetrics enemyUnit = this.findNearestEnemy(protector);
             AbstractStrategy.StrategyStage objectiveStage = this.determineObjectiveStage(enemyUnit, protector.getLatestMetrics());
            int indexOfUnitToProtect = i % (this.rangedUnits.size());

            ProtectUnitObjective protectObjective = new ProtectUnitObjective(this.rangedUnits.get(indexOfUnitToProtect).getLatestMetrics(), enemyUnit, protector.getLatestMetrics(), objectiveStage);
        
            
            objectives.put(protector.getLatestMetrics().getUnitName(), protectObjective);
        
        }
        
        for(Unit.UnitStateAlbum ranged : this.rangedUnits)
        {
             UnitMetrics enemyUnit = this.findNearestEnemy(ranged);
AbstractStrategy.StrategyStage objectiveStage = (numActiveMelee > 0) ? this.determineRangedObjectiveStage(enemyUnit, ranged.getLatestMetrics()) : StrategyStage.COMBAT;
            ShootAtRangeObjective shootObjective = new ShootAtRangeObjective(enemyUnit, ranged.getLatestMetrics(), objectiveStage);
            objectives.put(ranged.getLatestMetrics().getUnitName(), shootObjective);
        }
        }
        
        
        
        
        return objectives;
    }
 
    
    public AbstractStrategy.StrategyStage determineObjectiveStage(UnitMetrics enemy, UnitMetrics ally)
{
    AbstractStrategy.StrategyStage objectiveStage = StrategyStage.FORMING_UP;
     int distanceAway = (int)ally.getCenterMass().distance(enemy.getCenterMass());
    
     if(ally.getPercentEngaged() > 0.01 || this.stage == StrategyStage.COMBAT)
     {
         objectiveStage = StrategyStage.COMBAT;
         this.stage = StrategyStage.COMBAT;
     }
     else if (distanceAway < ally.getWidth()) //TODO: magic number. this should be in reference to front line
     {
         objectiveStage = StrategyStage.FINAL_APPROACH;
     }
     else if (distanceAway >= 380 && !ally.isInFormation())
     {
         objectiveStage = StrategyStage.FORMING_UP;
     }
     else if (distanceAway >= 380)
     {
         objectiveStage = StrategyStage.ADVANCE;
     }
     
    
    return objectiveStage;
}
    
        public AbstractStrategy.StrategyStage determineRangedObjectiveStage(UnitMetrics enemy, UnitMetrics ally)
{
    AbstractStrategy.StrategyStage objectiveStage = StrategyStage.FORMING_UP;
     int distanceAway = (int)ally.getCenterMass().distance(enemy.getCenterMass());
    
     if(distanceAway < 550 || this.stage == StrategyStage.COMBAT)
     {
         objectiveStage = StrategyStage.COMBAT;
     }
 else if(distanceAway < 700 && this.stage != StrategyStage.FINAL_APPROACH)
     {
         objectiveStage = StrategyStage.FINAL_APPROACH;
         if(this.timeSpentWaitingForApproach > 100)
         {
         this.stage = StrategyStage.FINAL_APPROACH;
         }
         else
         {
             this.timeSpentWaitingForApproach ++;
         }
     }
     else if (distanceAway >= 600 && !ally.isInFormation())
     {
         objectiveStage = StrategyStage.FORMING_UP;
     }
     else if (distanceAway >= 600)
     {
         objectiveStage = StrategyStage.ADVANCE;
     }  

    
    return objectiveStage;
}

    @Override
    public Map<String, BaseUnitFormation> setInitialFormation()
    {
                Map<String, BaseUnitFormation> formations = new HashMap<>();
         return formations;    }
}
