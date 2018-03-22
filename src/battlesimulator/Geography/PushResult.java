/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import battlesimulator.BattlefieldObject;
import java.awt.Point;



/**
 *
 * @author Arlo
 */
public class PushResult {
    public enum PushOutcomes
    {
        PUSHED,
        RESISTED,
        TRAMPLED,
        REINFORCED
    };
    
    int pushPowerRemaining;
    Point pushedTo;
    PushOutcomes outcome;
    BattlefieldObject pushed;
    
    public PushResult(BattlefieldObject pushed, int pushPowerRemaining, Point pushedTo, PushOutcomes outcome)
    {
        this.pushed = pushed;
        this.pushPowerRemaining = pushPowerRemaining;
        this.pushedTo = pushedTo;
        this.outcome = outcome;
    }

    public int getPushPowerRemaining() {
        return pushPowerRemaining;
    }

    public Point getPushedTo() {
        return pushedTo;
    }

    public PushOutcomes getOutcome() {
        return outcome;
    }

    public BattlefieldObject getPushed() {
        return pushed;
    }

    public void setOutcome(PushOutcomes outcome) {
        this.outcome = outcome;
    }
    
    
    
}
