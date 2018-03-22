/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import battlesimulator.Actions.Action;
import battlesimulator.BattlefieldObject;
import battlesimulator.BattlefieldRoster;
import battlesimulator.ObjectState;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Arlo
 */
public class UnitBlock extends BattlefieldObject
{
    String allegiance;
    String name;
    short maximumSpeed;
    Point centerPoint;
    Map<UUID, Point> positionsMap;
    Officer officer;
    
    public UnitBlock(String allegiance, String name, Officer officer) {
        this(allegiance, name, new Point(0, 0), officer);
    }

    public UnitBlock(String allegiance, String name, Point centerPoint, Officer officer) {
        super(centerPoint);
        this.allegiance = allegiance;
        this.name = name;
        this.positionsMap = new HashMap<>();
        this.officer = officer;
    }
    
    public void addSoliders (CopyOnWriteArrayList<Soldier> soldiers, Point centerPoint)
    {
        this.positionsMap.clear();
        this.centerPoint = centerPoint;
        long distanceSquared = Integer.MAX_VALUE;
        soldiers.parallelStream().forEach(soldier -> {
            Point point = soldier.getCenterPoint();
            int difX = point.x - centerPoint.x;
            int difY = point.y - centerPoint.y;
        this.positionsMap.put(soldier.getObjectID(), new Point(difX, difY));
    });
        
        this.personalSpaceRadius = (short)Math.sqrt(distanceSquared);
    }

    @Override
    public void receiveAction(Action action)
    {    }

    @Override
    public Action tick(int tickElapsed)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasAllegiance()
    {
        return true;
    }

    @Override
    public String getAllegience()
    {
        return this.officer.getAllegience();
    }

    @Override
    public ObjectState generateState(int tickNumber)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getClassScore()
    {
        return 0;
    }

    @Override
    public boolean isActive()
    {
        return true;
    }
    
    public CopyOnWriteArrayList<Soldier> getAllSoldiers()
    {
      CopyOnWriteArrayList<Soldier> allSoldiers = new CopyOnWriteArrayList<>();
      
      for(UUID soldierId : this.positionsMap.keySet())
      {
          BattlefieldObject bObject =  BattlefieldRoster.Instance().lookup(soldierId);
          if(bObject != null)
          {
              allSoldiers.add((Soldier)bObject);
          }
      }
      return allSoldiers;
    }

    @Override
    public void setOutOfBounds()
    {
       for(Soldier soldier: this.getAllSoldiers())
       {
           soldier.setOutOfBounds();
       }
    }

    @Override
    public void markKilled()
    {
        
    }

    @Override
    public CombatResult resolveCombat()
    {
        //todo
        return null;
    }

    @Override
    public void receiveWound()
    {
        //todo
    }

    @Override
    public void evaluateBonuses()
    {
        
    }
    
}
