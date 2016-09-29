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

public class Triangle {
    
    public static final int SIZE = 3;
    public Vertex[] p = new Vertex[SIZE];
    public Direction normal;
    public ColorDbl color;
    public final int REFLECTION_TYPE;
    public static final int REFLECTION_LAMBERTIAN = 100, REFLECTION_ORENNAYAR = 101;
    public final int triangleIndex;
    // reflectionCoeff. p is 0<=p<=1.
    public double reflectionCoefficient;
    
    public Triangle(Vertex[] p, ColorDbl c, int index)
    {
        if(p.length != 3)
        {
            System.out.println("Vertex Points Wrong Size!!!");
            System.exit(1);
        }
        
        for(int i = 0; i<SIZE; i++)
        {
            this.p[i] = p[i];
        }
        REFLECTION_TYPE = REFLECTION_LAMBERTIAN;
        color = c;
        calculateNormal();
        triangleIndex = index;
    }
    
    private void calculateNormal()
    {
        Vertex retval = VektorProdukt(p[0],p[1],p[2]);
        normal = new Direction(retval.x, retval.y, retval.z);
        double normalFaktor = returnLength(dirToVertex(normal));
        normal.x = normal.x/normalFaktor;
        normal.y = normal.y/normalFaktor;
        normal.z = normal.z/normalFaktor;
        System.out.println("Normal " + normal.toString());
        System.out.println("Triangle \n" + this.toString());
    }
    
    public void setReflectionCoefficient(double p)
    {
        reflectionCoefficient = p;
    }
    
    public double rayIntersection(Ray r)
    {   
        return Möller_Trumbore(r.end, r.start, p[0], p[1], p[2]);
    }
    
    public String toString()
    {
        String result = "";
        for(int ids = 0; ids < SIZE; ++ids)
        {
            result += p[ids].toString() + "\n";
        }
        return result;
    }
    
}
