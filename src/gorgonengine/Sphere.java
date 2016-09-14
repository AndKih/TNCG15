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
    
    public Ray rayIntersection(Ray r, PointLightSource ls)
    {
        final double EPSILON = 0.000001;
        //||x - C||^2 = r^2
        //x = o + dI
        //a = (I*I) = 1
        //b = 2I*(o - C)
        //c = (o - C)*(o - C) - r^2
        //d = (-b/2)+- sqrt((b/2)^2 - a*c)
        double dup, ddown;
        Vertex i = new Vertex((r.end.x - r.start.x), (r.end.y - r.start.y), (r.end.z - r.start.z));
        i = normalize(i);
//        System.out.println("i: " + i);
        double a = SkalärProdukt(i, i);
        double b = SkalärProdukt(VektorMultiplikation(i, 2), VektorSubtraktion(r.start, center));
        double c = SkalärProdukt(VektorSubtraktion(r.start, center), VektorSubtraktion(r.start, center)) - Math.pow(radius, 2);
//        System.out.println("a: " + a);
//        if(b > 0)
//        {
//            System.out.println("b: " + b);
//        }
        
//        System.out.println("c: " + c);
        if((b/2) - a*c >= 0)
            System.out.println("Schwosh");
        dup = (-b/2) + Math.sqrt(Math.pow(b/2, 2) - a*c);
        ddown = (-b/2) - Math.sqrt(Math.pow(b/2, 2) - a*c);
//        System.out.println("dup: " + dup);
//        System.out.println("ddown: " + ddown);
        Vertex x1 = VektorAddition(r.start, VektorMultiplikation(i, dup));
        Vertex x2 = VektorAddition(r.start, VektorMultiplikation(i, ddown));
//        System.out.println("x1: " + x1 + " x2: " + x2);
//        if(Math.pow(Math.abs(Math.sqrt(Math.pow(x1.x - center.x, 2) + Math.pow(x1.y - center.y, 2) + Math.pow(x1.z - center.z, 2))), 2) == Math.pow(radius, 2))
//        {
//            System.out.println("HEJ!");
//            return new Ray(r.start, x1, color);
//        }
//        else if(Math.pow(Math.abs(Math.sqrt(Math.pow(x2.x - center.x, 2) + Math.pow(x2.y - center.y, 2) + Math.pow(x2.z - center.z, 2))), 2) == Math.pow(radius, 2))
//        {
//            System.out.println("HEJ!");
//            return new Ray(r.start, x2, color);
//        }
        if(Math.pow(Math.abs(Math.sqrt(Math.pow(x1.x - center.x, 2) + Math.pow(x1.y - center.y, 2) + Math.pow(x1.z - center.z, 2))), 2) - Math.pow(radius, 2) < EPSILON)
        {
            Ray result = new Ray(r.start, x1, color);
            return new Ray(r.start, x1, color);
        }
        else if(Math.pow(Math.abs(Math.sqrt(Math.pow(x2.x - center.x, 2) + Math.pow(x2.y - center.y, 2) + Math.pow(x2.z - center.z, 2))), 2) - Math.pow(radius, 2) < EPSILON)
        {
            return new Ray(r.start, x2, color);
        }
        else
            return new Ray(r.start, VektorMultiplikation(VektorSubtraktion(r.end,r.start), 10000), r.color);
    }

    @Override
    public void rotateX(double angle) {
        center = rotateXVertex(center, angle);
    }
    
    
    public void rotateY(double angle) {
        center = rotateYVertex(center, angle);
    }
    
    
    public void rotateZ(double angle) {
        center = rotateZVertex(center, angle);
    }
    
    
}
