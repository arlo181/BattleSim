/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import Orders.ChargeOrder;
import Orders.Countdown;
import Orders.EngageEnemiesOrder;
import Orders.Forever;
import Orders.MarchOrder;
import Strategy.AbstractObjective;
import Strategy.AbstractStrategy;
import Strategy.StrategyService;
import UnitFormations.BaseUnitFormation;
import battlesimulator.Actions.Action;
import battlesimulator.Actions.DoNothingAction;
import battlesimulator.Actions.FallOnYourSwordAction;
import battlesimulator.Actions.MoveAction;
import battlesimulator.Actions.MovementModes;
import battlesimulator.Actions.TakeStockAction;
import battlesimulator.Military.Army.ArmyStats;
import battlesimulator.Utils;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Arlo
 */
public class General extends Soldier implements Leader
{

    Army army;
    AbstractStrategy strategy;
    boolean waitingForCompletion;
    ArrayList<Unit.UnitStateAlbum> enemyUnitsMetrics;
    ArrayList<Unit.UnitStateAlbum> alliedUnitMetrics;
    Unit.UnitStateAlbum nearestEnemyUnit;
    Point nearestAlliedUnit;
    double safeDistance = 25;
    String preferedStrategy;


    public General(String allegiance, String name)
    {
        super(allegiance, name);
        this.speed = 3;
        this.waitingForCompletion = false;
        this.personalSpaceRadius = 6;
        this.enemyUnitsMetrics = new ArrayList<>();
        this.alliedUnitMetrics = new ArrayList<>();
        this.nearestEnemyUnit = null;
        this.nearestAlliedUnit = null;
        this.preferedStrategy = "";
    }

    public String getPreferedStrategy()
    {
        return preferedStrategy;
    }

    public void setPreferedStrategy(String preferedStrategy)
    {
        this.preferedStrategy = preferedStrategy;
    }

    @Override
    public void issueOrders()
    {

        //this is where the logic comes from for issuing orders
        for (Soldier subordinate : this.getSubordinates())
        {
            if (Officer.class.isAssignableFrom(subordinate.getClass()))
            {
                Officer officer = (Officer) subordinate;
                double bearingOfCharge = Utils.findBearing(officer.getUnit().getCenterMass(), this.nearestEnemyUnit.getLatestMetrics().getCenterMass());
                if (!officer.getUnit().getStateAlbum().getLatestMetrics().getBoundingRectangle().intersects(this.nearestEnemyUnit.getLatestMetrics().getBoundingRectangle()))
                {
                    MarchOrder march = new MarchOrder(bearingOfCharge, 5, true, new Countdown(14));
                    officer.receiveOrder(march, false);
                    //System.out.println("March " + officer.getUnit().getName() );
                } else if (!this.nearestEnemyUnit.getLatestMetrics().getBoundingRectangle().contains(officer.getUnit().getStateAlbum().getLatestMetrics().getCenterMass()))
                {
                    ChargeOrder charge = new ChargeOrder(bearingOfCharge, new Countdown(14));
                    officer.receiveOrder(charge, false);
                    //System.out.println("Charge " + officer.getUnit().getName() );
                } else
                {
                    EngageEnemiesOrder engageEnemies = new EngageEnemiesOrder(new Forever());
                    officer.receiveOrder(engageEnemies, true);
                    //System.out.println("Engage " + officer.getUnit().getName() );
                }
            }
        }

    }

    @Override
    public short needsAwarenessWithin()
    {
        return this.awarenessRadius;
    }

    @Override
    public ArrayList<Soldier> getSubordinates()
    {
        ArrayList<Soldier> officers = new ArrayList<>();
        ArrayList<Unit> units = this.army.getUnits();
        for (Unit unit : units)
        {
            officers.add(unit.getOfficer());
        }

        return officers;
    }

    public void takeStock(ArrayList<Unit> unitList)
    {
        this.enemyUnitsMetrics.clear();
        this.alliedUnitMetrics.clear();
        long allyDistance = Long.MAX_VALUE;
        long enemyDistance = Long.MAX_VALUE;
        //set list of enemy unit positions and find nearest enemy/ally unit to help keep general safe
        for (Unit unit : unitList)
        {
            long distance;
            if (!unit.getAllegiance().equals(this.getAllegience()))
            {
                Unit.UnitStateAlbum metrics = unit.getStateAlbum();
                Point enemyPoint = metrics.getLatestMetrics().getCenterMass();
                this.enemyUnitsMetrics.add(metrics);
                distance = Utils.getSquaredDistance(this.getCenterPoint(), enemyPoint);
                if (distance < enemyDistance)
                {
                    enemyDistance = distance;
                    this.nearestEnemyUnit = metrics;
                }
            } else
            {
                Unit.UnitStateAlbum metrics = unit.getStateAlbum();
                Point allyPoint = metrics.getLatestMetrics().getCenterMass();
                this.alliedUnitMetrics.add(metrics);
                distance = Utils.getSquaredDistance(this.getCenterPoint(), allyPoint);
                if (distance < allyDistance)
                {
                    allyDistance = distance;
                    this.nearestAlliedUnit = allyPoint;
                }
            }
        }
        if (this.strategy == null)
        {
            this.strategy = this.decideOnStrategy(this.alliedUnitMetrics, this.enemyUnitsMetrics);
            this.distributeObjectives();
        }
    }

    public AbstractStrategy decideOnStrategy(ArrayList<Unit.UnitStateAlbum> allies, ArrayList<Unit.UnitStateAlbum> enemies)
    {

        ArrayList<AbstractStrategy> strats;
        StrategyService service = StrategyService.getInstance();
        strats = service.getStratsList();

        //find if we've chosen a preferred strategy
        for (AbstractStrategy strat : strats)
        {
            if (strat.getClass().getSimpleName().equals(this.preferedStrategy))
            {
                if (strat.isValidStrategy(this, allies, enemies))
                {
                    this.strategy = strat;
                }
            }
        }
        Random rand = new Random();
        //use random valid strategy
        while (this.strategy == null)
        {
            int choice = rand.nextInt(strats.size());
            AbstractStrategy strat = strats.get(choice);
            if (strat.isValidStrategy(this, allies, enemies))
            {
                this.strategy = strats.get(choice);
            }
        }
        return strategy;
    }
    
    public void getArmyInFormation()
    {
        if(this.strategy != null)
        {
           Map<String, BaseUnitFormation> formationMap =  this.strategy.setInitialFormation();
           
           for(Unit unit : this.army.getUnits())
           {
               BaseUnitFormation unitFormation = formationMap.get(unit.getName());
               if(unitFormation != null)
               {
                   unit.setDefaultFormation(unitFormation);
               }
           }
        }
    }

    @Override
    public boolean keepWaiting(int ticksElapsed)
    {
        return this.waitingForCompletion;
    }

    public void setArmy(Army army)
    {
        this.army = army;
    }

    @Override
    public Action tick(int ticksElapsed)
    {
        Action action = new DoNothingAction(0, this);
        //take stock every 15 ticks.
        
        if (ticksElapsed == 1 || ticksElapsed % 15 == 0)
        {
            action = new TakeStockAction(0, this);
        } else if (this.nearestEnemyUnit != null)
        {
            action = maintainSafeDistance();
        }

        if (this.strategy != null && ticksElapsed % 3 == 0)
        {
            this.distributeObjectives();
        }

        //if the General is the last one left, have him do the honorable thing...
       if( this.army.getLatestStats().totalActiveTroops ==0)
       {
           action = new FallOnYourSwordAction(0, this);
       }
        
        return action;
    }

    protected void distributeObjectives()
    {
        Map<String, AbstractObjective> objectiveMap = this.strategy.determineObjectives();

        for (Unit unit : this.army.getUnits())
        {
            AbstractObjective objective = objectiveMap.get(unit.getName());
            if (objective != null)
            {
                unit.getOfficer().setObjective(objective);
            }
        }
    }

    protected Action maintainSafeDistance()
    {

        //find a point behind nearest friendly army, away from nearest enemy army.
        double bearing = Utils.findBearing(nearestEnemyUnit.getLatestMetrics().getCenterMass(), nearestAlliedUnit);
        Point safePlace = Utils.getPointAtDistanceInDirection(this.nearestAlliedUnit, bearing, this.safeDistance);
        double newBearing = Utils.findBearing(this.centerPoint, safePlace);
        MoveAction move = new MoveAction(this, this.speed, newBearing, MovementModes.PUSH);
        return move;
    }
}
