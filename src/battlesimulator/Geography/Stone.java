/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import battlesimulator.Actions.Action;
import battlesimulator.Actions.DoNothingAction;
import battlesimulator.Actions.MoveAction;
import battlesimulator.BattlefieldObject;
import battlesimulator.Military.CombatResult;
import battlesimulator.ObjectState;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Arlo
 */
public class Stone extends BattlefieldObject {

    public Stone(Point centerpoint)
    {
        super(centerpoint);
        this.personalSpaceRadius=4;
        this.facingDirection = 55;
    }
    @Override
    public void receiveAction(Action action) {
    }

    @Override
    public Action tick(int ticksElapsed) {
        return new DoNothingAction(0, this);
    }

    @Override
    public boolean hasAllegiance() {
        return false;
    }

    @Override
    public String getAllegience() {
        return "";
    }

   

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setOutOfBounds() {
        
    }

    @Override
    public void markKilled() {
       
    }
    
    @Override
    public PushResult resolvePush(BattlefieldObject pusher, int remainingPushPower, Point pushOrigin, double pushDirection) {
        PushResult result = super.resolvePush(pusher, remainingPushPower, pushOrigin, pushDirection);
        
        if(result.getOutcome() == PushResult.PushOutcomes.TRAMPLED)
        {
            result.setOutcome(PushResult.PushOutcomes.PUSHED);
        }
        return result;
    }

    @Override
    public CombatResult resolveCombat() {
        return new CombatResult(this, CombatResult.CombatOutcome.DEFENDED, null);
    }

    @Override
    public void receiveWound() {
        
    }

    @Override
    public void evaluateBonuses() {
        
    }

    @Override
    public int getClassScore()
    {
        return 0;
    }

    public class StoneState extends ObjectState
    {
int centerX;
int centerY;
int personalSpaceRadius;

StoneState(Point centerPoint, int personalSpaceRadius)
{
    this.centerX = centerPoint.x;
    this.centerY= centerPoint.y;
    this.personalSpaceRadius = personalSpaceRadius;
}

 @Override
    public void paintObject(Graphics2D g) {
        int x = this.centerX - this.personalSpaceRadius;
        int y = this.centerY - this.personalSpaceRadius;
        Ellipse2D elipse = new Ellipse2D.Double(x, y,
                2 * this.personalSpaceRadius,
                2 * this.personalSpaceRadius);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.BLACK);
        g.draw(elipse);
        g.fill(elipse);
    }
        
    }
    @Override
    public ObjectState generateState(int numTicks) {
        return new StoneState(this.centerPoint, this.personalSpaceRadius);
    }


}
