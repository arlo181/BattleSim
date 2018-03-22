/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Arlo
 */
public abstract class Region implements java.io.Serializable{
    Shape shape;
public Region(Shape shape)
{
    this.shape = shape;
}
    public Shape getShape()
    {
        return shape;
    }

    
     public Shape translateShapeToParentCoordinateSystem(Point startingPoint)
    {
        AffineTransform transform = AffineTransform.getTranslateInstance(
              startingPoint.x, startingPoint.y);
      return transform.createTransformedShape(shape);
    }
    
    public boolean containsPoint(Point testPoint)
    {
        return this.shape.contains(testPoint);
    }
    
    
    
    
}
