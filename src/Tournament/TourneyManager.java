/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tournament;

import SimBuilderTools.QuickArmyBuildParser;
import battlesimulator.BattleResults;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Army;
import battlesimulator.SimulationEngine;
import battlesimulator.SimulationSeed;
import java.awt.Color;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arlo
 */
public class TourneyManager
{

    ArrayList<TourneyEntrant> tournamentEntries;
    ArrayList<TourneyEntrant> allArmies;
    ArrayList<TourneyEntrant> validArmies;
    ArrayList<String> allBattlefields;
    final static String ARMIES_DIRECTORY = System.getProperty("user.dir") + "\\SavedData\\QuickStart\\Armies\\";
    final static String BATTLEFIELDS_DIRECTORY = System.getProperty("user.dir") + "\\SavedData\\QuickStart\\Battlefields\\";
    final static String BYE_ARMY_STRING = "BYE; Black; RushInStrategy;byeUnit:F1;";
    boolean showSims;
    boolean pausAfterSims;

    QuickArmyBuildParser buildParser;
    ArrayList<TourneyMatch> matches;
    TourneyEntrant byeEntry;

    int maxAllowableScore;
    int bestOf;

    public enum TournamentType
    {
        BRACKET,
        ROUND_ROBIN;
    }

    public TourneyManager(int maxScore, int bestOf, boolean showSims, boolean pauseAfterSims)
    {
        tournamentEntries = new ArrayList<>();
        allArmies = new ArrayList<>();
        allBattlefields = new ArrayList<>();
        this.validArmies = new ArrayList<>();
        buildParser = new QuickArmyBuildParser();
        maxAllowableScore = maxScore;
        this.loadAllArmyStrings();
        this.loadAllBattlefieldStrings();
        matches = new ArrayList<>();
        this.bestOf = bestOf;
        this.showSims = showSims;
        this.pausAfterSims = pauseAfterSims;

        byeEntry = new TourneyEntrant("BYE", BYE_ARMY_STRING, Color.GRAY);
    }

    public void startTournament()
    {
        String results = "";

        this.updateValidEntries();

        //for now, just use all valid
        for (TourneyEntrant entrant : this.validArmies)
        {
            this.tournamentEntries.add(entrant);
        }

        this.SetUpTournament(TournamentType.BRACKET, this.allBattlefields.get(0));

        for (TourneyMatch matchup : this.matches)
        {
            results += "\nmatchup: " + matchup.toString() + "\n";
            while (!matchup.isVictorDetermined())
            {
                try
                {
                matchup.addResults(runMatchup(matchup));
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }

            matchup.victorMovesOn();
            results += matchup.getWinner().getEntrantName() + " WINS!!!\n";
        }
        System.out.println(results);
    }

    public BattleResults runMatchup(TourneyMatch matchup)
    {
        matchup.setRunning(true);
//        String winnerByAlphabet = matchup.getContestants().get(0).getEntrantName();
//        //TODO:  actually run sim
//        for(TourneyEntrant entrant : matchup.getContestants())
//        {
//            if (entrant.getEntrantName().compareTo(winnerByAlphabet) < 0)
//            {
//                winnerByAlphabet = entrant.getEntrantName();
//            }
//        }
//        BattleResults results = new BattleResults(winnerByAlphabet, new ArrayList<ArmyStats>());

        System.out.println("\nRunning : " + matchup.toString());
        try
        {
            Thread.sleep(100);
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(TourneyManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.buildParser.clearArmies();
        for (TourneyEntrant entrant : matchup.presentMatchup())
        {
            System.out.println("adding Army: " + entrant.getEntrantName() + " to matchup");
            this.buildParser.addArmy(this.buildParser.buildArmyFromString(entrant.getArmyString()));
        }

        Battlefield field = this.buildParser.addBattlefieldFromString(matchup.getBattlefieldString());
        this.buildParser.assignStartingPositions();
        this.buildParser.ensureUniqueColors();
        Army[] armyArray = this.buildParser.getArmies().toArray(new Army[this.buildParser.getArmies().size()]);
        SimulationSeed seed = new SimulationSeed(field, armyArray);

        SimulationEngine engine = new SimulationEngine(seed, 35, false, !this.pausAfterSims, this.showSims, 100);
        System.out.println("starting simulation...");
        return engine.startSimulation();
    }

    public boolean isValidEntrant(TourneyEntrant entrant, int scoreLimit)
    {
        boolean valid = false;
        try
        {
            String armyString = entrant.getArmyString();
            Army army = buildParser.buildArmyFromString(armyString);
            int score = army.generateArmyStats(0).getArmyScore();
            valid = score <= maxAllowableScore;
        } catch (Exception ex)
        {
            ex.printStackTrace();
            System.out.println(entrant.getEntrantName() + " is not valid.");
        }

        return valid;
    }

    public void selectEntry(String entryName)
    {
        for (TourneyEntrant entry : this.validArmies)
        {
            if (entry.getEntrantName().equals(entryName))
            {
                this.tournamentEntries.add(entry);
            }
        }
    }

    //TODO need a data structure for battlefield name + battlefield String
    public void SetUpTournament(TournamentType type, String battlefieldName)
    {
        Collections.shuffle(this.tournamentEntries);
        matches.clear();

        switch (type)
        {
            case BRACKET:
            {
                SetUpBracketTourney(battlefieldName);
                break;
            }
            case ROUND_ROBIN:
            {
                break;
            }
        }

    }

    public void SetUpBracketTourney(String battlefield)
    {
//        if (this.tournamentEntries.size() % 2 != 0) //if odd, add a BYE
//        {
//            this.tournamentEntries.add(this.byeEntry);
//        }

        //first round
        //pair off into matches
        ArrayDeque<TourneyMatch> nonFinalMatchups = new ArrayDeque<>();

        //create n-1 matchups.  Each matchup sees someone eliminated
        for (int index = 0; (index < (this.tournamentEntries.size() - 1)); index++)
        {
            TourneyMatch tourneyMatch = new TourneyMatch(this.bestOf, 2, this.allBattlefields.get(0));
            this.matches.add(tourneyMatch);
        }

        int matchupIndex = 0;
        TourneyMatch currentMatch = this.matches.get(matchupIndex);

        //add each entrant to a match
        for (TourneyEntrant entrant : this.tournamentEntries)
        {
            if (!currentMatch.matchupReady())
            {
                currentMatch.addContestant(entrant);
            }

            //if that matchup is full, add it to the queue, and increment matchups
            if (currentMatch.matchupReady())
            {
                nonFinalMatchups.addLast(currentMatch);
                matchupIndex++;
                if(matchupIndex < this.matches.size())
                {
                currentMatch = this.matches.get(matchupIndex);
                }

            }
        }

        while (matchupIndex < (this.matches.size()))
        {
            currentMatch = this.matches.get(matchupIndex);

            //the match still needs a contestant
            while (currentMatch.totalNumContestants() < currentMatch.numberOfContestantsRequired)
            {
                TourneyMatch topPendingMatch = nonFinalMatchups.pollFirst();

                topPendingMatch.setNextMatch(currentMatch);
                currentMatch.addPendingContestant(topPendingMatch);
            }
            nonFinalMatchups.addLast(currentMatch);

            matchupIndex++;

        }

        //add all matches into master matches list
        //numMatches = numEntrants/2
        //temp array of matches
        //next round:
        //pair off each match in temp array matches -- create new matches
    }

    //TODO:  add seeding to entries -- sort by them
    //TODO: check for duplicate entry names
    public ArrayList<TourneyEntrant> updateValidEntries()
    {
        this.validArmies.clear();
        for (TourneyEntrant entrant : this.allArmies)
        {
            if (this.isValidEntrant(entrant, this.maxAllowableScore))
            {
                this.validArmies.add(entrant);
            }
        }
        return this.validArmies;
    }
    
    public static File[] listOfArmyFiles()
    {
        File folder = new File(ARMIES_DIRECTORY);
        File[] listOfFiles = folder.listFiles();
        return listOfFiles;
    }

    public void loadAllArmyStrings()
    {
        File folder = new File(ARMIES_DIRECTORY);
        File[] listOfFiles = folder.listFiles();
        this.allArmies.clear();
        for (File armyFile : listOfFiles)
        {
            String armyString = buildParser.readArmyFile(armyFile.getAbsolutePath(), false);
            Army army = buildParser.buildArmyFromString(armyString);
            TourneyEntrant potentialEntrant = new TourneyEntrant(army.getAllegiance(), armyString, army.getColor());
            this.allArmies.add(potentialEntrant);
        }

    }

    public void loadAllBattlefieldStrings()
    {
        File folder = new File(BATTLEFIELDS_DIRECTORY);
        File[] listOfFiles = folder.listFiles();
        for (File fieldFile : listOfFiles)
        {
            String fieldString = buildParser.readBattlefieldFile(fieldFile.getAbsolutePath(), false);

            this.allBattlefields.add(fieldString);
        }
    }

    public String toString()
    {
        String string = "";
        int i = 0;
        for (TourneyMatch match : this.matches)
        {
            ++i;
            string += match.toString() + "\n\n";

            if (i == this.matches.size() && match.isVictorDetermined())
            {
                string += "CHAMPION:  " + match.getWinner().getEntrantName() + "!!!\n";
            }
        }

        return string;
    }

}
