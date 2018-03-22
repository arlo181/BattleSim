/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import Objects.ArchProjectile;
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
public class ArchProjectileMove extends Action{
    public ArchProjectileMove(int delayTicks, BattlefieldObject battlefieldObject)
    {
        super(delayTicks, battlefieldObject);
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {

                    ArchProjectile projectile = (ArchProjectile) this.battlefieldObject;
                    Point destination = Utils.getPointAtDistanceInDirection(projectile.getCenterPoint(),
                            Utils.findBearing(projectile.getCenterPoint(), projectile.getTargetLocation()),
                            projectile.getSpeed());
                    projectile.setCenterPoint(destination);
    }
}
