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
    
    public static final ColorDbl BLACK = new ColorDbl(0, 0, 0), PURPLE =  new ColorDbl(90, 0, 100), ORANGE  = new ColorDbl(10, 5.5, 0);
    public static final ColorDbl RED = new ColorDbl(100, 0, 0), GREEN = new ColorDbl(0, 100, 0), BLUE = new ColorDbl(0, 0, 100);
    public static final ColorDbl CYAN = new ColorDbl(0, 100, 100), MAGENTA = new ColorDbl(100, 0, 100), YELLOW = new ColorDbl(100, 100, 0);
    public double r, g, b;
    
    
    public ColorDbl(double r, double g, double b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public ColorDbl(double c)
    {
        this.r = c;
        this.g = c;
        this.b = c;
    }
    
    public ColorDbl(ColorDbl color)
    {
        r = color.r;
        g = color.g;
        b = color.b;
    }

    public ColorDbl() 
    {
        r = 0;
        g = 0;
        b = 0;
    }
    
    public double getTotalColorValue()
    {
        return r + g + b;
    }
    
    public double returnR()
    {
        return r;
    }
    
    public void setIntensity(ColorDbl intensity)
    {
        r *= intensity.r;
        g *= intensity.g;
        b *= intensity.b;
    }
    

    void setIntensity(ColorDbl intensity, ColorDbl tmpCol) {
        r += tmpCol.r*intensity.r;
        g += tmpCol.g*intensity.g;
        b += tmpCol.b*intensity.b;
    }
    
    public void setIntensity(double intensity)
    {
        if(intensity < 0)
            intensity=0;
        r *= intensity;
        g *= intensity;
        b *= intensity;
    }
    
    public void addColor(ColorDbl col)
    {
        r += col.r;
        g += col.g;
        b += col.b;
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

    public double meanIntensity() {
        double oldr = r;
        double oldg = g;
        double oldb = b;
        double mean = r + g + b;
        mean /= 3;
        if(r!=oldr || g != oldg || b != oldb)
        {
            System.out.println(toString());
        }
        return mean;
    }
}
