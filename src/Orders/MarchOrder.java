/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public class MarchOrder extends AbstractOrder{
    double direction;

    public MarchOrder(double direction, int speed, boolean inFormation, AbstractOrderDuration duration) {
        this.direction = direction;
        this.speed = speed;
        this.inFormation = inFormation;
        
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
