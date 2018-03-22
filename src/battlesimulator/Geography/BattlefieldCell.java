/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import battlesimulator.BattlefieldObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Arlo
 */
public class BattlefieldCell extends Region implements java.io.Serializable {
    double elevation;
    int row;
    int column;
    List<BattlefieldObject> battlefieldObjects =new CopyOnWriteArrayList<>();
    //enum for footing
    
    public BattlefieldCell(Rectangle rectangle, int row, int column)
    {
        super(rectangle);
        this.row = row;
        this.column = column;
    }

    public double getElevation() {
        return elevation;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    
    

    public List<BattlefieldObject> getBattlefieldObjects() {
        return battlefieldObjects;
    }
    
    public void addBattlefieldObject (BattlefieldObject bObject)
    {
        this.battlefieldObjects.add(bObject);
    }
    
     public void removeBattlefieldObject (BattlefieldObject bObject)
    {
        this.battlefieldObjects.remove(bObject);
    }
     
     public void paintObject(Graphics2D g, int tickNumber) {
        
        
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.BLACK);
        g.draw(this.shape);

        
    }
    
}
