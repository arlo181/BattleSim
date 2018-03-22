/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ArmyFormations;
import UnitFormations.PositionInFormation;
import battlesimulator.Utils;
import battlesimulator.Military.Officer;
import battlesimulator.Military.Unit;
import battlesimulator.Military.UnitMetrics;
import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class BaseArmyFormation implements java.io.Serializable {

    Point formationOrigin;
    int orientation; //degrees clockwise
    ArrayList<PositionInFormation<UnitMetrics> >positionsInFormation;
    PositionInFormation officersPosition;
    String name;

    public BaseArmyFormation(String name, Point formationOrigin, ArrayList<Point> pointsInFormation) {
        this(name, formationOrigin, pointsInFormation, formationOrigin);
    }

    public BaseArmyFormation(String name, Point formationOrigin, ArrayList<Point> pointsInFormation, Point officerPosition) {
        this(name, formationOrigin, officerPosition);
        int index = 0;
        for (Point point : pointsInFormation) {
            this.positionsInFormation.add(new PositionInFormation(point, "" + index, index));
            index ++;
        }
    }

    public BaseArmyFormation(String name, Point formationOrigin, Point officerPosition) {
        this.name = name;
        this.formationOrigin = formationOrigin;
        this.positionsInFormation = new ArrayList<>();
        this.officersPosition = new PositionInFormation(officerPosition, null, -1);
        this.orientation = 0;

    }
    
        public BaseArmyFormation(String name, ArrayList<PositionInFormation<UnitMetrics> > positionsInFormation, Point formationOrigin,  Point officerPosition) {
        this.name = name;
        this.formationOrigin = formationOrigin;
        this.positionsInFormation = positionsInFormation;
        this.officersPosition = new PositionInFormation(officerPosition, null, -1);
        this.orientation = 0;

    }
    
    public void setOrientation(int orientation)
    {
        this.orientation = orientation;
        this.rotateFormation(orientation, new Point(0,0));
    }
    
    public int getOrientation()
    {
        return this.orientation;
    }
    
    public PositionInFormation<UnitMetrics> getPositionByName(String name)
    {
        PositionInFormation<UnitMetrics> positionLookingFor = null;
        for(PositionInFormation<UnitMetrics> position : this.positionsInFormation)
        {
            if (position.getPositionName().equals(name))
            {
                positionLookingFor = position;
                break;
            }
        }
        
        return positionLookingFor;
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
    
    public Point getOffsetOfPosition(String name) {
        Point pointInFormation = new Point(0,0);
        PositionInFormation<UnitMetrics> position = this.getPositionByName(name);
        if(position != null)
        {
            pointInFormation = position.getLocationWithinFormation();
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

    public void claimPositionInFormation(int positionIndex, UnitMetrics unit) {
        this.positionsInFormation.get(positionIndex).setObjectInPosition(unit);
    }

    public void abandonPositionInFormation(int positionIndex) {
        this.positionsInFormation.get(positionIndex).setObjectInPosition(null);
    }

    public ArrayList<PositionInFormation<UnitMetrics>> getPointsInFormation() {
        return positionsInFormation;
    }

    public void setPointsInFormation(ArrayList<PositionInFormation<UnitMetrics>> pointsInFormation) {
        this.positionsInFormation = pointsInFormation;
    }

    public void clearPositions() {
        for (PositionInFormation position : this.positionsInFormation) {
            position.setObjectInPosition(null);
        }
    }
}
