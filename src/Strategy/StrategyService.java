/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arlo
 */
public class StrategyService
{
    ArrayList<Class<? extends AbstractStrategy>> strategyClasses;
    static StrategyService instance;
     private ServiceLoader<AbstractStrategy> loader;
    
    public static StrategyService getInstance()
    {
        if(instance == null)
        {
            instance = new StrategyService();
        }
        
        return instance;
    }
    private StrategyService()
    {
         loader = ServiceLoader.load(AbstractStrategy.class);
         Iterator<AbstractStrategy> iter =  loader.iterator();
         this.strategyClasses = new ArrayList<>();
       while(iter.hasNext())
       {
        this.strategyClasses.add(iter.next().getClass());
       }
    }
    
    public  ArrayList<Class<? extends AbstractStrategy>> getAllStratClasses()
    {
       return this.strategyClasses;
    }
    
    public ArrayList<AbstractStrategy> getStratsList()
    {
        ArrayList <AbstractStrategy> stratsList = new ArrayList<>();
        for(Class<? extends AbstractStrategy> stratClass : this.strategyClasses)
        {
            try
            {
                AbstractStrategy strat = stratClass.newInstance();
                stratsList.add(strat);
            } catch (InstantiationException | IllegalAccessException ex)
            {
                ex.printStackTrace();
                Logger.getLogger(StrategyService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return stratsList;
    }
    
    public ArrayList<String> getStratsNames()
    {
        ArrayList <String> stratsNameList = new ArrayList<>();
        for(Class<? extends AbstractStrategy> stratClass : this.strategyClasses)
        {
            stratsNameList.add(stratClass.getSimpleName());
        }
        return stratsNameList;
    }
}
