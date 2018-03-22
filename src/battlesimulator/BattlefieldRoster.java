/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Arlo
 */
public class BattlefieldRoster
{

    HashMap<UUID, BattlefieldObject> roster = new HashMap();
    static BattlefieldRoster battlefieldRoster;
public BattlefieldRoster()
{
    
}

public static BattlefieldRoster Instance()
{
    if(battlefieldRoster == null)
    {
        battlefieldRoster = new BattlefieldRoster();
    }
    
    return battlefieldRoster;
}
    public BattlefieldObject lookup(UUID id)
    {
        return roster.get(id);
    }

    public void addObject(BattlefieldObject object)
    {
        this.roster.put(object.getObjectID(), object);
    }
}
