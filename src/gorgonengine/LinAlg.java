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
    
    public static Direction normalize(Direction dir)
    {
        double length = Math.sqrt(Math.pow(dir.x, 2) + Math.pow(dir.y, 2) + Math.pow(dir.z, 2));
        dir.x = dir.x/length;
        dir.y = dir.y/length;
        dir.z = dir.z/length;
        return dir;
    }
    
    public static Vertex normalize(Vertex vr)
    {
        double length = Math.sqrt(Math.pow(vr.x, 2) + Math.pow(vr.y, 2) + Math.pow(vr.z, 2));
        vr.x = vr.x/length;
        vr.y = vr.y/length;
        vr.z = vr.z/length;
        return vr;
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
    
    public static Boolean VektorDistansJämförelse(Vertex p0, Vertex p1)
    {
        //Jag tror inte att denna funkar som den ska.
        if((p0.x*p0.x + p0.y*p0.y + p0.z*p0.z) < (p1.x*p1.x + p1.y*p1.y + p1.z*p1.z))
            return true;
        else
            return false;
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
    
}
