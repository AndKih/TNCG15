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
    private double reflectionCoefficient;
    //public Triangle[] mesh;
    
    public Sphere(ColorDbl c, Vertex center, double radius)
    {
        this.center = center;
        this.radius = radius;
        color = c;
    }
    
    public void setObjectReflection(double p)
    {
        reflectionCoefficient = p;
    }
    
    public Ray rayIntersection(Ray r, PointLightSource[] ls)
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
        if((b/2) - a*c >= 0)
            System.out.println("Schwosh");
        dup = (-b/2) + Math.sqrt(Math.pow(b/2, 2) - a*c);
        ddown = (-b/2) - Math.sqrt(Math.pow(b/2, 2) - a*c);
        Vertex x1 = VektorAddition(r.start, VektorMultiplikation(i, dup));
        Vertex x2 = VektorAddition(r.start, VektorMultiplikation(i, ddown));
        
        if(returnLength(VektorSubtraktion(x2, center))-radius<EPSILON || 
                returnLength(VektorSubtraktion(x1, center))-radius<EPSILON)
        {
            if(returnLength(VektorSubtraktion(x1, r.start)) < 
                    returnLength(VektorSubtraktion(x2, r.start)))
            {
                ColorDbl col = intensityCalc(x1,ls);
                return new Ray(r.start, x1, col);
            }
            else
            {
                ColorDbl col = intensityCalc(x2,ls);
                return new Ray(r.start, x2, col);
            }
        }
        else
            return new Ray(r.start, VektorMultiplikation(VektorSubtraktion(r.end,r.start), 10000), r.color);
    }
    
    public boolean shadowRayIntersection(Ray r, PointLightSource ls, int sphereid)
    {
        
        
        return false;
    }
    
    private ColorDbl intensityCalc(Vertex x , PointLightSource[] ls)
    {
        Direction normal = new Direction(VektorSubtraktion(x,center));
        ColorDbl tmpCol = new ColorDbl(color);
        ColorDbl res = new ColorDbl();
        for(int i = 0; i<ls.length; i++)
        {
            res.setIntensity(getLightIntensity(normal, x, ls[i], -1), tmpCol);
        }
            return res;
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
