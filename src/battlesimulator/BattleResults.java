/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import battlesimulator.Military.Army;
import battlesimulator.Military.Army.ArmyStats;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class BattleResults
{
    String winner;
    ArrayList<ArmyStats> armyStats;
    
    public BattleResults(String winningArmy, ArrayList<ArmyStats> resultsStats)
    {
        this.winner = winningArmy;
        this.armyStats = resultsStats;
    }

    public String getWinner()
    {
        return winner;
    }

    public ArrayList<ArmyStats> getArmyStats()
    {
        return armyStats;
    }
    
    
}
