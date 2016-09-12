/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

import static gorgonengine.LinAlg.VektorAddition;
import static gorgonengine.LinAlg.VektorMultiplikation;
import static gorgonengine.LinAlg.VektorSubtraktion;
import static gorgonengine.Scene.SIZE;

/**
 *
 * @author Andreas
 */
public class Mesh extends Object{
    
    public final int SIZE;
    public Triangle[] mesh;
    
    public Mesh(Triangle[] shape)
    {
        SIZE = shape.length;
        mesh = new Triangle[SIZE];
        for(int i = 0; i < shape.length; i++)
        {
            mesh[i] = new Triangle(shape[i].p, shape[i].color);
        }
    }
    
    public Ray rayIntersection(Ray r)
    {
        double t = -1, smallT = -10;
        Boolean firstHit = true;
        int savedID = -1;
        for(int idt = 0; idt < SIZE; ++idt)
        {
            t = mesh[idt].rayIntersection(r);
            if(firstHit && t>=0)
            {
                smallT = t;
                savedID = idt;
                firstHit = false;
            }
            else if(!firstHit && t>=0 && t < smallT)
            {
                smallT = t;
                savedID = idt;
            }
        }
        if(smallT != -10)
        {
            return new Ray(r.start, VektorAddition(r.start, VektorMultiplikation(
                        VektorSubtraktion(r.end, r.start), smallT)), 
                        new ColorDbl(mesh[savedID].color), savedID);
        }
        return new Ray(r.start, VektorMultiplikation(r.end, 10000), r.color);
    }
    
}
