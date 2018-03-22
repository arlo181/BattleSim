//NOTE:  Build formation first... based on type of unit to fill position.  Most important positions first.
//then assign positions as you loop through units.
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import ArmyFormations.BaseArmyFormation;
import UnitFormations.BaseUnitFormation;
import UnitFormations.GridFormation;
import UnitFormations.PositionInFormation;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.General;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.Unit;
import battlesimulator.Military.Unit.UnitStateAlbum;
import battlesimulator.Military.UnitMetrics;
import battlesimulator.Utils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Arlo
 */
public class TestArmyFormationStrategy extends AbstractStrategy
{

    BaseArmyFormation formation;

    Map<AbstractRole, Unit.UnitStateAlbum> roleAssignmentMap;
    AbstractRole frontCenter;
    AbstractRole leftWing;
    AbstractRole rightWing;
    AbstractRole archerSupport;

    public TestArmyFormationStrategy()
    {
        this.roleAssignmentMap = new HashMap<>();
        this.frontCenter = new FrontLineRole("FrontCenter");
        this.leftWing = new FrontLineRole("LeftWing");
        this.rightWing = new FrontLineRole("RightWing");
        this.archerSupport = new SupportArtilleryRole("ArcherSupport");

    }

    @Override
    public boolean isValidStrategy(General general, ArrayList<Unit.UnitStateAlbum> friendlyUnits, ArrayList<Unit.UnitStateAlbum> enemyUnits)
    {
        ArrayList<PositionInFormation<UnitMetrics>> positions = new ArrayList<>();
        positions.add(new PositionInFormation<>(frontCenter.getName(), new Point(300, 100), null, 0));

        positions.add(new PositionInFormation<>(leftWing.getName(), new Point(0, 100), null, 1));

        positions.add(new PositionInFormation<>(rightWing.getName(), new Point(600, 100), null, 2));

        positions.add(new PositionInFormation<>(archerSupport.getName(), new Point(100, 400), null, 2));

        //Fill in the positions
        formation = new BaseArmyFormation("Test formation", positions, general.getCenterPoint(), general.getCenterPoint());

        this.friendlyUnits = friendlyUnits;
        this.enemyUnits = enemyUnits;
        boolean everyUnitHasRole = true;
        //find orientation:
        double bearingFromProtecteeToEnemy = Utils.findBearing(this.friendlyUnits.get(0).getLatestMetrics().getCenterMass(), this.enemyUnits.get(0).getLatestMetrics().getCenterMass());
        int formationOrientation = (int) (bearingFromProtecteeToEnemy);
        this.formation.setOrientation(formationOrientation);

        //Fill in the roles
        for (Unit.UnitStateAlbum alliedUnit : friendlyUnits)
        {

            if (this.roleAssignmentMap.get(frontCenter) == null
                    && frontCenter.canFulfillRole(alliedUnit))
            {
                this.roleAssignmentMap.put(frontCenter, alliedUnit);
            } else if (this.roleAssignmentMap.get(leftWing) == null
                    && leftWing.canFulfillRole(alliedUnit))
            {
                this.roleAssignmentMap.put(leftWing, alliedUnit);
            } else if (this.roleAssignmentMap.get(archerSupport) == null
                    && archerSupport.canFulfillRole(alliedUnit))
            {
                this.roleAssignmentMap.put(archerSupport, alliedUnit);
            } else if (this.roleAssignmentMap.get(rightWing) == null
                    && rightWing.canFulfillRole(alliedUnit))
            {
                this.roleAssignmentMap.put(rightWing, alliedUnit);
            } else
            {
                everyUnitHasRole = false;
            }
        }

        return everyUnitHasRole;
    }

    @Override
    public Map<String, AbstractObjective> determineObjectives()
    {
        Map<String, AbstractObjective> objectives = new HashMap<>();

        this.assignObjectiveForRole(frontCenter, objectives);
        this.assignObjectiveForRole(leftWing, objectives);
        this.assignObjectiveForRole(rightWing, objectives);
        this.assignObjectiveForRole(archerSupport, objectives);

        return objectives;
    }

    public void assignObjectiveForRole(AbstractRole role, Map<String, AbstractObjective> objectivesMap)
    {
        Unit.UnitStateAlbum unitAssigned = this.roleAssignmentMap.get(role);
        AbstractStrategy.StrategyStage unitStage = this.determineObjectiveStage();
        if (unitAssigned != null)
        {
            AbstractObjective objective;
            UnitMetrics nearestEnemyUnit = this.findNearestEnemy(unitAssigned);

            //TODO:  This is where I would use the state machine to get the objective for this role
            if (unitStage == StrategyStage.FORMING_UP || unitStage == StrategyStage.ADVANCE)
            {
                objective = new TakePositionObjective(this.formation, role.getName(), this.stage, unitAssigned.getLatestMetrics());
            } else
            {
                objective = new WedgeChargeObjective(nearestEnemyUnit, unitStage);
            }

            objectivesMap.put(unitAssigned.getLatestMetrics().getUnitName(), objective);
        }
    }

    public AbstractStrategy.StrategyStage determineObjectiveStage()
    {
        AbstractStrategy.StrategyStage objectiveStage = StrategyStage.FORMING_UP;
        int minDistanceAway = Integer.MAX_VALUE;
        String unitName = "";
        boolean allInFormation = true;
        for (Unit.UnitStateAlbum allies : this.friendlyUnits)
        {
            int distanceToEnemy = allies.getLatestMetrics().getDistanceToClosestEnemy();
            //System.out.println("DistanceToNEarest enemy: " + distanceToEnemy);
            if (distanceToEnemy < minDistanceAway)
            {
                minDistanceAway = distanceToEnemy;
            }
            allInFormation &= allies.getLatestMetrics().isInFormation();
            unitName = allies.getLatestMetrics().getUnitName();
        }

        if (this.stage == StrategyStage.FORMING_UP && allInFormation)
        {
            objectiveStage = StrategyStage.ADVANCE;
            this.stage = StrategyStage.ADVANCE;
        } else if (minDistanceAway < 300)
        {
            objectiveStage = StrategyStage.COMBAT;
            this.stage = StrategyStage.COMBAT;
        }

        return objectiveStage;
    }

    @Override
    public Map<String, BaseUnitFormation> setInitialFormation()
    {
        Map<String, BaseUnitFormation> formations = new HashMap<>();

        for (AbstractRole role : this.roleAssignmentMap.keySet())
        {
            UnitStateAlbum unitAlbum = this.roleAssignmentMap.get(role);
            if (unitAlbum != null)
            {
                UnitMetrics alliedUnitMetrics = unitAlbum.getLatestMetrics();
                Point formationCenterPoint = this.formation.getOffsetOfPosition(role.getName());
                int spacing = alliedUnitMetrics != null && alliedUnitMetrics.mostlyMounted() ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
                int numInRow = (int) (Math.sqrt(alliedUnitMetrics.getActiveNumber()));
                //set up a grid formation.
                GridFormation unitFormation = new GridFormation("protective grid", formationCenterPoint, this.formation.getOrientation(), alliedUnitMetrics.getActiveNumber(), spacing, numInRow);
                formations.put(alliedUnitMetrics.getUnitName(), unitFormation);
            }

        }
        return formations;
    }

}
