/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public class ChargeOrder extends AbstractOrder{
    double direction;
    
    public ChargeOrder(double direction, AbstractOrderDuration duration)
    {
        this.direction = direction;
        this.duration = duration;
    }

    public double getDirection() {
        return direction;
    }
    
        @Override
    public AbstractOrderDuration getDuration() {
        return this.duration;
    }

}
