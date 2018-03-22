/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import GUI.ViewSimFrame;
import Objects.Projectile;
import battlesimulator.Actions.*;
import battlesimulator.Geography.*;
import battlesimulator.Military.*;
import battlesimulator.Military.Army.ArmyStats;
import com.thebuzzmedia.imgscalr.Scalr;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Arlo
 */
public class SimulationEngine
{

    SimulationSeed seed;
    int delayBetweenTicksMS;
    int numTicksEllapsed;
    boolean killSim;
    Battlefield battlefield;
    List<BattlefieldObject> activeBattlefieldObjects = Collections.synchronizedList(new ArrayList<>());

    ArrayList<SceneSnapshot> scenes = new ArrayList<>();

    ConcurrentHashMap allegianceHashMap = new ConcurrentHashMap<>();
    Set<String> allegiances = allegianceHashMap.newKeySet();
    List<CombatResult> combatResults = Collections.synchronizedList(new ArrayList<>());
    List<Action> actions = Collections.synchronizedList(new CopyOnWriteArrayList<>());
    static int numLoops = 0;
    boolean simulationComplete = false;
    int sceneNumber = 0;
    public static final int MAX_LOOPS = 10000;
    long averageTickTimeMS = 0;
    long totalSimulationTimeMS = 0;
    boolean pauseAnimation = false;
    int initialDelayMS = 0;
    ViewSimFrame simFrame;
    javax.swing.Timer displayTimer;
    boolean shutdown;
    int roundsFinished = 0;
    final int ROUNDS_AFTER_THRESHOLD = 3;
    ArrayList<Unit> unitList;
    String fileStorePath = "";
    boolean straightToFile;
    boolean justRunAll;
    boolean showSim;

    int recordScale;

    public SimulationEngine(SimulationSeed seed, int delayBetweenTicksMS, boolean recordOnly, boolean justRunAll, boolean showSim, int scale)
    {
        this.seed = seed;
        this.battlefield = seed.getBattlefield();
        this.numTicksEllapsed = 0;
        this.delayBetweenTicksMS = delayBetweenTicksMS;
        this.activeBattlefieldObjects = this.battlefield.getAllBattleObjects();
        Collections.sort(this.activeBattlefieldObjects);
        killSim = false;
        this.justRunAll = justRunAll;

        this.straightToFile = recordOnly;
        this.shutdown = false;
        this.recordScale = scale;
        this.showSim = showSim;

        SceneSnapshot snapshot = new SceneSnapshot(0); //initial 

        //generate initial state:
        for (BattlefieldObject bObject : this.activeBattlefieldObjects)
        {
            bObject.evaluateBonuses();
            bObject.applyBonuses();
            snapshot.addStateToScene(bObject.generateState(0));
            if (bObject.hasAllegiance())
            {
                this.allegiances.add(bObject.getAllegience());
            }

        }
        for (Army army : this.getArmies())
        {
            snapshot.addArmyStats(army.generateArmyStats(0));
        }
        this.scenes.add(snapshot);
        simFrame = new ViewSimFrame(this);

        this.unitList = new ArrayList<>();
    }

    public ArrayList<Army> getArmies()
    {
        return this.seed.getArmies();
    }

    public ArrayList<ArmyStats> getArmyStatsForTick(int tickNumber)
    {
        ArrayList<ArmyStats> stats = new ArrayList<>();
        if (tickNumber < this.scenes.size())
        {
            stats = this.scenes.get(tickNumber).getArmyStats();
        }

        return stats;
    }

    public BattleResults startSimulation()
    {
        long durationOfProcAct;

        Collections.sort(this.activeBattlefieldObjects);

        Thread displayThreadStarter = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(initialDelayMS);
                } catch (InterruptedException ex)
                {
                    ex.printStackTrace();
                    Logger.getLogger(SimulationEngine.class.getName()).log(Level.SEVERE, null, ex);
                }
                displayAnimation();
            }
        };
        numLoops = 0;
        if (!straightToFile && this.showSim)
        {
            displayThreadStarter.start();
        } else if (straightToFile)
        {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM_dd_hhmmss");
            String time = dateFormat.format(now);
            File dir = new File(time);
            dir.mkdir();
            this.fileStorePath = dir.getAbsolutePath();
        }

        //generate initial unit metrics
        for (Army army : this.getArmies())
        {
            for (Unit unit : army.getUnits())
            {
                unit.clearMetrics();
                unit.clearStateAlbum();
                this.unitList.add(unit);
                unit.generateUnitMetrics(0);
            }
        }

        //set up inital formations/strategies
        //generate initial unit metrics
        for (Army army : this.getArmies())
        {
            army.getGeneral().takeStock(this.unitList);
            army.getGeneral().getArmyInFormation();
        }

        List<BattlefieldObject> newlyInactiveObjects = Collections.synchronizedList(new ArrayList<>());;
        //while there's greater than one army still active
        boolean exceptionBreak = false;
        while ((allegiances.size() > 1) && numLoops < MAX_LOOPS && shutdown == false || exceptionBreak)
        {
            exceptionBreak = false;
            try
            {
                durationOfProcAct = 0;
                allegiances.clear();
                this.battlefield.setObjects((ArrayList<BattlefieldObject>) this.activeBattlefieldObjects);

                newlyInactiveObjects.clear();
                numLoops++;

                long startTickTimeMS = System.currentTimeMillis();

                combatResults.clear();

                //finalize the input each object has given to its unit  NOTE: this will always be one tick behind.
                this.unitList.parallelStream().forEach(unit ->
                {
                    unit.generateUnitMetrics(numLoops);
                });

                this.activeBattlefieldObjects.parallelStream().forEach(bObject ->
                {
                    boolean objectIsActive = bObject != null ? bObject.isActive() : false;
                    if (objectIsActive)
                    {
                        //add its allegiance to the list of those remaining
                        if (bObject.hasAllegiance())
                        {
                            this.allegiances.add(bObject.getAllegience());
                        }

                        if (bObject.isEngagedInCombat())
                        {
                            CombatResult result = bObject.resolveCombat();
                            this.combatResults.add(result);
                        }

                        //determine if we need to refresh the objet's awareness circle
                        short needsAwarenessWithin = bObject.needsAwarenessWithin();
                        if (objectIsActive && needsAwarenessWithin > 0)
                        {
                            this.setObjectAwareness(bObject, needsAwarenessWithin);
                        }


                        //tick
                        if (objectIsActive && bObject.getCurrentAction() == null)
                        {
                            // object's awareness radius
                            Action action = bObject.tick(numLoops);
                            if (action != null)
                            {
                                this.actions.add(action);
                            }

                        }

                        bObject.evaluateBonuses();
                        bObject.applyBonuses();
                    }//if active
                    else if (bObject != null)
                    {
                            newlyInactiveObjects.add(bObject); //mark this as an object to be removed.  We don't need to loop over it every time   
                    }
                });//for each object

                long startProcActTimeMS = System.currentTimeMillis();
                this.processActions();
                long endProcActTimeMS = System.currentTimeMillis();
                durationOfProcAct = (endProcActTimeMS - startProcActTimeMS);

                processCombatResults();

                this.activeBattlefieldObjects.removeAll(newlyInactiveObjects);

                //generate state:
                SceneSnapshot snapshot = new SceneSnapshot(this.numLoops);

                this.battlefield.getAllBattleObjects().parallelStream().forEach(bObject ->
                {
                    if (bObject.doesExist() && numLoops < MAX_LOOPS)
                    {
                        snapshot.addStateToScene(bObject.generateState(this.numLoops));
                    }
                });

                for (Army army : this.getArmies())
                {
                    snapshot.addArmyStats(army.generateArmyStats(numLoops));
                }
                this.scenes.add(snapshot);
                if (straightToFile && numLoops % 20 == 0)
                {
                    this.paintSceneToFileInBatches(0, this.fileStorePath, this.recordScale);
                    this.scenes.clear();
                    this.sceneNumber = 0;
                }

                this.numTicksEllapsed++;
                // durationOfProcAct = endProcActTimeMS - startProcActTimeMS;
                long endTickTimeMS = System.currentTimeMillis();
                long durationOfTickMS = endTickTimeMS - startTickTimeMS;
                this.totalSimulationTimeMS += durationOfTickMS;
                this.averageTickTimeMS = totalSimulationTimeMS / numTicksEllapsed;
//            System.out.println("Tick: " + this.numTicksEllapsed
//                    + " - tick MS: " + durationOfTickMS
//                    + " - AVG TickMS: " + this.averageTickTimeMS
//                    + " - Total Sec: " + this.totalSimulationTimeMS / 1000.0
//                    + " - MS to ProcAct: " + durationOfProcAct
//                    + " = " + (int) (((((double) durationOfProcAct)) / durationOfTickMS) * 100) + "%"
//                    + " - numActive: " + this.activeBattlefieldObjects.size());

//temp output to see if progress is being made

            } catch (Exception ex)
            {
                //Even if we get an exception, try to keep going.
                exceptionBreak = true;
                System.out.println("KBO!!!");
                ex.printStackTrace();
            }
        }//while not win

        System.out.println("done");
        this.simulationComplete = true;
        this.activeBattlefieldObjects.clear();
        this.battlefield.clearAllObjectsFromField();
        if (straightToFile)
        {

            this.paintSceneToFileInBatches(0, this.fileStorePath, this.recordScale);
        }
        String winningArmy = "";
        ArrayList<ArmyStats> finalSceneArmyStats = this.scenes.get(this.scenes.size() - 1).getArmyStats();
        int highestScore = -1;
        System.out.println("RESULTS:");
        int numTicks = 0;
        for (ArmyStats armyStats : finalSceneArmyStats)
        {
            System.out.println(armyStats.getAllegiance() + " finished with score: " + armyStats.getArmyScore()) ;
            if (armyStats.getArmyScore() > highestScore)
            {
                highestScore = armyStats.getArmyScore();
                winningArmy = armyStats.getAllegiance();
                numTicks = armyStats.getTickCount();
            }
        }
        System.out.println(" in " + numTicks + " ticks");

//wait to return results until done with the sim
        while (!this.killSim && !justRunAll)
        {
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException ex)
            {
                ex.printStackTrace();
                Logger.getLogger(SimulationEngine.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(winningArmy + "WINS!\n");
        BattleResults results = new BattleResults(winningArmy, finalSceneArmyStats);
        return results;
    }

    public Unit findUnit(String allegience, String unitName)
    {
        Unit unitFound = null;

        for (Unit unit : this.unitList)
        {
            if (unit.getName().equals(unitName) && unit.getAllegiance().equals(allegience))
            {
                unitFound = unit;
            }
        }

        return unitFound;
    }

    public void setSceneNumber(int sceneNumberRequested)
    {
        this.sceneNumber = (sceneNumberRequested <= this.numLoops) ? sceneNumberRequested : this.numLoops;
    }

    public void pauseAnimation(boolean pause)
    {
        this.pauseAnimation = pause;
    }

    public void killSimulation()
    {
        displayTimer.stop();
        this.simFrame.setVisible(false);
        this.killSim = true;

        //sleep to give the main thread (running the sim) the chance to return its results
        try
        {
            Thread.sleep(3000);
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
            Logger.getLogger(SimulationEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayAnimation()
    {
        simFrame.setBattlefield(this.battlefield);
        simFrame.setVisible(true);

        displayTimer = new javax.swing.Timer(this.delayBetweenTicksMS, new ActionListener()
        {
            int lastUpdateToSlider = 0;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                //update the slider's max every 10 scenes or when simulation is complete
                if (((numLoops % 10 == 0) || simulationComplete) && (lastUpdateToSlider != numLoops))
                {
                    simFrame.updateMaxScenes(numLoops);
                    lastUpdateToSlider = numLoops;
                }

                if ((sceneNumber <= numLoops) && !pauseAnimation)
                {
                    simFrame.updateSlider(sceneNumber, numLoops);
                    sceneNumber++;
                } else if (simulationComplete)
                {
                    simFrame.updateSlider(sceneNumber, numLoops);
                }

            }
        });

        displayTimer.start();
    }

    public void paintSceneToFileInBatches()
    {
        this.paintSceneToFileInBatches(0, this.fileStorePath, 100);
    }

    //this function paints all scenes to file, but uses the battlefield drawing from scene 0 every time
    public void paintSceneToFileInBatches(int sceneNumberToPaint, String directory, int scale)
    {
        CopyOnWriteArrayList<BufferedImage> images = new CopyOnWriteArrayList<>();
        String path = directory;
        System.out.println("PAINT BATCH...");
        int width = this.seed.battlefield.getPixelsWidth() + 1;
        int height = this.seed.battlefield.getPixelsHeight() + 1;
        int newWidth = (int) (width * ((double) scale / 100));
        int newHeight = (int) (height * ((double) scale / 100));
        for (SceneSnapshot scene : this.scenes)
        {

            System.out.println("printing scene: " + scene.getSceneNumber());

            BufferedImage imageToDraw
                    = new BufferedImage(width,
                            height, BufferedImage.BITMASK);
            Graphics2D g2 = imageToDraw.createGraphics();
            Rectangle fieldOutline = new Rectangle(0, 0, width, height);
            g2.setColor(Color.WHITE);
            g2.fill(fieldOutline);
            scene.paintScene(g2);
            g2.dispose();

            imageToDraw.setAccelerationPriority(0.5F);
            imageToDraw = Scalr.resize(imageToDraw, Scalr.Method.BALANCED, newWidth, newHeight);

            try
            {
                //  System.out.println("saving scene...: " + scene.getSceneNumber());
                //BufferedImage imageToDraw = images.get(scene.getSceneNumber() - scenes.get(0).getSceneNumber());
                String imageName = path + File.separator + "scene" + String.format("%05d", scene.getSceneNumber()) + ".png";

                File outputfile = new File(imageName);
                ImageIO.write(imageToDraw, "png", outputfile);

                //System.out.println("done saving scene: " + scene.getSceneNumber());
            } catch (IOException ex)
            {
                ex.printStackTrace();
                System.out.println("CRASH!");
                Logger.getLogger(SimulationEngine.class.getName()).log(Level.SEVERE, null, ex);

            }
        }

        System.out.println("done with batch!");
    }

    public BufferedImage paintBattlefield(int tickNumber)
    {
        BufferedImage tickImage
                = new BufferedImage(this.seed.battlefield.getPixelsWidth() + 1,
                        this.seed.battlefield.getPixelsHeight() + 1, BufferedImage.SCALE_SMOOTH);

        Graphics2D g2 = tickImage.createGraphics();
        this.battlefield.paintBattlefield(g2, tickNumber);
        if (tickNumber < this.scenes.size())
        {
            this.scenes.get(tickNumber).paintScene(g2);
        }

        g2.dispose();
        tickImage.setAccelerationPriority(0.5F);
        return tickImage;

    }

    private void processActions()
    {
        List<Action> actionsToRemove = new CopyOnWriteArrayList<>();
        List<Action> actionsToRedo = new CopyOnWriteArrayList<>();

        this.actions.parallelStream().forEach(action ->
        {
            BattlefieldObject bObject = action.getBattlefieldObject();

            if (action.getDelayTicks() > 0)
            {
                bObject.setCurrentAction(action);
                action.delay();

            } else if (bObject.isActive())
            {
                action.processAction(this.unitList, this.battlefield);
                if (RangedAttack.class.isAssignableFrom(action.getClass()))
                {
                    RangedAttack rangedAttack = (RangedAttack) action;
                    rangedAttack.processAction(this.unitList, this.battlefield);
                    Projectile projectile = rangedAttack.getProjectile();
                    if (projectile != null)
                    {
                        this.activeBattlefieldObjects.add(projectile);
                    }

                }

                if (Utils.pointOutOfBounds(bObject.getCenterPoint(),
                        this.seed.battlefield.getPixelsWidth(),
                        this.seed.battlefield.getPixelsHeight()))
                {
                    bObject.setOutOfBounds();
                }

                bObject.setCurrentAction(null);
                actionsToRemove.add(action);
            }
        });
        this.actions.removeAll(actionsToRemove);
        this.actions.addAll(actionsToRedo);
    }

    //todo:  make this more efficient (use grid cell search
    private void setObjectAwareness(BattlefieldObject bObject, short radius)
    {
        long distanceToEnemy = Integer.MAX_VALUE;
        long distanceToAlly = Integer.MAX_VALUE;
        long awarenessRadiusSquared = bObject.getAwarenessRadius() * bObject.getAwarenessRadius();
        long threatenedRadiusSquared = bObject.getThreatenedRadius() * bObject.getThreatenedRadius();
        long reachSquared = bObject.getReach() * bObject.getReach();
        BattlefieldObject enemy = null;
        BattlefieldObject ally = null;
        boolean bObjectHasAllegiance = bObject.hasAllegiance();
        String bObjectAllegiance = bObject.getAllegience();

        bObject.clearKnownObjects();
        bObject.clearThreatenedObjects();
        bObject.clearReachableObjects();
        bObject.clearEnemyObjects();
        //TEMP:  Trying to do get Cells, and then loop over them here, rather than return the arrayList of their objects.
        //ArrayList<BattlefieldObject> possibleObjects = battlefield.getObjectsWithin(bObject.getCenterPoint(), radius);
        ArrayList<BattlefieldCell> cellsConcerned = battlefield.getCellsWithin(bObject.getCenterPoint(), radius);
        for (BattlefieldCell cell : cellsConcerned)
        {
            //ArrayList<BattlefieldObject> possibleObjects = cell.getBattlefieldObjects();
            //for (int bObjectIndex = 0; bObjectIndex < possibleObjects.size(); bObjectIndex++) {
            for (BattlefieldObject otherObject : cell.getBattlefieldObjects())
            {
                //BattlefieldObject otherObject = possibleObjects.get(bObjectIndex);
                long distanceAway = Utils.getSquaredDistance(bObject.getCenterPoint(), otherObject.getCenterPoint());

                boolean otherObjectIsActive = otherObject.isActive();
                boolean thisEqualsOther = bObject.equals(otherObject);
                boolean otherHasAllegiance = otherObject.hasAllegiance();
                //find nearest ally
                if (bObjectHasAllegiance
                        && !thisEqualsOther
                        && otherHasAllegiance
                        && bObjectAllegiance.equals(otherObject.getAllegience())
                        && distanceAway < distanceToAlly
                        && !bObject.equals(otherObject)
                        && otherObjectIsActive)
                {
                    ally = otherObject;
                    distanceToAlly = distanceAway;
                } //find nearest enemy
                else if (bObjectHasAllegiance
                        && !thisEqualsOther
                        && otherHasAllegiance
                        && !bObjectAllegiance.equals(otherObject.getAllegience())
                        && distanceAway < distanceToEnemy
                        && otherObjectIsActive)
                {
                    enemy = otherObject;
                    bObject.addEnemyObject(enemy);
                    distanceToEnemy = distanceAway;
                }

                if (!thisEqualsOther && (distanceAway < reachSquared) && otherObjectIsActive)
                {
                    bObject.addReachableObject(otherObject);
                    bObject.addThreatenedObject(otherObject);
                    bObject.addKnownObject(otherObject);
                } else if (!thisEqualsOther && (distanceAway < threatenedRadiusSquared) && otherObjectIsActive)
                {
                    bObject.addThreatenedObject(otherObject);
                    bObject.addKnownObject(otherObject);
                } else if (!thisEqualsOther && (distanceAway < awarenessRadiusSquared) && otherObjectIsActive)
                {
                    bObject.addKnownObject(otherObject);
                }

            }//for all other objects
        }
        bObject.setNearestAlly(ally);
        bObject.setNearestEnemy(enemy);

        if (enemy != null)
        {
            bObject.setDistanceToEnemy(distanceToEnemy);
        }
    }

    private void processCombatResults()
    {
        this.combatResults.parallelStream().forEach(result ->

        {
            if (result != null)
            {
                CombatResult.CombatOutcome outcome = result.getOutcome();
                BattlefieldObject subject = result.getSubject();
                BattlefieldObject inflictor = result.getInflictor();
                switch (outcome)
                {
                    case DEFENDED:
                        break;
                    case KILLED:
                        subject.markKilled();
                        inflictor.addKillCount();

                        break;
                    case PUSHED:
                        break;
                    case WOUNDED:
                        subject.receiveWound();

                        break;

                }
            }
        });
    }
}
