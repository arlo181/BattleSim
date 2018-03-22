/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitFormations;

import battlesimulator.Military.Officer;
import battlesimulator.Military.Soldier;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class WedgeFormation extends BaseUnitFormation {

    int orientation; //angle in degrees
    int numSoldiers;
    int spacing; //personal space of units in formation

    public WedgeFormation(String name, Point formationOrigin, int orientation, int numSoldiers, int personalSpaceRadius) {
        this(name, formationOrigin, formationOrigin, orientation, numSoldiers, personalSpaceRadius);
    }

    public WedgeFormation(String name, Point formationOrigin, Point officerPosition, int orientation, int numSoldiers, int personalSpaceRadius) {
        super(name, formationOrigin, officerPosition);
        this.orientation = orientation;
        this.numSoldiers = numSoldiers;
        this.spacing = personalSpaceRadius * 2;
        int gap = (int) (2.5 * this.spacing);
        int sumX = 0;
        int sumY = 0;
        int  pointIndex = 0;
        ArrayList<Integer> oddRowXValues = new ArrayList<>();
        ArrayList<Integer> evenRowXValues = new ArrayList<>();

        //set up first odd row
        Point firstPoint = new Point(0, 0);
        int yToUse = firstPoint.y;
        int row = 0;
        this.positionsInFormation.add(new PositionInFormation(firstPoint, null, pointIndex));
         pointIndex++;
        numSoldiers--;
        evenRowXValues.add(firstPoint.x);
        row++;

        //set up first even row
        yToUse = row * gap;
        Point secondPoint = new Point(firstPoint.x - gap / 2, yToUse);
        this.positionsInFormation.add(new PositionInFormation(secondPoint, null, pointIndex));
         pointIndex++;
        oddRowXValues.add(secondPoint.x);
        sumX += secondPoint.x;
        sumY += secondPoint.y;
        numSoldiers--;
        Point thirdPoint = new Point(firstPoint.x + gap / 2, yToUse);
        this.positionsInFormation.add(new PositionInFormation(thirdPoint, null, pointIndex));
         pointIndex++;
        oddRowXValues.add(thirdPoint.x);
        sumX += thirdPoint.x;
        sumY += thirdPoint.y;
        numSoldiers--;
        row++;

        while (numSoldiers > 0) {
            ArrayList<Integer> listToUse = (row % 2 == 0) ? evenRowXValues : oddRowXValues;
            yToUse = row * gap;
            listToUse.add(0, listToUse.get(0) - gap);
            listToUse.add(listToUse.get(listToUse.size() - 1) + gap);

            for (Integer x : listToUse) {

                this.positionsInFormation.add(new PositionInFormation(new Point(x, yToUse), null, pointIndex));
                 pointIndex++;
                numSoldiers--;
                sumX += x;
                sumY += yToUse;
            }

            row++;
        }

        this.setOfficerPosition(new Point(firstPoint.x, row * gap + gap)); //put the officer at the back of the formation.
        if (this.numSoldiers != 0) {
            this.movePointsInFormation(-(sumX / this.numSoldiers), -(sumY / this.numSoldiers)); //center the formation's center of mass on the origin.
            this.rotateFormation(orientation, new Point(0, 0)); //center x will always be 0
        }

    }

    public void tightenFormation() {
        //this function will assign new positions to the soldiers already in formation that are closer together.
    }
}
