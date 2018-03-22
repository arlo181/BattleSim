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
public class SweepAttackAction extends Action
{

    int radius;
    int startAngle;
    int endAngle;

    public SweepAttackAction(BattlefieldObject attacker, int delay, int radius, int startAngle, int endAngle)
    {
        super(delay, attacker);
        this.radius = radius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;

    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
        Point thisCenterPoint = this.battlefieldObject.getCenterPoint();
        for (BattlefieldObject bObject : battlefield.getObjectsWithin(thisCenterPoint, this.radius))
        {
            if (!bObject.equals(this.getBattlefieldObject()))
            {
                double bearing = Utils.findBearing(thisCenterPoint, bObject.getCenterPoint());
                int distanceApart = this.battlefieldObject.distanceTo(bObject);
                if (distanceApart <= this.radius && Utils.angleIsBetween(this.startAngle, this.endAngle, bearing))
                {
                    bObject.addAttacker(this.battlefieldObject);
                }
            }
        }
    }

}
