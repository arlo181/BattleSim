/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import UnitFormations.BaseUnitFormation;
import UnitFormations.WedgeFormation;
import Orders.AbstractOrder;
import Orders.AssumeFormation;
import Orders.ChargeOrder;
import Orders.Countdown;
import Orders.EngageEnemiesOrder;
import Orders.Forever;
import Orders.MarchOrder;
import Orders.UntilComplete;
import Orders.UntilEngaged;
import battlesimulator.Utils;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.UnitMetrics;

/**
 *
 * @author Arlo
 */
public class WedgeChargeObjective extends AbstractObjective{
    UnitMetrics unitToEngage;
    public WedgeChargeObjective(UnitMetrics unitToEngage, AbstractStrategy.StrategyStage stage)
    {
        super(stage);
        this.unitToEngage = unitToEngage;
    }

    public UnitMetrics getUnitToEngage() {
        return unitToEngage;
    }

    @Override
    public AbstractOrder getNewestOrders(UnitMetrics thisUnitMetrics) {
        AbstractOrder order = null;
             
            int bearing = (int)Utils.findBearing(thisUnitMetrics.getCenterMass(), unitToEngage.getCenterMass());
        switch(this.stage)
        {
            case FORMING_UP:
           
                 int spacing = thisUnitMetrics.mostlyMounted() ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
                BaseUnitFormation formation = new WedgeFormation(thisUnitMetrics.getUnitName() + "formation", thisUnitMetrics.getCenterMass(), bearing, thisUnitMetrics.getActiveNumber(), spacing);
                order = new AssumeFormation(formation, new UntilComplete(250));
                break;
           
            case ADVANCE:
                   order = new MarchOrder(bearing, 10, true, new Countdown(150));
                break;
            case FINAL_APPROACH:
                order = new ChargeOrder(bearing, new UntilEngaged(100, 0.01));
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
