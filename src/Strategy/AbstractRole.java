/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import battlesimulator.Military.Unit;
import battlesimulator.Military.UnitMetrics;

/**
 *
 * @author Arlo
 */
public abstract class AbstractRole {
    String name;
    boolean assigned;
    AbstractRole(String name)
    {
        this.name = name;
        this.assigned = false;
    }

    public abstract boolean canFulfillRole(Unit.UnitStateAlbum metrics);
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
    
    
}
