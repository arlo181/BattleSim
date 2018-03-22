/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Geography.PushResult;
import battlesimulator.Military.Unit;
import battlesimulator.Utils;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class MoveAction extends Action
{

    static int randomDoubleIndex = 0;
    static double[] randomDoubles;

    static
    {
        MoveAction.randomDoubles = new double[250];
        for (int index = 0; index < MoveAction.randomDoubles.length; index++)
        {
            MoveAction.randomDoubles[index] = Math.random();
        }
    }
    int speedPixels;
    double directionCardinal;
    MovementModes mode;

    public MoveAction(BattlefieldObject battlefieldObject, int speedPixels, double directionCardinal, MovementModes mode)
    {
        super(0, battlefieldObject);
        this.speedPixels = speedPixels;
        this.directionCardinal = Utils.adjustAngleValue(directionCardinal);
        this.mode = mode;
    }

    public int getSpeedPixels()
    {
        return this.speedPixels;
    }

    public double getDirectionCardinal()
    {
        return directionCardinal;
    }

    public MovementModes getMode()
    {
        return this.mode;
    }

    public static synchronized double nextRandomDouble()
    {
        double random = MoveAction.randomDoubles[MoveAction.randomDoubleIndex];
        MoveAction.randomDoubleIndex++;

        if (MoveAction.randomDoubleIndex >= MoveAction.randomDoubles.length)
        {
            MoveAction.randomDoubleIndex = 0;
        }
        return random;
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {

        int modifiedSpeed;
        double modifiedDirection = this.directionCardinal;
        int attemptedSpeed = this.speedPixels;
        double attemptedDirection = this.directionCardinal;

        Point origin = this.battlefieldObject.getCenterPoint();

        Point destination = Utils.getPointAtDistanceInDirection(origin, this.directionCardinal, this.speedPixels);
        boolean validDirectionSpeed;
        ArrayList<BattlefieldObject> potentialObstacles = this.battlefieldObject.getObjectsWithin(this.speedPixels);
        BattlefieldObject nearestObstacle = this.battlefieldObject.nearestObstacleInDirection(this.directionCardinal, this.speedPixels, potentialObstacles);

        BattlefieldObject conflict = battlefield.getFirstConflict(this.battlefieldObject, destination, this.battlefieldObject.getPersonalSpaceRadius());

        //if there's no "nearest obstacle" but there's a conflict with the final position:
        if (nearestObstacle == null && conflict != null)
        {
            nearestObstacle = conflict;
        }

        if (nearestObstacle != null)
        {

            switch (this.mode)
            {
                case AVOID:
                    do
                    {
                        validDirectionSpeed = true;
                        if (attemptedSpeed == this.speedPixels)
                        {
                            attemptedDirection = this.battlefieldObject.adjustDirectionForObstacleAvoidance(attemptedDirection, attemptedSpeed, potentialObstacles);
                        } else
                        {
                            attemptedDirection = this.battlefieldObject.adjustDirectionForObstacleAvoidance(attemptedDirection, attemptedSpeed);
                        }
                        if (attemptedDirection == -1)
                        {
                            attemptedSpeed--;
                            attemptedDirection = this.directionCardinal;
                            validDirectionSpeed = false;
                        } else
                        {
                            modifiedDirection = attemptedDirection;
                        }
                    } while ((validDirectionSpeed == false) && (attemptedSpeed >= 0));//while we don't have a valid direction
                    modifiedSpeed = attemptedSpeed;

                    destination = Utils.getPointAtDistanceInDirection(origin, modifiedDirection, modifiedSpeed);

                    //if no movement at all or at a point in recent history...  take a small step in a random direction?
                    if (modifiedSpeed == 0 || (origin.equals(destination)) || this.battlefieldObject.isPointInHistory(1.0, destination))
                    {
                        double randomDirection = nextRandomDouble() * 360;
                        double randomSpeed = nextRandomDouble() * this.battlefieldObject.getSpeed() + 1;

                        destination = Utils.getPointAtDistanceInDirection(origin, randomDirection, randomSpeed);
                    }
                    //System.out.println(this.battlefieldObject.thisId + " -- attempted to move from: " + origin + " To: " + destination);
                    this.battlefieldObject.addPointToHistory(destination);

                    break;
                case PUSH:
                    int distanceToNearestObstacle = (int) Math.floor(this.battlefieldObject.distanceTo(nearestObstacle));
                    if (distanceToNearestObstacle >= 1)
                    {
                        destination = Utils.getPointAtDistanceInDirection(origin, this.directionCardinal, distanceToNearestObstacle);
                        if (battlefield.isConflict(this.battlefieldObject, destination, this.battlefieldObject.getPersonalSpaceRadius()))
                        {
                            destination = this.battlefieldObject.getCenterPoint();
                        }
                    } else
                    {
                        destination = this.battlefieldObject.getCenterPoint();
                    }
                    double speedRemaining = this.speedPixels - distanceToNearestObstacle;

                    if (speedRemaining >= 1)
                    {
                        this.processPush(this.battlefieldObject, speedRemaining, destination, nearestObstacle, this.directionCardinal, battlefield);
                        destination = this.battlefieldObject.getCenterPoint();
                    }
                    break;
                case SWARM:
                    distanceToNearestObstacle = (int) Math.floor(this.battlefieldObject.distanceTo(nearestObstacle));
                    if (distanceToNearestObstacle > 0)
                    {
                        destination = Utils.getPointAtDistanceInDirection(origin, this.directionCardinal, distanceToNearestObstacle);
                    } else
                    {
                        modifiedDirection = Utils.findBearing(origin, nearestObstacle.getCenterPoint());
                        //try to "shift" in another direction
                        boolean validShift = false;
                        double adjustmentAngle = 90;
                        int speed = this.battlefieldObject.getSpeed();
                        while (!validShift && speed > 0)
                        {
                            int directionFactor = (this.battlefieldObject.getInitiative() % 2 == 0) ? 1 : -1;
                            modifiedDirection = modifiedDirection + (adjustmentAngle * directionFactor);
                            destination = Utils.getPointAtDistanceInDirection(origin, modifiedDirection, this.battlefieldObject.getSpeed());
                            validShift = !battlefield.isConflict(this.battlefieldObject, destination, this.battlefieldObject.getPersonalSpaceRadius());
                            speed--;
                        }
                        if (!validShift)
                        {
                            destination = origin;
                        }

                    }
                    break;
                case WAIT:
                    destination = origin; //don't move this tick
                    break;

                case DELAYED:

                    break;
            }//switch
        } else //no obstacle nearby
        {
            destination = Utils.getPointAtDistanceInDirection(origin, this.directionCardinal, this.speedPixels);

        }

        //final check.  if the move is a conflict, don't do it.
        boolean isConflict = battlefield.isConflict(this.battlefieldObject, destination, this.battlefieldObject.getPersonalSpaceRadius());
        if (!isConflict)
        {
            battlefield.moveObjectInDirection(this.battlefieldObject, destination, true);
        } 
    }

    public boolean processPush(BattlefieldObject pusher, double speedRemaining, Point pushOrigin, BattlefieldObject nearestPush, double pushDirection, Battlefield battlefield)
    {
        int rootPushPowerRemaining = pusher.getPushPower();
        Point pusherCurrentLocation = pushOrigin;
        boolean resisted = false;

        while ((speedRemaining > 0) && (rootPushPowerRemaining > 0) && pusher.isActive() && !resisted)
        {
            //System.out.print("push power: " + rootPushPowerRemaining + "--");
            //pusher moves forward a step.  
            pusherCurrentLocation = Utils.getPointAtDistanceInDirection(pusherCurrentLocation, pushDirection, 1);
            speedRemaining--;

            ArrayList<PushResult> results = new ArrayList<>();
            //If conflict, add conflict(s) to list of pushed
            ArrayList<BattlefieldObject> pushed = battlefield.getConflicts(pusher, pusherCurrentLocation, pusher.getPersonalSpaceRadius());
            ArrayList<BattlefieldObject> allAffected = new ArrayList<>();
            allAffected.addAll(pushed);

            for (BattlefieldObject pushedObj : pushed)
            {
                //for each conflict, push.  If object is pushed, add pushed to list of pushed. update push power remaining

                PushResult result = pushedObj.resolvePush(pusher, rootPushPowerRemaining, pusherCurrentLocation, pushDirection);
                if (!hasAlreadyBeenPushed(results, result))
                {
                    results.add(result);
                    rootPushPowerRemaining = this.processSecondaryPush(pushedObj, result, results, pushDirection, battlefield);
                } else
                {
                    resisted = true;
                }

            }//for each object pushed

            //if our push wasn't resisted, process the results of the proposed push
            if (rootPushPowerRemaining >= 0)
            {
                //that push is done... process results...
                //verify that no one resisted.
                for (int pushIndex = results.size() - 1; pushIndex >= 0; pushIndex--)
                {
                    PushResult result = results.get(pushIndex);
                    if (!resisted)
                    {
                        switch (result.getOutcome())
                        {
                            case PUSHED:
                                battlefield.moveObjectInDirection(result.getPushed(), result.getPushedTo(), false);
                                break;
                            case RESISTED:
                                resisted = true;
                                break;
                            case TRAMPLED:
                                result.getPushed().markTrampled();
                                break;
                            case REINFORCED:
                                result.getPushed().reinforcePush(result.getPushPowerRemaining());
                                break;
                        }//switch
                    }
                }//for each result
                if (!resisted)
                {
                    battlefield.moveObjectInDirection(pusher, pusherCurrentLocation, true);
                }
                results.clear();
                pushed.clear();
            }//if didn't run out of power
            else
            {
                resisted = true;
            }
            //System.out.print("push power remaining: " + rootPushPowerRemaining + "--");

        }//while speed/power remaining

        return resisted;
    }

    boolean hasAlreadyBeenPushed(ArrayList<PushResult> results, PushResult newResult)
    {
        boolean hasAlreadyBeenPushed = false;
        for (PushResult previousResult : results)
        {
            if (previousResult.getPushed().equals(newResult.getPushed()))
            {
                hasAlreadyBeenPushed = true;
                break;
            }
        }

        return hasAlreadyBeenPushed;
    }

    public int processSecondaryPush(BattlefieldObject pusher, PushResult previousResult, ArrayList<PushResult> results, double pushDirection, Battlefield battlefield)
    {

        Point pusherCurrentLocation = previousResult.getPushedTo();
        int rootPushPowerRemaining = previousResult.getPushPowerRemaining();
        if (previousResult.getOutcome() != PushResult.PushOutcomes.RESISTED)
        { //no need to do all this if resisted
            //If conflict, add conflict(s) to list of pushed
            ArrayList<BattlefieldObject> pushed = battlefield.getConflicts(pusher, pusherCurrentLocation, pusher.getPersonalSpaceRadius());
            for (BattlefieldObject pushedObj : pushed)
            {
                //for each conflict, push.  If object is pushed, add push result to list and generate follow-up push results

                PushResult result = pushedObj.resolvePush(pusher, rootPushPowerRemaining, pusherCurrentLocation, pushDirection);

                if (!hasAlreadyBeenPushed(results, result))
                {
                    results.add(result);

                    rootPushPowerRemaining = this.processSecondaryPush(pushedObj, result, results, pushDirection, battlefield);
                }

            }
        } else
        {
            rootPushPowerRemaining = 0;
        }
        return rootPushPowerRemaining;
    }

}
