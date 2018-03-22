/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

import UnitFormations.BaseUnitFormation;

/**
 *
 * @author Arlo
 */
public class AssumeFormation extends AbstractOrder{
    BaseUnitFormation formation;

    public AssumeFormation(BaseUnitFormation formation, AbstractOrderDuration orderDuration) {
        this.formation = formation;
        this.duration = orderDuration;
    }

    public BaseUnitFormation getFormation() {
        return formation;
    }

    @Override
    public AbstractOrderDuration getDuration() {
        return this.duration;
    }
    
}
