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
    
    public static final ColorDbl BLACK = new ColorDbl(0, 0, 0), PURPLE =  new ColorDbl(900000000, 0, 1000000000), ORANGE  = new ColorDbl(1000000000, 550000000, 0);
    public static final ColorDbl RED = new ColorDbl(1000000000, 0, 0), GREEN = new ColorDbl(0, 1000000000, 0), BLUE = new ColorDbl(0, 0, 1000000000);
    public static final ColorDbl CYAN = new ColorDbl(0, 1000000000, 1000000000), MAGENTA = new ColorDbl(1000000000, 0, 1000000000), YELLOW = new ColorDbl(1000000000, 1000000000, 0);
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

    ColorDbl() 
    {
        r = 0;
        g = 0;
        b = 0;
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
