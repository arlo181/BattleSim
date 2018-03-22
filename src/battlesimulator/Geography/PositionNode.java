/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battlesimulator.Geography;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Arlo
 */
public class PositionNode
{
    ArrayList<PositionNode> neighbors;
    boolean positionOpen;
    Point point; //todo:  do I need points, or can I just use x, y

    public PositionNode()
    {
        this.neighbors = new ArrayList<>();
        this.positionOpen = true;
    }
    
    public Point getPoint()
    {
        return point;
    }

    public void setPoint(Point point)
    {
        this.point = point;
    }
    
    public void addNeighbor(PositionNode neighbor)
    {
        neighbors.add(neighbor);
    }
    
    public void setClosed()
    {
        this.positionOpen = false;
    }

    public boolean isPositionOpen()
    {
        return positionOpen;
    }
    
    
}
