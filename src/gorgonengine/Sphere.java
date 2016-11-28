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
    public double reflectionCoefficient;
    private int objectID;
    private int reflectorType;
    private boolean lightsource;
    private boolean transparent;
    private double MATERIAL_PROPERTY;
    //public Triangle[] mesh;
    
    public Sphere(ColorDbl c, Vertex center, double radius, int index)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        this.center = center;
        this.radius = radius;
        color = c;
        objectID = index;
        lightsource = false;
        transparent = false;
        MATERIAL_PROPERTY = Object.PROP_AIR;
    }
    
    public Sphere(ColorDbl c, Vertex center, double radius, int index, boolean ls, boolean trans)
    {
        this.center = center;
        this.radius = radius;
        color = c;
        objectID = index;
        lightsource = ls;
        transparent = trans;
        setReflectorType(Object.REFLECTOR_SPECULAR);
        MATERIAL_PROPERTY = Object.PROP_GLASS;
    }
    
    public void setObjectReflection(double p)
    {
        reflectionCoefficient = p;
    }
    
    public int getSize()
    {
        return -1;
    }
    
    public Ray rayIntersection(Ray r, PointLightSource[] ls)
    {
        if(!transparent && r.getObjectIndex() == objectID)
        {
            System.out.println("Non transparent objects cant hit self.");
            return Ray.ERROR_RAY;
        }
        //final double EPSILON = 0.000001;
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
        dup = (-b/2) + Math.sqrt(Math.pow(b/2, 2) - a*c);
        ddown = (-b/2) - Math.sqrt(Math.pow(b/2, 2) - a*c);
        Vertex x1 = VektorAddition(r.start, VektorMultiplikation(i, dup));
        Vertex x2 = VektorAddition(r.start, VektorMultiplikation(i, ddown));
//        if(r.returnIndex() == -1)
//        {
//            System.out.println("Length: " + returnLength(VektorSubtraktion(x1, r.end)));
//            System.out.println("Start: " + r.start);
//            System.out.println("End: " + r.end);
//            System.out.println("x1: " + x1);
//            System.out.println("X2: " + x2);
//        }
            
        if(returnLength(VektorSubtraktion(x1, r.end)) < EPSILON || 
                returnLength(VektorSubtraktion(x2, r.end)) < EPSILON)
        {
            System.out.println("Distance between x1/x2 and r.end is too short");
            return Ray.ERROR_RAY;
        }
        if(returnLength(VektorSubtraktion(x1, r.start)) < EPSILON || 
                returnLength(VektorSubtraktion(x2, r.start)) < EPSILON)
        {
//            System.out.println("Distance between x1/x2 and r.start is too short");
            return Ray.ERROR_RAY;
        }
        if(returnLength(VektorSubtraktion(x2, center))-radius<EPSILON || 
                returnLength(VektorSubtraktion(x1, center))-radius<EPSILON)
        {
            if(returnLength(VektorSubtraktion(x1, r.start)) < 
                    returnLength(VektorSubtraktion(x2, r.start)))
            {
                Ray dirtest = new Ray(r.start, x1, ColorDbl.BLACK, -1, Ray.RAY_SHADOW);
                double angle = VektorVinkel(r.dir, dirtest.dir);
                if(angle > Math.PI/2 || angle < 0)
                {
//                    System.out.println("Angle between incoming ray and normal too large");
                    return Ray.ERROR_RAY;
                }
                ColorDbl col = intensityCalc(x1,ls);
                Ray resultRay = new Ray(r.start, x1, col, -1, Ray.RAY_IMPORTANCE);
//                resultRay.setImportance(r.getImportance()*reflectionCoefficient);
                resultRay.setImportance(r.getImportance());
                resultRay.setObjectIndex(objectID);
                if(returnLength(VektorSubtraktion(resultRay.start, resultRay.end)) < EPSILON)
                {
                    System.out.println("resultray length too short.");
                    return Ray.ERROR_RAY;
                }
                if(returnLength(VektorSubtraktion(resultRay.start, center))-radius<EPSILON && 
                        returnLength(VektorSubtraktion(resultRay.end, center))-radius<EPSILON && 
                        !transparent)
                {
//                    System.out.println("resultRay start and end is on sphere");
                    return Ray.ERROR_RAY;
                }
                return resultRay;
            }
            else
            {
                Ray dirtest = new Ray(r.start, x2, ColorDbl.BLACK, -1, Ray.RAY_SHADOW);
                Vertex dir1 = dirToVertex(r.dir);
                Vertex dir2 = dirToVertex(dirtest.dir);
                double angle = SkalärProdukt(dir1,dir2)/(returnLength(dir1)*returnLength(dir2));
                if(angle > Math.PI/2 || angle < 0)
                    {
//                    System.out.println("Angle between incoming ray and normal too large");
                    return Ray.ERROR_RAY;
                }
                ColorDbl col = intensityCalc(x2,ls);
                Ray resultRay = new Ray(r.start, x2, col, -1, Ray.RAY_IMPORTANCE);
//                resultRay.setImportance(r.getImportance()*reflectionCoefficient);
                resultRay.setImportance(r.getImportance());
                resultRay.setObjectIndex(objectID);
                if(returnLength(VektorSubtraktion(resultRay.start, resultRay.end)) < EPSILON)
                {
                    System.out.println("resultray length too short.");
                    return Ray.ERROR_RAY;
                }
                    
                if(returnLength(VektorSubtraktion(resultRay.start, center))-radius<EPSILON && 
                        returnLength(VektorSubtraktion(resultRay.end, center))-radius<EPSILON && 
                        !transparent)
                {
//                    System.out.println("resultRay start and end is on sphere");
                    return Ray.ERROR_RAY;
                }
                return resultRay;
            }
        }
        else
        {
            return Ray.ERROR_RAY;
        }
    }
    
    public boolean shadowRayIntersection(Ray r, PointLightSource ls, int sphereid)
    {
        double smallT = -10, t = -1;
        if(sphereid == -1)
        {
            for(int id1 = 0; id1 < Scene.objects.length; ++id1)
            {
                if(!Scene.objects[id1].isSphere())
                {
                    for(int id2 = 0; id2 < Scene.objects[id1].returnSize(); ++id2)
                    {
//                        System.out.println("TEST");
                        t = Scene.objects[id1].returnTriangleByIndex(id2).rayIntersection(r);
                        if(smallT <= t && t > 0)
                            smallT = t;
                    }
                }
                else
                {
                    if(id1 != objectID)
                    {
                        
                    }
                }
            }
        }
        else
        {
            //final double EPSILON = 0.000001;
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
                    Ray dirtest = new Ray(r.start, x1, ColorDbl.BLACK, -1, Ray.RAY_SHADOW);
                    Vertex dir1 = dirToVertex(r.dir);
                    Vertex dir2 = dirToVertex(dirtest.dir);
                    double angle = SkalärProdukt(dir1,dir2)/(returnLength(dir1)*returnLength(dir2));
                    if(angle < Math.PI/2 && angle > 0)
                        smallT = 0.5;
                    else
                        smallT = -10;
                }
                else
                {
                    Ray dirtest = new Ray(r.start, x2, ColorDbl.BLACK, -1, Ray.RAY_SHADOW);
                    Vertex dir1 = dirToVertex(r.dir);
                    Vertex dir2 = dirToVertex(dirtest.dir);
                    double angle = SkalärProdukt(dir1,dir2)/(returnLength(dir1)*returnLength(dir2));
                    if(angle < Math.PI/2 && angle > 0)
                        smallT = 0.5;
                    else
                        smallT = -10;
                }
            }
            else
            {
                smallT = -10;
            }
        }
        
        if(smallT != -10 && smallT <= 1 && !transparent)
        {
//            System.out.println("TEST");
            return true;
        }
        return false;
    }
    public double returnSize()
    {
        return radius;
    }
    
    public Direction returnNormal(Vertex vr)
    {
        //double EPSILON = 0.0000001;
        if(returnLength(VektorSubtraktion(vr, center))-radius<EPSILON || 
                returnLength(VektorSubtraktion(vr, center))-radius<EPSILON)
        {
            return normalize(vertexToDir(VektorSubtraktion(vr, center)));
        }
        else
            return Direction.DUMMY;
    }
    
    private ColorDbl intensityCalc(Vertex x , PointLightSource[] ls)
    {
        Direction normal = new Direction(VektorSubtraktion(x,center));
        ColorDbl tmpCol = new ColorDbl(color);
        ColorDbl res = new ColorDbl();
        if(Camera.photonLight)
        {
            ColorDbl retint = getPhotonLight(normal, x, -1);
            res.setIntensity(retint, tmpCol);
        }else if(Camera.areaLightsource)
        {
            ColorDbl retint = getMCAreaLightIntensity(normal, x, -1);
            res.setIntensity(retint, tmpCol);
        }else{
            for(int i = 0; i<ls.length; i++)
            {
                res.setIntensity(getLightIntensity(normal, x, ls[i], -1), tmpCol);
            }
        }
            return res;
    }
    
    public Triangle returnTriangleByIndex(int index)
    {
        return Triangle.DUMMY;
    }
    
    public Triangle returnTriangleById(int id)
    {
        return Triangle.DUMMY;
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
    
    public boolean isSphere()
    {
        return true;
    }
    
    public boolean isTransparent()
    {
        return transparent;
    }
    
    public boolean isLightsource()
    {
        return lightsource;
    }
    
    public int getReflectorType()
    {
        return reflectorType;
    }
    
    public void setReflectorType(int newType)
    {
        if(!transparent)
            reflectorType = newType;
        else
            reflectorType = Object.REFLECTOR_SPECULAR;
    }
    
    public double returnProperty()
    {
        return MATERIAL_PROPERTY;
    }
    
    public boolean checkTriangleIndexes(int index)
    {
        return false;
    }

    @Override
    public void setLightsource() {
        lightsource = true;
    }
    
    public Vertex getRandomPointOnSphere()
    {
        Vertex result = Vertex.DUMMY;
        
        return result;
    }
    
}
