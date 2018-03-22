/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import Objects.Projectile;
import battlesimulator.BattlefieldObject;
import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.Unit;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class RangedAttack extends Action
{

    Projectile projectile;

    public RangedAttack(int delayTicks, BattlefieldObject battlefieldObject, Projectile projectile)
    {
        super(delayTicks, battlefieldObject);
        this.projectile = projectile;
    }

    public Projectile getProjectile()
    {
        return this.projectile;
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
        if (projectile != null)
        {
            if (!projectile.hasBeenFired())
            {
                battlefield.addNeutralObject(projectile);
            }
        }
    }

}
