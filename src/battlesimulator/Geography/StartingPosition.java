/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import battlesimulator.Military.Army;
import java.awt.Point;
import java.awt.Shape;

/**
 *
 * @author Arlo
 */
public class StartingPosition extends Region {
    Army assignedArmy;
    
    public StartingPosition(Shape shape)
    {
        super(shape);
        
    }
    
    public Army getAssignedArmy()
    {
        return this.assignedArmy;
    }
    
    public void deAssignArmy()
    {
        this.assignedArmy = null;
    }
    
    public void assignArmy(Army army)
    {
        this.assignedArmy = army;     
    }
    
    public boolean isArmyAssigned()
    {
        return this.assignedArmy!= null;
    }
    
    public Point getCornerPoint()
    {
        return this.shape.getBounds().getLocation();
    }
   
    }
    

