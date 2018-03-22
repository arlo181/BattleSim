/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import UnitFormations.BaseUnitFormation;
import UnitFormations.GridFormation;
import Orders.AbstractOrder;
import Orders.AssumeFormation;
import Orders.UntilComplete;
import SimBuilderTools.FormationBuilder;
import battlesimulator.Geography.*;
import battlesimulator.Military.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Arlo
 */
public class BattleSimulator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
FormationBuilder builder = new FormationBuilder();
//builder.setVisible(true);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedObstacle(), 125);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedThreeArmies(1000), 125);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedSwarms(2000), 125);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedCalvaryCharge(750), 125);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedPushTest(), 125);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedOfficer(500), 125);
        //SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedOpposingOfficers(1000), 35);
        SimulationEngine engine = new SimulationEngine(generateTestSimulationSeedMultipleOfficers(2000), 35, false, true, true, 100); //test
        //SimulationEngine engine = new SimulationEngine(generateTestSeedArcher(50), 35);
//SimulationEngine engine = new SimulationEngine(testLoadFormation(100), 35);
        //SimulationEngine engine = new SimulationEngine(openSeedFromFile("testSerialize.ser"), 125);
        engine.startSimulation();
    }
   
    
    public static SimulationSeed generateTestSimulationSeedMultipleOfficers(int numSoldiers) {
        //creates a simulationseed to use for testing
        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
        Battlefield battlefield = new Battlefield(95, 95, 25, "test");
        battlefield.generateRandomCells();
        
        Army westArmy = new Army(Color.blue, "blue");
        

        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
        Rectangle startingRectangleWest = new Rectangle(600, 600, 25, 50);
        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
        
        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
        Rectangle startingRectangleEast = new Rectangle(1450, 600, 25, 50);
        StartingPosition startingPositionEast = new StartingPosition(startingRectangleEast);
        
        battlefield.addStartingPosition("blue", startingPositionWest);
                battlefield.addStartingPosition("east", startingPositionEast);
        
        
        
             General westGeneral = new General ("blue", "general Grant");
             westArmy.setGeneral(westGeneral);
        Point formationW1Origin = new Point(215, 235);
        ArrayList<Point> positionsInFormationW1 = new ArrayList<>();
        Point formationW2Origin = new Point(310, 357);
        ArrayList<Point> positionsInFormationW2 = new ArrayList<>();
        Point formationW3Origin = new Point(215, 495);
        ArrayList<Point> positionsInFormationW3 = new ArrayList<>();

        int numInRow = 120;

        //West officer 1
       
                //West unit 1
        for (int i = 0; i < numSoldiers / 3; i++) {
             positionsInFormationW1.add(new Point(16 * (i % (numInRow / 9))+8, 5 + (16 * (i / (numInRow / 9)))));
        }
         BaseUnitFormation blueFormationW1 = new BaseUnitFormation("w1",formationW1Origin, positionsInFormationW1);
        blueFormationW1.setOfficerPosition(new Point(0, 150));
        Officer westOfficer = new Officer("blue", "marty");
        westOfficer.setCenterPoint(new Point(200, 50));
        
        Unit westUnit1 = new Unit(westOfficer, blueFormationW1, Color.blue, "blue", "unit1");
        
         for (int i = 0; i < numSoldiers / 3; i++) {
            Soldier westSoldier = null;
            westSoldier = new Soldier("blue", "george");
           
            westUnit1.addSoldier(westSoldier);
            //temporarily just set his position
           // westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 50));
        }
         
         //Mage killer = new Mage("blue", "Harry");
        // westUnit1.addSoldier(killer);
        // killer.setCenterPoint(new Point(50,50));


        
        //West unit 2
        
        //West officer 2
        
        for (int i = 0; i < numSoldiers / 3; i++) {
            positionsInFormationW2.add(new Point(16 * (i % (numInRow / 6))+8, 5 + (16 * (i / (numInRow /6)))));
        }
        BaseUnitFormation blueFormationW2 = new BaseUnitFormation("w2",formationW2Origin, positionsInFormationW2);
        blueFormationW2.setOfficerPosition(new Point(0, 150));
        Officer westOfficer2 = new Officer("blue", "bob");
        westOfficer2.setCenterPoint(new Point(400, 110));
        westOfficer2.setUnitFormation(blueFormationW2);
        GridFormation gridw2 = new GridFormation("wedge2", formationW2Origin, 95, numSoldiers, Soldier.getPersonalSpace(), 35);
        Unit westUnit2 = new Unit(westOfficer2, gridw2, Color.blue, "blue", "unit2");
         for (int i = 0; i < numSoldiers / 3; i++) {
            Soldier westSoldier = null;
            westSoldier = new Soldier("blue", "whatever");
            
            westUnit2.addSoldier(westSoldier);
            //temporarily just set his position
            //westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 110));
            
        }


        //West officer 3
       
          //West unit 3
        for (int i = 0; i < numSoldiers / 3; i++) {
             positionsInFormationW3.add(new Point(16 * (i % (numInRow / 9))+8, 5 + (16 * (i / (numInRow /9)))));
        }
         BaseUnitFormation blueFormationW3 = new BaseUnitFormation("w3",formationW3Origin, positionsInFormationW3);
        blueFormationW3.setOfficerPosition(new Point(0, 150));
        Officer westOfficer3 = new Officer("blue", "hello");
        westOfficer3.setCenterPoint(new Point(200, 170));
        westOfficer3.setUnitFormation(blueFormationW3);
         
        Unit westUnit3 = new Unit(westOfficer3, blueFormationW3, Color.blue, "blue", "unit3");
         for (int i = 0; i < numSoldiers / 7; i++) {
            Soldier westSoldier = null;
            westSoldier = new Archer("blue", "world");
            
            westUnit3.addSoldier(westSoldier);
            //temporarily just set his position
            //westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 170));
           
        }
    
        LinkedList<AbstractOrder> queuedOrdersW3 = new LinkedList<>();
        queuedOrdersW3.addLast(new AssumeFormation(blueFormationW3, new UntilComplete(70)));

    
   
        
        westArmy.addUnit(westUnit1);
        westArmy.addUnit(westUnit2);
        westArmy.addUnit(westUnit3);
        
       
            
        
     
        Army eastArmy = new Army(Color.red, "red");
       General eastGeneral = new General ("red", "general Lee");
             eastArmy.setGeneral(eastGeneral);
 
         BaseUnitFormation redFormation = new GridFormation("form", new Point(600,500), 0, numSoldiers, Soldier.SOLDIER_PERSONAL_SPACE_RADIUS, (int) Math.sqrt(numSoldiers));
       
         redFormation.setOfficerPosition(new Point(400, 400));
        //East officer
        Officer eastOfficer = new Officer("red", "ted");
        eastOfficer.setCenterPoint(new Point(1100, 500));
        eastOfficer.setUnitFormation(redFormation);
        Unit eastUnit1 = new Unit(eastOfficer, redFormation, Color.red, "red", "unitred");
          for (int i = 0; i < numSoldiers; i++) {
            Soldier eastSoldier = new Soldier("red", "anonymous");
            
            eastSoldier.setAggressionLevel(5);
            //eastSoldier.addBonus(new BaseBonus("SUPER SOLDIER", BaseBonus.Attribute.STRENGTH, 0, 1000));
            eastUnit1.addSoldier(eastSoldier);

            
        }

//Giant giant1 = new Giant("red", "urgh");
//Giant giant2 = new Giant("red", "gurgh");
//Giant giant3 = new Giant("red", "gaurgh");
//eastUnit1.addSoldier(giant1);
//eastUnit1.addSoldier(giant2);
//eastUnit1.addSoldier(giant3);


eastArmy.addUnit(eastUnit1);

        battlefield.assignArmyStartingPoint("blue", westArmy);
        

        battlefield.assignArmyStartingPoint("east", eastArmy);
        
        
        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy);
        
//        
//        try {
//            FileOutputStream fileOut =
//                    new FileOutputStream("SavedData\\Seeds\\twoArmyMultipleUnits.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(testSeed);
//            out.close();
//            fileOut.close();
//            System.out.printf("Serialized data is saved in \\SavedData\\Seeds\\");
//        } catch (IOException i) {
//            i.printStackTrace();
//        }
//        
        return testSeed;
    }
//    
//    public static SimulationSeed generateTestSimulationSeedOfficer(int numSoldiers) {
//        //creates a simulationseed to use for testing
//        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
//        Battlefield battlefield = new Battlefield(50, 25, 25, "test");
//        battlefield.generateRandomCells();
//        
//        Army westArmy = new Army(Color.blue, "swarm");
//        Unit westUnit1 = new Unit();
//        westArmy.addUnit(westUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleWest = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
//        
//        Point formationOrigin = new Point(12, 35);
//        ArrayList<Point> positionsInFormation = new ArrayList<>();
//        
//        Army eastArmy = new Army(Color.red, "soldier");
//        Unit eastUnit1 = new Unit();
//        Unit eastUnit2 = new Unit();
//        eastArmy.addUnit(eastUnit1);
//        eastArmy.addUnit(eastUnit2);
//        
//        int numInRow = 90;
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier westSoldier = new Soldier("swarm");
//            
//            westUnit1.addSoldier(westSoldier);
//            westSoldier.setAggressionLevel(5);
//
//            //temporarily just set his position
//            westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 50));
//            positionsInFormation.add(new Point(16 * (i % (numInRow / 10)), 5 + (16 * (i / (numInRow / 10)))));
////            System.out.println("x:  " +  15 * (i % (numInRow/20) ) + "y:  " + 5 + (15 *  (i / (numInRow/20))));
//        }
//        
//        BaseFormation formation = new BaseFormation(formationOrigin, positionsInFormation);
//        
//        Officer westOfficer = new Officer("swarm");
//        westOfficer.setCenterPoint(new Point(200, 50));
//        westOfficer.setUnitFormation(formation);
//        
//        westUnit1.addOfficer(westOfficer);
//        LinkedList<AbstractOrder> queuedOrders = new LinkedList<>();
//        queuedOrders.addLast(new AssumeFormation(formation, new UntilComplete(250)));
//        queuedOrders.addLast(new MarchOrder(90, 2, true, new Countdown(50)));
//        queuedOrders.addLast(new MarchOrder(0, 3, false, new Countdown(15)));
//        queuedOrders.addLast(new ChargeOrder(90, new Forever()));
//        westOfficer.setOrderQueue(queuedOrders);
//        
//        for (int i = 0; i < 1; i++) {
//            Soldier eastSoldier = new Avoider("soldier");
//            
//            eastUnit1.addSoldier(eastSoldier);
//            //temporarily just set his position
//            eastSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 350 + (9 * (i / numInRow))));
//            eastSoldier.setAggressionLevel(5);
//            eastSoldier.speed = 0;
//        }
//
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleEast = new Rectangle(75, 150, 25, 50);
//        StartingPosition startingPositionEast = new StartingPosition(startingRectangleEast);
//        
//        battlefield.addStartingPosition("west", startingPositionWest);
//        battlefield.assignArmyStartingPoint("west", westArmy);
//        
//        battlefield.addStartingPosition("east", startingPositionEast);
//        battlefield.assignArmyStartingPoint("east", eastArmy);
//        
//        
//        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy);
//        
//        return testSeed;
//    }
//    
//    public static SimulationSeed generateTestSimulationSeedCalvaryCharge(int numSoldiers) {
//        //creates a simulationseed to use for testing
//        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
//        Battlefield battlefield = new Battlefield(50, 25, 25, "test");
//        battlefield.generateRandomCells();
//        
//        Army westArmy = new Army(Color.blue, "swarm");
//        Unit westUnit1 = new Unit();
//        westArmy.addUnit(westUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleWest = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
//        
//        
//        
//        Army eastArmy = new Army(Color.red, "soldier");
//        Unit eastUnit1 = new Unit();
//        Unit eastUnit2 = new Unit();
//        eastArmy.addUnit(eastUnit1);
//        eastArmy.addUnit(eastUnit2);
//        
//        int numInRow = 100;
//        for (int i = 0; i < numSoldiers * 3; i++) {
//            Soldier westSoldier = new Swarmer("swarm");
//            
//            westUnit1.addSoldier(westSoldier);
//            westSoldier.setAggressionLevel(5);
//            //temporarily just set his position
//            westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 50));
//            
//        }
//        
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier eastSoldier = new Avoider("soldier");
//            
//            eastUnit1.addSoldier(eastSoldier);
//            //temporarily just set his position
//            eastSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 350 + (9 * (i / numInRow))));
//            eastSoldier.setAggressionLevel(5);
//        }
//        
//        for (int i = 0; i < numSoldiers / 5; i++) {
//            Soldier eastSoldier = new Cavalry("soldier");
//            
//            eastUnit2.addSoldier(eastSoldier);
//            //temporarily just set his position
//            eastSoldier.setCenterPoint(new Point(16 * (i % (numInRow / 4)) + 400, 450 + (16 * (i / (numInRow / 4)))));
//            eastSoldier.setAggressionLevel(5);
//        }
//
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleEast = new Rectangle(75, 150, 25, 50);
//        StartingPosition startingPositionEast = new StartingPosition(startingRectangleEast);
//        
//        battlefield.addStartingPosition("west", startingPositionWest);
//        battlefield.assignArmyStartingPoint("west", westArmy);
//        
//        battlefield.addStartingPosition("east", startingPositionEast);
//        battlefield.assignArmyStartingPoint("east", eastArmy);
//        
//        
//        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy);
//        
//        return testSeed;
//    }
//    
//    public static SimulationSeed generateTestSimulationSeedSwarms(int numSoldiers) {
//        //creates a simulationseed to use for testing
//        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
//        Battlefield battlefield = new Battlefield(50, 25, 25, "test");
//        battlefield.generateRandomCells();
//        
//        Army westArmy = new Army(Color.blue, "swarm");
//        Unit westUnit1 = new Unit();
//        westArmy.addUnit(westUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleWest = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
//        
//        
//        
//        Army eastArmy = new Army(Color.red, "soldier");
//        Unit eastUnit1 = new Unit();
//        eastArmy.addUnit(eastUnit1);
//        
//        
//        int numInRow = 100;
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier westSoldier = new Swarmer("swarm");
//            
//            westUnit1.addSoldier(westSoldier);
//            westSoldier.setAggressionLevel(5);
//            //temporarily just set his position
//            westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 50));
//            
//        }
//        
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier eastSoldier = new Avoider("soldier");
//            
//            eastUnit1.addSoldier(eastSoldier);
//            //temporarily just set his position
//            eastSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 350 + (9 * (i / numInRow))));
//            eastSoldier.setAggressionLevel(5);
//        }
//
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleEast = new Rectangle(75, 150, 25, 50);
//        StartingPosition startingPositionEast = new StartingPosition(startingRectangleEast);
//        
//        battlefield.addStartingPosition("west", startingPositionWest);
//        battlefield.assignArmyStartingPoint("west", westArmy);
//        
//        battlefield.addStartingPosition("east", startingPositionEast);
//        battlefield.assignArmyStartingPoint("east", eastArmy);
//        
//        
//        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy);
//        
//        return testSeed;
//    }
//    
//    public static SimulationSeed generateTestSimulationSeedThreeArmies(int numSoldiers) {
//        //creates a simulationseed to use for testing
//        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
//        Battlefield battlefield = new Battlefield(100, 100, 25, "test");
//        battlefield.generateRandomCells();
//        
//        Army westArmy = new Army(Color.blue, "west");
//        Unit westUnit1 = new Unit();
//        westArmy.addUnit(westUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleWest = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
//        
//        Army northArmy = new Army(Color.green, "north");
//        Unit northUnit1 = new Unit();
//        northArmy.addUnit(northUnit1);
//
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleNorth = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionNorth = new StartingPosition(startingRectangleNorth);
//        
//        Army eastArmy = new Army(Color.red, "east");
//        Unit eastUnit1 = new Unit();
//        eastArmy.addUnit(eastUnit1);
//        
//        
//        int numInRow = 75;
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier westSoldier = new Swarmer("west");
//            
//            westUnit1.addSoldier(westSoldier);
//            westSoldier.setAggressionLevel(5);
//            //temporarily just set his position
//            westSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 5 + (9 * (i / numInRow)) + 50));
//            
//        }
//        
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier eastSoldier = new Avoider("east");
//            
//            eastUnit1.addSoldier(eastSoldier);
//            //temporarily just set his position
//            eastSoldier.setCenterPoint(new Point(9 * (i % numInRow) + 100, 350 + (9 * (i / numInRow))));
//            eastSoldier.setAggressionLevel(5);
//        }
//        
//        
//        for (int i = 0; i < numSoldiers; i++) {
//            Soldier northSoldier = new Soldier("north");
//            
//            northUnit1.addSoldier(northSoldier);
//            //temporarily just set his position
//            northSoldier.setCenterPoint(new Point(9 * (i % (numInRow / 2)) + 800, 50 + (9 * (i / (numInRow / 2)))));
//            northSoldier.setAggressionLevel(5);
//        }
//        
//        
//        Army lonerArmy = new Army(Color.DARK_GRAY, "The Reaper");
//        Unit loneUnit1 = new Unit();
//        lonerArmy.addUnit(loneUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleReaper = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionReaper = new StartingPosition(startingRectangleReaper);
//        
//        AuraKiller reaper = new AuraKiller("The Reaper", new Point(650,250));
//        loneUnit1.addSoldier(reaper);
//
//
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleEast = new Rectangle(75, 150, 25, 50);
//        StartingPosition startingPositionEast = new StartingPosition(startingRectangleEast);
//        
//        battlefield.addStartingPosition("west", startingPositionWest);
//        battlefield.assignArmyStartingPoint("west", westArmy);
//        
//        battlefield.addStartingPosition("east", startingPositionEast);
//        battlefield.assignArmyStartingPoint("east", eastArmy);
//        
//        battlefield.addStartingPosition("north", startingPositionNorth);
//        battlefield.assignArmyStartingPoint("north", northArmy);
//        
//        battlefield.addStartingPosition("The Reaper", startingPositionReaper);
//        battlefield.assignArmyStartingPoint("The Reaper", lonerArmy);
//        
//        
//        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy, northArmy, lonerArmy);
//        
//        return testSeed;
//    }
//    
//    public static SimulationSeed generateTestSimulationSeedPushTest() {
//        //creates a simulationseed to use for testing
//        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
//        Battlefield battlefield = new Battlefield(30, 15, 25, "test");
//        battlefield.generateRandomCells();
//        
//        Army westArmy = new Army(Color.blue, "soldier");
//        Unit westUnit1 = new Unit();
//        westArmy.addUnit(westUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleWest = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
//        
//        
//        
//        Army eastArmy = new Army(Color.red, "east");
//        Unit eastUnit1 = new Unit();
//        eastArmy.addUnit(eastUnit1);
//
//        //push increasing number of stones
//        Soldier westSoldier = new Soldier("west", new Point(200, 70));
//        westUnit1.addSoldier(westSoldier);
//        westSoldier.setAggressionLevel(1);
//        
//        battlefield.addNeutralObject(new Stone(new Point(200, 85)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 115)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 145)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 175)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 205)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 235)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 265)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(200, 355)));
//
////push stones at angle
//        Soldier westSoldier2 = new Soldier("west", new Point(200, 10));
//        westUnit1.addSoldier(westSoldier2);
//        westSoldier2.setAggressionLevel(3);
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 85)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 115)));
//        battlefield.addNeutralObject(new Stone(new Point(254, 145)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 175)));
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 205)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 205)));
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 235)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 235)));
//        battlefield.addNeutralObject(new Stone(new Point(262, 235)));
//        battlefield.addNeutralObject(new Stone(new Point(238, 235)));
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 265)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 265)));
//        battlefield.addNeutralObject(new Stone(new Point(262, 265)));
//        battlefield.addNeutralObject(new Stone(new Point(238, 265)));
//        battlefield.addNeutralObject(new Stone(new Point(270, 265)));
//        battlefield.addNeutralObject(new Stone(new Point(230, 265)));
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(262, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(238, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(270, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(230, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(278, 295)));
//        battlefield.addNeutralObject(new Stone(new Point(222, 295)));
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(262, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(238, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(270, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(230, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(278, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(222, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(286, 325)));
//        battlefield.addNeutralObject(new Stone(new Point(214, 325)));
//        
//        battlefield.addNeutralObject(new Stone(new Point(254, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(246, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(262, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(238, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(270, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(230, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(278, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(222, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(286, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(214, 355)));
//        battlefield.addNeutralObject(new Stone(new Point(294, 355)));
//        
//        battlefield.addStartingPosition("west", startingPositionWest);
//        battlefield.assignArmyStartingPoint("west", westArmy);
//        
//        
//        
//        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy);
//        
//        return testSeed;
//    }
//    
//    public static SimulationSeed generateTestSimulationSeedObstacle() {
//        //creates a simulationseed to use for testing
//        //battlefield is 10 cells wide by 5 cells high.  Each cell is a 25x25 pixel square.  2 Army starting positions.
//        Battlefield battlefield = new Battlefield(30, 15, 25, "test");
//        battlefield.generateRandomCells();
//        
//        Army westArmy = new Army(Color.blue, "soldier");
//        Unit westUnit1 = new Unit();
//        westArmy.addUnit(westUnit1);
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleWest = new Rectangle(25, 75, 25, 50);
//        StartingPosition startingPositionWest = new StartingPosition(startingRectangleWest);
//        
//        
//        
//        Army eastArmy = new Army(Color.red, "east");
//        Unit eastUnit1 = new Unit();
//        eastArmy.addUnit(eastUnit1);
//        
//        Soldier westSoldier = new Soldier("west", new Point(200, 70));
//        westUnit1.addSoldier(westSoldier);
//        westSoldier.setAggressionLevel(1);
//
//
////small wall, should go short way around it.
//        battlefield.addNeutralObject(new Stone(new Point(200, 85)));
//        battlefield.addNeutralObject(new Stone(new Point(192, 85)));
//        battlefield.addNeutralObject(new Stone(new Point(184, 85)));
//        battlefield.addNeutralObject(new Stone(new Point(176, 85)));
//
////start slalm
//        int x = 214;
//        int y = 100;
//        int xDiff = 14;
//        int yDiff = 15;
//        for (int i = 0; i < 7; i++) {
//            battlefield.addNeutralObject(new Stone(new Point(x, y)));
//            y += yDiff;
//            x += (i % 2 == 0) ? xDiff * -1 : xDiff;
//            if (i > 2) {
//                x += 3;
//            }
//        }
//
////long wall
//        for (int wallX = 50; wallX < 316; wallX += 8) {
//            battlefield.addNeutralObject(new Stone(new Point(wallX, 250)));
//        }
//        
//        
//        battlefield.addNeutralObject(new Stone(new Point(32, 300)));
//        battlefield.addNeutralObject(new Stone(new Point(42, 300)));
//        battlefield.addNeutralObject(new Stone(new Point(50, 300)));
//        battlefield.addNeutralObject(new Stone(new Point(50, 290)));
////battlefield.addNeutralObject(new Stone(new Point(50, 282)));
////battlefield.addNeutralObject(new Stone(new Point(238, 300)));
////battlefield.addNeutralObject(new Stone(new Point(230, 300)));
////battlefield.addNeutralObject(new Stone(new Point(222, 300)));
////battlefield.addNeutralObject(new Stone(new Point(214, 300)));
////battlefield.addNeutralObject(new Stone(new Point(206, 300)));
////battlefield.addNeutralObject(new Stone(new Point(198, 300)));
////battlefield.addNeutralObject(new Stone(new Point(190, 300)));
//
////        Soldier eastSoldier = new Soldier("east");
////
////        eastUnit1.addSoldier(eastSoldier);
////        //temporarily just set his position
////        eastSoldier.setCenterPoint(new Point(200, 230));
////        eastSoldier.setAggressionLevel(5);
//
//
//        //the starting position will start in the second grid cell in andthird one up(grid cell 1,3) and will cover an area of 2 grid cells (25 pixels wide by 50 pixels tall)
//        Rectangle startingRectangleEast = new Rectangle(75, 150, 25, 50);
//        StartingPosition startingPositionEast = new StartingPosition(startingRectangleEast);
//        
//        battlefield.addStartingPosition("west", startingPositionWest);
//        battlefield.assignArmyStartingPoint("west", westArmy);
//        
//        battlefield.addStartingPosition("east", startingPositionEast);
//        battlefield.assignArmyStartingPoint("east", eastArmy);
//        
//        
//        SimulationSeed testSeed = new SimulationSeed(battlefield, eastArmy, westArmy);
//        
//        return testSeed;
//    }
}
