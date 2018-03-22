/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public class Countdown extends AbstractOrderDuration{
    int ticksRemaining;
    
    public Countdown(int ticks)
    {
        this.ticksRemaining = ticks;
    }
    
    public void AddTick()
    {
        this.ticksRemaining --;
    }
    
    public boolean countdownComplete()
    {
        return this.ticksRemaining <=0;
    }
}
