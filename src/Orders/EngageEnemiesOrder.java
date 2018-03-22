/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public class EngageEnemiesOrder extends AbstractOrder{

    
    public EngageEnemiesOrder( AbstractOrderDuration duration)
    {

        this.duration = duration;
    }

    
        @Override
    public AbstractOrderDuration getDuration() {
        return this.duration;
    }
}
