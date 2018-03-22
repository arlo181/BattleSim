/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Army;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class SimulationSeed implements java.io.Serializable {
    Battlefield battlefield;
        ArrayList<Army> armies = new ArrayList<>();
    public SimulationSeed(Battlefield battlefield, Army... armies)
    {
        this.battlefield = battlefield;
       for(Army army : armies)
       {
           this.armies.add(army);
       }
    }
    
     public SimulationSeed()
    {
      
    }

    public ArrayList<Army> getArmies() {
        return armies;
    }
    
    

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public void setBattlefield(Battlefield battlefield) {
        this.battlefield = battlefield;
    }

    public void setArmies(Army... armies) {
        this.armies.clear();
       for(Army army : armies)
       {
           this.armies.add(army);
       }
    }
    
        public void setArmies(ArrayList<Army> armies) {
        this.armies.clear();
       for(Army army : armies)
       {
           this.armies.add(army);
       }
    }
    
    public void addArmy(Army army)
    {
        this.armies.add(army);
    }
    
    
    
    
}
