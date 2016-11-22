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
    public static final double PHOTON_SEARCH_RADIUS = 0.5;
    public static final double STANDARD_FLUX = 1;
    public static Node<PhotonContainer> octreeRoot;
    
    public static void createOctree(Node<PhotonContainer> root)
    {
        Vertex max = root.returnData().getMaxPos(), min = root.returnData().getMinPos();
        Vertex mid = new Vertex((min.x + max.x)/2, (min.y + max.y)/2, (min.z + max.z)/2);
        double xMod = max.x - mid.x, yMod = max.y - mid.y, zMod = max.z - mid.z;
        
        for(int idnr = 0; idnr < 8; ++idnr)
        {
            Vertex newMax = Vertex.DUMMY, newMin = Vertex.DUMMY;
            switch(idnr)
            {
                case 0:
                    newMax = new Vertex(mid.x + xMod, mid.y + yMod, mid.z + zMod);
                    newMin = mid;
                    break;
                case 1:
                    newMax = new Vertex(mid.x + xMod, mid.y, mid.z + zMod);
                    newMin = new Vertex(mid.x, mid.y - yMod, mid.z);
                    break;
                case 2:
                    newMax = new Vertex(mid.x + xMod, mid.y + yMod, mid.z);
                    newMin = new Vertex(mid.x, mid.y, mid.z - zMod);
                    break;
                case 3:
                    newMax = new Vertex(mid.x + xMod, mid.y, mid.z);
                    newMin = new Vertex(mid.z, mid.y - yMod, mid.z - zMod);
                    break;
                case 4:
                    newMax = new Vertex(mid.x, mid.y + yMod, mid.z + zMod);
                    newMin = new Vertex(mid.x -xMod, mid.y, mid.z);
                    break;
                case 5:
                    newMax = new Vertex(mid.x, mid.y, mid.z + zMod);
                    newMin = new Vertex(mid.x - xMod, mid.y - yMod, mid.z);
                    break;
                case 6:
                    newMax = new Vertex(mid.x, mid.y + yMod, mid.z);
                    newMin = new Vertex(mid.x - xMod, mid.y, mid.z - zMod);
                    break;
                case 7:
                    newMax = mid;
                    newMin = new Vertex(mid.x - xMod, mid.y - yMod, mid.z - zMod);
                    break;
            }
            double[] newDiameters = new double[]{returnLength(new Vertex(newMax.x - newMin.x, 0, 0)), 
                                                 returnLength(new Vertex(0, newMax.y - newMin.y, 0)), 
                                                 returnLength(new Vertex(0, 0, newMax.z - newMin.z))};
            Node<PhotonContainer> it = new Node<PhotonContainer>(new PhotonContainer(newMax, newMin, newDiameters));
            
            root.addChild(it);
        }
    }
    
    public static void addPhotonToTree(Vertex pos, double flux, Direction dir, int type, Node<PhotonContainer> cur)
    {
        for(int idb = 0; idb < octreeRoot.returnChildrenAmount(); ++idb)
        {
            boolean in = true;
            if(pos.x < cur.returnChild(idb).returnData().getMaxPos().x && 
                    pos.y < cur.returnChild(idb).returnData().getMaxPos().y && 
                    pos.z < cur.returnChild(idb).returnData().getMaxPos().z)
                in = true;
            else
                in = false;
            if(pos.x > cur.returnChild(idb).returnData().getMinPos().x && 
                    pos.y > cur.returnChild(idb).returnData().getMinPos().y && 
                    pos.z > cur.returnChild(idb).returnData().getMinPos().z)
                in = true;
            else
                in = false;
            if(in)
            {
                boolean checkDiameters = true;
                double[] curDiameters = cur.returnChild(idb).returnData().getDiameters();
                for(int idd = 0; idd < 3; ++idd)
                {
                    if(curDiameters[idd] > PHOTON_SEARCH_RADIUS)
                    {
                        checkDiameters = false;
                        break;
                    }
                }
                if(!checkDiameters)
                {
                    createOctree(cur.returnChild(idb));
                    addPhotonToTree(pos, flux, dir, type, cur.returnChild(idb));
                }
                else
                {
                    cur.returnChild(idb).returnData().addPhoton(new Photon(pos, flux, dir, type));
                }
                break;
            }
        }
    }
    
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
//        if(Math.acos(cart.z/returnLength(cart)) > (Math.PI/2) && Math.acos(cart.z/returnLength(cart)) < 0)
//        {
//            return new HemisCoords();
//        }
        result.theta = Math.acos(cart.z/result.r);
        //Phi def. range is [0, 2*Math.pi[
        if(cart.y == 0 && cart.x == 0)
            result.phi = 0;
        else
            result.phi = Math.atan(cart.y/cart.x);
        while(result.phi >= 2*Math.PI)
        {
            result.phi -= 2*Math.PI;
        }
        while(result.phi < 0)
        {
            result.phi += 2*Math.PI;
        }
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
//        System.out.println("Sneeeeels lååååååå");
        return Math.asin((n1*Math.sin(angle1))/n2);
    }
    
    public static double BrewsterAngle(double n1, double n2)
    {
        return Math.asin(n2/n1);
    }
    
    public static ColorDbl getLightIntensity(Direction normal, Vertex endpt, PointLightSource ls, int triangleID)
    {
//        getMCAreaLightIntensity(normal, endpt, ls, triangleID);
        return getPointLightIntensity(normal, endpt, ls, triangleID);
    }
    public static ColorDbl getMCAreaLightIntensity(Direction normal, Vertex endpt, int triangleID)
    {
        ColorDbl intensity = new ColorDbl();
        Vertex axis1;
        Vertex axis2;
        Triangle triangle;
        double length1, length2;
        
        PointLightSource fakePoint;
        
        Vertex endPoint;
        ColorDbl returnedIntensity;
        double nHits =0;
        double G =0;
        
        for(int i =0; i< Scene.objects.length; i++)
        {
            if(Scene.objects[i].isLightsource() && !Scene.objects[i].isSphere())
            {
                nHits++;
                for(int r = 0; r<Camera.N_AREALIGHTSOURCEPOINTS; r++)
                {
                    triangle = Scene.objects[i].returnTriangleByIndex(0);
                    axis1 = VektorSubtraktion(triangle.p[1],triangle.p[0]);
                    axis2 = VektorSubtraktion(triangle.p[2],triangle.p[0]);
                    length1 = Math.random();
                    length2 = Math.random();
                    while(length1+length2>1)
                    {
                        length1 = Math.random();
                        length2 = Math.random();
                    }

                    //endpoint = p0 + axis1*length1 + axis2*length2
                    endPoint = triangle.p[0];
                    endPoint = VektorAddition(VektorMultiplikation(axis1,length1), endPoint);
                    endPoint = VektorAddition(VektorMultiplikation(axis2,length2), endPoint);


                    if((endPoint.x > triangle.p[0].x && endPoint.x > triangle.p[1].x && 
                            endPoint.x > triangle.p[2].x) || (endPoint.x < triangle.p[0].x 
                            && endPoint.x < triangle.p[1].x && endPoint.x < triangle.p[2].x))
                    {
                        System.out.println(endPoint);
                    }

                    fakePoint = new PointLightSource(endPoint, triangle.color.meanIntensity()/Scene.LIGHTCOLOR.meanIntensity());
                    returnedIntensity = getPointLightIntensity(normal, endpt,fakePoint , triangleID);
                    double area = 0.5*Math.abs(SkalärProdukt(axis1,axis2));
//                    System.out.println("area "+area);
//                    System.out.println("\n\nArea"+area+"\n\n");
//                    returnedIntensity.setIntensity(1/Camera.N_AREALIGHTSOURCEPOINTS);
//                    G = SkalärProdukt(dirToVertex(normal), VektorSubtraktion(fakePoint.pos,endpt))/
//                            (returnLength(VektorSubtraktion(fakePoint.pos,endpt))*returnLength(dirToVertex(normal)));
                    if(Camera.AREALIGHTAFFECTOR)
                    {
                    G = SkalärProdukt(dirToVertex(triangle.normal), VektorSubtraktion(endpt,fakePoint.pos))/
                            (returnLength(VektorSubtraktion(endpt,fakePoint.pos))*returnLength(dirToVertex(triangle.normal)));
//                    G /=Math.pow(returnLength(VektorSubtraktion(endpt,fakePoint.pos)),2);
                    }
                    else
                    {
                        G = 1;
                    }
                    returnedIntensity.setIntensity(area/Camera.N_AREALIGHTSOURCEPOINTS);
                    returnedIntensity.setIntensity(G);
                    intensity.addColor(returnedIntensity);
                }
            }
        }
//        System.out.println("Intensity "+intensity);
//        intensity.setIntensity((double)1/((double)Camera.N_AREALIGHTSOURCEPOINTS*nHits));
////        System.out.println("Intensity "+intensity+"\n-----------------\n");
        
        if(intensity.meanIntensity()>100)
        {
            System.out.println(intensity);
        }
//                    System.out.println("Intensity " + intensity);
        return intensity;
    }
    public static Vertex randomPointOnTriangle(Triangle triangle)
    {
        Vertex axis1;
        Vertex axis2;
        double length1;
        double length2;
        Vertex endPoint;
        axis1 = VektorSubtraktion(triangle.p[1],triangle.p[0]);
        axis2 = VektorSubtraktion(triangle.p[2],triangle.p[0]);
        length1 = Math.random();
        length2 = Math.random();
        while(length1+length2>1)
        {
            length1 = Math.random();
            length2 = Math.random();
        }

        //endpoint = p0 + axis1*length1 + axis2*length2
        endPoint = triangle.p[0];
        endPoint = VektorAddition(VektorMultiplikation(axis1,length1), endPoint);
        endPoint = VektorAddition(VektorMultiplikation(axis2,length2), endPoint);
        
        return endPoint;
    }
    public static ColorDbl OLDgetMCAreaLightIntensity(Direction normal, Vertex endpt, int triangleID)
    {
        int nrays = 5;
        ColorDbl intensity = new ColorDbl();
        Vertex axis1;
        Vertex axis2;
        Triangle triangle;
        double length1, length2;
        
        ColorDbl tmpCol = new ColorDbl();
        PointLightSource fakePoint;
        
        Vertex endPoint;
        ColorDbl supertemporary;
        for(int i =0; i< Scene.objects.length; i++)
        {
            if(Scene.objects[i].isLightsource() && !Scene.objects[i].isSphere())
            {
                tmpCol = ColorDbl.BLACK; //get rid of garbage vals from prev iteration
                //at this moment ONLY runs for the FIRST triangle in mesh
                
                for(int rays=0; rays<nrays; rays++)
                {
                    triangle = Scene.objects[i].returnTriangleByIndex(0);
                    axis1 = VektorSubtraktion(triangle.p[1],triangle.p[0]);
                    axis2 = VektorSubtraktion(triangle.p[2],triangle.p[0]);
                    length1 = Math.random();
                    length2 = Math.random();
                   
                    //endpoint = p0 + axis1*length1 + axis2*length2
                    endPoint = triangle.p[0];
                    endPoint = VektorAddition(VektorMultiplikation(axis1,length1), endPoint);
                    endPoint = VektorAddition(VektorMultiplikation(axis2,length2), endPoint);
                    
                    fakePoint = new PointLightSource(endPoint, triangle.color.meanIntensity()/100);
                    supertemporary=getPointLightIntensity(normal, endpt,fakePoint , triangleID);
                    System.out.println("Point: ");
                    System.out.println(fakePoint.color);
                    System.out.println(fakePoint.pos);
                    System.out.println(fakePoint.radiance);
                    System.out.println("supertemporary "+supertemporary);
                    tmpCol.addColor(supertemporary);
                    
                }
                double mean = (double)1/nrays;
//                System.out.println("mean "+mean);
                tmpCol.setIntensity(mean);
//                if(tmpCol.meanIntensity()>0.5)
//                System.out.println(tmpCol);
                intensity.addColor(tmpCol);
            }
        }
        return intensity;
    }
    
    public static ColorDbl getPointLightIntensity(Direction normal, Vertex endpt, PointLightSource ls, int triangleID)
    {
        Vertex n = dirToVertex(normal);
        Vertex l = ls.getLightVectorFrom(endpt);
        Ray shadowRay = new Ray(endpt, ls.pos, new ColorDbl(0, 0, 0), -1, Ray.RAY_SHADOW);
        ++Camera.nrRays;
        
        for(int ids = 0; ids < Scene.objects.length; ++ids)
        {
            boolean test = Scene.objects[ids].shadowRayIntersection(shadowRay, ls, triangleID);
            if(test)
            {
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
//        if(res.meanIntensity()>100)
//        {
//            System.out.println(res);
//            System.out.println("length n "+returnLength(n));
//            System.out.println("length l "+returnLength(l));
//        }
        if(Double.isInfinite(res.r) || Double.isInfinite(res.g) || Double.isInfinite(res.b))
        {
            System.out.println(res);
            System.out.println("length n "+returnLength(n));
            System.out.println("length l "+returnLength(l));
        }
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
    
    
    
    public static double VektorVinkel(Direction d1, Direction d2)
    {
        
        return Math.acos(SkalärProdukt(dirToVertex(d1), dirToVertex(d2))/(returnLength(dirToVertex(d1))*returnLength(dirToVertex(d2))));
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
    public static HemisCoords HemisAddition(HemisCoords a1, HemisCoords a2)
    {
        HemisCoords retval = new HemisCoords(a1.theta+a2.theta,a1.phi+a2.phi,a1.r+a2.r);
        retval = negativeAngleFixer(retval);
        return retval;
    }
    public static HemisCoords HemisSubtraction(HemisCoords a1, HemisCoords a2)
    {
        HemisCoords retval = new HemisCoords(a1.theta-a2.theta,a1.phi-a2.phi,a1.r-a2.r);
        retval = negativeAngleFixer(retval);
        return retval;
    }
    public static HemisCoords negativeAngleFixer(HemisCoords in)
    {
        double phi = in.phi;
        double theta = in.theta;
        while(phi<0)
            phi+=Math.PI*2;
        while(theta<0)
            theta+=Math.PI*2;
        return new HemisCoords(theta, phi, in.r);
            
    }
    public static Vertex rotateVertexAroundAxis(Vertex point, Direction axis, double angle)
    {
        Vertex u = dirToVertex(axis);
        double newx = (Math.cos(angle)+Math.pow(u.x,2)*(1-Math.cos(angle)))*point.x;
        newx += (u.x*u.y*(1-Math.cos(angle)))*point.y;
        newx += (u.x*u.z*(1-Math.cos(angle))+u.y*Math.sin(angle))*point.z;
        
        double newy = (u.y*u.x*(1-Math.cos(angle))+u.z*Math.signum(angle))*point.x;
        newy += (Math.cos(angle)+Math.pow(u.y, 2)*(1-Math.cos(angle)))*point.y;
        newy += (u.y*u.z*(1-Math.cos(angle))-u.x*Math.sin(angle))*point.z;
        
        double newz = (u.z*u.x*(1-Math.cos(angle)))*point.x;
        newz += (u.z*u.y*(1-Math.cos(angle))+u.x*Math.sin(angle))*point.y;
        newz += (Math.cos(angle)+Math.pow(u.z, 2)*(1-Math.cos(angle)))*point.z;
        return new Vertex(newx,newy,newz);
    }

    
//    public boolean russianRoullette()
    
}
