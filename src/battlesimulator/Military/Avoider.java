/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.Actions.Action;
import battlesimulator.Actions.DoNothingAction;
import battlesimulator.Actions.EngageCombatAction;
import battlesimulator.Actions.MoveAction;
import battlesimulator.Actions.MoveTowardNearestEnemyAction;
import battlesimulator.Actions.MovementModes;
import battlesimulator.Utils;
import java.awt.Point;


public class Avoider extends Soldier {

    public Avoider(String allegiance, String name) {
        this(allegiance, name, new Point(0,0));
    }

    public Avoider(String allegiance, String name, Point centerPoint) {
        super(allegiance, name, centerPoint);
    }
    
    @Override
    public Action tick(int ticksElapsed) {

        Action action;
         if (this.aggressionLevel > 2) {
            //todo:  if target is null, he can find nearest enemy based on awareness
            if (this.target != null && target.isActive() && Utils.targetInReach(this, target, this.reach)) {
                action = new EngageCombatAction(this, this.target);
                //this.state = SoldierState.IN_COMBAT; //todo when will it ever come out of state?
            } else {
                action = new MoveTowardNearestEnemyAction(this, this.speed, MovementModes.AVOID); // todo move the "find" to awareness circle (within class?);
            }
        } else  if (this.aggressionLevel > 0){
                    action = new MoveAction(this, 15, 180, MovementModes.DELAYED);

        }
         else
        {
            action = new DoNothingAction(1, this);
        }

        directionRandomizer++;
        return action;
    }
    
}
