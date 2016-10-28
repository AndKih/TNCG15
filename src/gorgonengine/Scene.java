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

import static gorgonengine.Camera.N_REFLECTEDRAYS;
import static gorgonengine.LinAlg.*;
import java.util.Vector;

public class Scene {
    
    public final static int SIZE = 24;
    public static final ColorDbl LIGHTCOLOR = new ColorDbl(100,100,100);
    public final int NROBJECTS;
    public Triangle[] mesh = new Triangle[SIZE];
    Sphere sphere;
    
    
    public static int counter = 1;
    public static Object[] objects;
    PointLightSource[] lights;
    
    public Scene()
    {
        //Middleposition: roof, (0, 0, 5). Floor: (0, 0, -5)
        final int TRIANGLESIZE = 3;
        Vertex[] p = new Vertex[TRIANGLESIZE];
        //Vägg norr (+y)
        p[0] = new Vertex(10, 6, 5);
        p[1] = new Vertex(0, 6, 5);
        p[2] = new Vertex(0, 6, -5);
        mesh[0] = new Triangle(p, new ColorDbl(0, 0, 100), counter);
        ++counter;
        p[0] = new Vertex(10, 6, 5);
        p[1] = new Vertex(0, 6, -5);
        p[2] = new Vertex(10, 6, -5);
        mesh[1] = new Triangle(p, new ColorDbl(0, 0, 100), counter);
        
        ++counter;
        //Vägg nordväst (+y, -x)
        p[0] = new Vertex(0, 6, 5);
        p[1] = new Vertex(-3, 0, 5);
        p[2] = new Vertex(-3, 0, -5);
        mesh[2] = new Triangle(p, new ColorDbl(0, 100, 0), counter);
        
        ++counter;
        p[0] = new Vertex(0, 6, 5);
        p[1] = new Vertex(-3, 0, -5);
        p[2] = new Vertex(0, 6, -5);
        mesh[3] = new Triangle(p, new ColorDbl(0, 100, 0), counter);
        
        ++counter;
        //Vägg sydväst (-y, -x)
        p[0] = new Vertex(-3, 0, 5);
        p[1] = new Vertex(0, -6, 5);
        p[2] = new Vertex(0, -6, -5);
        mesh[4] = new Triangle(p, new ColorDbl(100, 0, 0), counter);
        
        ++counter;
        p[0] = new Vertex(-3, 0, 5);
        p[1] = new Vertex(0, -6, -5);
        p[2] = new Vertex(-3, 0, -5);
        mesh[5] = new Triangle(p, new ColorDbl(100, 0, 0), counter);
        
        ++counter;
        //Vägg Syd (-y)
        p[0] = new Vertex(0, -6, 5);
        p[1] = new Vertex(10, -6, 5);
        p[2] = new Vertex(10, -6, -5);
        mesh[6] = new Triangle(p, new ColorDbl(100, 100, 0), counter);
        
        ++counter;
        p[0] = new Vertex(0, -6, 5);
        p[1] = new Vertex(10, -6, -5);
        p[2] = new Vertex(0, -6, -5);
        mesh[7] = new Triangle(p, new ColorDbl(100, 100, 0), counter);
        
        ++counter;
        //Vägg Sydost (-y, +x)
        p[0] = new Vertex(10, -6, 5);
        p[1] = new Vertex(13, 0, 5);
        p[2] = new Vertex(13, 0, -5);
        mesh[8] = new Triangle(p, new ColorDbl(100, 0, 100), counter);
        
        ++counter;
        p[0] = new Vertex(10, -6, 5);
        p[1] = new Vertex(13, 0, -5);
        p[2] = new Vertex(10, -6, -5);
        mesh[9] = new Triangle(p, new ColorDbl(100, 0, 100), counter);
        
        ++counter;
        //Väg Nordost (+y, +x)
        p[0] = new Vertex(10, 6, 5);
        p[1] = new Vertex(10, 6, -5);
        p[2] = new Vertex(13, 0, 5);
        mesh[10] = new Triangle(p, new ColorDbl(0, 100, 100), counter);
        
        ++counter;
        p[0] = new Vertex(13, 0, 5);
        p[1] = new Vertex(10, 6, -5);
        p[2] = new Vertex(13, 0, -5);
        mesh[11] = new Triangle(p, new ColorDbl(0, 100, 100), counter);
        
        ++counter;
        //Tak norr (+y)
        p[0] = new Vertex(0, 6, 5);
        p[1] = new Vertex(10, 6, 5);
        p[2] = new Vertex(0, 0, 5);
        mesh[12] = new Triangle(p, new ColorDbl(100, 100, 100), counter);
        
        ++counter;
        //Tak Nordväst (+y, -x)
        p[0] = new Vertex(-3, 0, 5);
        p[1] = new Vertex(0, 6, 5);
        p[2] = new Vertex(0, 0, 5);
        mesh[13] = new Triangle(p, new ColorDbl(100, 100, 100), counter);
        
        ++counter;
        //Tak Sydväst (-y, -x)
        p[0] = new Vertex(0, -6, 5);
        p[1] = new Vertex(-3, 0, 5);
        p[2] = new Vertex(0, 0, 5);
        mesh[14] = new Triangle(p, new ColorDbl(100, 100, 100), counter);
        
        ++counter;
        //Tak syd (-y)
        p[0] = new Vertex(10, -6, 5);
        p[1] = new Vertex(0, -6, 5);
        p[2] = new Vertex(0, 0, 5);
        mesh[15] = new Triangle(p, new ColorDbl(100, 100, 100), counter);
        
        ++counter;
        //Tak Sydost (-y, +x)
        p[0] = new Vertex(13, 0, 5);
        p[1] = new Vertex(10, -6, 5);
        p[2] = new Vertex(0, 0, 5);
        mesh[16] = new Triangle(p, new ColorDbl(100, 100, 100), counter);
        
        ++counter;
        //Tak Nordost (+y, +x)
        p[0] = new Vertex(10, 6, 5);
        p[1] = new Vertex(13, 0, 5);
        p[2] = new Vertex(0, 0, 5);
        mesh[17] = new Triangle(p, new ColorDbl(100, 100, 100), counter);
        
        ++counter;
        //Golv Norr (+y)
        p[0] = new Vertex(0, 0, -5);
        p[1] = new Vertex(10, 6, -5);
        p[2] = new Vertex(0, 6, -5);
        mesh[18] = new Triangle(p, new ColorDbl(50, 50, 50), counter);
        
        ++counter;
        //Golv Nordväst (+y, -x)
        p[0] = new Vertex(0, 0, -5);
        p[1] = new Vertex(0, 6, -5);
        p[2] = new Vertex(-3, 0, -5);
        mesh[19] = new Triangle(p, new ColorDbl(50, 50, 50), counter);
        
        ++counter;
        //Golv Sydväst (-y, -x)
        p[0] = new Vertex(0, 0, -5);
        p[1] = new Vertex(-3, 0, -5);
        p[2] = new Vertex(0, -6, -5);
        mesh[20] = new Triangle(p, new ColorDbl(50, 50, 50), counter);
        
        ++counter;
        //Golv Syd (-y)
        p[0] = new Vertex(0, 0, -5);
        p[1] = new Vertex(0, -6, -5);
        p[2] = new Vertex(10, -6, -5);
        mesh[21] = new Triangle(p, new ColorDbl(50, 50, 50), counter);

        ++counter;
        //Golv Sydost (-y, +x)
        p[0] = new Vertex(13, 0, -5);
        p[1] = new Vertex(0, 0, -5);
        p[2] = new Vertex(10, -6, -5);
        mesh[22] = new Triangle(p, new ColorDbl(50, 50, 50), counter);
        ++counter;
        //Golv Nordost (+y, +x)
        p[0] = new Vertex(0, 0, -5);
        p[1] = new Vertex(13, 0, -5);
        p[2] = new Vertex(10, 6, -5);
        mesh[23] = new Triangle(p, new ColorDbl(50, 50, 50), counter);
        
        ++counter;
        
        for(int idm = 0; idm < SIZE; ++idm)
        {
            mesh[idm].setReflectionCoefficient(1);
        }
        NROBJECTS = 5;
        objects = new Object[NROBJECTS];
        objects[0] = new Mesh(0, mesh);
        
        Triangle[] mesh2 = new Triangle[4];
        Vertex[] pnew = new Vertex[TRIANGLESIZE];
        pnew[0] = new Vertex(10, 3, 3);
        pnew[1] = new Vertex(10, 1, -1);
        pnew[2] = new Vertex(8, 1, 1);
        mesh2[0] = new Triangle(pnew, new ColorDbl(50, 70, 25), counter);
        
        ++counter;
        pnew[0] = new Vertex(10, 1, -1);
        pnew[1] = new Vertex(10, -1, 3);
        pnew[2] = new Vertex(8, 1, 1);
        mesh2[1] = new Triangle(pnew, new ColorDbl(70, 25, 50), counter);
        
        ++counter;
        pnew[0] = new Vertex(8, 1, 1);
        pnew[1] = new Vertex(10, -1, 3);
        pnew[2] = new Vertex(10, 3, 3);
        mesh2[2] = new Triangle(pnew, new ColorDbl(60, 30, 80), counter);
        
        ++counter;
        pnew[0] = new Vertex(10, 3, 3);
        pnew[1] = new Vertex(10, -1, 3);
        pnew[2] = new Vertex(10, 1, -1);
        mesh2[3] = new Triangle(pnew, new ColorDbl(25, 50, 70), counter);
        
        ++counter;
        
        p[0] = new Vertex(-3, 0, 5);
        p[1] = new Vertex(0, 6, 5);
        p[2] = new Vertex(0, 0, 5);
        
        Triangle[] mesh3 = new Triangle[1];
        Vertex[] pnew2 = new Vertex[TRIANGLESIZE];
        pnew2[0] = new Vertex(10, 6, 4.999);
        pnew2[1] = new Vertex(13, 0, 4.999);
        pnew2[2] = new Vertex(0, 0, 4.999);
        
//        pnew2[0] = new Vertex(-3, 0, 4.999);
//        pnew2[1] = new Vertex(0, 6, 4.999);
//        pnew2[2] = new Vertex(0, 0, 4.999);
        mesh3[0] = new Triangle(pnew2, new ColorDbl(100,100,100), counter);
        
        
        ++counter;
        
        
        
        objects[0].setObjectReflection(0.5);
//        objects[0].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[1] = new Mesh(1, mesh2);
        objects[1].setObjectReflection(0.5);
//        objects[1].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[2] = new Sphere(new ColorDbl(0, 0, 0), new Vertex(11, -2, 1), 1, 2);
        objects[2].setObjectReflection(0.5);
//        objects[2].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
//        objects[4] = new Mesh(new Vertex(7, 2, 2), Mesh.TYPE_RECTANGLE);
//        objects[4].setObjectReflection(0.5);
//        objects[4].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[3] = new Mesh(3, new double[] {2}, new Vertex(5, -3, -2), Mesh.TYPE_CUBE, Mesh.COLOR_ORANGE, false, false);
        objects[3].setObjectReflection(0.8);
        
        
//        objects[4] = new Sphere(ColorDbl.GREEN, new Vertex(8, -3.5, -2.5), 1, 4);
//        objects[4].setObjectReflection(0.5);
        
        
        objects[4] = new Mesh(4, mesh3);
        objects[4].setLightsource();
//        objects[4].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
//        objects[5] = new Mesh(new double[] {2, 3, 4}, new Vertex(6, 3, -2), Mesh.TYPE_RECTANGLE, Mesh.COLOR_PURPLE);
//        objects[5].setObjectReflection(0.5);
//        objects[5].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
//        objects[6] = new Sphere(ColorDbl.GREEN, new Vertex(10, -3, -4), 1, 6);
//        objects[6].setObjectReflection(0.5);
//        objects[6].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        if(false)
        {
            int n=20;
            lights = new PointLightSource[n];
            
            Triangle triangle = Scene.objects[4].returnTriangleByIndex(0);
            Vertex axis1;
            Vertex axis2;
            double length1;
            double length2;
            Vertex endPoint;
            for(int i = 0; i<n; i++)
            {
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
                lights[i] = new PointLightSource(endPoint,0.33);
            }
            
        }else{
            lights = new PointLightSource[2];
            lights[0] = new PointLightSource(new Vertex(2,-3,-2),1.0);
            lights[1] = new PointLightSource(new Vertex(3,4,-3),0.7);
        }
        
        
        for(int i = 0; i < NROBJECTS; i++)
        {
            objects[i].setReflectorType(Object.REFLECTOR_DIFFUSE);
        }
        objects[2].setReflectorType(Object.REFLECTOR_SPECULAR);
//        objects[3].setReflectorType(Object.REFLECTOR_SPECULAR);
        
        
    }
    
    public Ray rayIntersection(Node<Ray> r)
    {
        Ray newRay, reflectedRay = Ray.ERROR_RAY, refractedRay = Ray.ERROR_RAY, resultRay;
        
        Node<Ray> rayit = new Node<Ray>(), rayfrit = new Node<Ray>();
        
        Ray largestRay = objects[0].rayIntersection(r.returnData(), lights);
        largestRay.setReflectionType(Ray.RAY_REFLECTION);
//        boolean failsafe = true;
//        if(largestRay.returnIndex() == -3)
//            System.out.println("Wall mesh rayintersection returns an error ray!");
        for(int idt = 1; idt < objects.length; ++idt)
        {
            if(r.returnData().getObjectIndex() == idt && r.returnData().getReflectionType() != Ray.RAY_REFRACTION && !objects[idt].isTransparent())
                continue;
            newRay = objects[idt].rayIntersection(r.returnData(), lights);
//            if(idt==1 && VektorDistansJämförelse(r.end, newRay.end))
//            {
//                System.out.println(newRay.end.toString()+"   |   "+
//                        largestRay.end.toString());
//            }
//            if(failsafe && r.returnData().returnIndex() <= 24 && r.returnData().returnIndex() == largestRay.returnIndex())
//            {
//                System.out.println("Failsafe.");
//                largestRay = new Ray(newRay.start, newRay.end, newRay.color, newRay.returnIndex(), Ray.RAY_IMPORTANCE);
//                largestRay.setReflectionType(Ray.RAY_REFLECTION);
//                largestRay.setObjectIndex(newRay.getObjectIndex());
//                largestRay.setImportance(newRay.getImportance());
//                failsafe = false;
//                continue;
//            }
            if(newRay.returnIndex() != -3)
            {
                if(VektorDistansJämförelse(newRay.end, largestRay.end, r.returnData().start))
                {
                    
                    largestRay = new Ray(newRay.start, newRay.end, newRay.color, newRay.returnIndex(), Ray.RAY_IMPORTANCE);
                    largestRay.setReflectionType(Ray.RAY_REFLECTION);
                    largestRay.setObjectIndex(newRay.getObjectIndex());
                    largestRay.setImportance(newRay.getImportance());
                }
            }
        }
        if(objects[largestRay.getObjectIndex()].isLightsource())
        {
//            if( largestRay.getObjectIndex() == 4)
//            System.out.println(largestRay.color);
//            if(objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).color != 
//                    LIGHTCOLOR)
//            {
//                System.out.println("\n\nCOlor changed ");
//                System.out.println(objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).color+"\n\n");
//            }
//            return new Ray(largestRay.start,largestRay.end,
//                    objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).color);
            Ray retRay = new Ray(largestRay.start,largestRay.end, new ColorDbl(
                    objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).color.r*
                            objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).getArea(),
                    objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).color.g*
                            objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).getArea(),
                    objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).color.b*
                            objects[largestRay.getObjectIndex()].returnTriangleByIndex(0).getArea()));
//            Ray retRay = new Ray(largestRay.start,largestRay.end, largestRay.color);
            retRay.setImportance(largestRay.getImportance());
            return retRay;
        }
        
//        if(largestRay.equals(Ray.ERROR_RAY))
//        {
//            System.out.println("largestRay is an ERROR_RAY! Incoming ray is: " + r.returnData().returnIndex() + " and " + r.returnData().getObjectIndex());
//            System.out.println("Full ray stats: " + r.returnData().toString());
//            Node<Ray> traverse = r;
//            int length = 0;
//            while(traverse.checkHasParent())
//            {
//                ++length;
//                traverse = traverse.returnParent();
//                System.out.println("Current node: " + traverse.returnData());
////                if(length > 4)
////                {
////                    System.out.println("Length greater than 4! Stuck in:" + traverse.returnData().getObjectIndex());
////                    System.out.println("Current importance: " + traverse.returnData().getImportance());
////                }
//                    
//                
//            }
//            System.out.println("Length of tree at this point: " + length);
//        }
        
            
//        if(r.returnData().returnIndex() == 1)
//            System.out.println("Triangle 1: " + r.returnData().color);
//        if(largestRay.returnIndex() ==  -3)
//        {
//            System.out.println("LargestRay is now an error ray!");
//        }
        
//        if(r.returnData().returnIndex() == largestRay.returnIndex() && r.returnData().returnIndex() != -1 && largestRay.returnIndex() != -1)
//        {
//            System.out.println("Same index:" + largestRay.returnIndex());
//            System.out.println("LargestRay start: " + largestRay.start + "\nLargestRay   end: " + largestRay.end);
//            System.out.println("returnedRay start:" + r.returnData().start + "\nreturnedRay   end:" + r.returnData().end);
//        }

        if(largestRay.getImportance() > Camera.IMPORTANCETHRESHOLD)
        {
            int nRefl = N_REFLECTEDRAYS;
//            if(objects[objIndex].isTransparent())
//            {
//                nRefl = 2;
//            }
            if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
            {
                nRefl = 1;
            }
            if(objects[largestRay.getObjectIndex()].isLightsource())
            {
                nRefl = 0;
            }
            
//            Ray[] tmpRay = new Ray[nRefl]; 
            for(int indRefl = 0; indRefl < nRefl; indRefl++)
            {
    //            System.out.println("Current importance: " + largestRay.getImportance());
                r.setData(largestRay);

                if(!objects[largestRay.getObjectIndex()].isSphere())
                {
                    Triangle pick = objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex());
                    Direction normal = pick.normal;
                    Vertex refEnd = Vertex.DUMMY;
//                    double largestAngle = Math.acos((SkalärProdukt(invert(dirToVertex(largestRay.dir)), dirToVertex(normal))));
                    double largestAngle = VektorVinkel(invert(largestRay.dir), normal);
                    boolean totalReflection = false;
                    if(r.returnData().getReflectionType() == Ray.RAY_REFLECTION)
                    {
                        if(largestAngle > BrewsterAngle(Object.PROP_AIR, objects[largestRay.getObjectIndex()].returnProperty()))
                            totalReflection = true;
                    }
                    else
                    {
                        if(largestAngle > BrewsterAngle(objects[largestRay.getObjectIndex()].returnProperty(), Object.PROP_AIR))
                            totalReflection = true;
                    }
                    //Random angle reflection
                    if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_DIFFUSE)
//                        refEnd = randomAngle(normal, objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex())
//                                    );
                        refEnd = randomAngleMC2(normal, objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex())
                                    ,vertexToDir(VektorSubtraktion(largestRay.start, largestRay.end)));
                    else if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
                    {
                                //System.out.println("Specular reflection!");
                        refEnd = VektorSubtraktion(dirToVertex(largestRay.dir), VektorMultiplikation( 
                            VektorMultiplikation(dirToVertex(normal), SkalärProdukt(
                                    dirToVertex(largestRay.dir), dirToVertex(normal))/Math.pow(returnLength(dirToVertex(normal)), 2))
                                    , 2));
                    }
                    refEnd = normalize(refEnd);
//                    if(Double.isNaN(refEnd.x) || Double.isNaN(refEnd.y) || Double.isNaN(refEnd.z))
//                    {
//                        System.out.println("YOU GOT NAN!!!");
//                        System.out.println("LargestRay: " + largestRay);
//                        if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_DIFFUSE)
//                            System.out.println("Diffuse reflection");
//                        else
//                            System.out.println("Specular reflection");
//                        Node<Ray> traverse = r;
//                        int length = 0;
//                        while(traverse.checkHasParent())
//                        {
//                            ++length;
//                            traverse = traverse.returnParent();
//                            System.out.println("Current node: " + traverse.returnData());
//            //                if(length > 4)
//            //                {
//            //                    System.out.println("Length greater than 4! Stuck in:" + traverse.returnData().getObjectIndex());
//            //                    System.out.println("Current importance: " + traverse.returnData().getImportance());
//            //                }
//
//
//                        }
//                        System.out.println("Length of tree at this point: " + length);
//                    }
                    reflectedRay = new Ray(largestRay.end, VektorAddition(largestRay.end, refEnd), largestRay.color, largestRay.returnIndex(), Ray.RAY_IMPORTANCE);
                    reflectedRay.setReflectionType(Ray.RAY_REFLECTION);
//                    System.out.println("reflectedRay reflectionType: " + reflectedRay.getReflectionType());
                    reflectedRay.setObjectIndex(largestRay.getObjectIndex());
                    HemisCoords impIn = cartToHemis(dirToVertex(largestRay.dir));
                    HemisCoords impOut = cartToHemis(dirToVertex(reflectedRay.dir));
                    if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
                    {
                        reflectedRay.setImportance(largestRay.getImportance()*
                                objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex()).reflectionCoefficient);
                    }
                    else
                    {
                        reflectedRay.setImportance(largestRay.getImportance()*Math.PI*BRDF(
                            objects[getObjectByTriangleIndex(largestRay.returnIndex())].
                            returnTriangleById(largestRay.returnIndex()), 
                            Triangle.REFLECTION_ORENNAYAR, impIn, impOut));
                    }
                    
                    if(objects[largestRay.getObjectIndex()].isTransparent() && !totalReflection)
                    {
                        Vertex refrEnd = Vertex.DUMMY;
                        double refrAngle = 0;
                        if(r.returnData().getReflectionType() == Ray.RAY_REFLECTION)
                            refrAngle = SnellsLaw(Object.PROP_AIR, objects[largestRay.getObjectIndex()].returnProperty(), largestAngle);
                        else
                            refrAngle = SnellsLaw(objects[largestRay.getObjectIndex()].returnProperty(), Object.PROP_AIR, largestAngle);
                        Vertex projDir = projection_getter(pick, invert(largestRay.dir));
                        Vertex ortoDir = VektorSubtraktion(dirToVertex(largestRay.dir), projDir);
                        refrEnd = VektorAddition(VektorMultiplikation(dirToVertex(normal), Math.cos(refrAngle)), 
                                VektorMultiplikation(ortoDir, Math.sin(refrAngle)/returnLength(ortoDir)));
                        refractedRay = new Ray(largestRay.end, VektorAddition(largestRay.end, refrEnd), largestRay.color, largestRay.returnIndex(), Ray.RAY_IMPORTANCE);
                        refractedRay.setReflectionType(Ray.RAY_REFRACTION);
//                        System.out.println("refractedRay reflectionType: " + refractedRay.getReflectionType());
                        refractedRay.setObjectIndex(largestRay.getObjectIndex());
                        refractedRay.setImportance(largestRay.getImportance()*
                                objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex()).reflectionCoefficient);
//                        if(Double.isNaN(refractedRay.end.x) || Double.isNaN(refractedRay.end.y) || Double.isNaN(refractedRay.end.z))
//                        {
//                            System.out.println("NaN detected in refractedRay!");
//                            System.out.println("Ray stats: " + refractedRay);
//                            System.out.println("Parent ray: " + r.returnData());
//                            System.out.println("refrAngle: " + refrAngle);
//                            System.out.println("largestAngle: " + largestAngle);
//                            System.out.println("projDir: " + projDir);
//                            System.out.println("ortoDir: " + ortoDir);
//                            System.out.println("refrEnd: " + refrEnd);
//                        }
                        rayfrit = new Node<Ray>(refractedRay, r);
//                        System.out.println("rayfrit reflectionType: " + rayfrit.returnData().getReflectionType());
                        r.addChild(rayfrit);
                    }
                    rayit = new Node<Ray>(reflectedRay, r);
//                    System.out.println("rayit reflectionType: " + rayit.returnData().getReflectionType());
                    r.addChild(rayit);
                }
                else
                {
                    for(int ido = 0; ido < objects.length; ++ido)
                    {
                        if(!objects[ido].isSphere())
                            continue;
                        else
                        {
                            Direction normal = objects[ido].returnNormal(largestRay.end);
                            if(normal == Direction.DUMMY)
                                continue;
                            else
                            {
//                                if(ido == 2 && largestRay.getSphereIndex() == 2)
//                                {
//                                    System.out.println(objects[ido].getReflectorType());
//                                    System.out.println("Number of reflections: " + nRefl);
//                                }
                                Vertex refEnd = Vertex.DUMMY;
                                if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_DIFFUSE)
                                {
//                                    refEnd = randomAngle(normal, objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex())
//                                    );
                                refEnd = randomAngleMC2(normal, objects[largestRay.getObjectIndex()].returnTriangleById(largestRay.returnIndex())
                                    ,vertexToDir(VektorSubtraktion(largestRay.start, largestRay.end)));
                                }
                                    else if(objects[largestRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
                                {
                                    refEnd = VektorSubtraktion(dirToVertex(largestRay.dir), VektorMultiplikation( 
                                        VektorMultiplikation(dirToVertex(normal), SkalärProdukt(
                                                dirToVertex(largestRay.dir), dirToVertex(normal))/Math.pow(returnLength(dirToVertex(normal)), 2))
                                                , 2));
                                }
                                else
                                {
                                    System.out.println("Object "+getObjectByTriangleIndex(largestRay.returnIndex()) + " don't have any specified reflection type.");
                                }
                                refEnd = normalize(refEnd);
                                reflectedRay = new Ray(largestRay.end, VektorAddition(largestRay.end, refEnd), largestRay.color, -1, Ray.RAY_IMPORTANCE);
                                reflectedRay.setReflectionType(Ray.RAY_REFLECTION);
                                HemisCoords impIn = cartToHemis(dirToVertex(largestRay.dir));
                                HemisCoords impOut = cartToHemis(dirToVertex(reflectedRay.dir));
                                if(objects[reflectedRay.getObjectIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
                                {
                                    reflectedRay.setImportance(largestRay.getImportance()*objects[largestRay.getObjectIndex()].reflectionCoefficient);
                                }
                                else
                                {
                                    reflectedRay.setImportance(largestRay.getImportance()*Math.PI*BRDFsphere(
                                        (Sphere)objects[largestRay.getObjectIndex()], reflectedRay.end,
                                        Triangle.REFLECTION_ORENNAYAR, impIn, impOut));
                                }
//                                reflectedRay.setImportance(largestRay.getImportance());
                                rayit = new Node<Ray>(reflectedRay, r);
                                r.addChild(rayit);
                                break;
                            }
                        }
                    }
                }
                
//                tmpRay[indRefl] = rayIntersection(rayit);
//                ColorDbl col = new ColorDbl(largestRay.color);
//                col.setIntensity(r.returnData().getImportance());
//                tmpRay[indRefl].color.addColor(col);
            }
//            for(int idc = 0; idc < r.returnChildrenAmount(); ++idc)
//            {
//                Node<Ray> it = r.returnChild(idc);
//                System.out.println("Stored reflectionTypes: " + it.returnData().getReflectionType());
////                if(Double.isNaN(it.returnData().end.x) || Double.isNaN(it.returnData().end.y) || Double.isNaN(it.returnData().end.z))
////                {
////                    System.out.println("NaN detected!");
////                    System.out.println("Ray stats: " + it.returnData());
////                    System.out.println("Reflection model: " + it.returnData().getReflectionType());
////                    System.out.println("Current importance: " + it.returnData().getImportance());
////                    System.out.println("Parent ray: " + r.returnData());
////                }
//            }
            
            Vector<Ray> tmpRay = new Vector<>();
//            Node<Ray> traverse = r;
//            int length = 0;
//            while(traverse.checkHasParent())
//            {
//                ++length;
//                traverse = traverse.returnParent();
//                if(length > 4)
//                {
//                    System.out.println("Length greater than 4! Stuck in:" + traverse.returnData().getObjectIndex());
//                    System.out.println("Current importance: " + traverse.returnData().getImportance());
//                }
//                    
//                
//            }
            int offset = 0;
            int initLimit = r.returnChildrenAmount();
            for(int idn = 0; idn < initLimit; ++idn)
            {
//                System.out.println("PRE:\nSize1: " + r.returnChildrenAmount() + "\nSize2: " + tmpRay.size());
//                System.out.println("initLimit: " + initLimit);
//                System.out.println("Current index: " + idn);
                Node<Ray> it = r.returnChild(idn - offset);
                if(it.isEmpty())
                {
                    it.setData(Ray.ERROR_RAY);
                    tmpRay.add(it.returnData());
                }
                else
                    tmpRay.add(rayIntersection(it));
//                System.out.println("MID:\nSize1: " + r.returnChildrenAmount() + "\nSize2: " + tmpRay.size());
                if(tmpRay.get(idn - offset).equals(Ray.ERROR_RAY))
                {
//                    if(idn == initLimit - 1 && r.returnChildrenAmount() == 2)
//                    {
//                        System.out.println("pre r: " + r.returnChild(idn - offset).returnData());
//                    }
                    r.removeChild(r.returnChild(idn - offset).returnData());
                    ++offset;
                    tmpRay.remove(tmpRay.lastElement());
//                    System.out.println("POSTDELETE:\nSize1: " + r.returnChildrenAmount() + "\nSize2: " + tmpRay.size());
//                    if(r.returnChildrenAmount() != tmpRay.size() && idn == initLimit - 1)
//                    {
//                        System.out.println("post r: " + r.returnChild(idn).returnData());
//                    }
                    continue;
                }
                
                ColorDbl col = new ColorDbl(largestRay.color);
                col.setIntensity(r.returnData().getImportance());
                tmpRay.get(idn - offset).color.addColor(col);
                tmpRay.get(idn - offset).setReflectionType(it.returnData().getReflectionType());
//                System.out.println("POSTADD:\nSize1: " + r.returnChildrenAmount() + "\nSize2: " + tmpRay.size());
            }
//            if(r.returnChildrenAmount() != tmpRay.size())
//            {
//                System.out.println("Outputting both r and tmp...");
//                for(int idr = 0; idr < r.returnChildrenAmount(); ++idr)
//                {
//                    System.out.println("r, index " + idr + ": " + r.returnChild(idr).returnData());
//                }
//                for(int idt = 0; idt < tmpRay.size(); ++idt)
//                {
//                    System.out.println("tmp, index " + idt + ": " + tmpRay.get(idt));
//                }
//            }
            if(tmpRay.isEmpty())
            {
                ColorDbl col = new ColorDbl(r.returnData().color);
                col.setIntensity(r.returnData().getImportance());
                Ray endRay = new Ray(r.returnData().start, r.returnData().end, col, r.returnData().returnIndex(), Ray.RAY_IMPORTANCE);
                endRay.setImportance(0);
                endRay.setObjectIndex(r.returnData().getObjectIndex());
                return endRay;
            }
            resultRay = tmpRay.get(0);
            resultRay.setReflectionType(Ray.RAY_REFLECTION);
            ColorDbl tmpCol = new ColorDbl();
            for(int idn = 0; idn < r.returnChildrenAmount(); ++idn)
            {
                if(r.returnChild(idn).returnData().getReflectionType() == Ray.RAY_REFRACTION)
                {
//                    System.out.println("Refracted ray adding to colour!");
                    resultRay.setReflectionType(Ray.RAY_REFRACTION);
                }
                    
//                System.out.println("Index: " + idn + " gets " + tmpRay.get(0) + "from tmp");
//                System.out.println("Index: " + idn + " gets " + r.returnChild(0).returnData() + "from r");
//                System.out.println("Size1: " + r.returnChildrenAmount() + "\nSize2: " + tmpRay.size());
                if(objects[largestRay.getObjectIndex()].isTransparent() && r.returnChild(idn).returnData().getReflectionType() == Ray.RAY_REFLECTION)
                {
                    tmpCol.addColor(tmpRay.get(idn).color);
                    tmpCol.setIntensity(0.2);
                }
                else
                    tmpCol.addColor(tmpRay.get(idn).color);
                tmpCol.r /= r.returnChildrenAmount();
                tmpCol.g /= r.returnChildrenAmount();
                tmpCol.b /= r.returnChildrenAmount();
            }
//            for(int indRefl = 0; indRefl < nRefl; indRefl++)
//            {
//                tmpCol.addColor(tmpRay[indRefl].color);
//                tmpCol.r /= nRefl;
//                tmpCol.g /= nRefl;
//                tmpCol.b /= nRefl;
//            }
            resultRay.color = tmpCol;
            
        }
        else
        {
//            if(r.checkHasParent())
//                System.out.println("r parent index: " + r.returnParent().returnData().returnIndex());
//            System.out.println("Current data for r index:" + r.returnData().returnIndex());
//            System.out.println("Current data for r dir: " + r.returnData().dir);
//            System.out.println("Current data for r start: " + r.returnData().start);
//            System.out.println("Current data for r end: " + r.returnData().end);
//            rayit = new Node<Ray>(largestRay, r);
//            r.addChild(rayit);
            r.setData(largestRay);
//            r.addChild(rayit);
            resultRay = new Ray(largestRay);
            resultRay.color.setIntensity(r.returnData().getImportance());
            resultRay.setReflectionType(r.returnData().getReflectionType());
//            System.out.println("Triangleindex end: " + rayit.returnData().returnIndex());
//            Node <Ray> it = rayit.returnParent();
//            do
//            {
//                System.out.println("Triangleindex: " + it.returnData().returnIndex());
//                it = it.returnParent();
//            }while(it.returnData().returnIndex() != -2);
            
                
        }
//        if(!r.checkHasParent() && r.returnData().getObjectIndex() == 3)
//        {
//            int length = 0;
//            System.out.println(r.returnData());
//            Node<Ray> it = r;
//            while(it.checkIfParent())
//            {
//                ++length;
//                
//            }
//        }
//        if(!r.checkHasParent() && r.returnData().getObjectIndex() != 0)
//        {
//            System.out.println("Final color for non wall ray: " + r.returnData().color);
//        }
//        if(r.returnData().returnIndex() == 1)
//            System.out.println("Triangle 1: " + r.returnData().color);
//        if(!r.checkHasParent() && r.returnData().returnIndex() != 2)
//        {
////            int length = 1;
//            System.out.println("Final color: " + r.returnData().color + " for index: " + r.returnData().returnIndex());
////            Node<Ray> it = r;
////            
////            while(it.checkIfParent())
////            {
////                ++length;
////                it = it.returnChild();
////            }
//        }
//        if(r.returnData().getObjectIndex() == 2)
//        {
//            System.out.println("r is a sphere!");
//            System.out.println(r.returnData());
//            System.out.println(resultRay);
//        }
//        if(resultRay.getReflectionType() == Ray.RAY_REFRACTION)
//            System.out.println("Refraction ray returned!");
        return resultRay;
    }
    public Vertex randomAngleMC2(Direction limit, Triangle t, Direction incoming)
    {
        Vertex predicted = dirToVertex(incoming);
        predicted = normalize(predicted);
        Vertex light = dirToVertex(incoming);
        Vertex normal = dirToVertex(limit);
        Vertex result = dirToVertex(limit);
        Direction rotateAround;
//        System.out.println("Limit "+limit.toString());
//        System.out.println("incom "+incoming.toString());
                
        if(Math.abs(normal.x-predicted.x)<EPSILON && Math.abs(normal.y-predicted.y)<EPSILON
                && Math.abs(normal.z-predicted.z)<EPSILON)
        {
            
        }else{
            //create reflection
            predicted = VektorSubtraktion(predicted, VektorMultiplikation( 
                VektorMultiplikation(normal, SkalärProdukt(
                predicted, normal)/Math.pow(returnLength(normal), 2)), 2));
        }
        
        int iterations = 100;
        double randAng=0;
        double estimate = 0;
        int nInside = 0;
        for(int imc = 0; imc<iterations; imc++)
        {
            randAng = PDF2theta();
            if(Math.abs(randAng)<EPSILON)
            {
                randAng+=Math.PI*2;
            }
            if(randAng>Math.PI/2 || randAng<Math.PI*1.5)
            {
                estimate+= Math.acos(SkalärProdukt(normal,predicted)/(returnLength(normal)*
                        returnLength(predicted)))/randAng;
                nInside++;
            }
        }
        if(nInside>0)
        {
            rotateAround = vertexToDir(KryssProdukt(normal,predicted));
            estimate/=nInside;
//            System.out.println("Estimate: "+estimate);
            result = rotateVertexAroundAxis(result, rotateAround, estimate);
        }else{
            System.out.println("ERROR ALL OUT OF BOUNDS! Theta");
            System.out.println("RandomAngle "+randAng);
        }
//        System.out.println("Est Theta "+estimate);
        estimate = 0;
        nInside = 0;
        for(int imc = 0; imc<iterations; imc++)
        {
            randAng = PDF2phi();
            if(Math.abs(randAng)<EPSILON)
            {
                randAng+=Math.PI*2;
            }
            estimate+= Math.PI/randAng;
            nInside++;
        }
        if(nInside>0)
        {
            rotateAround = limit;
            estimate/=nInside;
//            System.out.println("Estimate: "+estimate);
            result = rotateVertexAroundAxis(result, rotateAround, estimate-Math.PI);
        }else{
            System.out.println("ERROR ALL OUT OF BOUNDS! Phi");
        }
        
//        System.out.println("Est Theta "+estimate);
//        System.out.println("Result:  "+result.toString());
//        System.out.println("predcit: "+predicted.toString());
//        System.out.println("normal:  "+normal.toString());
        return result;
    }
    public Vertex randomAngleMC(Direction limit, Triangle t, Direction incomming)
    {
        HemisCoords resultHemis = new HemisCoords();
        Vertex result = Vertex.DUMMY;
        Vertex lim = dirToVertex(limit);
        Vertex in = dirToVertex(incomming);
        //limit so they can't be reflected INTO the plane
        HemisCoords normal = cartToHemis(lim);
        HemisCoords predicted = cartToHemis(in);
        
        predicted.phi = predicted.phi+(2*(normal.phi-predicted.phi));
        predicted.theta = predicted.theta+(2*(normal.theta-predicted.theta));
        resultHemis = predicted;
        
        HemisCoords transform = new HemisCoords(-1*predicted.theta,-1*predicted.phi,-1*predicted.r);
        normal = HemisAddition(normal, transform);
//        System.out.println("\n"+predicted.phi);
        predicted = HemisAddition(predicted, transform);
//        System.out.println(predicted.phi);
        
        if(Double.isNaN(normal.phi))
        {
            System.out.println("phi is NaN");
            System.out.println("In: " + limit);
        }
        
        int iterations = 100;
        double randAng;
        double estimate = 0;
        int nInside = 0;
        double savedang = 0;
        for(int imc = 0; imc<iterations; imc++)
        {
            randAng = predicted.phi+PDF2phi();
            //no rejection sampling since it's the azimuth, IE around
                if(Math.abs(randAng)<EPSILON)
                {
                    randAng=predicted.phi/randAng;
                }
                estimate+=randAng;
                nInside++;
            
        }
        if(nInside > 0)
        {
            estimate/=nInside;
            resultHemis.phi = estimate;
        }
        else
        {
            System.out.println("It is outside bounds");
        }
        //randAng = (Math.random()*pi) - (pi/2);
        estimate = 0;
        nInside = 0;
        for(int imc = 0; imc<iterations; imc++)
        {
            randAng = PDF2theta();
            savedang=randAng;
            //rejection sampling, reject all values inside surface
                
//            if(randAng < normal.theta+(0.5*Math.PI))
//            {
//                System.out.println("Test"+(normal.theta+(0.5*Math.PI)));
//                System.out.println("norm"+normal.theta);
                if(Math.abs(randAng)<EPSILON)
                {
                    //not to divide by zero
                    randAng = Math.PI*2;
                }
                randAng=predicted.theta/randAng;
                estimate+=randAng;
                nInside++;
//            }
            
        }
        if(nInside > 0)
        {
            estimate/=nInside;
            resultHemis.theta = estimate;
        }else{
            System.out.println("It is outside bounds");
            System.out.println("Random Angle "+(savedang)+"\nnormal.Phi "+
                    (normal.theta)+"\nnormal.Phi "+
                    ((normal.theta+(0.5*Math.PI)))+
                    "\npredicted.Phi "+(predicted.phi));
        }
        resultHemis = HemisSubtraction(resultHemis,transform);
        result = hemisToCart(resultHemis);
        
        if(Double.isNaN(result.x) || Double.isNaN(result.y) || Double.isNaN(result.z))
        {
            System.out.println("RandomAngle is resulting in NAN values for some reason!!!!");
        System.out.println("normal: "+normal.phi+", "+normal.theta+", "+normal.r);
        System.out.println("predicted: "+predicted.phi+", "+predicted.theta+", "+predicted.r);
        }
        return result;
    }
    public Vertex randomAngle(Direction limit, Triangle t)
    {
        Vertex lim = dirToVertex(limit);
        
        //limit so they can't be reflected INTO the plane
        HemisCoords refEndPol = cartToHemis(lim);
        HemisCoords predicted = cartToHemis(lim);
        if(Double.isNaN(refEndPol.phi))
        {
            System.out.println("phi is NaN");
            System.out.println("In: " + limit);
        }
        double pi = Math.PI-(2*Math.asin(EPSILON*10));
        Direction test = new Direction();
        Vertex result = Vertex.DUMMY;
        do
        {
        double randAng;
            randAng = (PDF1())*(pi/2);
            while(randAng > Math.PI)
                randAng = (PDF1()-1)*(pi/2);
        refEndPol.phi +=randAng;
        //randAng = (Math.random()*pi) - (pi/2);
        randAng = (PDF1())*(pi/2);
        refEndPol.theta +=randAng;
        HemisCoords add = cartToHemis(lim);
        refEndPol.phi += add.phi;
        refEndPol.theta += add.theta;
        
        result = hemisToCart(refEndPol);
        test = vertexToDir(result);
        }while(VektorVinkel(test, t.normal) > Math.PI/2 - 2*Math.asin(EPSILON*10));
        
        
//        if(Math.abs(result.x) < EPSILON || Math.abs(result.y) < EPSILON)
//        {
//            System.out.println("In: " + limit);
//            HemisCoords test1 = cartToHemis(lim);
//            Vertex test2 = hemisToCart(test1);
//            System.out.println("TEST1PHI: " + test1.phi);
//            System.out.println("TEST1THETA: " + test1.theta);
//            System.out.println("TEST2" + test2);
//            System.out.println("randAng: " + randAng);
//            System.out.println("Phi: " + refEndPol.phi);
//            System.out.println("Theta: " + refEndPol.theta);
//            System.out.println("Out: " + result);
//        }
//        Direction test = vertexToDir(result);
//        if(!limit.equals(t.normal))
//        {
//            System.out.println("Limit does not equal normal. WTF");
//            System.out.println("limit: " + limit);
//            System.out.println("t.normal: " + t.normal);
//        }
            
//        if(VektorVinkel(test, t.normal) > Math.PI/2)
//        {
//            System.out.println("Felaktig random angle!!!");
//            System.out.println("Vinkel: " + VektorVinkel(test, t.normal));
//            System.out.println("In: " + limit);
//            System.out.println("Out: " + result);
//            HemisCoords test1 = cartToHemis(lim);
////            Vertex test2 = hemisToCart(test1);
//            System.out.println("TEST1PHI: " + test1.phi);
//            System.out.println("TEST1THETA: " + test1.theta);
//            System.out.println("Phi: " + refEndPol.phi);
//            System.out.println("Theta: " + refEndPol.theta);
//        }
        if(Double.isNaN(result.x) || Double.isNaN(result.y) || Double.isNaN(result.z))
            System.out.println("RandomAngle is resulting in NAN values for some reason!!!!");
        return result;
    }
    public double PDF1()
    {
        double randVal = Math.random()*2;
        return 0.75*(1-Math.pow(randVal-1,2));
    }
    public double PDF2phi()
    {
        return 2*Math.PI*Math.random();
    }
    public double PDF2theta()
    {
        return Math.pow(Math.cos(Math.sqrt(Math.random())), -1);
    }
    
    private int getObjectByTriangleIndex(int index)
    {
        int result = 0;
        if(index != -1)
        {
            for(int ido = 0; ido < NROBJECTS; ++ido)
            {
                if(objects[ido].checkTriangleIndexes(index))
                {
                    result = ido;
                }
            }
        }
        else
        {
            result = -1;
        }
        return result;
    }
    
    public void rotateX(double angle)
    {
        for(int i = 0; i < objects.length; i++)
        {
            objects[i].rotateX(angle);
        }
        for(int idl = 0; idl < lights.length; ++idl)
        {
            lights[idl].rotateX(angle);
        }
    }
    public void rotateY(double angle)
    {
        for(int i = 0; i < objects.length; i++)
        {
            objects[i].rotateY(angle);
        }
        for(int idl = 0; idl < lights.length; ++idl)
        {
            lights[idl].rotateY(angle);
        }
    }
    public void rotateZ(double angle)
    {
        for(int i = 0; i < objects.length; i++)
        {
            objects[i].rotateZ(angle);
        }
        for(int idl = 0; idl < lights.length; ++idl)
        {
            lights[idl].rotateZ(angle);
        }
    }
}
