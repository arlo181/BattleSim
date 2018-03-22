/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tournament;

import java.awt.Color;

/**
 *
 * @author Arlo
 */
public class PendingEntrant extends TourneyEntrant
{
    TourneyMatch matchupDetermines;
    
    public PendingEntrant(String entrantName, String armyString, Color preferredColor)
    {
        this(entrantName, armyString, preferredColor, null);
    }
    
        public PendingEntrant(String entrantName, String armyString, Color preferredColor, TourneyMatch matchupDetermines)
    {
        super(entrantName, armyString, preferredColor);
        this.matchupDetermines = matchupDetermines;
    }

    public TourneyMatch getMatchupDetermines()
    {
        return matchupDetermines;
    }
        
        
    
}
