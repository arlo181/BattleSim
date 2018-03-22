/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

/**
 *
 * @author Arlo
 */
public enum SoldierConditions {
    MARCHING(true),
    READY_FOR_ORDERS(true),
    IN_COMBAT(true),
    RETREATED(false),
    DEAD(false),
    WOUNDED(false),
    CAPTURED(false);
    
    boolean active;
    SoldierConditions(boolean active)
    {
        this.active=active;
    }
    public static boolean isActiveState(SoldierConditions state)
    {
                return state.active;
    }
}
