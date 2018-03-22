/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public class UntilEngaged extends Countdown{
    double percentage;
    public UntilEngaged(int timeoutTicks, double percentage)
    {
        super(timeoutTicks);
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }
    
    
}
