/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import Bonuses.BaseBonus;
import UnitFormations.BaseUnitFormation;
import Orders.AbstractOrder;
import Orders.AssumeFormation;
import Orders.AttackAtRange;
import Orders.ChargeOrder;
import Orders.EngageEnemiesOrder;
import Orders.Forever;
import Orders.HoldGround;
import Orders.MarchOrder;
import battlesimulator.Actions.*;
import battlesimulator.BattlefieldObject;
import battlesimulator.Utils;
import battlesimulator.ObjectState;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collection;
import java.util.Random;

/**
 *
 * @author Arlo
 */
public class Soldier extends BattlefieldObject implements Movable
{

    public static final short SOLDIER_PERSONAL_SPACE_RADIUS = 3;
    public static final short SOLDIER_CLASS_SCORE = 1;
    int sweepAngle;
    String allegiance;
    int directionRandomizer = 1;
    SoldierConditions state;
    String name;
    Color color;
    double aggressionLevel;
    short roundsDead = 0;
    //base WAG stats for combat
    int luckScores[];
    int luckIndex = 0;
    final static int DEAD_FADE_TIME_ROUNDS = 25;
    final static int NUM_LUCK_SCORES = 5;
    AbstractOrder orders;
    BaseUnitFormation formation;
    int indexOfFormationPosition;
    MovementModes preferredMovementMode = MovementModes.WAIT;
    String unitName;
    boolean isRanged;
    boolean isMelee;
    boolean isMounted;

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Soldier(String allegiance, String name)
    {
        this(allegiance, name, new Point(0, 0));
    }

    public Soldier(String allegiance, String name, Point centerPoint)
    {
        super(centerPoint);
        this.allegiance = allegiance;
        this.sweepAngle = 0;
        this.name = name;
        this.state = SoldierConditions.MARCHING;
        this.personalSpaceRadius = SOLDIER_PERSONAL_SPACE_RADIUS;
        this.reach = 2;
        this.aggressionLevel = 3;
        this.threatenedRadius = 15;
        this.awarenessRadius = 25;
        this.orders = null;
        this.aware = true;
        this.preferredMovementMode = MovementModes.AVOID;
        this.isRanged = false;
        this.isMelee = true;
        this.isMounted = false;
        Random rand = new Random();

        strength = rand.nextInt(17) + 1;
        defense = rand.nextInt(20) + 2;
        endurance = rand.nextInt(20) + 10; //10-30 endurance
        luckScores = new int[NUM_LUCK_SCORES];
        for (int luckIndex = 0; luckIndex < NUM_LUCK_SCORES; luckIndex++)
        {
            luckScores[luckIndex] = rand.nextInt(5) + 1;
        }
        luckIndex = 0;
        this.speed = 6;//rand.nextInt(3) + 2;
        this.pushPower = 2;
        this.initiative = (short) rand.nextInt(100);
        //System.out.println(this.allegiance + " s,d,l: " + strength + "," + defense + "," + luckScores);

    }

    public static short getPersonalSpace()
    {
        return SOLDIER_PERSONAL_SPACE_RADIUS;
    }

    public String getUnitName()
    {
        return unitName;
    }

    public SoldierConditions getState()
    {
        return state;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }

    private void incrementLuckIndex()
    {
        this.luckIndex++;

        if (this.luckIndex >= NUM_LUCK_SCORES)
        {
            this.luckIndex = 0;
        }
    }

    public void setAllegiance(String allegiance)
    {
        this.allegiance = allegiance;
    }

    public void setFormation(BaseUnitFormation formation)
    {
        this.formation = formation;
        this.takePositionInFormation();
        this.setCenterPoint(this.formation.getOffsetOfPosition(this.indexOfFormationPosition));
    }

    public void setPointInFormation()
    {
        if (this.formation != null)
        {
            this.setCenterPoint(this.formation.getOffsetOfPosition(this.indexOfFormationPosition));
        }
    }

    @Override
    public void receiveAction(Action action)
    {
    }

    public synchronized void receiveOrder(AbstractOrder order, boolean supercede)
    {
        this.orders = order;

        if ((this.orders != null) && AssumeFormation.class.isAssignableFrom(this.orders.getClass()))
        {
            BaseUnitFormation formation = ((AssumeFormation) order).getFormation();
            this.formation = formation;
            this.takePositionInFormation();

        }
    }

    public void takePositionInFormation()
    {
        if (this.formation != null)
        {
            if (!Officer.class.isAssignableFrom(this.getClass()))
            {
                //otherwise, a normal soldier, find a spot
                for (int index = 0; index < formation.getPointsInFormation().size(); index++)
                {
                    if (!formation.getPointsInFormation().get(index).postionTaken())
                    {
                        this.indexOfFormationPosition = index;
                        this.formation.claimPositionInFormation(index, this);
                        break;
                    }
                }
            } else
            {
                this.formation.claimOfficerPosition((Officer) this);
            }
        }
    }

    public boolean isIsRanged()
    {
        return isRanged;
    }

    public boolean isIsMelee()
    {
        return isMelee;
    }

    public boolean isIsMounted()
    {
        return isMounted;
    }

    @Override
    public Action tick(int ticksElapsed)
    {

        Action action;
        this.directionRandomizer = ticksElapsed + this.getLuck() % 360;
        if (this.orders == null)
        {
            if (this.aggressionLevel > 2)
            {
                action = this.attackNearestEnemy(this.speed, ticksElapsed);

            } else
            {
                action = new MoveAction(this, this.speed, 180, MovementModes.WAIT);
                this.state = SoldierConditions.MARCHING;
            }

            directionRandomizer++;
        } else
        {
            action = followOrders(ticksElapsed);

        }
        return action;
    }

    protected Action hostileMovement(double direction, int speed)
    {
        Action action;
        BattlefieldObject bObject = this.findNearestEnemyInDirection(this.threatenedObjects, direction, 30);
        double distanceToEnemy = Double.MAX_VALUE;
        double threatenedDistance = Math.max(this.threatenedRadius, speed);
        if (bObject != null)
        {
            distanceToEnemy = bObject.distanceTo(this);
        }

        if (distanceToEnemy > threatenedDistance)
        {
            action = new MoveAction(this, speed, direction, this.preferredMovementMode);
            this.state = SoldierConditions.MARCHING;
        } else if (Utils.targetInReach(this, bObject, this.reach))
        {
            this.target = bObject;
            action = new EngageCombatAction(this, this.target);
            this.state = SoldierConditions.IN_COMBAT;
            this.facingDirection = Utils.findBearing(this.centerPoint, this.target.getCenterPoint());
        } else
        {
            action = new MoveTowardNearestEnemyAction(this, speed, MovementModes.PUSH);
            this.state = SoldierConditions.MARCHING;
        }

        return action;

    }

    //don't move. just hold ground and fight anything that gets close
    protected Action holdGround()
    {
        Action action;
        this.target = this.nearestEnemy;
        //todo:  if target is null, he can find nearest enemy based on awareness
        if (this.target != null && Utils.targetInReach(this, target, this.reach))
        {
            action = new EngageCombatAction(this, this.target);
            this.state = SoldierConditions.IN_COMBAT; //todo when will it ever come out of state?
        } else
        {
            action = new DoNothingAction(0, this);
            this.state = SoldierConditions.READY_FOR_ORDERS;
        }

        this.bonuses.add(new BaseBonus("Hold Your Ground", BaseBonus.Attribute.DEFENSE, 6, 1));
        return action;
    }

    public void addBonus(BaseBonus bonus)
    {
        this.bonuses.add(bonus);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public void evaluateBonuses()
    {
        //adjacent ally provides shielding
        if (this.nearestAlly != null)
        {
            boolean allyWithinReach = Utils.targetInReach(this, nearestAlly, this.reach);
            if (allyWithinReach)
            {
                this.bonuses.add(new BaseBonus("Overlapping shields", BaseBonus.Attribute.DEFENSE, 3, 1));
            }
        }

        //crowded units cannot swing a weapon as well
        if (this.getReachableObjects().size() > 5) //TODO: magic number.  What should this value be?
        {
            this.bonuses.add(new BaseBonus("Crowded", BaseBonus.Attribute.STRENGTH, -3, 1));
        }

        //factor in fatigue.  It affects defense twice as much as strength
        if (fatigue > endurance)
        {
            int fatigueFactor = fatigue / endurance;
            this.bonuses.add(new BaseBonus("ShieldSoHeavy", BaseBonus.Attribute.DEFENSE, -2 * fatigueFactor, 1));
            this.bonuses.add(new BaseBonus("ShieldSoHeavy", BaseBonus.Attribute.STRENGTH, -1 * fatigueFactor, 1));
        }

    }

    @Override
    public short needsAwarenessWithin()
    {
        //basic requirements
//        boolean needsUpdate = super.needsAwarenessWithin();
//        boolean allyWithinReach = this.nearestAlly== null ? false : Utils.targetInReach(this, nearestAlly, this.reach);
//        needsUpdate &= (this.nearestEnemy == null || !allyWithinReach);
        return this.threatenedRadius;
    }

    protected Action attackNearestEnemy(int speedToAttack, int tickNumber)
    {
        Action action;
        this.target = this.nearestEnemy;
        //todo:  if target is null, he can find nearest enemy based on awareness
        if (this.target != null && target.isActive())
        {
            if (Utils.targetInReach(this, target, this.reach))
            {
                action = new EngageCombatAction(this, this.target);
                this.state = SoldierConditions.IN_COMBAT; //todo when will it ever come out of state?
            } else if (this.distanceTo(target) <= this.threatenedRadius)
            {
                action = new MoveTowardNearestEnemyAction(this, speedToAttack, MovementModes.AVOID);
                this.state = SoldierConditions.MARCHING;
            } else
            {
                action = new MoveTowardNearestEnemyAction(this, speedToAttack, MovementModes.AVOID);
                this.state = SoldierConditions.MARCHING;
            }
        } else
        {
            action = new MoveTowardNearestEnemyAction(this, speedToAttack, MovementModes.AVOID);
            this.state = SoldierConditions.MARCHING;
        }
        return action;
    }

    protected synchronized Action followOrders(int tickNumber)
    {
        Action action = null;
        AbstractOrder ordersToFollow = this.orders;
        if (ordersToFollow != null)
        {
            if ((ordersToFollow != null) && AssumeFormation.class.isAssignableFrom(ordersToFollow.getClass()))
            {
                action = this.assumeFormation();
            } else if ((ordersToFollow != null) && MarchOrder.class.isAssignableFrom(ordersToFollow.getClass()))
            {
                MarchOrder march = (MarchOrder) ordersToFollow;
                action = new MoveAction(this, Math.min(this.speed, march.getSpeed()), march.getDirection(), MovementModes.WAIT);
                this.state = SoldierConditions.MARCHING;
            } else if ((ordersToFollow != null) && ChargeOrder.class.isAssignableFrom(ordersToFollow.getClass()))
            {
                ChargeOrder chargeOrder = (ChargeOrder) ordersToFollow;
                action = hostileMovement(chargeOrder.getDirection(), this.speed);
            } else if ((ordersToFollow != null) && EngageEnemiesOrder.class.isAssignableFrom(ordersToFollow.getClass()))
            {
                action = attackNearestEnemy(this.speed, tickNumber);
            } else if ((ordersToFollow != null) && HoldGround.class.isAssignableFrom(ordersToFollow.getClass())
                    || AttackAtRange.class.isAssignableFrom(ordersToFollow.getClass()))
            { //since they can't attack at range, hold position
                action = this.holdGround();
            } else
            {
                action = new DoNothingAction(1, this);
                this.state = SoldierConditions.READY_FOR_ORDERS;
            }
        } else
        {
            action = new DoNothingAction(1, this);
        }
        return action;
    }

    public Action assumeFormation()
    {
        Action action = this.holdGround(); //this will verify that I'm not ignoring anyone that's really close to me
        Point formationPosition = this.formation.getOffsetOfPosition(this.indexOfFormationPosition);
        int distanceToPoint = this.distanceTo(formationPosition);
        double bearing = Utils.findBearing(this.centerPoint, formationPosition);

        if (DoNothingAction.class.isAssignableFrom(action.getClass())) //if hold ground says do nothing (no enemies immediately close)
        {
            if (distanceToPoint >= 1)
            {
                action = new MoveAction(this, Math.min(this.speed, distanceToPoint), bearing, MovementModes.AVOID);
                this.state = SoldierConditions.MARCHING;
            } else if (distanceToPoint == 0)
            {
                action = new DoNothingAction(0, this);
                this.state = SoldierConditions.READY_FOR_ORDERS;
            } else
            {
                action = new TeleportAction(this, formationPosition);
                this.state = SoldierConditions.READY_FOR_ORDERS;
            }
        }

        return action;
    }

    @Override
    public boolean hasAllegiance()
    {
        return true;
    }

    protected BattlefieldObject findNearestEnemy(Collection<BattlefieldObject> searchGroup)
    {
        BattlefieldObject objectToFind = null;
        double minDistance = Double.MAX_VALUE;
        for (BattlefieldObject bObject : searchGroup)
        {
            if (bObject.isActive() && bObject.hasAllegiance() && (!bObject.getAllegience().equals(this.getAllegience())))
            {
                double distanceToTarget = bObject.getCenterPoint().distance(this.getCenterPoint());
                if ((distanceToTarget < minDistance))
                {
                    minDistance = distanceToTarget;
                    objectToFind = bObject;
                }
            }
        }
        return objectToFind;
    }

    protected BattlefieldObject findNearestEnemyInDirection(Collection<BattlefieldObject> searchGroup, double direction, double threshold)
    {
        BattlefieldObject objectToFind = null;
        double minDistance = Double.MAX_VALUE;
        for (BattlefieldObject bObject : this.knownObjects)
        {
            if (bObject.isActive() && bObject.hasAllegiance() && (!bObject.getAllegience().equals(this.getAllegience())))
            {
                double distanceToTarget = bObject.getCenterPoint().distance(this.getCenterPoint());
                double directionToTarget = Utils.findBearing(this.centerPoint, bObject.getCenterPoint());
                if ((distanceToTarget < minDistance) && (Math.abs(direction - directionToTarget) <= threshold))
                {
                    minDistance = distanceToTarget;
                    objectToFind = bObject;
                }
            }
        }
        return objectToFind;
    }

    protected BattlefieldObject findNearestAlly(Collection<BattlefieldObject> searchGroup)
    {
        BattlefieldObject objectToFind = null;
        double minDistance = Double.MAX_VALUE;
        for (BattlefieldObject bObject : searchGroup)
        {
            if (bObject.isActive() && bObject.hasAllegiance() && (bObject.getAllegience().equals(this.getAllegience())))
            {
                double distanceToTarget = bObject.getCenterPoint().distance(this.getCenterPoint());
                if ((distanceToTarget < minDistance))
                {
                    minDistance = distanceToTarget;
                    objectToFind = bObject;
                }
            }
        }
        return objectToFind;
    }

    protected BattlefieldObject findNearestAllyInDirection(Collection<BattlefieldObject> searchGroup, double direction, double threshold)
    {
        BattlefieldObject objectToFind = null;
        double minDistance = Double.MAX_VALUE;
        for (BattlefieldObject bObject : searchGroup)
        {
            if (bObject.isActive() && bObject.hasAllegiance() && (bObject.getAllegience().equals(this.getAllegience())))
            {
                double distanceToTarget = bObject.getCenterPoint().distance(this.getCenterPoint());
                double directionToTarget = Utils.findBearing(this.centerPoint, bObject.getCenterPoint());
                if ((distanceToTarget < minDistance) && (Math.abs(direction - directionToTarget) <= threshold))
                {
                    minDistance = distanceToTarget;
                    objectToFind = bObject;
                }
            }
        }
        return objectToFind;
    }

    @Override
    public String getAllegience()
    {
        return this.allegiance;
    }

    @Override
    public boolean isActive()
    {
        //System.out.println(this.allegiance + " " + this.state.name() + ": " + SoldierState.isActiveState(this.state));
        return SoldierConditions.isActiveState(this.state);
    }

    @Override
    public void setOutOfBounds()
    {
        //System.out.println(this.centerPoint.x + "," + this.centerPoint.y);
        this.state = SoldierConditions.RETREATED;
    }

    public double getWeaponReach()
    {
        return reach;
    }

    @Override
    public int getDefense()
    {
        return this.defense;
    }

    public void setWeaponReach(short weaponReach)
    {
        this.reach = weaponReach;
    }

    public double getAggressionLevel()
    {
        return aggressionLevel;
    }

    public void setAggressionLevel(double aggressionLevel)
    {
        this.aggressionLevel = aggressionLevel;
    }

    @Override
    public CombatResult resolveCombat()
    {
        CombatResult result;
        int strengthOfEnemy = 0;
        for (BattlefieldObject object : this.combatedBy)
        {
            if (Soldier.class.isAssignableFrom(object.getClass()))
            {

                Soldier attacker = (Soldier) object;
                strengthOfEnemy += attacker.getStrength();
                strengthOfEnemy += attacker.getLuck();
            }
            object.incrementFatigue(1);
        }

        int difference = (this.getLuck() + this.defense) - strengthOfEnemy;

        //Todo:  make this much more in-depth.  Right now, he'll die as long as anyone is attacking him
        if (difference < 0)
        {
            result = new CombatResult(this, CombatResult.CombatOutcome.KILLED, this.combatedBy.get(0));
        } else if (difference < 2)
        {
            result = new CombatResult(this, CombatResult.CombatOutcome.WOUNDED, this.combatedBy.get(0));
        } else
        {
            result = new CombatResult(this, CombatResult.CombatOutcome.DEFENDED, null);

        }
        this.combatedBy.clear();
        this.incrementFatigue(1);
        return result;
    }

    @Override
    public void markKilled()
    {
        this.state = SoldierConditions.DEAD;
        this.color = Color.GRAY;

    }

    public int getStrength()
    {
        return strength;
    }

    public int getLuck()
    {
        int index = this.luckIndex % this.luckScores.length;
        
        int luck = this.luckScores[index];
        this.incrementLuckIndex();
        return luck;
    }

    public void setSpeed(short speed)
    {
        this.speed = speed;
    }

    @Override
    public void receiveWound()
    {
        this.defense -= (this.getLuck());
        //this.state = SoldierConditions.IN_COMBAT;
    }

    @Override
    public SoldierState generateState(int numTicks)
    {
        SoldierState soldierState = null;
        if (this.state == SoldierConditions.DEAD)
        {
            this.roundsDead++;
        }
        if (this.roundsDead < DEAD_FADE_TIME_ROUNDS)
        {
            //this.objectStates.put(numTicks, 
            soldierState
                    = new SoldierState(this.centerPoint,
                            this.personalSpaceRadius,
                            this.facingDirection,
                            this.roundsDead,
                            this.state,
                            this.color,
                            this.isActive(),
                            this.reach,
                            this.sweepAngle,
                            numTicks);
        }

        return soldierState;
    }

    @Override
    public int getClassScore()
    {
        return Soldier.SOLDIER_CLASS_SCORE;
    }

    public class SoldierState extends ObjectState
    {

        short centerX;
        short centerY;
        short personalSpaceRadius;
        double facingDirection;
        short roundsDead;
        SoldierConditions condition;
        Color color;
        boolean isActive;
        int sweepAngle;
        short reach;
        int numTicks;

        SoldierState(Point centerPoint, short personalSpaceRadius, double facingDirection, short roundsDead, SoldierConditions condition, Color color, boolean isActive, short reach, int sweepAngle, int ticks)
        {
            this.centerX = (short) centerPoint.x;
            this.centerY = (short) centerPoint.y;
            this.personalSpaceRadius = personalSpaceRadius;
            this.facingDirection = facingDirection;
            this.roundsDead = roundsDead;
            this.condition = condition;
            this.color = color;
            this.isActive = isActive;
            this.sweepAngle = sweepAngle;
            this.reach = reach;
            this.numTicks = ticks;
        }

        public boolean isActive()
        {
            return isActive;
        }

        @Override
        public void paintObject(Graphics2D g)
        {
            if (this.roundsDead < DEAD_FADE_TIME_ROUNDS)
            {
                Point facingPoint = Utils.getPointAtDistanceInDirection(new Point(this.centerX, this.centerY),
                        this.facingDirection,
                        this.personalSpaceRadius);
                Point reachPoint = Utils.getPointAtDistanceInDirection(facingPoint,
                        this.facingDirection,
                        this.reach);
                
                //draw the combat graphic first, so it show up behind the soldier.
                if (this.condition == SoldierConditions.IN_COMBAT && this.reach < 15 && this.numTicks % 2 == 0)//todo remove magic number.  it's there to keep archer's from doing stupid things
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.setStroke(new BasicStroke(2));
                    if (this.sweepAngle == 0)
                    {
                        Line2D.Double weaponLine = new Line2D.Double(facingPoint, reachPoint);

                        g.draw(weaponLine);
                    } else
                    {
                        //coordinates of bounding rectangle
                        int sweepRadius = this.personalSpaceRadius + this.reach;
                        int x = this.centerX - sweepRadius;
                        int y = this.centerY - sweepRadius;
                        //find start/end angles in Java's angle scheme where East is 0 degrees
                        int sweepStart = (int) Utils.convertCompassDirectionToJava(this.facingDirection - this.sweepAngle / 2);

                        g.fillArc(x, y, sweepRadius * 2, sweepRadius * 2, sweepStart, -this.sweepAngle);
                        //g.fillArc(100, 100, 100, 100, 70, 30);
                    }
                }
                g.setStroke(new BasicStroke(1));
                
                int x = this.centerX - this.personalSpaceRadius;
                int y = this.centerY - this.personalSpaceRadius;
                Ellipse2D elipse = new Ellipse2D.Double(x, y,
                        2 * this.personalSpaceRadius,
                        2 * this.personalSpaceRadius);
                
                Line2D.Double facingLine = new Line2D.Double(new Point(this.centerX, this.centerY), facingPoint);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g.setColor(this.color);

                g.draw(elipse);
                g.fill(elipse);
                g.setColor(Color.BLACK);
                g.draw(facingLine);

            }
        }
    }
}
