/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

import static gorgonengine.LinAlg.VektorSubtraktion;

/**
 *
 * @author Valdemar
 */
public class PointLightSource {
    Vertex pos;
    ColorDbl color;
    double intensity;
    
    public PointLightSource(Vertex p0, double intensity)
    {
        pos = p0;
        this.intensity = intensity;
        color = new ColorDbl(1000000000, 1000000000, 1000000000);
    }
    
    public PointLightSource(Vertex p0, double intensity, ColorDbl col)
    {
        pos = p0;
        this.intensity = intensity;
        color = color;
    }
    
    public Vertex getLightVectorFrom(Vertex p)
    {
        return VektorSubtraktion(p,pos);
    }
}
