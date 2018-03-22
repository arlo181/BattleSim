/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.Actions.Action;
import battlesimulator.Actions.EngageCombatAction;
import battlesimulator.Actions.MovementModes;
import battlesimulator.Actions.SweepAttackAction;
import battlesimulator.Utils;
import java.awt.Point;
import java.util.Random;

public class Giant extends Soldier {

    public static final int GIANT_PERSONAL_SPACE_RADIUS = 50;
    public static final short GIANT_CLASS_SCORE=500;
    
    
    public Giant(String allegiance, String name) {
        this(allegiance, name, new Point(0, 0));
    }

    public Giant(String allegiance, String name, Point centerPoint) {
        super(allegiance, name, centerPoint);
        Random rand = new Random();
        this.personalSpaceRadius = 50;
        this.sweepAngle = 120;
        this.speed = 2;
        this.strength = 150;
        this.defense = 1000;
        this.endurance = 10; //20-40 *3 endurance (*3 added with the advent of the ability to attack multiple foes)
        this.reach = 15;
        this.pushPower = 200;
        this.preferredMovementMode = MovementModes.PUSH;
        this.isRanged = false;
        this.isMelee = true;
        this.isMounted = true;
    }
    
    public static short getPersonalSpace()
    {
        return GIANT_PERSONAL_SPACE_RADIUS;
    }
    @Override
    public int getClassScore()
    {
        return Giant.GIANT_CLASS_SCORE;
    }
    @Override
    public Action tick(int ticksElapsed) {

//        Action action;
//        if(directionRandomizer < 4)
//        {
//            action = new DoNothingAction(10, this);
//        }
//        else if (this.aggressionLevel > 2) {
//this.target = this.findNearestEnemy();
//
//            //todo:  if target is null, he can find nearest enemy based on awareness
//            if (this.target != null && target.isActive() && Utils.targetInReach(this, target, this.reach)) {
//                action = new EngageCombatAction(this, this.target);
//                //this.state = SoldierState.IN_COMBAT; //todo when will it ever come out of state?
//            } else if (this.target != null && target.isActive() && !Utils.targetInReach(this, this.target, this.reach))
//            {
//                //charge!
//                //todo: make charge action
//                action = new MoveAction(this, this.speed, Utils.findBearing(this.centerPoint, target.getCenterPoint()), MovementModes.PUSH);
//            }
//            
//            else {
//                action = new MoveTowardNearestEnemyAction(this, this.speed, MovementModes.AVOID); // todo move the "find" to awareness circle (within class?);
//            }
//        } else {
//                    action = new MoveAction(this, 15, 180, MovementModes.DELAYED);
//
//        }
//
//        directionRandomizer++;
//        return action;
        Action action = super.tick(ticksElapsed);
        if(action != null && EngageCombatAction.class.isAssignableFrom(action.getClass()))
        {
            EngageCombatAction engageAction = (EngageCombatAction)action;
            double bearing = Utils.findBearing(this.centerPoint, engageAction.getTarget().getCenterPoint());
            int startAngle = (int) Utils.adjustAngleValue(bearing - this.sweepAngle/2);
            int endAngle = (int) Utils.adjustAngleValue(bearing + this.sweepAngle/2);
            action = new SweepAttackAction(this, 3, this.personalSpaceRadius + this.reach, startAngle, endAngle);
        }
        
        return action;
    }

}
