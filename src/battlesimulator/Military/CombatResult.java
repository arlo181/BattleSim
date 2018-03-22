/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.BattlefieldObject;

/**
 *
 * @author Arlo
 */
public class CombatResult {
    
    public enum CombatOutcome
    {
        KILLED,
        WOUNDED,
        PUSHED,
        DEFENDED;   
    }
    
    CombatOutcome outcome;
    BattlefieldObject subject;
    BattlefieldObject inflictor;
    
    public CombatResult(BattlefieldObject subject, CombatOutcome outcome, BattlefieldObject inflictor)
    {
        this.outcome = outcome;
        this.subject = subject;
        this.inflictor = inflictor;
        
        if(this.outcome == CombatOutcome.KILLED)
        {
            this.subject.markKilled();
        }
    }

    public CombatOutcome getOutcome() {
        return outcome;
    }

    public BattlefieldObject getSubject() {
        return subject;
    }

    public BattlefieldObject getInflictor() {
        return inflictor;
    }
    
    
}
