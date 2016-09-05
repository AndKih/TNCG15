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
    
    double x, y, z, w;
    
    public Vertex(double x, double y, double z)
    {
        this.x = x;
        this.y = y; 
        this.z = z;
        w = 1;
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
    
}
