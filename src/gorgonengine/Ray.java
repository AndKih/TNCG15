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

import static gorgonengine.LinAlg.*;

public class Ray {
    
    public static final Ray ERROR_RAY = new Ray(new Vertex(0, 0, 0), new Vertex(0, 0, 0), new ColorDbl(0, 0, 0));
    public Vertex start, end;
    public Direction dir;
    public ColorDbl color;
    private int rayIndex;
    private double t;
    private double radiance;
    private double importance;
    public static final int RAY_LIGHT = 100, RAY_IMPORTANCE = 101, RAY_SHADOW = 102;
    public final int RAY_TYPE;
    
    public Ray(Vertex s, Vertex e, ColorDbl c)
    {
        t = -1;
        start = s;
        end = e;
        dir = calculateVectorDirection(s, e);
        color = c;
        rayIndex = -1;
        RAY_TYPE = RAY_LIGHT;
        radiance = 1;
    }
    
    public Ray(Vertex s, Vertex e, ColorDbl c, int index)
    {
        t = -1;
        start = s;
        end = e;
        dir = calculateVectorDirection(s, e);
        color = c;
        rayIndex = index;
        RAY_TYPE = RAY_LIGHT;
        radiance = 1;
    }
    
    public Ray(ColorDbl c, Vertex s, Vertex e, int raytype)
    {
        t = -1;
        start = s;
        end = e;
        dir = calculateVectorDirection(s, e);
        color = c;
        rayIndex = -1;
        RAY_TYPE = raytype;
        switch(raytype)
        {
            case RAY_LIGHT:
                radiance = 1;
                importance = -1;
                break;
            case RAY_IMPORTANCE:
                importance = 1;
                radiance = -1;
                break;
            case RAY_SHADOW:
                radiance = -1;
                importance = -1;
                break;
            default:
                
                break;
        }
    }
    
    public Ray(Vertex s, Vertex e, ColorDbl c, int index, int raytype)
    {
        t = -1;
        start = s;
        end = e;
        dir = calculateVectorDirection(s, e);
        color = c;
        rayIndex = index;
        RAY_TYPE = raytype;
        switch(raytype)
        {
            case RAY_LIGHT:
                radiance = 1;
                importance = -1;
                break;
            case RAY_IMPORTANCE:
                importance = 1;
                radiance = -1;
                break;
            case RAY_SHADOW:
                radiance = -1;
                importance = -1;
                break;
            default:
                
                break;
        }
    }
    
    public void setT(double t)
    {
        this.t = t; 
    }
    
    public void setRadiance(double newR)
    {
        radiance = newR;
    }
    
    public double getRadiance()
    {
        return radiance;
    }
    
    public void reflect()
    {
        
    }
    
    public int returnIndex()
    {
        return rayIndex;
    }
    public boolean equals(Ray ray)
    {
        if(ray.end.x != end.x)
        {
            return false;
        }
        if(ray.end.y != end.y)
        {
            return false;
        }
        if(ray.end.z != end.z)
        {
            return false;
        }
        if(ray.start.x != start.x)
        {
            return false;
        }
        if(ray.start.y != start.y)
        {
            return false;
        }
        if(ray.start.z != start.z)
        {
            return false;
        }
        if(ray.color != color)
        {
            return false;
        }
        return true;
                
    }
    
}
