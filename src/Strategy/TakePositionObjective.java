/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import ArmyFormations.BaseArmyFormation;
import UnitFormations.BaseUnitFormation;
import UnitFormations.GridFormation;
import Orders.AbstractOrder;
import Orders.AssumeFormation;
import Orders.EngageEnemiesOrder;
import Orders.Forever;
import Orders.HoldGround;
import Orders.MarchOrder;
import Orders.UntilComplete;
import Orders.UntilEngaged;
import battlesimulator.Utils;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.UnitMetrics;
import java.awt.Point;

/**
 *
 * @author Arlo
 */
public class TakePositionObjective extends AbstractObjective {

    BaseArmyFormation armyFormation;
    String roleNameInFormation;
    UnitMetrics unitMetric;

    public TakePositionObjective(BaseArmyFormation formation, String roleNameInFormation, AbstractStrategy.StrategyStage stage, UnitMetrics unitMetric) {
        super(stage);
        this.armyFormation = formation;
        this.roleNameInFormation = roleNameInFormation;
        this.unitMetric = unitMetric;
    }

    public BaseUnitFormation getUnitFormation() {
       Point formationCenterPoint = this.armyFormation.getOffsetOfPosition(this.roleNameInFormation);
        int spacing = this.unitMetric != null && this.unitMetric.mostlyMounted() ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
        int numInRow = (int) (Math.sqrt(this.unitMetric.getActiveNumber()));
        //set up a grid formation.
        GridFormation unitFormation = new GridFormation("protective grid", formationCenterPoint, this.armyFormation.getOrientation(), this.unitMetric.getActiveNumber(), spacing, numInRow);

        //todo: iterate until... they don't overlap?  I should add a bounding rectangle method to the formation class.
        return unitFormation;
    }

    @Override
    public AbstractOrder getNewestOrders(UnitMetrics thisUnitMetrics) {
        AbstractOrder order = null;

        switch (this.stage) {
            case FORMING_UP:

                BaseUnitFormation formation = this.getUnitFormation();
                order = new AssumeFormation(formation, new UntilComplete(120));
                break;

            case ADVANCE:
                formation = this.getUnitFormation();
                order = new MarchOrder(this.armyFormation.getOrientation(), 5, true, new UntilEngaged(10, 0.01));
                break;
            case FINAL_APPROACH:
                //order = (new HoldGround(new UntilEngaged(100, 0.05)));
                order = new EngageEnemiesOrder(new Forever());
                break;
            case COMBAT:

                order = new EngageEnemiesOrder(new Forever());
                break;
            case RESERVES:
            case MOP_UP:
            default:
                order = new EngageEnemiesOrder(new Forever());
                break;
        }

        return order;
    }
}
