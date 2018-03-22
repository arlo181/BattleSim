/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Unit;
import battlesimulator.Utils;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class MoveTowardNearestEnemyAction extends MoveAction {

    public MoveTowardNearestEnemyAction(BattlefieldObject battlefieldObject, int speedPixels, MovementModes mode)
    {
        super(battlefieldObject, speedPixels, 0, mode);
    }
    
    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
        BattlefieldObject enemy = this.battlefieldObject.getNearestEnemy();
                    double direction = 0;
                    double distanceBetween = Double.MAX_VALUE;
                    if (enemy != null)
                    {
                        this.battlefieldObject.setTarget(enemy);
                        direction = Utils.findBearing(this.battlefieldObject.getCenterPoint(), enemy.getCenterPoint());
                        distanceBetween = this.battlefieldObject.getCenterPoint().distance(enemy.getCenterPoint()) - this.battlefieldObject.getPersonalSpaceRadius() - enemy.getPersonalSpaceRadius();
                        if (Utils.targetInReach(this.battlefieldObject, enemy, this.battlefieldObject.getReach()))
                        {
                            distanceBetween = 0;
                        }
                    } else
                    {
                        direction = Utils.findBearing(this.battlefieldObject.getCenterPoint(), findNearestEnemyUnitCenterPoint(this.battlefieldObject, unitList));

                    }
                    double speedOfAction = this.speedPixels;
                    this.speedPixels = (int) Math.floor(Math.min(speedOfAction, distanceBetween));
                    this.directionCardinal = direction;
                     super.processAction(unitList, battlefield);
    }
    
    
    private Point findNearestEnemyUnitCenterPoint(BattlefieldObject incitingObject, ArrayList<Unit> unitList)
    {

        //break the search in half by only searching through enemy army(ies)
        Point pointToFind = new Point(0, 0);//null;
        long minDistance = Long.MAX_VALUE;
        String incitingAllegiance = incitingObject.getAllegience();
        
                for (Unit unit : unitList)
                {
                    if (!unit.getAllegiance().equals(incitingAllegiance) && unit.getNumActive() > 0 )
                    {
                        if (unit.getCenterMass().equals(new Point(0, 0)))
                        {
                            System.out.print("");
                        }
                        long distance = Utils.getSquaredDistance(unit.getCenterMass(), incitingObject.getCenterPoint());
                        if (distance < minDistance)
                        {
                            minDistance = distance;
                            pointToFind = unit.getCenterMass();

                        }
                    } 

                }
            

        return pointToFind;
    }
}
