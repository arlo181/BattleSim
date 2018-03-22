/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import UnitFormations.BaseUnitFormation;
import UnitFormations.GridFormation;
import Orders.AbstractOrder;
import Orders.AssumeFormation;
import Orders.EngageEnemiesOrder;
import Orders.Forever;
import Orders.HoldGround;
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
public class ProtectUnitObjective extends AbstractObjective {

    UnitMetrics unitToProtect;
    UnitMetrics enemyUnit;
    UnitMetrics protector;

    public ProtectUnitObjective(UnitMetrics unitToProtect, UnitMetrics enemyUnit, UnitMetrics protector, AbstractStrategy.StrategyStage stage) {
        super(stage);
        this.unitToProtect = unitToProtect;
        this.enemyUnit = enemyUnit;
        this.protector = protector;
    }

    public UnitMetrics getUnitToProtect() {
        return unitToProtect;
    }

    public UnitMetrics getEnemyUnit() {
        return enemyUnit;
    }

    public BaseUnitFormation getProtectiveFormation(UnitMetrics protectingUnit) {
        double bearingFromProtecteeToEnemy = Utils.findBearing(this.unitToProtect.getCenterMass(), this.enemyUnit.getCenterMass());
        int formationOrientation = (int) (bearingFromProtecteeToEnemy);
        int spacing = protectingUnit.mostlyMounted() ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
        int numInRow = (int) (Math.sqrt(protectingUnit.getActiveNumber()) * 4);
        Point formationOrigin = this.unitToProtect.getCenterMass();

        int distance = Math.min((int)this.unitToProtect.getWidth(), (int)this.unitToProtect.getHeight());
        formationOrigin = Utils.getPointAtDistanceInDirection(formationOrigin, bearingFromProtecteeToEnemy, distance); //Todo:  What distance?

        //set up a grid formation.
        GridFormation formation = new GridFormation("protective grid", formationOrigin, formationOrientation, protectingUnit.getActiveNumber(), spacing, numInRow);

        //todo: iterate until... they don't overlap?  I should add a bounding rectangle method to the formation class.
        return formation;
    }

    @Override
    public AbstractOrder getNewestOrders(UnitMetrics thisUnitMetrics) {
        AbstractOrder order = null;

        switch (this.stage) {
            case FORMING_UP:

                BaseUnitFormation formation = this.getProtectiveFormation(this.protector);
                order = new AssumeFormation(formation, new UntilComplete(120));
                break;

            case ADVANCE:
                formation = this.getProtectiveFormation(this.protector);
                order = new AssumeFormation(formation, new UntilEngaged(120, 0.01));
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
