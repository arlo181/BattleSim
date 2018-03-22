/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import Objects.Projectile;
import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Unit;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class ProjectileStrike extends Action
{

    Point strikingPoint;
    int radiusEffect;

    public ProjectileStrike(int delayTicks, Projectile projectile, Point strikingPoint, int radiusEffect)
    {
        super(delayTicks, projectile);
        this.strikingPoint = strikingPoint;
        this.radiusEffect = radiusEffect;
    }

    public Point getStrikingPoint()
    {
        return strikingPoint;
    }

    public int getRadiusEffect()
    {
        return radiusEffect;
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {

        Projectile projectile = (Projectile) this.battlefieldObject;

        Point landingPoint = this.strikingPoint;
        projectile.setCenterPoint(landingPoint);
        for (BattlefieldObject struckObject : battlefield.getConflicts(projectile,
                landingPoint,
                projectile.getImpactRadius()))
        {
            projectile.strikeTarget(struckObject);
        }
        projectile.land();

    }

}
