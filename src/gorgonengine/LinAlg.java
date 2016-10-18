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

import java.lang.Math.*;

public class LinAlg {
    
    public final static double EPSILON = 0.00000001;
    
    public static Vertex VektorProdukt(Vertex p0, Vertex p1, Vertex p2)
    {
        double x, y, z;
        Vertex a, b;
        a = new Vertex(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z);
        b = new Vertex(p2.x - p0.x, p2.y - p0.y, p2.z - p0.z);
        x = (a.y*b.z) - (a.z*b.y);
        y = (a.z*b.x) - (a.x*b.z);
        z = (a.x*b.y) - (a.y*b.x);
        return new Vertex(x, y, z);
    }
    //Dummy
    public static double irradiance(Vertex x, HemisCoords ohm, PointLightSource[] ls, int iteration)
    {
        Vertex p = hemisToCart(ohm);
        double result = 0;
        double incRadiance = 0;
        final int STEPSIZE = 100000;
        for(int idl = 0; idl < ls.length; ++idl)
        {
            Vertex l = ls[idl].getLightVectorFrom(x);
            double angle = SkalärProdukt(p, l)/(returnLength(p)*returnLength(l));
            incRadiance += ls[idl].radiance*angle;
        }
//        for(int idp = 0; idp < 2*Math.PI; idp += (2*Math.PI)/STEPSIZE)
//        {
//            for(int idt = 0; idt < Math.PI/2; idt += (Math.PI/2)/STEPSIZE)
//            {
//                HemisCoords dir = new HemisCoords(idt, idp, 1);
//                //Find triangle located in dir path.
//                if(iteration < maxiterations)
//                    irradiance(newx, dir, ls, ++iteration);
//                //end cycle when iterations reach a certain number
//                //Send a ray in all directions and check the radiance emmitted by targeted triangles, using the radiance function.
//            }
//        }
        return result;
    }
    //Dummy
    public static double radiosity()
    {
        return 0;
    }
    //Dummy
    public static boolean visible(Ray r, int triangleID)
    {
        
        
        
        return true;
    }
    //Dummy
    public static double importance()
    {
        
        return 0;
    }
    
    public static double radiance(Vertex x, Triangle t, HemisCoords in, PointLightSource[] ls)
    {
        Vertex p = hemisToCart(in);
        double result;
        double emmitance = 0;
        double incRadiance = 0;
        int STEPSIZE = 100000;
        for(int idl = 0; idl < ls.length; ++idl)
        {
            Vertex l = ls[idl].getLightVectorFrom(x);
            double angle = SkalärProdukt(p, l)/(returnLength(p)*returnLength(l));
            incRadiance += ls[idl].radiance*angle;
        }
        
//        for(int idp = 0; idp < 2*Math.PI; idp += (2*Math.PI)/STEPSIZE)
//        {
//            for(int idt = 0; idt < Math.PI/2; idt += (Math.PI/2)/STEPSIZE)
//            {
//                HemisCoords out = new HemisCoords(idt, idp, 1);
//                
//                //Send radiance in all directions
//
//                result += BRDF(t, Triangle.REFLECTION_ORENNAYAR, in, out)*irradiance(x, out, ls, 0);
//            }
//        }


        return 0;
    }
    
//    public static double formfactor()
//    {
//        return 0;
//    }
    
    public static double BRDF(Triangle t, int reflector_type, HemisCoords vIn, HemisCoords vOut)
    {
        double result;
        switch(reflector_type)
        {
            case Triangle.REFLECTION_LAMBERTIAN:
                result = t.reflectionCoefficient/Math.PI;
                break;
            case Triangle.REFLECTION_ORENNAYAR:
                double standardDeviation = Math.PI/16;
                double A = 1 - (Math.pow(standardDeviation, 2)/2*(Math.pow(standardDeviation, 2) + 0.33));
                double B = (0.45*Math.pow(standardDeviation, 2)/(Math.pow(standardDeviation, 2) + 0.09));
                double alf = Math.max(vIn.theta, vOut.theta);
                double beta = Math.min(vIn.theta, vOut.theta);
                result = (t.reflectionCoefficient/Math.PI)*(A+B*(Math.max(0, Math.cos(vIn.phi - vOut.phi)))*Math.sin(alf)*Math.sin(beta));
                break;
            default:
                result = 0;
                break;
        }
        return result;
    }
    
//    public static 
    
    public static Direction calculateVectorDirection(Vertex s, Vertex e)
    {
        Direction result = new Direction(e.x - s.x, e.y - s.y, e.z - s.z);
        return normalize(result);
    }
    
    public static Vertex hemisToCart(HemisCoords h)
    {
        Vertex result = new Vertex(0, 0, 0);
        result.x = h.r*Math.cos(h.phi)*Math.sin(h.theta);
        result.y = h.r*Math.sin(h.phi)*Math.sin(h.theta);
        result.z = h.r*Math.cos(h.theta);
        return result;
    }
    
    public static HemisCoords cartToHemis(Vertex cart)
    {
        HemisCoords result = new HemisCoords();
        result.r = returnLength(cart);
        //Theta def. range is [0, (Math.pi/2)].
        if(Math.atan(cart.y/cart.x) > (Math.PI/2))
        {
            return new HemisCoords();
        }
        result.theta = Math.atan(cart.y/cart.x);
        //Phi def. range is [0, 2*Math.pi[
        result.phi = Math.atan((Math.sqrt(Math.pow(cart.x, 2) + Math.pow(cart.y, 2))/2));
        if(result.phi >= Math.PI*2)
            while(result.phi > Math.PI*2)
                result.phi -= Math.PI*2;
        return result;
    }
    
    public static Direction normalize(Direction dir)
    {
        if(dir.equals(Direction.DUMMY))
            return dir;
        double length = Math.sqrt(Math.pow(dir.x, 2) + Math.pow(dir.y, 2) + Math.pow(dir.z, 2));
        dir.x = dir.x/length;
        dir.y = dir.y/length;
        dir.z = dir.z/length;
        return dir;
    }
    
    public static Vertex dirToVertex(Direction dir)
    {
        return new Vertex(dir.x, dir.y, dir.z);
    }
    
    public static Direction vertexToDir(Vertex p)
    {
        return new Direction(p.x, p.y, p.z);
    }
    
    public static Vertex normalize(Vertex vr)
    {
        double length = Math.sqrt(Math.pow(vr.x, 2) + Math.pow(vr.y, 2) + Math.pow(vr.z, 2));
        vr.x = vr.x/length;
        vr.y = vr.y/length;
        vr.z = vr.z/length;
        return vr;
    }
    
    public static Vertex invert(Vertex vr)
    {
        Vertex result = new Vertex(-vr.x, -vr.y, -vr.z);
        return result;
    }
    
    public static Vertex reflect(Vertex vr, Direction norm)
    {
        Vertex result = new Vertex(0, 0, 0);
        Vertex vrnorm = dirToVertex(norm);
        return result;
    }
    
    public static Vertex KryssProdukt(Vertex a, Vertex b)
    {
        double x, y, z;
        x = (a.y*b.z) - (a.z*b.y);
        y = (a.z*b.x) - (a.x*b.z);
        z = (a.x*b.y) - (a.y*b.x);
        return new Vertex(x, y, z);
    }
    
    public static double SkalärProdukt(Vertex p0, Vertex p1)
    {
        double result;
        result = (p0.x * p1.x) + (p0.y * p1.y) + (p0.z * p1.z);
        return result;
    }
    
    public static Vertex VektorAddition(Vertex p0, Vertex p1)
    {
        return new Vertex(p0.x + p1.x, p0.y + p1.y, p0.z + p1.z);
    }
    
    public static Vertex VektorSubtraktion(Vertex p0, Vertex p1)
    {
        return new Vertex(p0.x - p1.x, p0.y - p1.y, p0.z - p1.z);
    }
    
    public static Vertex VektorMultiplikation(Vertex p, double m)
    {
        return new Vertex(m*p.x, m*p.y, m*p.z);
    }
    
    public static Vertex VektorMultiplikation(Vertex p0, Vertex p1)
    {
        return new Vertex(p0.x*p1.x, p0.y*p1.y, p0.z*p1.z);
    }
    
    public static Boolean VektorDistansJämförelse(Vertex p0, Vertex p1, Vertex start)
    {
        Vertex dist0 = VektorSubtraktion(p0, start);
        Vertex dist1 = VektorSubtraktion(p1, start);
        if(returnLength(dist0) < returnLength(dist1))
            return true;
        else
            return false;
    }
    public static double VektorKvadratLängd(Vertex p0)
    {
        return p0.x*p0.x + p0.y*p0.y + p0.z*p0.z;
    }
    
    public static double returnLength(Vertex p)
    {
        return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2) + Math.pow(p.z, 2));
    }
    
    public static ColorDbl getLightIntensity(Direction normal, Vertex endpt, PointLightSource ls, int triangleID)
    {
        int objectID = -1;
        if(triangleID == -1)
            objectID = 2;
        Vertex n = dirToVertex(normal);
        Vertex l = ls.getLightVectorFrom(endpt);
        Ray shadowRay = new Ray(endpt, ls.pos, new ColorDbl(0, 0, 0), -1, Ray.RAY_SHADOW);
        Vertex vr = new Vertex(VektorSubtraktion(shadowRay.end, shadowRay.start));
        
        for(int ids = 0; ids < Scene.objects.length; ++ids)
        {
            boolean test = Scene.objects[ids].shadowRayIntersection(shadowRay, ls, triangleID);
            if(test)
            {
//                System.out.println("IS BLOCKED");
                return ColorDbl.BLACK;
            }
        }
        double angle =  SkalärProdukt(n,l)/(returnLength(n)*returnLength(l));
        
//        if(angle < 0)
//        {
//            System.out.println("Endpt  " + endpt);
//            System.out.println("Scalar " +SkalärProdukt(n,l));
//            System.out.println("Length normal"+ returnLength(n));
//            System.out.println("Length light "+returnLength(l));
//            System.out.println("Multi  " + returnLength(n)*returnLength(l));
//            System.out.println("Normal " + normal.toString());
//            System.out.println("Light  " + l.toString());
//            System.out.println("Angle  " + angle);
//        }
//            System.out.println("Angle: " + angle);
//            System.out.println("pos: " + endpt.toString());
        ColorDbl res = new ColorDbl(ls.color);
        res.setIntensity(angle);
        return res;
    }
    
    public static double Möller_Trumbore(Vertex pe, Vertex ps, Vertex p0, Vertex p1, Vertex p2)
    {
        double t, u, v;
        
        Vertex T  = VektorSubtraktion(ps,p0);
        Vertex E1 = VektorSubtraktion(p1,p0);
        Vertex E2 = VektorSubtraktion(p2,p0);
        Vertex D  = VektorSubtraktion(pe,ps);
        Vertex P  = KryssProdukt(D,E2);
        Vertex Q  = KryssProdukt(T,E1);
        Double dela = SkalärProdukt(P,E1);
//        System.out.println("Värden:");
//        System.out.println(T.toString());
//        System.out.println(E1.toString());
//        System.out.println(D.toString());
//        System.out.println(Q.toString());
//        System.out.println(P.toString());
//        System.out.println(E2.toString());
//        System.out.println("Dela: " + dela);
//        System.out.println(SkalärProdukt(Q,D));

//        if(!dela.equals(0.0) && !dela.equals(-0.0))
//        {
//            System.out.println(dela);
//        }
        
        t = (SkalärProdukt(Q,E2)/ dela);
        u = (SkalärProdukt(P,T)/ dela);
        v = (SkalärProdukt(Q,D)/ dela);
//        System.out.println("t: " + t);
//        System.out.println("u: " + u);
//        System.out.println("v: " + v);
        
        //error calc
        if(u < 0 || v < 0 || u + v > 1)
        {
            t = -1;
        }
        
        return t;
    }
    
    public static Vertex rotateXVertex(Vertex p, double angle)
    {
        double nx = p.x;
        double ny = (p.y*Math.cos(angle)) + (p.z*Math.sin(angle));
        double nz = (-p.y*Math.sin(angle)) + (p.z*Math.cos(angle));
        return new Vertex(nx, ny, nz);
    }
    
    public static Vertex rotateYVertex(Vertex p, double angle)
    {
        double nx = (p.x*Math.cos(angle)) + (p.z*Math.sin(angle));
        double ny = p.y;
        double nz = (-p.x*Math.sin(angle)) + (p.z*Math.cos(angle));
        return new Vertex(nx, ny, nz);
    }
    
    public static Vertex rotateZVertex(Vertex p, double angle)
    {
        double nx = (p.x*Math.cos(angle)) + (p.y*Math.sin(angle));
        double ny = (-p.x*Math.sin(angle)) + (p.y*Math.cos(angle));
        double nz = p.z;
        return new Vertex(nx, ny, nz);
    }
    
    public static Vertex translateVertex(Vertex p, Vertex dir, double length)
    {
        Vertex result;
        Vertex normDir = normalize(dir);
        result = VektorAddition(p, VektorMultiplikation(normDir, length));
        return result;
    }
    
    public static double VektorVinkel(Vertex v1, Vertex v2)
    {
        return SkalärProdukt(v1,v2)/(returnLength(v1)*returnLength(v2));
    }
    
//    public static Vertex perspectiveProjection(Triangle t, Vertex start, Vertex p)
//    {
//        
//        return new Vertex(0, 0, 0);
//    }
//    
//    public static HemisCoords CalculateHemisCoords(Direction normal, Vertex incoming, Triangle t)
//    {
//        Vertex n = dirToVertex(normal);
//        double theta = Math.acos(VektorVinkel(n, incoming));
//        
//        return new HemisCoords(theta, 0, 0);
//    }
    public static Vertex H_getter(Triangle tri, Direction hLine)
    {
        Vertex hl = dirToVertex(hLine);
        Vertex v1 = VektorSubtraktion(tri.p[1],tri.p[0]);
        Vertex v2 = VektorSubtraktion(tri.p[2],tri.p[0]);
        
        double mult = SkalärProdukt(hl, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(hl, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex h = VektorAddition(h_v1,h_v2);
        h = normalize(h);
        return h;
    }
    
}
