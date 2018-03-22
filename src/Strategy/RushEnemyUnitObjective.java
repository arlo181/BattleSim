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
public class RushEnemyUnitObjective extends AbstractObjective{

    public RushEnemyUnitObjective(AbstractStrategy.StrategyStage stage)
    {
        super(stage);
    }

    @Override
    public AbstractOrder getNewestOrders(UnitMetrics thisUnitMetrics) {
        AbstractOrder order = null;

           order = new EngageEnemiesOrder(new Forever());

        
        return order;
    }
    
    
}
