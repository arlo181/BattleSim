/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import battlesimulator.Utils;
import battlesimulator.BattlefieldObject;
import battlesimulator.Military.Army;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Arlo
 */
public class Battlefield implements java.io.Serializable {

    int numCellsWidth;
    int numCellsHeight;
    int sizeOfCells;
    String name;
    HashMap<String, StartingPosition> startingPositions = new HashMap<>();
    BattlefieldCell[][] gridOfCells;
    ArrayList<BattlefieldObject> outOfBoundsObjects = new ArrayList<>();

    public Battlefield(int numCellsWidth, int numCellsHeight, int sizeOfCells, String name) {
        this.numCellsWidth = numCellsWidth;
        this.numCellsHeight = numCellsHeight;
        this.name = name;

        this.gridOfCells = new BattlefieldCell[numCellsHeight][numCellsWidth];
        this.sizeOfCells = sizeOfCells;
        generateRandomCells();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
        
    public void removeStartingPosition(String name)
    {
        this.startingPositions.remove(name);
    }
    
    public ArrayList<BattlefieldObject> getNeutralObjects()
    {
        ArrayList<BattlefieldObject> neutralObjects = new ArrayList<>();
        for(BattlefieldObject bObject : this.getAllBattleObjects())
        {
            if(!bObject.hasAllegiance())
            {
                neutralObjects.add(bObject);
            }
        }
        return neutralObjects;
    }

    public Point getRandomPoint()
    {
        Random rand = new Random();
        
        int maxX = this.numCellsWidth * this.sizeOfCells;
        int maxY = this.numCellsHeight * this.sizeOfCells;
        
        Point randPoint = new Point(rand.nextInt(maxX), rand.nextInt(maxY));
         return randPoint;      
    }

    public void generateRandomCells() {
        for (int row = 0; row < numCellsHeight; row++) {
            for (int column = 0; column < numCellsWidth; column++) {
                int x = column * sizeOfCells;
                int y = row * sizeOfCells; //this is +1 because rectangles starting point 
                //is in the top left, so we'll never start a rectangle at 0,0
                Rectangle cell = new Rectangle(x, y, sizeOfCells, sizeOfCells);
                BattlefieldCell battlefieldCell = new BattlefieldCell(cell, row, column);
                this.gridOfCells[row][column] = battlefieldCell;
            }
        }
    }
    
    
    public void paintBattlefield(Graphics2D g, int tickNumber) {
        Rectangle fieldOutline = new Rectangle(0, 0, this.getPixelsWidth(), this.getPixelsHeight());
        g.setColor(Color.GREEN);
        g.draw(fieldOutline);
        g.setColor(Color.WHITE);

        g.fill(fieldOutline);

        for (int row = 0; row < numCellsHeight; row++) {
            for (int column = 0; column < numCellsWidth; column++) {
                this.gridOfCells[row][column].paintObject(g, tickNumber);
            }
        }
        g.setColor(Color.GREEN);
        g.draw(fieldOutline);
    }

    public int getPixelsWidth() {
        return this.numCellsWidth * this.sizeOfCells;
    }

    public int getPixelsHeight() {
        return this.numCellsHeight * this.sizeOfCells;
    }

    public void addStartingPosition(String key, StartingPosition position) {
        this.startingPositions.put(key, position);
    }

    public BattlefieldCell getCellByLocation(Point point) {
        BattlefieldCell cell = null;
        if ((point.x < (numCellsWidth * sizeOfCells) && point.x > 0)
                && (point.y < (numCellsHeight * sizeOfCells) && point.y > 0)) {
            int column = point.x / this.sizeOfCells;
            int row = point.y / this.sizeOfCells;
            
            row = Math.min(row, this.numCellsHeight);
            column = Math.min(column, this.numCellsWidth);
            cell = this.gridOfCells[row][column];
        }
        return cell;
    }

    public HashMap<String, StartingPosition> getStartingPositions() {
        return startingPositions;
    }
    
    public void clearAllObjectsFromField()
    {
        for (int row = 0; row < numCellsHeight; row++) {
            for (int column = 0; column < numCellsWidth; column++) {
                this.gridOfCells[row][column].battlefieldObjects.clear();
            }
        }
    }
    
        public void setObjects(ArrayList<BattlefieldObject> objects)
    {
        this.clearAllObjectsFromField();
       objects.parallelStream().forEach(bObject ->
            
        {
            BattlefieldCell toCell = null;
            if(bObject != null && bObject.getCenterPoint() != null)
            {
            toCell = this.getCellByLocation(bObject.getCenterPoint());
            }
            if(toCell != null)
            {
            toCell.addBattlefieldObject(bObject);
            }
        });
    }
        
        public ArrayList<BattlefieldObject> getObjectsWithin(Point centerPoint, int radius)
        {
            ArrayList<BattlefieldObject> objects = new ArrayList<>();
            for(BattlefieldCell cell : this.getCellsWithin(centerPoint, radius))
            {
                objects.addAll(cell.getBattlefieldObjects());
            }
            
            return objects;
        }
        
        public ArrayList<BattlefieldCell> getCellsWithin(Point centerPoint, int radius)
        {
            ArrayList<BattlefieldCell> cells = new ArrayList<>();
            int maxRow = this.numCellsHeight -1;
            int minRow = 0;
            int maxCol = this.numCellsWidth -1;
            int minCol = 0;
            BattlefieldCell centerCell = this.getCellByLocation(centerPoint);
            int centerRow = centerCell != null ? centerCell.getRow() : minRow;
            int centerCol = centerCell != null ? centerCell.getColumn() : minCol;
            int numCells = (int)Math.ceil(((double)radius) / this.sizeOfCells);
            
            
            maxCol = Math.min(maxCol, centerCol + numCells);
            minCol = Math.max(minCol, centerCol - numCells);
            maxRow = Math.min(maxRow, centerRow + numCells);
            minRow = Math.max(minRow, centerRow - numCells);
            
            for(int rowCount = minRow; rowCount <= maxRow;rowCount ++)
            {
                for (int colCount = minCol; colCount <= maxCol;colCount++)
                {
                   cells.add(this.gridOfCells[rowCount][colCount]);
                }
            }
            return cells;
        }

    public int getSizeOfCells() {
        return sizeOfCells;
    }

    public int getNumCellsWidth() {
        return numCellsWidth;
    }

    public int getNumCellsHeight() {
        return numCellsHeight;
    }

    public synchronized void moveObjectInDirection(BattlefieldObject bObject, Point destination, boolean setFacing) {

        Point origin = bObject.getCenterPoint();
        BattlefieldCell toCell = this.getCellByLocation(destination);
        BattlefieldCell fromCell = this.getCellByLocation(origin);
        if ((bObject.getCenterPoint().distance(destination) != 0) && setFacing) {
            bObject.setFacingDirection(Utils.findBearing(origin, destination));
        }

        origin.setLocation(destination);
        bObject.setCenterPoint(destination);
        if (fromCell != null) {
            if (toCell == null) {
                //out of bounds
                fromCell.removeBattlefieldObject(bObject);
                this.outOfBoundsObjects.add(bObject);
                bObject.setOutOfBounds();
            } else if (!fromCell.equals(toCell)) {
                fromCell.removeBattlefieldObject(bObject);
                toCell.addBattlefieldObject(bObject);
            }
        }
    }

    public ArrayList<BattlefieldObject> getConflicts(BattlefieldObject bObject, Point point, int personalSpaceRadius) {
        ArrayList<BattlefieldObject> conflicts = new ArrayList<>();
        for (BattlefieldCell cell : this.getCellsNear(point)) {
            if (cell != null) {
                for (BattlefieldObject objInCell : cell.getBattlefieldObjects()) {

                    if ((!bObject.equals(objInCell))
                            && objInCell.isActive()
                            && objInCell.violatesPersonalSpace(point, personalSpaceRadius)) //todo isSolid
                    {
                        conflicts.add(objInCell);
                    }
                }
            }
        }
        return conflicts;
    }
    
        public BattlefieldObject getFirstConflict(BattlefieldObject bObject, Point point, int personalSpaceRadius) {
        BattlefieldObject conflict = null;
        for (BattlefieldCell cell : this.getCellsNear(point)) {
            if (cell != null) {
                for (BattlefieldObject objInCell : cell.getBattlefieldObjects()) {

                    if ((!bObject.equals(objInCell))
                            && objInCell.isActive()
                            && objInCell.violatesPersonalSpace(point, personalSpaceRadius)) //todo isSolid
                    {
                        conflict =objInCell;
                        break;
                    }
                }
            }
        }
        return conflict;
    }
    

    public boolean isConflict(BattlefieldObject bObject, Point point, int personalSpaceRadius) {
        boolean isConflict = false;

        for (BattlefieldCell cell : this.getCellsNear(point)) {
            if (cell != null) {
                for (BattlefieldObject objInCell : cell.getBattlefieldObjects()) {

                    if ((!bObject.equals(objInCell))
                            && objInCell.isActive()) //todo isSolid
                    {
                        isConflict |= objInCell.violatesPersonalSpace(point, personalSpaceRadius);
                    }
                }
            }
        }
        return isConflict;
    }

    public ArrayList<BattlefieldCell> getCellsNear(Point point) {
        ArrayList<BattlefieldCell> cells = new ArrayList<>();
if(point == null)
{
    System.out.println("break");
    return cells;
}

        int column = point.x / this.sizeOfCells;
        int row = point.y / this.sizeOfCells;
        int northRow = row - 1;
        int southRow = row + 1;
        int westCol = column - 1;
        int eastCol = column + 1;

        if ((row >= 0)
                && (column >= 0)
                && (row < this.numCellsHeight)
                && (column < this.numCellsWidth)) {
            cells.add(this.gridOfCells[row][column]);
        }

        //north
        if ((northRow >= 0)
                && (column >= 0)
                && (northRow < this.numCellsHeight)
                && (column < this.numCellsWidth)) {
            cells.add(this.gridOfCells[northRow][column]);
        }

        //south
        if ((southRow >= 0)
                && (column >= 0)
                && (southRow < this.numCellsHeight)
                && (column < this.numCellsWidth)) {
            cells.add(this.gridOfCells[southRow][column]);
        }

        //west
        if ((row >= 0)
                && (westCol >= 0)
                && (row < this.numCellsHeight)
                && (westCol < this.numCellsWidth)) {
            cells.add(this.gridOfCells[row][westCol]);
        }

        //east
        if ((row >= 0)
                && (eastCol >= 0)
                && (row < this.numCellsHeight)
                && (eastCol < this.numCellsWidth)) {
            cells.add(this.gridOfCells[row][eastCol]);
        }

        return cells;
    }

    public void addNeutralObject(BattlefieldObject bObject) {

        BattlefieldCell location = this.getCellByLocation(bObject.getCenterPoint());
        if (location != null) {
            location.addBattlefieldObject(bObject);
        }
    }

    public boolean isConflict(BattlefieldObject bObject) {
        return this.isConflict(bObject, bObject.getCenterPoint(), bObject.getPersonalSpaceRadius());
    }

    public void assignArmyStartingPoint(String key, Army army) {

        StartingPosition position = this.startingPositions.get(key);

        if (position != null) {
            position.assignArmy(army);
              army.setStartingPosition(key, position.getCornerPoint());

            for (BattlefieldObject bObject : army.getAllSoldiers()) {
                BattlefieldCell cell = this.getCellByLocation(bObject.getCenterPoint());
                if (cell != null) {
                    cell.addBattlefieldObject(bObject);
                } else {
                    this.outOfBoundsObjects.add(bObject);
                    bObject.setOutOfBounds();
                }
            }
        }
    }

    public List<BattlefieldObject> getAllBattleObjects() {
        ArrayList<BattlefieldObject> battlefieldObjects = new ArrayList<>();


        //randomize the order that we check the cells?
        for (int row = 0; row < numCellsHeight; row++) {
            for (int column = 0; column < numCellsWidth; column++) {
                if(this.gridOfCells[row][column] != null)
                {
                battlefieldObjects.addAll(this.gridOfCells[row][column].getBattlefieldObjects());
                }
            }
        }

        return battlefieldObjects;
    }
}
