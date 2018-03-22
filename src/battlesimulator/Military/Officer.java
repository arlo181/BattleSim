/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Military;

import UnitFormations.BaseUnitFormation;
import Orders.AbstractOrder;
import Orders.AbstractOrderDuration;
import Orders.AssumeFormation;
import Orders.Countdown;
import Orders.Forever;
import Orders.MarchOrder;
import Orders.UntilComplete;
import Orders.UntilEngaged;
import Orders.UntilTime;
import Strategy.AbstractObjective;
import battlesimulator.Actions.Action;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Arlo
 */
public class Officer extends Soldier implements Leader
{
    public static final short OFFICER_CLASS_SCORE=5;
    Unit unit;
    AbstractOrderDuration currentDuration;
    LinkedList<AbstractOrder> queuedOrders;
    AbstractObjective objective;
    boolean takingFormation = false;
    //todo: need a better way to update formation, rather than retaking formation...
    //todo:  maybe... take formation, then move/rotate formation?  Those can be separate objectives
    boolean inFormation;

    public Officer(String allegiance, String name)
    {
        super(allegiance, name);
        this.speed = 6;
        queuedOrders = new LinkedList<AbstractOrder>();
        this.defense = 50;
        this.strength = 15;
        this.awarenessRadius = 500;
        this.threatenedRadius = 500;
        this.personalSpaceRadius = 4;
        this.pushPower = 8;
        this.initiative = 100;
        this.inFormation = false;
    }

        @Override
    public int getClassScore()
    {
        return Officer.OFFICER_CLASS_SCORE;
    }
    
    @Override
    //this is where the decision logic is for what orders to issue
    public synchronized  void issueOrders()
    {
        AbstractOrder order = null;
        if (this.objective != null)
        {
            order = this.objective.getNewestOrders(this.unit.getStateAlbum().getLatestMetrics());
            this.currentDuration = order.getDuration();
        } else if (!this.queuedOrders.isEmpty())
        {

            order = this.queuedOrders.poll();
            this.currentDuration = order.getDuration();
        }
        if (order != null && order.isInFormation())
        {
            int minimumSpeed = order.getSpeed();
            for (Soldier subordinate : this.getSubordinates())
            {
                minimumSpeed = Math.min(minimumSpeed, subordinate.getSpeed());
            }
            order.setSpeed(minimumSpeed);
        }

        if (order != null && AssumeFormation.class.isAssignableFrom(order.getClass()))
        {
            this.formation = ((AssumeFormation) order).getFormation();
            this.takingFormation = true;
        }

        this.orders = order;
        for (Soldier subordinate : this.getSubordinates())
        {
            subordinate.receiveOrder(order, true);
            this.orders = order;
        }
    }

    public Unit getUnit()
    {
        return unit;
    }

    @Override
    public short needsAwarenessWithin()
    {
        return this.awarenessRadius;
    }

    public void relayOrders()
    {

        for (Soldier subordinate : this.getSubordinates())
        {
            //subordinate.receiveOrder(this.orders, true);
            subordinate.receiveOrder(new MarchOrder(90, 1, false, new Forever()), true);
        }
    }

    public void setUnitFormation(BaseUnitFormation formation)
    {
        this.formation = formation;

        this.formation.claimOfficerPosition((Officer) this);

        this.indexOfFormationPosition = -1;
        this.setCenterPoint(this.formation.getOffsetOfPosition(indexOfFormationPosition));

    }

    public void setOrderQueue(LinkedList<AbstractOrder> orders)
    {
        this.queuedOrders.addAll(orders);

    }

    public void addOrderToQueue(AbstractOrder order)
    {
        this.queuedOrders.add(order);
    }

    @Override
    public void receiveOrder(AbstractOrder order, boolean supercede)
    {
        if (supercede)
        {
            this.supercedeOrders(order);
        } else
        {
            this.orders = order;
            this.addOrderToQueue(order);
        }
    }

    public synchronized void  setObjective(AbstractObjective objective)
    {
        this.queuedOrders.clear();
        this.objective = objective;
        this.currentDuration = null;
        this.orders = null;
    }

    public synchronized void supercedeOrders(AbstractOrder order)
    {
        this.currentDuration = null;
        this.orders = order;
        this.queuedOrders.clear();
        this.addOrderToQueue(order);
        this.relayOrders();
    }

    @Override
    public ArrayList<Soldier> getSubordinates()
    {
        return this.unit.getSoldiers();
    }

    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    @Override
    public Action tick(int ticksElapsed)
    {
        directionRandomizer++;
        Action action;
        boolean waiting = keepWaiting(ticksElapsed);
        if (!waiting)
        {
            if (this.orders != null && AssumeFormation.class.isAssignableFrom(this.orders.getClass()))
            {
                if (this.takingFormation)
                {
                    this.inFormation = true;
                    this.takingFormation = false;
                    this.unit.setInFormation(this.inFormation);
                }
            }
            issueOrders();

        }
        this.unit.setInFormation(this.inFormation);

        action = super.tick(ticksElapsed);//new MoveAction(this, 1, 90, MovementModes.DELAYED);

        return action;
    }

    @Override
    public synchronized boolean keepWaiting(int ticksElapsed)
    {
        boolean keepWaiting = false;
        if (this.currentDuration != null)
        {
            if (this.currentDuration != null && UntilComplete.class.isAssignableFrom(this.currentDuration.getClass()))
            {
                boolean subordinatesReady = true;
                UntilComplete untilComplete = (UntilComplete) this.currentDuration;
                untilComplete.AddTick();
                double numSubordinatesReady = 0.0;
                int numActive = 0;

                for (Soldier subordinate : this.getSubordinates())
                {
                    if (subordinate.isActive())
                    {
                        numActive++;
                        if (subordinate.state == SoldierConditions.READY_FOR_ORDERS)
                        {
                            numSubordinatesReady++;
                        }
                    }

                    //subordinatesReady &= ((subordinate.state == SoldierConditions.READY_FOR_ORDERS)
                    //|| (!subordinate.isActive()));
                }
                //ready to go if 95% of the units are in formation.
                subordinatesReady = (numSubordinatesReady / numActive) >= 0.90;

                keepWaiting = !subordinatesReady && !untilComplete.countdownComplete();
            } else if (this.currentDuration != null && UntilEngaged.class.isAssignableFrom(this.currentDuration.getClass()))
            {
                double subordinatesEngaged = 0.0;
                double activeSubordinates = 0.0;

                UntilEngaged untilEngaged = (UntilEngaged) this.currentDuration;
                untilEngaged.AddTick();

                for (Soldier subordinate : this.getSubordinates())
                {
                    if (subordinate.state == SoldierConditions.IN_COMBAT)
                    {
                        subordinatesEngaged++;
                    }
                    if (subordinate.isActive())
                    {
                        activeSubordinates++;
                    }
                }

                keepWaiting = ((subordinatesEngaged / activeSubordinates) < untilEngaged.getPercentage()) && !untilEngaged.countdownComplete(); //if 10 percent are engaged?  TODO:  set percentage in order
                if (keepWaiting == false)
                {
                }
            } else if (this.currentDuration != null && Countdown.class.isAssignableFrom(this.currentDuration.getClass()))
            {
                Countdown countdown = (Countdown) this.currentDuration;
                countdown.AddTick();
                keepWaiting = !countdown.countdownComplete();

            } else if (this.currentDuration != null && UntilTime.class.isAssignableFrom(this.currentDuration.getClass()))
            {
                UntilTime untilTime = (UntilTime) this.currentDuration;

                keepWaiting = !untilTime.isItTIme(ticksElapsed);

            } else if (this.currentDuration != null && Forever.class.isAssignableFrom(this.currentDuration.getClass()))
            {
                keepWaiting = true;

            }
        }
        return keepWaiting;
    }

    @Override
    public void setOutOfBounds()
    {
        super.setOutOfBounds();
        this.clearOrders();
    }

    @Override
    public void markKilled()
    {
        super.markKilled();
        this.clearOrders();
    }

    protected void clearOrders()
    {
        for (Soldier subordinate : this.getSubordinates())
        {
            subordinate.receiveOrder(null, true);
        }
    }
}
