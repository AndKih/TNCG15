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
    
    public Ray(Vertex s, Vertex e, ColorDbl c)
    {
        start = s;
        end = e;
        color = c;
        rayIndex = -1;
    }
    
    public Ray(Vertex s, Vertex e, ColorDbl c, int index)
    {
        start = s;
        end = e;
        color = c;
        rayIndex = index;
    }
    
    public int returnIndex()
    {
        return rayIndex;
    }
    
}
