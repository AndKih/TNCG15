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
public class HemisCoords {
    
    public double theta, phi, r;
    
    public HemisCoords()
    {
        theta = 0;
        phi = 0;
        r = 1;
    }
    
    public HemisCoords(double t, double p, double r)
    {
        theta = t;
        phi = p;
        this.r = r;
    }
    
    public void normalizeDistance()
    {
        r = 1;
    }
    
}
