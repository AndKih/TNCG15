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
public class Ray {
    
    public Vertex start, end;
    public ColorDbl color;
    private int rayIndex;
    private double t;
    
    public Ray(Vertex s, Vertex e, ColorDbl c)
    {
        t = -1;
        start = s;
        end = e;
        color = c;
        rayIndex = -1;
    }
    
    public Ray(Vertex s, Vertex e, ColorDbl c, int index)
    {
        t = -1;
        start = s;
        end = e;
        color = c;
        rayIndex = index;
    }
    
    public void setT(double t)
    {
        this.t = t; 
    }
    
    public int returnIndex()
    {
        return rayIndex;
    }
    
}
