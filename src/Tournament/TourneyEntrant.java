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
public class TourneyEntrant
{
    String entrantName;
    String armyString;
    Color preferredColor;
    int armyScore;

    public TourneyEntrant(String entrantName, String armyString, Color preferredColor)
    {
        this.entrantName = entrantName;
        this.armyString = armyString;
        this.preferredColor = preferredColor;
    }

    public String getEntrantName()
    {
        return entrantName;
    }

    public String getArmyString()
    {
        return armyString;
    }

    
    
    
}
