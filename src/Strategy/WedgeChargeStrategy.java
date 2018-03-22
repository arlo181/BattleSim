/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import UnitFormations.BaseUnitFormation;
import UnitFormations.WedgeFormation;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.General;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.Unit;
import battlesimulator.Military.UnitMetrics;
import battlesimulator.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arlo
 */
public class WedgeChargeStrategy extends AbstractStrategy
{

    @Override
    public boolean isValidStrategy(General general, ArrayList<Unit.UnitStateAlbum> friendlyUnits, ArrayList<Unit.UnitStateAlbum> enemyUnits)
    {
        this.friendlyUnits = friendlyUnits;
        this.enemyUnits = enemyUnits;
        return true;
    }

    @Override
    public Map<String, AbstractObjective> determineObjectives()
    {
        Map<String, AbstractObjective> objectives = new HashMap<>();

        for (Unit.UnitStateAlbum alliedUnits : this.friendlyUnits)
        {

            UnitMetrics nearestEnemyUnit = this.findNearestEnemy(alliedUnits);
            WedgeChargeObjective engageUnitObjective = new WedgeChargeObjective(nearestEnemyUnit, this.determineObjectiveStage(nearestEnemyUnit, alliedUnits.getLatestMetrics()));
            objectives.put(alliedUnits.getLatestMetrics().getUnitName(), engageUnitObjective);
        }

        return objectives;
    }

    public AbstractStrategy.StrategyStage determineObjectiveStage(UnitMetrics enemy, UnitMetrics ally)
    {
        AbstractStrategy.StrategyStage objectiveStage = StrategyStage.FORMING_UP;
        int distanceAway = (int) ally.getCenterMass().distance(enemy.getCenterMass());

        if (ally.getPercentEngaged() > 0.01 || this.stage == StrategyStage.COMBAT)
        {
            objectiveStage = StrategyStage.COMBAT;
            this.stage = StrategyStage.COMBAT;
        } else if (distanceAway < 380) //TODO: magic number. t his should be in reference to front line
        {
            objectiveStage = StrategyStage.FINAL_APPROACH;
        } else if (distanceAway >= 380 && !ally.isInFormation() && this.stage != StrategyStage.ADVANCE)
        {
            objectiveStage = StrategyStage.FORMING_UP;
        } else if (distanceAway >= 380)
        {
            objectiveStage = StrategyStage.ADVANCE;
            this.stage = StrategyStage.ADVANCE;
        }

        return objectiveStage;
    }

    @Override
    public Map<String, BaseUnitFormation> setInitialFormation()
    {
        Map<String, BaseUnitFormation> formations = new HashMap<>();

        for (Unit.UnitStateAlbum alliedUnit : this.friendlyUnits)
        {
            UnitMetrics alliedUnitMetrics = alliedUnit.getLatestMetrics();
            UnitMetrics nearestEnemyUnit = this.findNearestEnemy(alliedUnit);
            int bearing = (int) Utils.findBearing(alliedUnitMetrics.getCenterMass(), nearestEnemyUnit.getCenterMass());
            int spacing = alliedUnitMetrics.mostlyMounted() ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
            BaseUnitFormation formation = new WedgeFormation(alliedUnitMetrics.getUnitName() + "formation", alliedUnitMetrics.getCenterMass(), bearing, alliedUnitMetrics.getActiveNumber(), spacing);
            formations.put(alliedUnit.getLatestMetrics().getUnitName(), formation);
        }
        return formations;
    }

}
