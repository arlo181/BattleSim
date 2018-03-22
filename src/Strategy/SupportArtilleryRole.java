/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy;

import battlesimulator.Military.Unit;
import battlesimulator.Military.UnitMetrics;

/**
 *
 * @author Arlo
 */
public class SupportArtilleryRole extends AbstractRole{

    public SupportArtilleryRole(String name) {
        super(name);
    }

    @Override
    public boolean canFulfillRole(Unit.UnitStateAlbum metrics) {
       return metrics.getLatestMetrics().mostlyRanged();
    }
    
}
