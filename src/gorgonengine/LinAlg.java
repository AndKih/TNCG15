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
    public static final double ORENNAYAR_STANDARD_DEVIATION = Math.PI/4;
    
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
        Vertex bisectorOfLight = Vertex.DUMMY, h_projection = Vertex.DUMMY, t_line = Vertex.DUMMY;
        double t_var, u_var, v_var, vprim_var, w_var, d, s, diff_wavelength, fresnel_wavelength, non_light_obstruction, facet_slope_dist;
        switch(reflector_type)
        {
            case Triangle.REFLECTION_LAMBERTIAN:
                result = t.reflectionCoefficient/Math.PI;
                break;
            case Triangle.REFLECTION_ORENNAYAR:
                double standardDeviation = ORENNAYAR_STANDARD_DEVIATION;
                double A = 1 - (Math.pow(standardDeviation, 2)/2*(Math.pow(standardDeviation, 2) + 0.33));
                double B = (0.45*Math.pow(standardDeviation, 2)/(Math.pow(standardDeviation, 2) + 0.09));
                double alf = Math.max(vIn.theta, vOut.theta);
                double beta = Math.min(vIn.theta, vOut.theta);
                result = (t.reflectionCoefficient/Math.PI)*(A+B*(Math.max(0, Math.cos(vIn.phi - vOut.phi)))*Math.sin(alf)*Math.sin(beta));
                break;
//            case Triangle.REFLECTION_COOKTORRANCE:
//                bisectorOfLight = VektorAddition(
//                        VektorMultiplikation(hemisToCart(vIn), returnLength(hemisToCart(vOut))), 
//                        VektorMultiplikation(hemisToCart(vOut), returnLength(hemisToCart(vIn))));
//                h_projection = projection_getter(t, vertexToDir(bisectorOfLight));
//                t_line = tangent_getter(t, vertexToDir(bisectorOfLight));
//                t_var = SkalärProdukt(bisectorOfLight, dirToVertex(t.normal));
//                u_var = SkalärProdukt(bisectorOfLight, hemisToCart(vOut));
//                v_var = SkalärProdukt(hemisToCart(vOut), dirToVertex(t.normal));
//                vprim_var = SkalärProdukt(hemisToCart(vIn), dirToVertex(t.normal));
//                w_var = SkalärProdukt(t_line, h_projection);
//                d = 0;
//                s = 1;
//                diff_wavelength = 0.5;
//                fresnel_wavelength = 0.5;
//                non_light_obstruction = 0.9;
//                facet_slope_dist = 100;
//                result = ((d/Math.PI)*diff_wavelength) + 
//                        (s/(4*Math.PI)*SkalärProdukt(hemisToCart(vOut), hemisToCart(vIn)))*
//                        fresnel_wavelength*non_light_obstruction*facet_slope_dist;
////                double w_var = SkalärProdukt(surface tangent vector and projection of bisector on surface);
//                break;
//            case Triangle.REFLECTION_WARD:
//                bisectorOfLight = VektorAddition(
//                        VektorMultiplikation(hemisToCart(vIn), returnLength(hemisToCart(vOut))), 
//                        VektorMultiplikation(hemisToCart(vOut), returnLength(hemisToCart(vIn))));
//                h_projection = projection_getter(t, vertexToDir(bisectorOfLight));
//                t_line = tangent_getter(t, vertexToDir(bisectorOfLight));
//                t_var = SkalärProdukt(bisectorOfLight, dirToVertex(t.normal));
//                u_var = SkalärProdukt(bisectorOfLight, hemisToCart(vOut));
//                v_var = SkalärProdukt(hemisToCart(vOut), dirToVertex(t.normal));
//                vprim_var = SkalärProdukt(hemisToCart(vIn), dirToVertex(t.normal));
//                w_var = SkalärProdukt(t_line, h_projection);
//                double m = 0.2, n = 0.2;
//                double exponent = ((Math.pow(t_var, 2) - 1)/Math.pow(t_var, 2)) * 
//                        ((Math.pow(w_var, 2)/Math.pow(m, 2)) + ((1 - Math.pow(w_var, 2))/Math.pow(n, 2)));
//                facet_slope_dist = (1/m*n)*Math.exp(exponent);
//                diff_wavelength = 0;
//                s = 1;
//                d = 0;
//                fresnel_wavelength = 0.5;
//                result = ((d/Math.PI)*diff_wavelength) + 
//                        (s/(4*Math.PI*Math.sqrt(SkalärProdukt(hemisToCart(vOut), hemisToCart(vIn)))))*
//                        fresnel_wavelength*facet_slope_dist;
//                break;
            default:
                result = 0;
                break;
        }
        return result;
    }
    
    public static double BRDFsphere(Sphere s, Vertex endpt, int reflector_type, HemisCoords vIn, HemisCoords vOut)
    {
        double result;
        switch(reflector_type)
        {
            case Triangle.REFLECTION_LAMBERTIAN:
                result = s.reflectionCoefficient/Math.PI;
                break;
            case Triangle.REFLECTION_ORENNAYAR:
                double standardDeviation = ORENNAYAR_STANDARD_DEVIATION;
                double A = 1 - (Math.pow(standardDeviation, 2)/2*(Math.pow(standardDeviation, 2) + 0.33));
                double B = (0.45*Math.pow(standardDeviation, 2)/(Math.pow(standardDeviation, 2) + 0.09));
                double alf = Math.max(vIn.theta, vOut.theta);
                double beta = Math.min(vIn.theta, vOut.theta);
                result = (s.reflectionCoefficient/Math.PI)*(A+B*(Math.max(0, Math.cos(vIn.phi - vOut.phi)))*Math.sin(alf)*Math.sin(beta));
                break;
//                case Triangle.REFLECTION_COOKTORRANCE:
////                Vertex bisectorOfLight = VektorAddition(
////                        VektorMultiplikation(hemisToCart(vIn), returnLength(hemisToCart(vOut))), 
////                        VektorMultiplikation(hemisToCart(vOut), returnLength(hemisToCart(vIn))));
////                double t_var = SkalärProdukt(bisectorOfLight, dirToVertex(t.normal));
////                double u_var = SkalärProdukt(bisectorOfLight, hemisToCart(vOut));
////                double v_var = SkalärProdukt(hemisToCart(vOut), dirToVertex(t.normal));
////                double vprim_var = SkalärProdukt(hemisToCart(vIn), dirToVertex(t.normal));
//                double d = 0, s_var = 1;
//                double diff_wavelength = 0.5;
//                double fresnel_wavelength = 0.5;
//                double non_light_obstruction = 0.9;
//                double facet_slope_dist = 100;
//                result = ((d/Math.PI)*diff_wavelength) + 
//                        (s_var/(4*Math.PI)*SkalärProdukt(hemisToCart(vOut), hemisToCart(vIn)))*
//                        fresnel_wavelength*non_light_obstruction*facet_slope_dist;
////                double w_var = SkalärProdukt(surface tangent vector and projection of bisector on surface);
//                break;
//            case Triangle.REFLECTION_WARD:
//                result = 0;
//                break;
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
        result.theta = Math.acos(cart.z/result.r);
        //Phi def. range is [0, 2*Math.pi[
        result.phi = Math.atan(cart.y/cart.x);
//        if(result.phi >= Math.PI*2)
//            while(result.phi > Math.PI*2)
//                result.phi -= Math.PI*2;
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
    
    public static Direction invert(Direction dir)
    {
        Direction result = new Direction(-dir.x, -dir.y, -dir.z);
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
    
    public static double SnellsLaw(double n1, double n2, double angle1)
    {
        return Math.asin((n1*Math.sin(angle1))/n2);
    }
    
    public static double BrewsterAngle(double n1, double n2)
    {
        return Math.asin(n2/n1);
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
    
    public static ColorDbl getLightIntensity(Direction normal, Vertex endpt, Mesh[] ls, int triangleID)
    {
        ColorDbl res = new ColorDbl();
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
    public static Vertex projection_getter(Triangle tri, Direction hLine)
    {
        Vertex hl = dirToVertex(hLine);
        Vertex v1 = VektorSubtraktion(tri.p[1],tri.p[0]);
        Vertex v2 = VektorSubtraktion(tri.p[2],tri.p[0]);
        
        double mult = SkalärProdukt(hl, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(hl, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex hLineProjection = VektorAddition(h_v1,h_v2);
        return hLineProjection;
    }
    
    public static Vertex tangent_getter(Triangle tri, Vertex inLight)
    {
        Vertex v1 = VektorSubtraktion(tri.p[1],tri.p[0]);
        Vertex v2 = VektorSubtraktion(tri.p[2],tri.p[0]);
        
        double mult = SkalärProdukt(inLight, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(inLight, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex tangentProjection = VektorAddition(h_v1,h_v2);
        
        tangentProjection = VektorSubtraktion(tangentProjection,VektorMultiplikation(tangentProjection,2));
        tangentProjection = normalize(tangentProjection);
        return tangentProjection;
    }
    
    public static Vertex projection_getter_sphere(Sphere sph, Vertex rayEnd, Direction hLine)
    {
        Vertex normal = VektorSubtraktion(rayEnd,sph.center);
        
        Vertex vi = new Vertex(1,0,0);
        //if normal parallell med x-planet
        if(Math.abs(normal.y) < EPSILON && Math.abs(normal.z) < EPSILON)
        {
            vi = new Vertex(0,1,0);
        }
        
        Vertex vi_n = VektorMultiplikation(normal,SkalärProdukt(vi, normal)/SkalärProdukt(normal,normal));
        
        Vertex v1 = normalize(VektorSubtraktion(vi,vi_n));
        Vertex v2 = KryssProdukt(v1,normal);
        
        Vertex hl = dirToVertex(hLine);
        
        double mult = SkalärProdukt(hl, v1)/SkalärProdukt(v1,v1);
        Vertex h_v1 = VektorMultiplikation(v1,mult);
        
        mult = SkalärProdukt(hl, v2)/SkalärProdukt(v2,v2);
        Vertex h_v2 = VektorMultiplikation(v2,mult);
        
        Vertex h = VektorAddition(h_v1,h_v2);
        return h;
    }
    
}
