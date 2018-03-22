/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import Bonuses.BaseBonus;
import battlesimulator.Actions.Action;
import battlesimulator.Geography.Flying;

import battlesimulator.Geography.PushResult;
import battlesimulator.Military.CombatResult;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Objects that should be drawn and that can take action on the battlefield
 *
 * Note: this class has a natural ordering that is inconsistent with equals.
 *
 * @author Arlo
 */
public abstract class BattlefieldObject implements Comparable<BattlefieldObject>, java.io.Serializable
{

    UUID objectID;
    protected double velocity;
    protected boolean isInConflict;
    protected double momentumDirection;
    protected double mass;
    protected double facingDirection;
    protected short awarenessRadius = 50;
    protected short threatenedRadius = 15;
    protected short personalSpaceRadius;
    protected Point centerPoint;
    protected Action currentAction;
    protected short speed; //todo:  make movable interface
    protected BattlefieldObject target;
    protected BattlefieldObject nearestEnemy;
    protected BattlefieldObject nearestAlly;
    protected List<BattlefieldObject> combatedBy = Collections.synchronizedList(new ArrayList<>());
    protected short killCount = 0;
    protected short initiative;
    protected short reach;
    protected short pushPower;
    protected short pushResistance;
    protected ArrayList<BattlefieldObject> knownObjects = new ArrayList<>();
    protected ArrayList<BattlefieldObject> threatenedObjects = new ArrayList<>();
    protected ArrayList<BattlefieldObject> reachableObjects = new ArrayList<>();
    protected ArrayList<BattlefieldObject> enemies = new ArrayList<>();

   

    protected ArrayList<BaseBonus> bonuses = new ArrayList<>();
    protected int strength;
    protected int defense;
    protected int endurance;
    protected int fatigue;
    //todo:  make accessors
    public static int classId = 0;
    public int thisId;
    //variables for circular position history
    static final int MAX_HISTORY = 3;
    protected int currentPositionIndex = 0;
    protected Point positionHistory[];
    protected boolean aware;
    protected boolean exists;
    protected int distanceToEnemy = 0;

    public BattlefieldObject(Point centerPoint)
    {
        this.centerPoint = centerPoint;
        this.isInConflict = false;
        this.objectID = UUID.randomUUID();

        this.thisId = classId;
        this.aware = false;  //default 
        classId++;
        this.initiative = 0;
        this.mass = 1;
        this.velocity = 0;
        this.endurance = 10;
        this.fatigue = 0;
        this.pushPower = 2;
        this.pushResistance = 1;
        this.facingDirection = 0;
        this.positionHistory = new Point[MAX_HISTORY];
        this.distanceToEnemy = Integer.MAX_VALUE;

        for (int index = 0; index < MAX_HISTORY; index++)
        {
            this.positionHistory[index] = new Point(0, 0);
        }
        BattlefieldRoster.Instance().addObject(this);
    }
    
    public abstract void receiveAction(Action action);

    public abstract Action tick(int tickElapsed);

    public abstract boolean hasAllegiance();

    public abstract String getAllegience();

    public abstract ObjectState generateState(int tickNumber);

    public void setNearestEnemy(BattlefieldObject nearestEnemy)
    {
        this.nearestEnemy = nearestEnemy;
    }

    public abstract int getClassScore();
    
    public UUID getObjectID()
    {
        return objectID;
    }

    public BattlefieldObject getNearestEnemy()
    {
        return this.nearestEnemy;
    }

    public void setNearestAlly(BattlefieldObject nearestAlly)
    {
        this.nearestAlly = nearestAlly;
    }

    public int getThreatenedRadius()
    {
        return threatenedRadius;
    }

    public boolean isAware()
    {
        return aware;
    }

    public void setIsInConflict(boolean inConflict)
    {
        this.isInConflict = inConflict;
    }

    public boolean isInConflict()
    {
        return this.isInConflict;
    }

    public boolean doesExist()
    {
        this.exists = this.aware || this.isActive();
        return this.exists;
    }

//    public ObjectState getStateForRound(int tickNumber) {
////        ObjectState state = this.objectStates[tickNumber];
////        if (this.objectStates.containsKey(tickNumber)) {
////            state = objectStates.get(tickNumber);
////        } else {
////            state = new DidNotExistObjectState();
////        }
////        return state;
//        ObjectState state = null;
//        
//
//            state = this.objectStates[tickNumber];
//
//        return state;
//    }
    public abstract boolean isActive();

    public void moveTo(int xTranslation, int yTranslation)
    {
        this.centerPoint.setLocation(xTranslation, yTranslation);
        this.addPointToHistory(this.centerPoint);
    }

    public Point[] getPositionHistory()
    {
        return this.positionHistory;
    }

    public boolean isPointInHistory(double tolerance, Point pointToCheck)
    {
        boolean isInHistory = false;

        for (Point pointInHistory : this.positionHistory)
        {
            isInHistory |= (pointInHistory.distance(pointToCheck) <= tolerance);
        }

        return isInHistory;
    }

    public int getDefense()
    {
        return defense;
    }

    public int getPushPower()
    {
        //todo
        return this.pushPower;
    }

    public int getPushResistance()
    {
        return pushResistance;
    }

    public void reinforcePush(double reinforceAmount)
    {
        //this.pushPower += reinforceAmount;
    }

    public PushResult resolvePush(BattlefieldObject pusher, int remainingPushPower, Point pushOrigin, double pusherDirectionOfTravel)
    {
        Point pushedToPoint = null; //return null if don't get pushed
        PushResult result;

        //the direction of the push follows the line between the two center points at the point of impact
        double directionOfPush = Utils.findBearing(pushOrigin, this.centerPoint);

        double directionDifference = Utils.adjustAngleValue(pusherDirectionOfTravel - directionOfPush);

        //difference between facing direction and the direction the push is coming from
        //Cosine it for a factor to multiply.  If direction difference is 0, then use full resistance, if not, use partial resistance
        double facingDirectionDifference = Utils.adjustAngleValue(Math.abs(directionOfPush - 180 - this.facingDirection));

        //System.out.print("facing dir before; " + facingDirectionDifference);
        if (facingDirectionDifference >= 90)
        {
            facingDirectionDifference = 0;
        } else
        {
            facingDirectionDifference = Math.cos(Utils.degreesToRadians(facingDirectionDifference));
        }

        //System.out.println("  facing dir after; " + facingDirectionDifference);
        //scalar projection to determine magnitude of push (a *cos(t)) divided by push 
        //for the fraction of the push that's dead-on.
        //    This will reduce the amount the defender is able to resist the push
        double ratioOfPush = Math.cos(Utils.degreesToRadians(directionDifference));
        //System.out.println("ratio: " + ratioOfPush);
        int resistance = (int) Math.round(this.pushResistance * ratioOfPush * facingDirectionDifference);
        resistance = Math.max(resistance, 1);//have to resist at least 1.
        //System.out.println("resistance: " + resistance);
        int pushDifferential = remainingPushPower - resistance;

        //distance equals the minimum distance apart the two can be minus the distance apart they already are.
        //so two objects with radius 3 that are 2 apart, needs to move 4 to put them safely at 6 apart again.
        int distanceOfPush = (int) Math.ceil(this.personalSpaceRadius + pusher.personalSpaceRadius - (this.centerPoint.distance(pushOrigin)));

        //calculate where the object got pushed to
        pushedToPoint = Utils.getPointAtDistanceInDirection(this.centerPoint, directionOfPush, distanceOfPush);
        if ((pushDifferential >= 0) && (pusher.isAlly(this)))
        {
            result = new PushResult(this, pushDifferential, pushedToPoint, PushResult.PushOutcomes.PUSHED);
        } else if ((pushDifferential > 5) && (ratioOfPush >= 0.75))
        { // if push is strong enough and direct enough
            //this object got trampled
            result = new PushResult(this, pushDifferential, pushedToPoint, PushResult.PushOutcomes.TRAMPLED);

        } else if (pushDifferential >= 0) //todo: tie goes to the pusher... for now
        {
            result = new PushResult(this, pushDifferential, pushedToPoint, PushResult.PushOutcomes.PUSHED);
        } else
        {
            result = new PushResult(this, -1, pushedToPoint, PushResult.PushOutcomes.RESISTED);
        }
        return result;
    }

    public double getMass()
    {
        //size and strength
        return this.mass;
    }

    public double caclulateMomentum()
    {
        double moment;

        if (velocity == 0)
        {
            moment = this.mass;
            this.momentumDirection = -1; //no direction
        } else
        {
            moment = this.mass * this.velocity;
        }

        //size times velocity
        return moment;
    }

    public Point getCenterPoint()
    {
        return centerPoint;
    }

    public Action getCurrentAction()
    {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction)
    {
        this.currentAction = currentAction;
    }

    public abstract void setOutOfBounds();

    public double getFacingDirection()
    {
        return facingDirection;
    }

    public int getAwarenessRadius()
    {
        return awarenessRadius;
    }

    public int getPersonalSpaceRadius()
    {
        return personalSpaceRadius;
    }
    public void clearKnownObjects()
    {
        this.knownObjects.clear();
    }
    
    public void addKnownObject(BattlefieldObject bObject)
    {
        this.knownObjects.add(bObject);
    }
    
    public void clearThreatenedObjects()
    {
        this.threatenedObjects.clear();
    }
    
    public void addThreatenedObject(BattlefieldObject bObject)
    {
        this.threatenedObjects.add(bObject);
    }
    
        public void clearReachableObjects()
    {
        this.reachableObjects.clear();
    }
    
    public void addReachableObject(BattlefieldObject bObject)
    {
        this.reachableObjects.add(bObject);
    }
    
            public void clearEnemyObjects()
    {
        this.enemies.clear();
    }
    
    public void addEnemyObject(BattlefieldObject bObject)
    {
        this.enemies.add(bObject);
    }

    public void setKnownObjects(ArrayList<BattlefieldObject> knownObjects)
    {
        
        this.knownObjects.addAll(knownObjects);
    }

    public void setDistanceToEnemy(long distanceSquared)
    {
        this.distanceToEnemy = (int) Math.sqrt(distanceSquared);

    }

    public int getDistanceToEnemy()
    {
        return distanceToEnemy;
    }

    public void setThreatenedObjects(ArrayList<BattlefieldObject> threatenedObjects)
    {
        this.threatenedObjects.clear();
        this.threatenedObjects.addAll(threatenedObjects);
    }

    public void setReachableObjects(ArrayList<BattlefieldObject> reachableObjects)
    {
        this.reachableObjects.clear();
        this.reachableObjects.addAll(reachableObjects);
    }

    public ArrayList<BattlefieldObject> getKnownObjects()
    {
        return knownObjects;
    }

    public ArrayList<BattlefieldObject> getThreatenedObjects()
    {
        return threatenedObjects;
    }

    public ArrayList<BattlefieldObject> getReachableObjects()
    {
        return reachableObjects;
    }

    public BattlefieldObject getTarget()
    {
        return target;
    }

    public void setFacingDirection(double facingDirection)
    {
        this.facingDirection = facingDirection;
    }

    public void setTarget(BattlefieldObject target)
    {
        this.target = target;
    }

    public void addAttacker(BattlefieldObject combatant)
    {
        combatedBy.add(combatant);
    }

    public void removeAttacker(BattlefieldObject combatant)
    {
        combatedBy.remove(combatant);
    }

    public void addKillCount()
    {
        this.killCount++;
    }

    public boolean isEngagedInCombat()
    {

        boolean inCombat = !this.combatedBy.isEmpty();

        return inCombat;
    }

    public abstract void markKilled();

    public void markTrampled()
    {
        this.markKilled();
    }

    public abstract CombatResult resolveCombat();

    public abstract void receiveWound();

    public int getInitiative()
    {
        return this.initiative;
    }

    @Override
    public int compareTo(BattlefieldObject bObject)
    {
        return this.initiative - bObject.getInitiative();
    }

    public void setInitiative(short initiative)
    {
        this.initiative = initiative;
    }

    //TODO:  when generating the awareness list of objects, order it by distance from the observer...
//These methods in BattlefieldObject... or in "movable" interface
    public boolean violatesPersonalSpace(BattlefieldObject bObject)
    {
        return this.violatesPersonalSpace(bObject.getCenterPoint(), bObject.getPersonalSpaceRadius());
    }

    public boolean violatesPersonalSpace(Point point, int personalSpaceRadius)
    {
        boolean violatesSpace = false;
        double distanceApart = point.distance(this.centerPoint);
        violatesSpace = distanceApart < (personalSpaceRadius + this.personalSpaceRadius);
        return violatesSpace;
    }

    public int distanceTo(Point point)
    {

        return (int) (this.centerPoint.distance(point) - this.personalSpaceRadius);
    }

    public int distanceTo(BattlefieldObject bObject)
    {
        return this.distanceTo(bObject.getCenterPoint()) - bObject.getPersonalSpaceRadius();
    }

    public boolean potentialCollision(BattlefieldObject bObject, double directionOfTravel, double distanceToTravel)
    {
        Point pointToMoveTo = Utils.getPointAtDistanceInDirection(this.centerPoint, directionOfTravel, distanceToTravel);
        Line2D lineOfTravel = new Line2D.Double(this.centerPoint, pointToMoveTo);
        double closestDistance = lineOfTravel.ptSegDist(bObject.getCenterPoint());
        boolean obstacleIsInFlight = false;

        if (Flying.class.isAssignableFrom(bObject.getClass()))
        {
            obstacleIsInFlight = ((Flying) bObject).inFlight();
        }
        boolean thisIsInFlight = false;

        if (Flying.class.isAssignableFrom(this.getClass()))
        {
            thisIsInFlight = ((Flying) this).inFlight();
        }
        //if one is flying and the other isn't, they won't collide
        boolean avoidDueToElevation = (obstacleIsInFlight != thisIsInFlight);
        return !avoidDueToElevation && (closestDistance < (this.personalSpaceRadius + bObject.getPersonalSpaceRadius()));
    }

    public Point getPointOfCollision(BattlefieldObject bObject, double directionOfTravel, int distanceToTravel)
    {
        boolean collision = false;
        Point testPoint = null;
        Point collisionPoint = null;
        //try each point along the path of travel to see if there's a collision
        int trialDistance = 0;
        while (trialDistance <= distanceToTravel && collision == false)
        {
            testPoint = Utils.getPointAtDistanceInDirection(this.getCenterPoint(), directionOfTravel, trialDistance);
            collision |= bObject.violatesPersonalSpace(testPoint, bObject.getPersonalSpaceRadius());
            trialDistance++;
        }
        if (collision)
        {
            collisionPoint = testPoint;
        }
        return collisionPoint;
    }

    public boolean isAlly(BattlefieldObject bObject)
    {
        boolean isAlly = (this.hasAllegiance() && bObject.hasAllegiance() && this.getAllegience().equals(bObject.getAllegience()));
        return isAlly;
    }

    public BattlefieldObject nearestObstacleInDirection(double directionOfTravel, double distanceToTravel, ArrayList<BattlefieldObject> potentialObstacles)
    {
        BattlefieldObject firstObstacle = null;
        double distanceToObstacle = Double.MAX_VALUE;

        for (BattlefieldObject potentialObstacle : potentialObstacles)
        {
            //determine if it's actually an obstacle
            boolean isObstacle = this.potentialCollision(potentialObstacle, directionOfTravel, distanceToTravel);
            if (isObstacle)
            {
                double distance = potentialObstacle.distanceTo(this);
                if (distance < distanceToObstacle)
                {
                    distanceToObstacle = distance;
                    firstObstacle = potentialObstacle;
                }
            }
        }

        return firstObstacle;
    }

    public BattlefieldObject nearestObstacleInDirection(double directionOfTravel, int distanceToTravel)
    {

        ArrayList<BattlefieldObject> potentialObstacles = this.getObjectsWithin(distanceToTravel);

        return this.nearestObstacleInDirection(directionOfTravel, distanceToTravel, potentialObstacles);
    }

    public double adjustDirectionForObstacleAvoidance(double directionOfTravel, double distanceToTravel, ArrayList<BattlefieldObject> potentialObstacles)
    {
        double newDirection = directionOfTravel;  //this is the indicator that there is no direction without obstacles at this distance

        if (!potentialObstacles.isEmpty())
        {
            BattlefieldObject firstObstacle = null;
            //find the first object in the way
            for (BattlefieldObject potentialObstacle : potentialObstacles)
            {
                //determine if it's actually an obstacle
                boolean isObstacle = this.potentialCollision(potentialObstacle, directionOfTravel, distanceToTravel);
                if (isObstacle)
                {
                    firstObstacle = potentialObstacle;
                    break;
                }
            }
            //if there was no obstacle in the path
            if (firstObstacle == null)
            {
                newDirection = directionOfTravel;
            } else
            {
                //otherwise, find a new direction to move (search in each direction)
                double cwResult = searchForOpening(directionOfTravel, distanceToTravel, potentialObstacles, true);
                double counterCwResult = searchForOpening(directionOfTravel, distanceToTravel, potentialObstacles, false);
                //if either result is -1, then there's no path.
                if (cwResult == -1 || counterCwResult == -1)
                {
                    newDirection = -1;
                    //there is no open path. 
                } else
                {
                    //choose whichever direction required the least adjustment
                    double cwDifference = Utils.adjustAngleValue(cwResult - directionOfTravel);
                    double counterCwDifference = Utils.adjustAngleValue(directionOfTravel - counterCwResult);

                    newDirection = (counterCwDifference < cwDifference) ? counterCwResult : cwResult;
                }
            }
        }

        return newDirection;
    }

    public double adjustDirectionForObstacleAvoidance(double directionOfTravel, int distanceToTravel)
    {
        ArrayList<BattlefieldObject> potentialObstacles = this.getObjectsWithin(distanceToTravel);

        return this.adjustDirectionForObstacleAvoidance(directionOfTravel, distanceToTravel, potentialObstacles);
    }

    public void setCenterPoint(Point centerPoint)
    {
        this.centerPoint = centerPoint;

        this.addPointToHistory(this.centerPoint);
    }

    public void addPointToHistory(Point pointToAdd)
    {
        this.currentPositionIndex++;
        if (this.currentPositionIndex >= this.positionHistory.length)
        {
            this.currentPositionIndex = 0;
        }
        this.positionHistory[this.currentPositionIndex].setLocation(pointToAdd);
    }

    public double searchForOpening(double originalDirection, double distance, ArrayList<BattlefieldObject> potentialObstacles, boolean searchClockwise)
    {
        //this method finds the first opening it can, in the given direction (cw if true, ccw if false);
        //if there's nothing in the way, return the original direction
        //if not, set the obstacle (in the way) as the "original" obstacle, then turn <direction> the minimum amount to avoid that obstacle
        //loop through all nearby objects again.
        //if we circle all the way around and find the "original" obstacle in our path again, then there is no opening.

        int initialIndex = -1;
        double currentDirection = originalDirection;
        ArrayList<Double> directionsTried = new ArrayList<>();
        //for each potentialObstacle,
        int index = -1;
        while ((index + 1 < potentialObstacles.size()) && currentDirection != -1)
        {
            index++;
            BattlefieldObject potentialObstacle = potentialObstacles.get(index);
            //determine if it's actually in the way
            boolean isObstacle = this.potentialCollision(potentialObstacle, currentDirection, distance);
            if (isObstacle)
            {
                //if this matches the inital index, then no possible path
                if (index == initialIndex)
                {
                    currentDirection = -1;
                    break;
                }
                //if we don't have an initial index, set it.
                if (initialIndex < 0)
                {
                    initialIndex = index;
                }
                //find the avoidance vector bearing (bool arg (searchClockwise))
                //set this as the new direction of travel
                currentDirection = this.getAvoidanceBearing(potentialObstacle, currentDirection, distance, searchClockwise);
                if (directionsTried.contains(currentDirection))
                {
                    //break out of the loop if we've already tried this direction
                    currentDirection = originalDirection;
                    break;
                } else
                {
                    directionsTried.add(currentDirection);
                }
                //restart the loop.
                index = -1;
            }

        }
        return currentDirection;
    }

    public double getAvoidanceBearing(BattlefieldObject bObject, double directionOfTravel, double distance, boolean clockwise)
    {
        int directionFactor = clockwise ? 1 : -1;
        double avoidanceBearing = directionOfTravel;
        double adjustmentAmount = 15 * directionFactor; //degrees
        double totalAdjustment = 0;
        boolean goodDirection = false;

        while (!goodDirection && (totalAdjustment < 360))
        {
            avoidanceBearing += adjustmentAmount;
            //System.out.println("checking direction: " + avoidanceBearing);
            totalAdjustment += Math.abs(adjustmentAmount);
            avoidanceBearing = Utils.adjustAngleValue(avoidanceBearing);

            Point pointToMoveTo = Utils.getPointAtDistanceInDirection(this.centerPoint, avoidanceBearing, distance);
            Line2D lineOfTravel = new Line2D.Double(this.centerPoint, pointToMoveTo);
            double closestDistance = lineOfTravel.ptSegDist(bObject.getCenterPoint());
            //System.out.println("distance to line: " + closestDistance + "(" + (this.personalSpaceRadius + bObject.getPersonalSpaceRadius()) + ")");
            goodDirection = (closestDistance >= (this.personalSpaceRadius + bObject.getPersonalSpaceRadius()));
        }

        if (!goodDirection)
        {
            avoidanceBearing = -1;
        }

        return avoidanceBearing;
    }


    /*
     //include this in method calling adjust Direction for obstacle
     //adjust direction
     //while direction is -1, 
     //shorten the distance.
     //adjust direction

     */
    public ArrayList<BattlefieldObject> getObjectsWithin(int distanceWithin)
    {
        ArrayList<BattlefieldObject> obstaclesWithin = new ArrayList<>();
        int index = 0;
        int distance = 0;
        
        while (index < this.knownObjects.size())
        { //&& distance <= distanceWithin) { //todo: this assumes a list sorted by closest objects first, and farther objects later
            BattlefieldObject potentialObstacle = this.knownObjects.get(index);
            if (potentialObstacle.isActive())
            {
                distance = this.distanceTo(potentialObstacle);
                if (distance <= distanceWithin) //todo:  make an "is solid" method rather than isActive
                {
                    obstaclesWithin.add(potentialObstacle);
                }
            }
            index++;
        }

        return obstaclesWithin;
    }

    private Rectangle2D buildRectangle(int radius)
    {
        int size = 2 * radius;
        Rectangle2D personalSpace = new Rectangle2D.Double(this.centerPoint.x - this.personalSpaceRadius,
                this.centerPoint.y - radius,
                size,
                size);

        return personalSpace;
    }

    public short needsAwarenessWithin() //speed
    {
        return (short) 0;
    }

    public int getSpeed()
    {
        return speed;
    }

    public ArrayList<Point> buildPathToPoint(Point destination)
    {
        ArrayList<Point> pointsAlongPath = new ArrayList<>();
        Point currentPointInPath = this.centerPoint;
        double distanceToPoint = currentPointInPath.distance(destination);
        Point previousPointInPath = new Point(-1, -1);
        while (distanceToPoint != 0)
        {

            double bearingToDestination = Utils.findBearing(currentPointInPath, destination);
            bearingToDestination = this.adjustDirectionForObstacleAvoidance(bearingToDestination, speed);
            Point pointToAdd = Utils.getPointAtDistanceInDirection(currentPointInPath, bearingToDestination, speed);

            //if pointToAdd matches ANY point already in the path, do not use it. Add a random movement? 
            previousPointInPath = currentPointInPath;
            currentPointInPath = pointToAdd;
            pointsAlongPath.add(pointToAdd);
            distanceToPoint = currentPointInPath.distance(destination);
        }
        return pointsAlongPath;
    }

    public int getReach()
    {
        return reach;
    }

    public void addBonus(BaseBonus bonus)
    {
        this.bonuses.add(bonus);
    }

    public void clearExpiredBonuses()
    {
        ArrayList<BaseBonus> bonusesToClear = new ArrayList<>();
        for (BaseBonus bonus : this.bonuses)
        {
            if (!bonus.stillActive())
            {
                //de-apply the bonus.
                this.deApplyBonus(bonus);
                bonusesToClear.add(bonus);
            }
        }
        this.bonuses.removeAll(bonusesToClear);
    }

    public void applyBonus(BaseBonus bonus)
    {
        switch (bonus.getAttributeAffected())
        {
            case DEFENSE:
                this.defense += bonus.getMagnitude();
                break;
            case REACH:
                this.reach += bonus.getMagnitude();
                break;
            case SPEED:
                this.speed += bonus.getMagnitude();
                break;
            case STRENGTH:
                this.strength += bonus.getMagnitude();
                break;

        }
    }

    public void deApplyBonus(BaseBonus bonus)
    {
        switch (bonus.getAttributeAffected())
        {
            case DEFENSE:
                this.defense -= bonus.getMagnitude();
                break;
            case REACH:
                this.reach -= bonus.getMagnitude();
                break;
            case SPEED:
                this.speed -= bonus.getMagnitude();
                break;
            case STRENGTH:
                this.strength -= bonus.getMagnitude();
                break;

        }
    }

    public void applyBonuses()
    {
        this.clearExpiredBonuses();
        for (BaseBonus bonus : this.bonuses)
        {
            if (bonus.stillActive() && bonus.bonusApplies(this.getClass()))
            {
                this.applyBonus(bonus);
                bonus.decrementDuration();
            }
        }

    }
    
    public void incrementFatigue(int fatigueAdded)
    {
        this.fatigue+= fatigueAdded;
    }
    
    public void rest(int fatigueRelieved)
    {
        this.fatigue -= fatigueRelieved;
    }

    public abstract void evaluateBonuses();
}
