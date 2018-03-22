/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SimBuilderTools;

import UnitFormations.BaseUnitFormation;
import UnitFormations.GridFormation;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Geography.StartingPosition;
import battlesimulator.Military.Archer;
import battlesimulator.Military.Army;
import battlesimulator.Military.Cavalry;
import battlesimulator.Military.General;
import battlesimulator.Military.Mage;
import battlesimulator.Military.Officer;
import battlesimulator.Military.Soldier;
import battlesimulator.Military.Unit;
import battlesimulator.SimulationSeed;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Arlo
 */
public class QuickArmyBuildParser
{

    static Map<String, Color> stringToColorMap;
    static Map<Color, String> colorToStringMap;

    //TODO:  use static variables in class themselves
    //TODO:  Better yet, use reflection to make a map of these chars so you can
    //       Build an army with any class in your reflection.
    static final char FOOTMAN = 'F';
    static final char ARCHER = 'A';
    static final char CAVALRY = 'C';
    static final char MAGE = 'M';

    ArrayList<Army> armies = new ArrayList<>();
    SimulationSeed seed;
    Battlefield battlefield;
    HashMap<String, StartingPosition> startingPositions = new HashMap<>();
    //set color map

    static
    {
        stringToColorMap = new HashMap<>();
        stringToColorMap.put("Red", Color.RED);
        stringToColorMap.put("Blue", Color.BLUE);
        stringToColorMap.put("Green", Color.GREEN);
        stringToColorMap.put("Purple", Color.MAGENTA);
        stringToColorMap.put("Black", Color.BLACK);
        stringToColorMap.put("Gray", Color.GRAY);
        stringToColorMap.put("Cyan", Color.CYAN);
        stringToColorMap.put("Orange", Color.ORANGE);

        colorToStringMap = new HashMap<>();
        colorToStringMap.put(Color.RED, "Red");
        colorToStringMap.put(Color.BLUE, "Blue");
        colorToStringMap.put(Color.GREEN, "Green");
        colorToStringMap.put(Color.MAGENTA, "Purple");
        colorToStringMap.put(Color.BLACK, "Black");
        colorToStringMap.put(Color.GRAY, "Gray");
        colorToStringMap.put(Color.CYAN, "Cyan");
        colorToStringMap.put(Color.ORANGE, "Orange");

    }

    public SimulationSeed buildSeedFromFile(String path)
    {
        try
        {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<String> armyPaths = new ArrayList<>();
            String line;
            //add battlefield first
            line = bufferedReader.readLine();
            this.readBattlefieldFile(line, true);

            while ((line = bufferedReader.readLine()) != null)
            {
                armyPaths.add(line);
            }
            fileReader.close();
            this.seed = this.generateSeedFromArmiesFile(armyPaths);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return this.seed;
    }

    public void ensureUniqueColors()
    {
        Set<Color> colorsUsed = new HashSet<>();

        ArrayList<Color> availableColors = new ArrayList<>();
        availableColors.addAll(stringToColorMap.values());
        Random rand = new Random();
        int attemptCutoff = 0;
        for (Army army : this.armies)
        {
            Color color = army.getColor();
            while (colorsUsed.contains(color))
            {
                int randomPick = rand.nextInt(availableColors.size());

                color = availableColors.get(randomPick);
                army.setColor(color);
                attemptCutoff++;
                
                if(attemptCutoff > 100 )
                {
                    System.out.println("Couldn't find unique Color.");
                    break;
                }
            }
            colorsUsed.add(color);
        }
    }

    public void clearArmies()
    {
        this.armies.clear();
    }

    public String readArmyFile(String path, boolean addToArray)
    {
        String armyString = "";
        try
        {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }
            fileReader.close();
            armyString = stringBuffer.toString();
            if (addToArray)
            {
                this.armies.add(buildArmyFromString(stringBuffer.toString()));
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return armyString;
    }

    public String readBattlefieldFile(String path, boolean generateBattlefield)
    {
        String fieldString = "";
        try
        {
            File file = new File(path);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
            }
            fileReader.close();
            fieldString = stringBuffer.toString();
            if (generateBattlefield)
            {
                this.battlefield = addBattlefieldFromString(fieldString);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return fieldString;
    }

    //TODO:  this is all pretty janky.  Replace with Yaml.
    public Battlefield addBattlefieldFromString(String fieldString)
    {
        String[] splitString = fieldString.split(";");
        String fieldDetails = splitString[0];
        String[] fieldDetailsSplit = fieldDetails.split(",");
        String battlefieldName = fieldDetailsSplit[0];
        int cellSize = Integer.parseInt(fieldDetailsSplit[1]);
        int width = Integer.parseInt(fieldDetailsSplit[2]);
        int height = Integer.parseInt(fieldDetailsSplit[3]);
        Battlefield field = new Battlefield(width, height, cellSize, battlefieldName);

        for (int startingPositionIndex = 1; startingPositionIndex < splitString.length; startingPositionIndex++)
        {
            String[] startingPositionSplit = splitString[startingPositionIndex].split(",");
            String startPosName = startingPositionSplit[0];
            int startPosX = Integer.parseInt(startingPositionSplit[1]);
            int startPosY = Integer.parseInt(startingPositionSplit[2]);
            StartingPosition position = new StartingPosition(new Rectangle(new Point(startPosX, startPosY)));
            field.addStartingPosition(startPosName, position);
        }
        this.battlefield = field;
        return field;
    }

    //TODO:  this is all pretty janky.  Replace with Yaml.
    public Army buildArmyFromString(String armyString)
    {
        String[] splitString = armyString.split(";");
        String armyName = splitString[0].trim();
        while (alreadyHaveArmyWithName(armyName))
        {
            armyName = armyName + "_other";
        }

        String armyColorString = splitString[1].trim();
        Color armyColor = stringToColorMap.get(armyColorString);
        if (armyColor == null)
        {
            armyColor = Color.GRAY;
        }
        Army army = new Army(armyColor, armyName);
        String preferredStrategy = splitString[2].trim();
        General general = new General(armyName, armyName + "General");
        army.setGeneral(general);
        general.setPreferedStrategy(preferredStrategy);

        for (int armyIndex = 3; armyIndex < splitString.length; armyIndex++)
        {
            String wholeUnitString = splitString[armyIndex].trim();
            String[] unitStringSplit = wholeUnitString.split(":");
            String unitName = unitStringSplit[0];
            int numSoldiers = 0;
            int numArchers = 0;
            int numMages = 0;
            int numCavalry = 0;

            for (int unitIndex = 1; unitIndex < unitStringSplit.length; unitIndex++)
            {
                String soldierType = unitStringSplit[unitIndex].trim();
                char soldierClass = soldierType.charAt(0);
                String numSoldiersString = soldierType.substring(1);
                int numTroops = Integer.parseInt(numSoldiersString);

                switch (soldierClass)
                {
                    case FOOTMAN:
                        numSoldiers += numTroops;
                        break;
                    case ARCHER:
                        numArchers += numTroops;
                        break;
                    case MAGE:
                        numMages += numTroops;
                        break;
                    case CAVALRY:
                        numCavalry += numTroops;
                        break;

                }

            }//for each troop type in a unit

            int totalNum = numSoldiers + numArchers + numCavalry + numMages;
            int spacing = (numCavalry >= (totalNum / 3)) ? Cavalry.getPersonalSpace() : Soldier.SOLDIER_PERSONAL_SPACE_RADIUS;
            int numInRow = (int) Math.sqrt(totalNum);

            Officer officer = new Officer(armyName, armyName + unitName + "Officer");
            BaseUnitFormation formation = new GridFormation("defaultGrid", new Point(0, 0), 0, totalNum, spacing, numInRow);
            Unit newUnit = new Unit(officer, formation, armyColor, armyName, unitName);
            newUnit.setOfficer(officer);

            for (int index = 0; index < numSoldiers; index++)
            {
                Soldier soldier = new Soldier(armyName, unitName + "Soldier" + index);
                newUnit.addSoldier(soldier);
            }

            for (int index = 0; index < numArchers; index++)
            {
                Archer soldier = new Archer(armyName, unitName + "Archer" + index);
                newUnit.addSoldier(soldier);
            }

            for (int index = 0; index < numCavalry; index++)
            {
                Cavalry soldier = new Cavalry(armyName, unitName + "Cavalry" + index);
                newUnit.addSoldier(soldier);
            }

            for (int index = 0; index < numMages; index++)
            {
                Mage mage = new Mage(armyName, unitName + "Wizard" + index);
                newUnit.addSoldier(mage);
            }

            newUnit.generateUnitMetrics(0);
            army.addUnit(newUnit);
        }//for each unit
        return army;
    }

    public void addArmy(Army army)
    {
        this.armies.add(army);
    }

    public SimulationSeed generateSeedFromArmiesFile(ArrayList<String> seedFilePaths)
    {
        for (String armyFilePath : seedFilePaths)
        {
            this.readArmyFile(armyFilePath, true);
        }
        //Battlefield field = findStartingPoints();
        this.assignStartingPositions();
        Army[] armyArray = armies.toArray(new Army[armies.size()]);
        SimulationSeed seed = new SimulationSeed(this.battlefield, armyArray);
        return seed;
    }

    public ArrayList<Army> getArmies()
    {
        return this.armies;
    }

    public Battlefield getBattlefield()
    {
        return this.battlefield;
    }

    public void assignStartingPositions()
    {
        int armyToAssign = 0;
        for (String startPosKey : this.battlefield.getStartingPositions().keySet())
        {
            if (armyToAssign < this.armies.size())
            {
                this.battlefield.assignArmyStartingPoint(startPosKey, armies.get(armyToAssign));
                armyToAssign++;
            }
        }
    }

    public Battlefield findStartingPoints()
    {
        int cellSize = 25;
        int width = 50;
        int height = 50;

        return findStartingPoints(cellSize, width, height);
    }

    public Battlefield findStartingPoints(int cellSize, int widthCells, int heightCells)
    {
        System.out.println("trying battlefield: " + widthCells + " x " + heightCells);
        //TODO:  figure out a smarter way to determine battlefield size...
        // Maybe each army can have a height, width that gets set when they're constructed...
        //TODO:  read this in from a file as well, with default starting points, rather than random starting points
        Battlefield field = new Battlefield(widthCells, heightCells, cellSize, "battlefield");
        boolean everyArmyHasPosition = true;
        for (Army army : armies)
        {
            int numAttempts = 0;
            int columnsIn = 0;
            int rowsDown = 0;
            boolean overlapsOrCutOff = true;
            while (overlapsOrCutOff && (rowsDown < heightCells) && (columnsIn < widthCells))
            {
                overlapsOrCutOff = false;
                Point randomPoint = new Point(columnsIn * cellSize, rowsDown * cellSize);
                //System.out.println("\ttrying point: " + randomPoint.x + ", " + randomPoint.y);
                Dimension dimension = new Dimension(1, 1);
                Rectangle rectangle = new Rectangle(randomPoint, dimension);
                String startingPositionKey = "staringPointFor" + army.getAllegiance();
                field.addStartingPosition(startingPositionKey, new StartingPosition(rectangle));
                //TODO:  check that the point isn't too close to the edge for this Army, or too close to another army

                Point before = army.getBoundingRectangle().getLocation();
                field.assignArmyStartingPoint(startingPositionKey, army);

                Point after = army.getBoundingRectangle().getLocation();
                overlapsOrCutOff = this.ovlapsAnotherArmy(army);
                overlapsOrCutOff |= this.armyCutOff(army, field);

                //on even attempts, bump down a row.  on odd attempts, bump in a column
                if (numAttempts % 2 == 0)
                {
                    rowsDown++;
                } else
                {
                    columnsIn++;
                }
                numAttempts++;
            }//while within the battlefield and something overlaps

            //if army still overlaps or is cut off, we need to expand the field
            if (overlapsOrCutOff == true)
            {
                everyArmyHasPosition = false;
                break;
            } else
            {
                System.out.println("found a spot for : " + army.getAllegiance() + "\n\t" + army.getGeneralPoint());
            }
        }//for Army

        if (everyArmyHasPosition)
        {

            return field;

        } else
        {
            return findStartingPoints(cellSize, widthCells + 10, heightCells + 10);
        }
    }

    public boolean ovlapsAnotherArmy(Army army)
    {
        boolean overlaps = false;
        for (Army otherArmy : this.armies)
        {
            if (!otherArmy.equals(army))
            {
                overlaps |= otherArmy.getBoundingRectangle().intersects(army.getBoundingRectangle());
            }
        }

        return overlaps;
    }

    public boolean armyCutOff(Army army, Battlefield field)
    {
        Rectangle armyRectangle = army.getBoundingRectangle();
        Point minXY = armyRectangle.getLocation();
        Point maxXY = new Point(minXY.x + armyRectangle.width, minXY.y + armyRectangle.height);
        boolean cutoff = false;
        Point fieldMaxXY = new Point(field.getPixelsWidth(), field.getPixelsHeight());

        if ((minXY.x <= 0) || (minXY.y <= 0) || (maxXY.x >= fieldMaxXY.x) || (maxXY.y >= fieldMaxXY.y))
        {
            cutoff = true;

        }
        return cutoff;
    }

    public boolean alreadyHaveArmyWithName(String name)
    {
        boolean nameExists = false;
        for (Army army : armies)
        {
            nameExists |= army.getAllegiance().equals(name);
        }
        return nameExists;
    }

    public boolean alreadyHaveUnitWithName(String name, Army army)
    {
        boolean nameExists = false;
        for (Unit unit : army.getUnits())
        {
            nameExists |= unit.getName().equals(name);
        }
        return nameExists;
    }
}
