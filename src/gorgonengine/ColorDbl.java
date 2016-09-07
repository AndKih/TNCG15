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
public class ColorDbl {
    
    public double r, g, b;
    
    public ColorDbl(double r, double g, double b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public ColorDbl(ColorDbl color)
    {
        r = color.r;
        g = color.g;
        b = color.b;
    }
    
    public double returnR()
    {
        return r;
    }
    
    public double returnG()
    {
        return g;
    }
    
    public double returnB()
    {
        return b;
    }
    
    public String toString()
    {
        return "(" + r + ", " + g + ", " + b + ")";
    }
}
