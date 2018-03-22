/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitFormations;

import java.awt.Point;

/**
 *
 * @author Arlo
 * @param <T> The object that is taking positions in the formation
 */
public class PositionInFormation<T> implements java.io.Serializable {

    Point locationWithinFormation;
    T objectInPosition;
    String positionName;
    int positionIndex;

    public PositionInFormation(Point locationWithinFormation, T objectInPosition, int index) {
        this.locationWithinFormation = locationWithinFormation;
        this.objectInPosition = objectInPosition;
        this.positionName = "";

    }

    public PositionInFormation(String name, Point locationWithinFormation, T objectInPosition, int index) {
        this(locationWithinFormation, objectInPosition, index);
        this.positionName = name;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    
    
    public Point getLocationWithinFormation() {
        return locationWithinFormation;
    }

    public String getPositionName() {
        return positionName;
    }

    public T getObjectInPosition() {
        return objectInPosition;
    }

    public void setLocationWithinFormation(Point locationWithinFormation) {
        this.locationWithinFormation = locationWithinFormation;
    }

    public boolean postionTaken() {
        return objectInPosition != null;
    }
    //TODO:  Maybe a standing order for this position in the formation?

    public void setObjectInPosition(T objectInPosition) {
        this.objectInPosition = objectInPosition;
    }
}
