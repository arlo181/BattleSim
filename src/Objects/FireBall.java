/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import battlesimulator.Actions.Action;
import battlesimulator.Actions.ArchProjectileMove;
import battlesimulator.Actions.ProjectileStrike;
import battlesimulator.BattlefieldObject;
import battlesimulator.Utils;
import battlesimulator.Military.CombatResult;
import battlesimulator.ObjectState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Random;

/**
 *
 * @author Arlo
 */
public class FireBall extends ArchProjectile
{

    boolean availableToFire = true;
    static int EXPLOSION_ROUNDS = 3;
    int roundsExploded;

    public FireBall(Point centerPoint,
            double accuracy,
            boolean rolls,
            short power,
            short speed,
            short impactRadius)
    {
        super(centerPoint, accuracy, rolls, power, speed, impactRadius);
        this.roundsExploded = 0;
        this.personalSpaceRadius = impactRadius;
        this.impactRadius = impactRadius;

        //System.out.println("Point 1: " + centerPoint + " to point: " + this.targetLocation + "direction: " + this.directionOfFlight);
    }

    @Override
    public void receiveAction(Action action)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTargetLocation(Point targetLocation)
    {

        double rangeOFShot = centerPoint.distance(targetLocation);
        double errorRadius = rangeOFShot * (1 - accuracy);
        Random rand = new Random();
        double randomDirection = rand.nextDouble() * 360;
        this.targetLocation = Utils.getPointAtDistanceInDirection(targetLocation, randomDirection, errorRadius);
        this.directionOfFlight = Utils.findBearing(centerPoint, this.targetLocation);
    }

    @Override
    public Action tick(int tickElapsed)
    {
        //one action for move closer
        //one action for striking.
        double distanceToTarget = this.centerPoint.distance(this.targetLocation);
        Action action;
        if (this.impact && this.roundsExploded >= FireBall.EXPLOSION_ROUNDS)
        {
            this.availableToFire = true;
            
            action = null;
        } else if (distanceToTarget > this.speed)
        {
            action = new ArchProjectileMove(0, this);
            this.ticksStationary = 0;
            this.availableToFire = false;
            roundsExploded = 0;
        } else
        {
            action = new ProjectileStrike(0, this, targetLocation, this.impactRadius);
            this.ticksStationary = 0;
            roundsExploded++;
            this.availableToFire = false;
        }
        return action;
    }

    public Point getTargetLocation()
    {
        return targetLocation;
    }

    @Override
    public FireBallState generateState(int numTicks)
    {

        FireBallState state = new FireBallState(centerPoint,
                this.impact,
                rolls,
                directionOfFlight,
                this.ticksStationary,
                this.availableToFire,
                this.impactRadius);
        if (this.impact && !this.staysOnField)
        {
            this.ticksStationary++;
        }
        //impact only lasts a few ticks.
        if (this.impact && this.ticksStationary > 5)
        {
            this.impact = false;
        }
        return state;
    }

    @Override
    public void setOutOfBounds()
    {
//        throw new UnsupportedOperationException("Not supported yet.");
        this.land();
    }

    @Override
    public boolean isActive()
    {
        return super.isActive() || (this.impact && this.ticksStationary < 3);
       
    }

    @Override
    public void markKilled()
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CombatResult resolveCombat()
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        return new CombatResult(this, CombatResult.CombatOutcome.DEFENDED, this);
    }

    @Override
    public void receiveWound()
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void land()
    {
        super.land();
        this.impact = true;
        this.ticksStationary = 0;
    }

    @Override
    public void evaluateBonuses()
    {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void strikeTarget(BattlefieldObject struckObject)
    {

        if (this.getPower() > struckObject.getDefense())
        {
            struckObject.markKilled();
            //System.out.println("KILLED!!!!!!!!!!!");
        } else
        {
            struckObject.receiveWound();
        }

    }

    public class FireBallState extends ArchProjectileState
    {
        boolean availableToFire;
        public FireBallState(Point centerPoint,
                boolean impact,
                boolean rolling,
                double directionOfTravel,
                short ticksStationary,
                boolean availableToFire,
                short impactRadius)//TODO: add in origin and destination to draw an arch-like pattern
        {
            super(centerPoint, impact, rolling, directionOfTravel, ticksStationary, impactRadius);
            this.availableToFire = availableToFire;
        }

        @Override
        public void paintObject(Graphics2D g)
        {
            if (!this.availableToFire)
            {
                if (!this.impact)
                {
                    Point facingPoint = Utils.getPointAtDistanceInDirection(centerPoint,
                            this.directionOfTravel,
                            2);
                    Line2D.Double facingLine = new Line2D.Double(centerPoint, facingPoint);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(Color.RED);
                    g.draw(facingLine);
                } else
                {
                    Ellipse2D elipse = new Ellipse2D.Double(centerPoint.x, centerPoint.y,
                            this.impactRadius, this.impactRadius);
                    switch (ticksStationary)
                    {
                        case 0:

                            g.setColor(Color.YELLOW);
                            break;
                        case 1:
                        case 2:
                        case 3:

                            g.setColor(Color.ORANGE);
                            break;
                        case 4:
                        case 5:
                            g.setColor(Color.RED);
                            break;
                    }

                    g.draw(elipse);
                    g.fill(elipse);
                }
            }
        }
    }
}
