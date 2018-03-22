/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import Orders.AbstractOrder;
import battlesimulator.Military.UnitMetrics;

/**
 *
 * @author Arlo
 */
public abstract class AbstractObjective {
    AbstractStrategy.StrategyStage stage;
    public AbstractObjective(AbstractStrategy.StrategyStage stage)
    {
        this.stage = stage;
    }
    
    public abstract AbstractOrder getNewestOrders(UnitMetrics thisUnitMetrics);
}
