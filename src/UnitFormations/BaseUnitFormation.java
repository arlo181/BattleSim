/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitFormations;

import battlesimulator.Utils;
import battlesimulator.Military.Officer;
import battlesimulator.Military.Soldier;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class BaseUnitFormation implements java.io.Serializable {

    Point formationOrigin;
    ArrayList<PositionInFormation<Soldier>> positionsInFormation;
    PositionInFormation officersPosition;
    String name;

    public BaseUnitFormation(String name, Point formationOrigin, ArrayList<Point> pointsInFormation) {
        this(name, formationOrigin, pointsInFormation, formationOrigin);
    }

    public BaseUnitFormation(String name, Point formationOrigin, ArrayList<Point> pointsInFormation, Point officerPosition) {
        this(name, formationOrigin, officerPosition);
        int pointNum=0;
        for (Point point : pointsInFormation) {
            this.positionsInFormation.add(new PositionInFormation(point, null, pointNum));
            pointNum++;
        }
    }

    public BaseUnitFormation(String name, Point formationOrigin, Point officerPosition) {
        this.name = name;
        this.formationOrigin = formationOrigin;
        this.positionsInFormation = new ArrayList<>();
        officersPosition = new PositionInFormation(officerPosition, null, -1);


    }
    
    public Point getTopLeft()
    {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
          for (PositionInFormation position : this.positionsInFormation) {
                  minX = Math.min(minX, position.locationWithinFormation.x);
                  minY = Math.min(minY, position.locationWithinFormation.y);
        }
          
          return new Point (minX+this.formationOrigin.x, minY+this.formationOrigin.y);
    }
    
    
    public int getHeight()
    {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
          for (PositionInFormation position : this.positionsInFormation) {
                  minX = Math.min(minX, position.locationWithinFormation.x);
                  maxX = Math.max(maxX, position.locationWithinFormation.x);
        }
          
          return maxX - minX;
    }


    public void rotateFormation(int degreesClockwise, Point pointToRotateAround) {
        for (PositionInFormation position : this.positionsInFormation) {
            position.setLocationWithinFormation(
                    Utils.rotatePoint(position.getLocationWithinFormation(), 
                    pointToRotateAround, 
                    degreesClockwise));
        }
        this.setOfficerPosition( Utils.rotatePoint(
                this.officersPosition.getLocationWithinFormation(), 
                pointToRotateAround, 
                degreesClockwise));
    }
    


    public Point getFormationOrigin() {
        return formationOrigin;
    }

    public PositionInFormation getOfficersPosition() {
        return officersPosition;
    }
    
    public void setFormationTopLeft(Point topLeft)
    {
        Point currentTopLeft = this.getTopLeft();
        int offsetX = this.formationOrigin.x - currentTopLeft.x;
        int offsetY = this.formationOrigin.y - currentTopLeft.y;
        
        
        this.formationOrigin = new Point (topLeft.x + offsetX, topLeft.y + offsetY);
    }

    public void setFormationOrigin(Point formationOrigin) {
        this.formationOrigin.x = formationOrigin.x;
        this.formationOrigin.y = formationOrigin.y;
    }
    
        public void movePointsInFormation(int x, int y) {
          for (PositionInFormation position : this.positionsInFormation) {
                   position.getLocationWithinFormation().translate(x, y);
        }
       
                this.officersPosition.getLocationWithinFormation().translate(x, y);
    }

    public void moveFormation(int x, int y) {
        this.formationOrigin.translate(x, y);
    }

    public Point getOffsetOfPosition(int positionIndex) {
        Point pointInFormation = new Point(0,0);
        if (positionIndex < 0) {
            pointInFormation = this.officersPosition.getLocationWithinFormation();
        } else if (!this.positionsInFormation.isEmpty() && positionIndex >= this.positionsInFormation.size()){

            pointInFormation = this.positionsInFormation.get(0).getLocationWithinFormation();
        }
        else if (!this.positionsInFormation.isEmpty())
        {
            pointInFormation = this.positionsInFormation.get(positionIndex).getLocationWithinFormation();
        }

        return new Point(pointInFormation.x + this.formationOrigin.x,
                pointInFormation.y + this.formationOrigin.y);


    }

    public void setOfficerPosition(Point point) {
        this.officersPosition = new PositionInFormation(point, this.officersPosition.getObjectInPosition(), -1);
    }

    public void claimOfficerPosition(Officer officer) {
        this.officersPosition.setObjectInPosition(officer);
    }

    public void claimPositionInFormation(int positionIndex, Soldier soldier) {
        this.positionsInFormation.get(positionIndex).setObjectInPosition(soldier);
    }

    public void abandonPositionInFormation(int positionIndex) {
        this.positionsInFormation.get(positionIndex).setObjectInPosition(null);
    }

    public ArrayList<PositionInFormation<Soldier>> getPointsInFormation() {
        return positionsInFormation;
    }

    public void setPointsInFormation(ArrayList<PositionInFormation<Soldier>> pointsInFormation) {
        this.positionsInFormation = pointsInFormation;
    }

    public void clearPositions() {
        for (PositionInFormation position : this.positionsInFormation) {
            position.setObjectInPosition(null);
        }
    }
}
