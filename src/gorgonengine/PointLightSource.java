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
    
    public PointLightSource(Vertex p0, double intensity)
    {
        pos = p0;
        color = new ColorDbl(1, 1, 1);
    }
    
    public PointLightSource(Vertex p0, double intensity, ColorDbl col)
    {
        pos = p0;
        color = col;
    }
    
    public Vertex getLightVectorFrom(Vertex p)
    {
        return VektorSubtraktion(pos,p);
    }
}
