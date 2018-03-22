/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator;

import java.awt.Graphics2D;
import java.io.Serializable;

/**
 *
 * @author Arlo
 */
public abstract class ObjectState implements Serializable{
    
    
    public abstract void paintObject(Graphics2D g);
}
