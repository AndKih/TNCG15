/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

import static gorgonengine.LinAlg.*;
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
    
    public Ray rayIntersection(Ray r, PointLightSource ls)
    {
        double t = -1, smallT = -10;
        Boolean firstHit = true;
        int savedID = -1;
        Direction normal = new Direction();
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
            normal = mesh[savedID].normal;
            ColorDbl res = new ColorDbl(mesh[savedID].color);
            res.setIntensity(getLightIntensity(normal, r.end, ls));
            return new Ray(r.start, VektorAddition(r.start, VektorMultiplikation(
                        VektorSubtraktion(r.end, r.start), smallT)), res, savedID);
        }
        return new Ray(r.start, VektorMultiplikation(r.end, 10000), r.color);
    }

    @Override
    public void rotateX(double angle) {
        for(int i = 0; i<mesh.length; i++)
        {
            for(int t = 0; t<3; t++)
            {
                mesh[i].p[t] = rotateXVertex(mesh[i].p[t], angle);
            }
            
        }
    }
    
    public void rotateY(double angle) {
        for(int i = 0; i<mesh.length; i++)
        {
            for(int t = 0; t<3; t++)
            {
                mesh[i].p[t] = rotateYVertex(mesh[i].p[t], angle);
            }
            
        }
    }
    
    public void rotateZ(double angle) {
        for(int i = 0; i<mesh.length; i++)
        {
            for(int t = 0; t<3; t++)
            {
                mesh[i].p[t] = rotateZVertex(mesh[i].p[t], angle);
            }
            
        }
    }
    
}
