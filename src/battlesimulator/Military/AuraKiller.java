/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.Actions.Action;
import battlesimulator.Actions.MassMurder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Arlo
 */
public class AuraKiller extends Soldier {

    public AuraKiller(String allegiance, String name) {
        super(allegiance, name);
        this.awarenessRadius = 50;
        this.initiative = 200;
    }
    
    public AuraKiller(String allegiance, String name, Point centerPoint) {
        super(allegiance, name, centerPoint);
        this.awarenessRadius = 50;
        this.initiative = 200;
    }

    @Override
    public Action tick(int tickElapsed) {
        return new MassMurder(0, this, this.getKnownObjects());
    }

    @Override
    public AuraKillerState generateState(int numTicks) {

        AuraKillerState state = new AuraKillerState(this.centerPoint,
                this.personalSpaceRadius,
                this.facingDirection,
                this.roundsDead,
                this.state,
                this.color,
                this.isActive(),
                this.awarenessRadius);

        //this.objectStates.put(numTicks, state);
      return state;
    }

    public class AuraKillerState extends SoldierState {

int deathRadius;
        public AuraKillerState(Point centerPoint, short personalSpaceRadius, double facingDirection, short roundsDead, SoldierConditions condition, Color color, boolean isActive, short deathRadius) {

            super(centerPoint, personalSpaceRadius, facingDirection, roundsDead, condition, color, isActive, deathRadius,0, 0);
            this.deathRadius = deathRadius;
        }

        @Override
        public void paintObject(Graphics2D g) {
           

                    Ellipse2D elipse = new Ellipse2D.Double(centerPoint.x- this.deathRadius, centerPoint.y- this.deathRadius,
                            2*this.deathRadius, 2*this.deathRadius);
                    g.setColor(Color.BLACK);
                    g.draw(elipse);
                     super.paintObject(g);
                  
                
            
        }
    }
}
