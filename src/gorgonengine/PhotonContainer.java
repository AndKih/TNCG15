/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

/**
 *
 * @author Andreas
 */

import java.util.Vector;

public class PhotonContainer {
    
    private Vector<Photon> photonList;
    private final Vertex maxPos;
    private final Vertex minPos;
    
    public PhotonContainer(Vertex maxPos, Vertex minPos)
    {
        this.maxPos = maxPos;
        this.minPos = minPos;
        photonList = new Vector<Photon>();
    }
    
    public void addPhoton(Photon p)
    {
        photonList.add(p);
    }
    
    public Vertex getMaxPos()
    {
        return maxPos;
    }
    
    public Vertex getMinPos()
    {
        return minPos;
    }
    
    public String toString()
    {
        return "MAX: " + maxPos + "\nMIN: " + minPos;
    }
    
}
