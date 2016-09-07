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

public class Sphere extends Object{
    
    public ColorDbl color;
    public double radius;
    public Vertex center;
    //public Triangle[] mesh;
    
    public Sphere(ColorDbl c, Vertex center, double radius)
    {
        this.center = center;
        this.radius = radius;
        color = c;
    }
    
    public Ray rayIntersection(Ray r)
    {
        //||x - C||^2 = r^2
        //x = o + dI
        //a = (I*I) = 1
        //b = 2I*(o - C)
        //c = (o - C)*(o - C) - r^2
        //d = (-b/2)+- sqrt((b/2)^2 - a*c)
        double dup, ddown;
        Vertex i = new Vertex((r.end.x - r.start.x), (r.end.y - r.start.y), (r.end.z - r.start.z));
        i = normalize(i);
        double a = SkalärProdukt(i, i);
        double b = SkalärProdukt(VektorMultiplikation(i, 2), VektorSubtraktion(r.start, center));
        double c = SkalärProdukt(VektorSubtraktion(r.start, center), VektorSubtraktion(r.start, center)) - Math.pow(radius, 2);
        dup = (-b/2) + Math.sqrt((b/2) - a*c);
        ddown = (-b/2) - Math.sqrt((b/2) - a*c);
        Vertex x1 = VektorAddition(r.start, VektorMultiplikation(i, dup));
        Vertex x2 = VektorAddition(r.start, VektorMultiplikation(i, ddown));
        if(Math.pow(Math.abs(Math.sqrt((x1.x - center.x) + (x1.y - center.y) + (x1.z - center.z))), 2) == Math.pow(radius, 2))
        {
            return new Ray(r.start, x1, color);
        }
        else if(Math.pow(Math.abs(Math.sqrt((x2.x - center.x) + (x2.y - center.y) + (x2.z - center.z))), 2) == Math.pow(radius, 2))
        {
            return new Ray(r.start, x2, color);
        }
        else
            return new Ray(r.start, VektorMultiplikation(VektorSubtraktion(r.end,r.start), 10000), r.color);
    }
    
}
