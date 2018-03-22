/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import Objects.ArchProjectile;
import Orders.AttackAtRange;
import battlesimulator.Actions.Action;
import battlesimulator.Actions.DoNothingAction;
import battlesimulator.Actions.MoveAction;
import battlesimulator.Actions.MoveTowardNearestEnemyAction;
import battlesimulator.Actions.RangedAttack;
import battlesimulator.BattlefieldObject;
import battlesimulator.Utils;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author Arlo
 */
public class Archer extends Soldier
{

    public static final short ARCHER_CLASS_SCORE = 3;
    int rateOfFire = 3; //how many rounds it takes to fire an arrow.
    int minRange = 5;
    int maxRange;
    int deadlyRange;
    double accuracy = 0.95;
    ArchProjectile quiver[];
    int arrowIndex = 0;
    int QUIVER_SIZE = 5;
    short IMPACT_RADIUS = 1;
    short ARROW_POWER = 25;
    short ARROW_SPEED = 15;

    public Archer(String allegiance, String name)
    {
        this(allegiance, name, new Point(0, 0));

    }

    public Archer(String allegiance, String name, Point centerPoint)
    {
        super(allegiance, name, centerPoint);
         Random rand = new Random();
        this.maxRange = rand.nextInt(25) + 225; //225-250 range
        this.deadlyRange = rand.nextInt(5) + 25; //225-250 range
        this.strength = 0;
        this.defense = rand.nextInt(7) + 2;
        this.awarenessRadius = 250;
        this.threatenedRadius = 50;
        quiver = new ArchProjectile[QUIVER_SIZE];
        this.isRanged = true;
        this.isMelee = false;
        this.isMounted = false;

        for (int quiverIndex = 0; quiverIndex < quiver.length; quiverIndex++)
        {
            //if the target is within deadly accuracy, accuracy = 100%
            
            ArchProjectile arrow = new ArchProjectile(centerPoint, this.accuracy, false, ARROW_POWER, ARROW_SPEED, IMPACT_RADIUS);
            this.quiver[quiverIndex] = arrow;
        }
    }

    @Override
    public int getClassScore()
    {
        return Archer.ARCHER_CLASS_SCORE;
    }

    public ArchProjectile getNextArrow()
    {
        ArchProjectile arrow = this.quiver[this.arrowIndex];
        arrowIndex++;
        if (arrowIndex >= this.quiver.length)
        {
            arrowIndex = 0;
        }
        return arrow;
    }

    @Override
    public Action tick(int ticksElapsed)
    {

        return super.tick(ticksElapsed);
    }

    public Point findEnemyCenterMass()
    {
        int xSum = 0;
        int ySum = 0;

        for (BattlefieldObject enemy : this.enemies)
        {
            xSum += enemy.getCenterPoint().x;
            ySum += enemy.getCenterPoint().y;
        }

        return new Point(xSum / this.enemies.size(), ySum / this.enemies.size());
    }

    @Override
    protected Action attackNearestEnemy(int speedToAttack, int tickNumber)
    {

        Action action;
        if (this.nearestEnemy != null)
        {
            Point targetPoint = this.nearestEnemy.getCenterPoint();
            if (!this.threatenedObjects.contains(nearestEnemy))
            {
                targetPoint = this.findEnemyCenterMass();
            }
            action = fireArrow(tickNumber, targetPoint);
        } else
        {
            action = new MoveTowardNearestEnemyAction(this, this.speed, this.preferredMovementMode);
        }

        return action;
    }

    @Override
    public void markKilled()
    {
        super.markKilled();
        for (ArchProjectile arrow : this.quiver)
        {
            arrow.markDestroyed(true);
        }
    }

    @Override
    protected Action followOrders(int tickNumber)
    {
        Action action = null;
        if (AttackAtRange.class.isAssignableFrom(this.orders.getClass()))
        { //since they can't attack at range, hold position
            AttackAtRange rangeAttackOrder = (AttackAtRange) this.orders;
            Point targetPoint = null;
            if (rangeAttackOrder.isSpecificTarget())
            {
                targetPoint = rangeAttackOrder.getTarget();
                double distanceToTarget = this.distanceTo(targetPoint);

                if (distanceToTarget > this.maxRange)
                {
                    if (this.nearestEnemy != null)
                    {
                        double bearings = Utils.findBearing(this.centerPoint, this.nearestEnemy.getCenterPoint());
                        double distanceToEnemy = this.distanceTo(this.nearestEnemy);
                        double splitDistance = distanceToEnemy;
                        if (splitDistance < this.maxRange)
                        {
                            targetPoint = Utils.getPointAtDistanceInDirection(this.getCenterPoint(), bearings, splitDistance);
                        }
                    }
                }
            } else
            {
                targetPoint = Utils.getPointAtDistanceInDirection(this.getCenterPoint(), rangeAttackOrder.getDirection(), Math.min(rangeAttackOrder.getDistance(), this.maxRange));
            }

            action = this.fireArrow(tickNumber, targetPoint);
        } else
        {
            action = super.followOrders(tickNumber);
        }
        return action;
    }

    public Action fireArrow(int tickNumber, Point targetPoint)
    {
        Action action = null;
        ArchProjectile arrow = this.getNextArrow();
        double distanceToTarget = this.distanceTo(targetPoint);
        
        //if close range, archer's have deadly aim.
        double accuracyToUse = distanceToTarget <= this.deadlyRange ? 1 : this.accuracy;
        arrow.setAccuracy(accuracyToUse);
        
        if (distanceToTarget <= this.maxRange && distanceToTarget >= this.minRange)
        { // if within distance
            if (!arrow.isActive() && (tickNumber % this.rateOfFire == 0))
            {
                arrow.setFlight(true);
                arrow.setCenterPoint(this.centerPoint);
                arrow.setTargetLocation(targetPoint);
                action = new RangedAttack(this.rateOfFire, this, arrow);
            } else
            {
                action = new DoNothingAction(0, this);
            }
        } else if (distanceToTarget > this.maxRange)
        { //out of range.  move closer
            action = new MoveTowardNearestEnemyAction(this, this.speed, this.preferredMovementMode);
        } else if (nearestEnemy != null) // too close!
        {   //todo:  add move away from nearest enemy action.
            action = new MoveAction(this, this.speed, Utils.findBearing(this.centerPoint, nearestEnemy.getCenterPoint()) + 180, preferredMovementMode);
        }

        return action;
    }

    @Override
    public short needsAwarenessWithin()
    {
        return (short) this.maxRange;
    }
}
