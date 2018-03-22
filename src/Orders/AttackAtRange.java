/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Orders;

import java.awt.Point;

/**
 *
 * @author Arlo
 */
public class AttackAtRange extends AbstractOrder{
    double direction;
    Point target;
    int distance;
    boolean specificTarget;
    

    public AttackAtRange(double direction, int distance, Point target, boolean specificTarget, AbstractOrderDuration duration) {
        this.direction = direction;
        this.inFormation = false;
        this.specificTarget = specificTarget;
        this.distance = distance;
        this.duration = duration;
        this.target = target;
    }

    public double getDirection() {
        return direction;
    }
        
        @Override
    public AbstractOrderDuration getDuration() {
        return this.duration;
    }

    public Point getTarget() {
        return target;
    }

    public int getDistance() {
        return distance;
    }

    public boolean isSpecificTarget() {
        return specificTarget;
    }
        
        
}
