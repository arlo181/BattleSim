/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import Objects.FireBall;
import battlesimulator.Actions.Action;
import java.awt.Point;

/**
 *
 * @author Arlo
 */
public class Mage extends Archer {

    public static final short MAGE_CLASS_SCORE=10;
FireBall[] quiver = new FireBall[1];

    public Mage(String allegiance, String name) {
        this(allegiance, name, new Point(0, 0));
        
    }

    public Mage(String allegiance, String name, Point centerPoint) {
        super(allegiance, name, centerPoint);
        
            rateOfFire = 5; //how many rounds it takes to fire an arrow.
    minRange = 5;
    maxRange = 250;
    accuracy = .95;
    FireBall quiver[];
    arrowIndex = 0;
    QUIVER_SIZE = 1;
    IMPACT_RADIUS = 15;
   ARROW_POWER = 50;
   ARROW_SPEED = 25;
        this.strength = 0;
        this.awarenessRadius = 250;
        this.threatenedRadius = 250;
        quiver = new FireBall[QUIVER_SIZE];
        
        for(int quiverIndex = 0; quiverIndex < quiver.length; quiverIndex++)
        {
             FireBall arrow = new FireBall(centerPoint, this.accuracy, false, ARROW_POWER, ARROW_SPEED, IMPACT_RADIUS);
             this.quiver[quiverIndex] = arrow;
        }
    }
    
        @Override
    public int getClassScore()
    {
        return Mage.MAGE_CLASS_SCORE;
    }
    
    @Override
    public FireBall getNextArrow()
{
    FireBall fireball = (FireBall) this.quiver[this.arrowIndex];
    arrowIndex++;
    if(arrowIndex >= this.quiver.length)
    {
        arrowIndex = 0;
    }
    return fireball;
}
    @Override
    public Action tick(int ticksElapsed) {

        return super.tick(ticksElapsed);
    }

}
