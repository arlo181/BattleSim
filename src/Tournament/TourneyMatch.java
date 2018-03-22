/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tournament;

import battlesimulator.BattleResults;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Arlo
 */
public class TourneyMatch
{

    Map<String, Integer> matchesWon;
    ArrayList<TourneyEntrant> contestants;
    ArrayList<TourneyMatch> pendingContestants;
    String MatchupName;
    TourneyMatch nextMatch;
    ArrayList<BattleResults> results;
    TourneyEntrant winner;
    int bestOf;
    boolean victorDetermined;
    int winsNeededToMoveOn;
    int numberOfContestantsRequired;
    String battlefieldString;
    Color[] alternativeColors;
    boolean isRunning;

    public TourneyMatch(int bestOf, int numContestantsInMatch, String fieldString)
    {
        this.bestOf = bestOf;
        this.isRunning = false;
        this.results = new ArrayList<>();
        this.victorDetermined = false;
        this.winsNeededToMoveOn = this.bestOf / 2 + 1;
        this.numberOfContestantsRequired = numContestantsInMatch;
        this.battlefieldString = fieldString;
        this.matchesWon = new HashMap<>();
        this.contestants = new ArrayList<>();
        this.pendingContestants = new ArrayList<>();
        this.alternativeColors = new Color[5];
        this.alternativeColors[0] = Color.BLUE;
        this.alternativeColors[1] = Color.RED;
        this.alternativeColors[2] = Color.GREEN;
        this.alternativeColors[3] = Color.MAGENTA;
        this.alternativeColors[4] = Color.ORANGE;

    }

    public ArrayList<TourneyEntrant> presentMatchup()
    {
        this.rotateEntrants();
        return this.contestants;
    }

    public boolean isVictorDetermined()
    {
        return victorDetermined;
    }

    public ArrayList<TourneyEntrant> getContestants()
    {
        return contestants;
    }

    public String getBattlefieldString()
    {
        return battlefieldString;
    }

    public void rotateEntrants()
    {
        if (!this.contestants.isEmpty())
        {
            TourneyEntrant lastContestant = this.contestants.get(this.contestants.size() - 1);
            this.contestants.remove(lastContestant);
            this.contestants.add(0, lastContestant);

        }
    }

    public TourneyEntrant getWinner()
    {
        return winner;
    }

    public void setNextMatch(TourneyMatch nextMatch)
    {
        this.nextMatch = nextMatch;
    }

    public void addContestant(TourneyEntrant entrant)
    {
        this.contestants.add(entrant);
    }

    public void addPendingContestant(TourneyMatch entrant)
    {
        this.pendingContestants.add(entrant);
    }

    public void addResults(BattleResults newResults)
    {
        this.results.add(newResults);

        this.matchesWon.merge(newResults.getWinner(), 1, (oldValue, one) -> oldValue + one);

        for (Map.Entry<String, Integer> entry : this.matchesWon.entrySet())
        {
            if (entry.getValue() >= this.winsNeededToMoveOn)
            {
                for (TourneyEntrant entrant : this.contestants)
                {
                    if (entrant.getEntrantName().equals(entry.getKey()))
                    {
                        this.winner = entrant;
                        break;
                    }
                }
                this.victorDetermined = true;
                break;
            }
        }
    }

    public void victorMovesOn()
    {
        if (this.nextMatch != null)
        {
            this.nextMatch.addContestant(winner);
        } else
        {
            System.out.println("!!!!!!!!!!!!CHAMPION IS: " + this.winner.getEntrantName());
        }
    }

    public boolean matchupReady()
    {
        boolean matchupReady = this.contestants.size() >= this.numberOfContestantsRequired;

        for (TourneyEntrant entrant : this.contestants)
        {
            if (PendingEntrant.class.isAssignableFrom(entrant.getClass()))
            {
                matchupReady = false;
            }
        }

        return matchupReady;
    }
    
    public void setRunning(boolean running)
    {
        this.isRunning = running;
    }

    public int totalNumContestants()
    {
        return this.contestants.size() + this.pendingContestants.size();
    }

    @Override
    public String toString()
    {
        String string = "";
        
        if(this.isRunning && !this.victorDetermined)
        {
            string = "Running... ";
        }

        if (!this.contestants.isEmpty())
        {
            String contestantName = this.contestants.get(0).getEntrantName();
           
            int numWins = 0;
            String winsIndicator = "";
            if (this.matchesWon.get(contestantName) != null)
            {
                numWins = this.matchesWon.get(contestantName);
            }
            winsIndicator = "(" + numWins + ")";
            
             if (this.victorDetermined && contestantName.equals(winner.getEntrantName()))
            {
                winsIndicator = "[W]" + winsIndicator;
            }
            string += winsIndicator + " " + contestantName;

            for (int i = 1; i < this.contestants.size(); i++)
            {
                contestantName = this.contestants.get(i).getEntrantName();
               
                numWins = 0;
                if (this.matchesWon.get(contestantName) != null)
                {
                    numWins = this.matchesWon.get(contestantName);
                }
                winsIndicator = "(" + numWins + ")";
                 if (this.victorDetermined && contestantName.equals(winner.getEntrantName()))
                {
                    winsIndicator = "[W]" + winsIndicator;
                }
                string += " -- VS -- " +winsIndicator + " " + contestantName;

                
            }
        }

        return string;

    }

}
