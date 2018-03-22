/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import Orders.AbstractOrder;
import battlesimulator.Actions.Action;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public interface Leader {
    void issueOrders(); //todo refine definition
    ArrayList<Soldier> getSubordinates();
    boolean keepWaiting(int ticksElapsed);
}
