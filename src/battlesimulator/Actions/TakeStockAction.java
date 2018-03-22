/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Actions;

import battlesimulator.Geography.Battlefield;
import battlesimulator.Military.General;
import battlesimulator.Military.Unit;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class TakeStockAction extends Action{
    public TakeStockAction(int delayTicks, General general)
    {
        super(delayTicks, general);
    }
    
    public General getGeneral()
    {
        return ((General)this.battlefieldObject);
    }

    @Override
    public void processAction(ArrayList<Unit> unitList, Battlefield battlefield)
    {
        this.getGeneral().takeStock(unitList);
    }
    
}
