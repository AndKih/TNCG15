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
public class Tetrahedron {
    
    public final static int SIZE = 4;
    public Triangle[] mesh = new Triangle[SIZE];
    
    public Tetrahedron()
    {
        final int TRIANGLESIZE = 3;
        Vertex[] p = new Vertex[TRIANGLESIZE];
        p[0] = new Vertex(10, 3, 3);
        p[1] = new Vertex(10, 1, -1);
        p[2] = new Vertex(8, 1, 1);
        mesh[0] = new Triangle(p, new ColorDbl(500000000, 700000000, 250000000));
        p[0] = new Vertex(10, 1, -1);
        p[1] = new Vertex(10, -1, 3);
        p[2] = new Vertex(8, 1, 1);
        mesh[1] = new Triangle(p, new ColorDbl(700000000, 250000000, 500000000));
        p[0] = new Vertex(8, 1, 1);
        p[1] = new Vertex(10, -1, 3);
        p[2] = new Vertex(10, 3, 3);
        mesh[2] = new Triangle(p, new ColorDbl(600000000, 300000000, 800000000));
        p[0] = new Vertex(10, 3, 3);
        p[1] = new Vertex(10, -1, 3);
        p[2] = new Vertex(10, 1, -1);
        mesh[3] = new Triangle(p, new ColorDbl(250000000, 500000000, 700000000));
    }
    
    public Ray rayIntersection(Ray r)
    {
        double t = -1, smallT = 100;
        int savedID = -1;
        for(int idt = 0; idt < SIZE; ++idt)
        {
            t = mesh[idt].rayIntersection(r);
            if(t>=0 && t < smallT)
            {
                smallT = t;
                savedID = idt;
            }
        }
        if(smallT != 100)
        {
            return new Ray(r.start, VektorAddition(r.start, VektorMultiplikation(
                        VektorSubtraktion(r.end, r.start), smallT)), 
                        new ColorDbl(mesh[savedID].color), savedID);
        }
        return new Ray(r.start, VektorMultiplikation(r.end, 10000), r.color);
    }
    
}
