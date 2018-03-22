/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

/**
 *
 * @author Arlo
 */
public abstract class AbstractOrder implements java.io.Serializable{
    double direction;
    boolean inFormation;
AbstractOrderDuration duration;
    int speed;

    

    public boolean isInFormation() {
        return inFormation;
    }

    public abstract AbstractOrderDuration getDuration();
    
    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    
    
}
