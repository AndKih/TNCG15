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

import static gorgonengine.LinAlg.*;

public class Scene {
    
    public final static int SIZE = 24;
    public Triangle[] mesh = new Triangle[SIZE];
    Tetrahedron tetra;
    Sphere sphere;
    
    public Scene()
    {
        tetra = new Tetrahedron();
        //Middleposition: roof, (0, 0, 5). Floor: (0, 0, -5)
        final int TRIANGLESIZE = 3;
        Vertex[] p = new Vertex[TRIANGLESIZE];
        //Vägg norr (+y)
        p[0] = new Vertex(10, 6, 5);
        p[1] = new Vertex(0, 6, 5);
        p[2] = new Vertex(0, 6, -5);
        mesh[0] = new Triangle(p, new ColorDbl(0, 0, 1000000000));
        p[0] = new Vertex(10, 6, 5);
        p[1] = new Vertex(0, 6, -5);
        p[2] = new Vertex(10, 6, -5);
        mesh[1] = new Triangle(p, new ColorDbl(0, 0, 1000000000));
        //Vägg nordväst (+y, -x)
        p[0] = new Vertex(0, 6, 5);
        p[1] = new Vertex(-3, 0, 5);
        p[2] = new Vertex(-3, 0, -5);
        mesh[2] = new Triangle(p, new ColorDbl(0, 1000000000, 0));
        p[0] = new Vertex(0, 6, 5);
        p[1] = new Vertex(-3, 0, -5);
        p[2] = new Vertex(0, 6, -5);
        mesh[3] = new Triangle(p, new ColorDbl(0, 1000000000, 0));
        //Vägg sydväst (-y, -x)
        p[0] = new Vertex(-3, 0, 5);
        p[1] = new Vertex(0, -6, 5);
        p[2] = new Vertex(0, -6, -5);
        mesh[4] = new Triangle(p, new ColorDbl(1000000000, 0, 0));
        p[0] = new Vertex(-3, 0, 5);
        p[1] = new Vertex(0, -6, -5);
        p[2] = new Vertex(-3, 0, -5);
        mesh[5] = new Triangle(p, new ColorDbl(1000000000, 0, 0));
        //Vägg Syd (-y)
        p[0] = new Vertex(0, -6, 5);
        p[1] = new Vertex(10, -6, 5);
        p[2] = new Vertex(10, -6, -5);
        mesh[6] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 0));
        p[0] = new Vertex(0, -6, 5);
        p[1] = new Vertex(10, -6, -5);
        p[2] = new Vertex(0, -6, -5);
        mesh[7] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 0));
        //Vägg Sydost (-y, +x)
        p[0] = new Vertex(10, -6, 5);
        p[1] = new Vertex(13, 0, 5);
        p[2] = new Vertex(13, 0, -5);
        mesh[8] = new Triangle(p, new ColorDbl(1000000000, 0, 1000000000));
        p[0] = new Vertex(10, -6, 5);
        p[1] = new Vertex(13, 0, -5);
        p[2] = new Vertex(10, -6, -5);
        mesh[9] = new Triangle(p, new ColorDbl(1000000000, 0, 1000000000));
        //Väg Nordost (+y, +x)
        p[0] = new Vertex(13, 0, 5);
        p[1] = new Vertex(10, 6, 5);
        p[2] = new Vertex(10, 6, -5);
        mesh[10] = new Triangle(p, new ColorDbl(0, 1000000000, 1000000000));
        p[0] = new Vertex(13, 0, 5);
        p[1] = new Vertex(10, 6, -5);
        p[2] = new Vertex(13, 0, -5);
        mesh[11] = new Triangle(p, new ColorDbl(0, 1000000000, 1000000000));
        //Tak norr (+y)
        p[0] = new Vertex(0, 0, 5);
        p[1] = new Vertex(0, 6, 5);
        p[2] = new Vertex(10, 6, 5);
        mesh[12] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Tak Nordväst (+y, -x)
        p[0] = new Vertex(0, 0, 5);
        p[1] = new Vertex(-3, 0, 5);
        p[2] = new Vertex(0, 6, 5);
        mesh[13] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Tak Sydväst (-y, -x)
        p[0] = new Vertex(0, 0, 5);
        p[1] = new Vertex(0, -6, 5);
        p[2] = new Vertex(-3, 0, 5);
        mesh[14] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Tak syd (-y)
        p[0] = new Vertex(0, 0, 5);
        p[1] = new Vertex(10, -6, 5);
        p[2] = new Vertex(0, -6, 5);
        mesh[15] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Tak Sydost (-y, +x)
        p[0] = new Vertex(0, 0, 5);
        p[1] = new Vertex(13, 0, 5);
        p[2] = new Vertex(10, -6, 5);
        mesh[16] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Tak Nordost (+y, +x)
        p[0] = new Vertex(0, 0, 5);
        p[1] = new Vertex(10, 6, 5);
        p[2] = new Vertex(13, 0, 5);
        mesh[17] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Golv Norr (+y)
        p[0] = new Vertex(0, 6, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(10, 6, -5);
        mesh[18] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Golv Nordväst (+y, -x)
        p[0] = new Vertex(-3, 0, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(0, 6, -5);
        mesh[19] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Golv Sydväst (-y, -x)
        p[0] = new Vertex(0, -6, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(-3, 0, -5);
        mesh[20] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Golv Syd (-y)
        p[0] = new Vertex(10, -6, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(0, -6, -5);
        mesh[21] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Golv Sydost (-y, +x)
        p[0] = new Vertex(13, 0, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(10, -6, -5);
        mesh[22] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        //Golv Nordost (+y, +x)
        p[0] = new Vertex(10, 6, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(13, 0, -5);
        mesh[23] = new Triangle(p, new ColorDbl(1000000000, 1000000000, 1000000000));
        
        sphere = new Sphere(new ColorDbl(400000000, 200000000, 600000000), new Vertex(5, -3, 3), 1);
        
    }
    
    public Ray rayIntersection(Ray r)
    {
        double t = -1;
        Ray newR = tetra.rayIntersection(r);
        
        Ray newray = sphere.rayIntersection(r);
        if(VektorDistansJämförelse(newray.end, newR.end))                //If true, newEnd is bigger.
        {
            newR = newray;
            //System.out.println(newray.end.toString());
        }
        
        for(int idt = 0; idt < SIZE; ++idt)
        {
            
            t = mesh[idt].rayIntersection(r);
            if(t>=0)
            {
                //System.out.println("TEST");
                Vertex newEnd = VektorAddition(r.start, VektorMultiplikation(
                        VektorSubtraktion(r.end, r.start), t));
                if(VektorDistansJämförelse(newEnd, newR.end))                //If true, newEnd is bigger.
                {
                    //System.out.println("TEST");
                    newR = new Ray(r.start, newEnd, new ColorDbl(mesh[idt].color));
                }
//                return new Ray(r.start, newEnd, new ColorDbl(objects[idt].color), idt);
            }
        }
        
        
        
        return newR;
    }
}
