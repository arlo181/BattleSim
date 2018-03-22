/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitFormations;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class GridFormation extends BaseUnitFormation {

    int orientation; //angle in degrees
    int numSoldiers;
    int spacing; //personal space of units in formation
    int numInRow;

    public GridFormation(String name, Point formationOrigin, int orientation, int numSoldiers, int personalSpaceRadius, int numInRow) {
        this(name, formationOrigin, formationOrigin, orientation, numSoldiers, personalSpaceRadius, numInRow);
    }

    public GridFormation(String name, Point formationOrigin, Point officerPosition, int orientation, int numSoldiers, int personalSpaceRadius, int numInRow) {
        super(name, formationOrigin, officerPosition);
        this.orientation = orientation;
        this.numSoldiers = numSoldiers;
        this.spacing = personalSpaceRadius * 2;
        this.numInRow = Math.max(numInRow, 1);
        int gap = (int) (2.5 * this.spacing);

        int numRows = this.numSoldiers / this.numInRow;
        
        //origin will be center of the formation.  top left is half the rows up and half the row size to the left
        int x = -numInRow*gap/2;
        int y = -numRows*gap/2;
        int maxX = x + numInRow*gap;
        int originalX = x;
        int pointIndex = 0;
        
        while(numSoldiers > 0)
        {
            this.positionsInFormation.add(new PositionInFormation(new Point(x, y), null, pointIndex));
            pointIndex++;
            numSoldiers --;
            x = (x + gap);
            if(x > maxX)
            {
                x = originalX;
                y += gap;
            }
        }
       
        this.setOfficerPosition(new Point(x, y));

        this.rotateFormation(orientation, new Point(0, 0)); //center x will always be 0

    }

    public void tightenFormation() {
        //this function will assign new positions to the soldiers already in formation that are closer together.
    }
}
