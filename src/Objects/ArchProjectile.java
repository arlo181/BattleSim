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
public class ArchProjectile extends Projectile {

    Point targetLocation;
    Point origin;
    boolean impact;
    boolean destroyed;

    public ArchProjectile(Point centerPoint,
            double accuracy,
            boolean rolls,
            short power,
            short speed,
            short impactRadius) {
        super(centerPoint, accuracy, rolls, power, speed);

        this.personalSpaceRadius = impactRadius;
        this.impactRadius = impactRadius;
        this.staysOnField = false;
        this.destroyed = false;
        //System.out.println("Point 1: " + centerPoint + " to point: " + this.targetLocation + "direction: " + this.directionOfFlight);
    }

    @Override
    public void receiveAction(Action action) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void markDestroyed(boolean destroyed)
    {
        this.destroyed = destroyed;
    }

    public void setTargetLocation(Point targetLocation) {
        Random rand = new Random();
        double chanceToHit = rand.nextDouble();
        if (chanceToHit > this.accuracy) {
            //miss -- calculate error radius
            double rangeOFShot = centerPoint.distance(targetLocation);
            double errorRadius = rangeOFShot * (1 - accuracy);
            double randomDirection = rand.nextDouble() * 360;
            this.targetLocation = Utils.getPointAtDistanceInDirection(targetLocation, randomDirection, errorRadius);
        } else {
            this.targetLocation = targetLocation;
        }

        this.directionOfFlight = Utils.findBearing(this.centerPoint, this.targetLocation);
    }

    @Override
    public Action tick(int tickElapsed) {
        //one action for move closer
        //one action for striking.
        double distanceToTarget = this.centerPoint.distance(this.targetLocation);
        Action action;
        if (distanceToTarget > this.speed) {
            action = new ArchProjectileMove(0, this);
        } else {
            action = new ProjectileStrike(0, this, targetLocation, this.impactRadius);
        }
        return action;
    }

    public Point getTargetLocation() {
        return targetLocation;
    }

    @Override
    public ArchProjectileState generateState(int numTicks) {

        ArchProjectileState state = new ArchProjectileState(centerPoint,
                this.impact,
                rolls,
                directionOfFlight,
                this.ticksStationary,
                this.impactRadius);
        if (this.impact && !this.staysOnField) {
            this.ticksStationary++;
        }
        //impact only lasts one tick.
        if (this.impact && this.ticksStationary > 2) {
            this.impact = false;
        }
        return state;
    }

    @Override
    public void setOutOfBounds() {
//        throw new UnsupportedOperationException("Not supported yet.");
        this.land();
    }

    @Override
    public boolean isActive() {
        return super.isActive() || (this.impact && (this.ticksStationary < 1 || this.destroyed));
    }

    @Override
    public void markKilled() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CombatResult resolveCombat() {
        return new CombatResult(this, CombatResult.CombatOutcome.DEFENDED, this);
    }

    @Override
    public void receiveWound() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void land() {
        super.land();
        this.impact = true;
    }

    @Override
    public void evaluateBonuses() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void strikeTarget(BattlefieldObject struckObject) {
        
        if (this.getPower() > struckObject.getDefense()) {
            struckObject.markKilled();
            //System.out.println("KILLED!!!!!!!!!!!");
        } else {
            struckObject.receiveWound();
            struckObject.incrementFatigue(3);
        }

    }

    public void setAccuracy(double accuracy)
    {
        this.accuracy = accuracy;
    }
    
    

    public class ArchProjectileState extends ObjectState {

        Point centerPoint;
        boolean impact;
        boolean rolling;
        short ticksStationary;
        short impactRadius;
        double directionOfTravel;

        public ArchProjectileState(Point centerPoint,
                boolean impact,
                boolean rolling,
                double directionOfTravel,
                short ticksStationary,
                short impactRadius)//TODO: add in origin and destination to draw an arch-like pattern
        {
            this.centerPoint = new Point(centerPoint.x, centerPoint.y);
            this.impact = impact;
            this.rolling = rolling;
            this.directionOfTravel = directionOfTravel;
            this.ticksStationary = ticksStationary;
            this.impactRadius = impactRadius;
        }

        @Override
        public void paintObject(Graphics2D g) {
            if (this.ticksStationary < 5) {
                if (!this.impact) {
                    Point facingPoint = Utils.getPointAtDistanceInDirection(centerPoint,
                            this.directionOfTravel,
                            2);
                    Line2D.Double facingLine = new Line2D.Double(centerPoint, facingPoint);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(Color.BLACK);
                    g.draw(facingLine);
                } else {
                    Ellipse2D elipse = new Ellipse2D.Double(centerPoint.x, centerPoint.y,
                            this.impactRadius, this.impactRadius);
                    g.setColor(Color.BLACK);
                    g.draw(elipse);
                    g.fill(elipse);
                }
            }
        }
    }
}
