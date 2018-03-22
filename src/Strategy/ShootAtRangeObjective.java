/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import UnitFormations.BaseUnitFormation;
import UnitFormations.GridFormation;
import Orders.AbstractOrder;
import Orders.AssumeFormation;
import Orders.AttackAtRange;
import Orders.EngageEnemiesOrder;
import Orders.Forever;
import Orders.MarchOrder;
import Orders.UntilComplete;
import battlesimulator.Utils;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.UnitMetrics;
import java.awt.Point;

/**
 *
 * @author Arlo
 */
public class ShootAtRangeObjective extends AbstractObjective{
    UnitMetrics unitToEngage;
    UnitMetrics shooter;
    public ShootAtRangeObjective(UnitMetrics unitToEngage, UnitMetrics shooter, AbstractStrategy.StrategyStage stage)
    {
        super(stage);
        this.unitToEngage = unitToEngage;
        this.shooter = shooter;
    }

    public UnitMetrics getUnitToEngage() {
        return unitToEngage;
    }
    
    public BaseUnitFormation getFiringFormation(UnitMetrics firingUnit)
    {
        double bearingFromProtecteeToEnemy = Utils.findBearing(firingUnit.getCenterMass(), this.unitToEngage.getCenterMass());
        int formationOrientation = (int)(bearingFromProtecteeToEnemy) ;
                        int spacing = firingUnit.mostlyMounted() ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
                        int numInRow = (int)(Math.sqrt(firingUnit.getActiveNumber()) * 2);
                        Point formationOrigin = firingUnit.getCenterMass();
                       
        GridFormation formation = new GridFormation("protective grid", formationOrigin, formationOrientation, firingUnit.getActiveNumber(), spacing, numInRow);
 
        //todo: iterate until... they don't overlap?  I should add a bounding rectangle method to the formation class.
        
        return formation;
    }

    @Override
    public AbstractOrder getNewestOrders(UnitMetrics thisUnitMetrics) {
                AbstractOrder order = null;
            int distanceAway = (int)this.shooter.getCenterMass().distance(this.unitToEngage.getCenterMass());
            int bearing = (int)Utils.findBearing(thisUnitMetrics.getCenterMass(), this.unitToEngage.getCenterMass());
        switch(this.stage)
        {
            case FORMING_UP:

                BaseUnitFormation formation = this.getFiringFormation(this.shooter);
                order = new AssumeFormation(formation, new UntilComplete(150));
                break;
           
            case ADVANCE:
                  
                order =  (new MarchOrder(bearing, 10, true, new Forever()));
                break;
            case FINAL_APPROACH:
                  order =(new AttackAtRange(bearing, distanceAway, this.unitToEngage.getCenterMass(), false, new Forever()));
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
