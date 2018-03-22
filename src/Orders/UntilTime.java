/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public class UntilTime extends AbstractOrderDuration {
    int timeTarget;
    
    public UntilTime(int timeTarget)
    {
        this.timeTarget = timeTarget;
    }

    public int getTimeTarget() {
        return timeTarget;
    }
    
    public boolean isItTIme(int time)
    {
        return this.timeTarget <= time;
    }
}
