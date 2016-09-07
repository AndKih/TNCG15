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
    
    public Pixel(ColorDbl c, int pIndex)
    {
        color = c;
        pixelIndex = pIndex;
        rayIndex = -1;
    }
    
}
