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
public class Vertex {
    
    public static final Vertex DUMMY = new Vertex(0, 0, 0);
    
    double x, y, z, w;
    
    public Vertex(double x, double y, double z)
    {
        this.x = x;
        this.y = y; 
        this.z = z;
        w = 1;
    }
    
    public Vertex(Vertex v)
    {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
    }
    
    public double returnCoordByIndex(int index)
    {
        if(index == 0)
            return x;
        else if (index == 1)
            return y;
        else if(index == 2)
            return z;
        else
            return 0;
    }
    
    public double returnX()
    {
        return x;
    }
    
    public double returnY()
    {
        return y;
    }
    
    public double returnZ()
    {
        return z;
    }
    
    public double returnW()
    {
        return w;
    }
    
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
    public boolean equals(Vertex v)
    {
        if(x != v.x)
            return false;
        if(y != v.y)
            return false;
        if(z != v.z)
            return false;
        return true;
    }
    
}
