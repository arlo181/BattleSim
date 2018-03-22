/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import battlesimulator.BattlefieldObject;
import java.util.ArrayList;

/**
 *Class that represents a node in the push tree.  The pusher pushes the list of pushed and is in turn pushed by an object.
 * @author Arlo
 */
public class Push {
    BattlefieldObject pusher;
    BattlefieldObject pushedBy;
    ArrayList<Push> pushes  = new ArrayList<>();
    double directionBeingPushed;
    
    public Push(BattlefieldObject pusher, BattlefieldObject pushedBy, double directionOfPush)
    {
        this.pusher = pusher;
        this.pushedBy = pushedBy;
        this.directionBeingPushed = directionOfPush;
    }
    
    public void addPush(Push push)
    {
        this.pushes.add(push);
    }

    public BattlefieldObject getPusher() {
        return pusher;
    }

    public BattlefieldObject getPushedBy() {
        return pushedBy;
    }

    public ArrayList<Push> getPushes() {
        return pushes;
    }

    public double getDirectionBeingPushed() {
        return directionBeingPushed;
    }
    
    
    
}
