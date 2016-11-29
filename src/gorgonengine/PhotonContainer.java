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
    private final double[] diameter;
    
    public PhotonContainer()
    {
        maxPos = Vertex.DUMMY;
        minPos = Vertex.DUMMY;
        diameter = new double[0];
    }
    
    public PhotonContainer(Vertex maxPos, Vertex minPos, double[] dia)
    {
        this.maxPos = maxPos;
        this.minPos = minPos;
        if(dia.length != 3)
            System.exit(0);
        diameter = dia;
        photonList = new Vector<Photon>();
    }
    
    public void addPhoton(Photon p)
    {
        photonList.add(p);
    }
    
    public Photon getPhoton(int index)
    {
        return photonList.get(index);
    }
    
    public int getContainerSize()
    {
        return photonList.size();
    }
    
    public Vertex getMaxPos()
    {
        return maxPos;
    }
    
    public Vertex getMinPos()
    {
        return minPos;
    }
    
    public double[] getDiameters()
    {
        return diameter;
    }
    
    public String toString()
    {
        String result = "MAX: " + maxPos + "\n"
                + "MIN: " + minPos + "\n"
                + "Diameters: " + diameter[0] + ", " + diameter[1] + ", " + diameter[2] + "\n"
                + "Size: " + photonList.size() + "\n";
        return result;
    }
    
    public boolean equals(PhotonContainer other)
    {
        if(other.diameter[0] == diameter[0] && other.diameter[1] == diameter[1] && other.diameter[2] == diameter[2] &&
                other.maxPos.equals(maxPos) && other.minPos.equals(minPos) && photonList.equals(other.photonList))
            return true;
        return false;
    }
    
}
