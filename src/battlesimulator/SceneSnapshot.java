/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import battlesimulator.Military.Army.ArmyStats;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Arlo
 */
public class SceneSnapshot {
    CopyOnWriteArrayList<ObjectState> objectStates;
    int sceneNumber;
    HashMap<String, ArmyStats> armyStats;
    
    public SceneSnapshot(int sceneNumber)
    {
        this.sceneNumber = sceneNumber;
        objectStates = new CopyOnWriteArrayList<>();
        armyStats = new HashMap<>();
    }
    
    public void addStateToScene(ObjectState state)
    {
        if(state != null)
        {
        this.objectStates.add(state);
        }
    }
    
    public void addArmyStats(ArmyStats armyStats)
    {
        this.armyStats.put(armyStats.getAllegiance(), armyStats);
    }
    
   //TODO: paint scene
    
    public void paintScene(Graphics2D graphics)
    {
         
        for(ObjectState state : this.objectStates)
        {
           
            state.paintObject(graphics);
        }
    }

    public int getSceneNumber()
    {
        return sceneNumber;
    }
    
        public  ArrayList<ArmyStats> getArmyStats() {
  ArrayList<ArmyStats> stats = new ArrayList<>();
        for (Map.Entry armyEntry : this.armyStats.entrySet()) {
            stats.add((ArmyStats)armyEntry.getValue());
        }
        
        return stats;
    }
    
}
