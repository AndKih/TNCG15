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
public class Pixel {
    
    public ColorDbl color;
    private int pixelIndex, rayIndex;
    public boolean hitLightSource;
    
    public Pixel(ColorDbl c, int pIndex)
    {
        hitLightSource = false;
        color = c;
        pixelIndex = pIndex; //pixel index. Goes from 1 to max
        rayIndex = -1;
    }
    public void addColor(ColorDbl col)
    {
        color.addColor(col);
    }
    
}
