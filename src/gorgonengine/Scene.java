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
        NROBJECTS = 7;
        objects = new Object[NROBJECTS];
        objects[0] = new Mesh(mesh);
        
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
        
        objects[0].setObjectReflection(0.5);
//        objects[0].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[1] = new Mesh(mesh2);
        objects[1].setObjectReflection(0.5);
//        objects[1].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[2] = new Sphere(new ColorDbl(0, 0, 0), new Vertex(11, -2, 1), 1, 2);
        objects[2].setObjectReflection(1);
//        objects[2].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[3] = new Mesh(new Vertex(7, 2, 2), Mesh.TYPE_RECTANGLE);
        objects[3].setObjectReflection(0.5);
//        objects[3].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[4] = new Mesh(new double[] {2}, new Vertex(5, -3, -2), Mesh.TYPE_CUBE, Mesh.COLOR_ORANGE);
        objects[4].setObjectReflection(0.5);
//        objects[4].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[5] = new Mesh(new double[] {2, 3, 4}, new Vertex(6, 3, -2), Mesh.TYPE_RECTANGLE, Mesh.COLOR_PURPLE);
        objects[5].setObjectReflection(0.5);
//        objects[5].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        objects[6] = new Sphere(ColorDbl.GREEN, new Vertex(10, -3, -4), 1, 6);
        objects[6].setObjectReflection(0.5);
//        objects[6].setReflectorType(Object.REFLECTOR_DIFFUSE);
        
        
        lights = new PointLightSource[2];
        lights[0] = new PointLightSource(new Vertex(2,-3,-1),1.0);
        lights[1] = new PointLightSource(new Vertex(3,4,2),0.7);
        
        for(int i = 0; i < NROBJECTS; i++)
        {
            objects[i].setReflectorType(Object.REFLECTOR_DIFFUSE);
        }
            objects[2].setReflectorType(Object.REFLECTOR_SPECULAR);
        
        objects[2].setReflectorType(Object.REFLECTOR_SPECULAR);
        
    }
    
    public Ray rayIntersection(Node<Ray> r)
    {
        Ray newRay, reflectedRay = new Ray(r.returnData()), resultRay;
        
        Node<Ray> rayit = new Node<Ray>();
        
        Ray largestRay = objects[0].rayIntersection(r.returnData(), lights);
//        if(largestRay.returnIndex() == -3)
//            System.out.println("Wall mesh rayintersection returns an error ray!");
        for(int idt = 1; idt < objects.length; ++idt)
        {
            newRay = objects[idt].rayIntersection(r.returnData(), lights);
//            if(idt==1 && VektorDistansJämförelse(r.end, newRay.end))
//            {
//                System.out.println(newRay.end.toString()+"   |   "+
//                        largestRay.end.toString());
//            }
            
            if(newRay.returnIndex() != -3)
            {
                if(VektorDistansJämförelse(newRay.end, largestRay.end, r.returnData().start))
                {
                    largestRay = new Ray(newRay.start, newRay.end, newRay.color, newRay.returnIndex(), Ray.RAY_IMPORTANCE);
                    if(largestRay.returnIndex() == -1)
                        largestRay.setSphereIndex(newRay.getSphereIndex());
                    largestRay.setImportance(newRay.getImportance());
                }
            }
        }
//        if(largestRay.returnIndex() ==  -3)
//        {
//            System.out.println("LargestRay is now an error ray!");
//        }
        
        if(r.returnData().returnIndex() == largestRay.returnIndex() && r.returnData().returnIndex() != -1 && largestRay.returnIndex() != -1)
        {
            System.out.println("Same index.");
            System.out.println("LargestRay start: " + largestRay.start + "\nLargestRay   end: " + largestRay.end);
            System.out.println("returnedRay start:" + r.returnData().start + "\nreturnedRay   end:" + r.returnData().end);
        }
        if(largestRay.getImportance() > Camera.IMPORTANCETHRESHOLD)
        {
            int nRefl = N_REFLECTEDRAYS;
            if(largestRay.returnIndex() == -1)
            {
//                System.out.println("SphereIndex: " + largestRay.getSphereIndex());
                if(objects[largestRay.getSphereIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
                    nRefl = 1;
            }
            else if(objects[getObjectByTriangleIndex(largestRay.returnIndex())].getReflectorType() == Object.REFLECTOR_SPECULAR)
            {
                nRefl = 1;
            }
            Ray[] tmpRay = new Ray[nRefl]; 
            for(int indRefl = 0; indRefl < nRefl; indRefl++)
            {
    //            System.out.println("Current importance: " + largestRay.getImportance());
                r.setData(largestRay);

                if(largestRay.returnIndex() != -1)
                {
                    Triangle pick = Triangle.DUMMY;
                    for(int ido = 0; ido < objects.length; ++ido)
                    {
                        pick = objects[ido].returnTriangleById(largestRay.returnIndex());
                        if(pick == Triangle.DUMMY)
                            continue;
                        else
                            break;
                    }
                    Direction normal = pick.normal;
                    Vertex refEnd = Vertex.DUMMY;

                    //Random angle reflection
                    if(objects[getObjectByTriangleIndex(largestRay.returnIndex())].getReflectorType() == Object.REFLECTOR_DIFFUSE)
                        refEnd = randomAngle(normal);
                    else if(objects[getObjectByTriangleIndex(largestRay.returnIndex())].getReflectorType() == Object.REFLECTOR_SPECULAR)
                        {
                                    //System.out.println("Specular reflection!");
                        refEnd = VektorSubtraktion(dirToVertex(largestRay.dir), VektorMultiplikation( 
                                VektorMultiplikation(dirToVertex(normal), SkalärProdukt(
                                        dirToVertex(largestRay.dir), dirToVertex(normal))/Math.pow(returnLength(dirToVertex(normal)), 2))
                                        , 2));
                        }
                    
                    refEnd = normalize(refEnd);
                    reflectedRay = new Ray(largestRay.end, VektorAddition(largestRay.end, refEnd), largestRay.color, largestRay.returnIndex(), Ray.RAY_IMPORTANCE);
                    reflectedRay.setImportance(largestRay.getImportance());
                    rayit = new Node<Ray>(reflectedRay, r);
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
                                if(objects[largestRay.getSphereIndex()].getReflectorType() == Object.REFLECTOR_DIFFUSE)
                                    refEnd = randomAngle(normal);
                                else if(objects[largestRay.getSphereIndex()].getReflectorType() == Object.REFLECTOR_SPECULAR)
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
                                reflectedRay.setImportance(largestRay.getImportance());
                                rayit = new Node<Ray>(reflectedRay, r);
                                r.addChild(rayit);
                                break;
                            }
                        }
                    }
                }
                tmpRay[indRefl] = rayIntersection(rayit);
                ColorDbl col = new ColorDbl(largestRay.color);
                col.setIntensity(r.returnData().getImportance());
                tmpRay[indRefl].color.addColor(col);
            }
            resultRay = tmpRay[0];
            ColorDbl tmpCol = new ColorDbl();
            for(int indRefl = 0; indRefl < nRefl; indRefl++)
            {
                tmpCol.addColor(tmpRay[indRefl].color);
                tmpCol.r /= nRefl;
                tmpCol.g /= nRefl;
                tmpCol.b /= nRefl;
            }
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
//            System.out.println("Triangleindex end: " + rayit.returnData().returnIndex());
//            Node <Ray> it = rayit.returnParent();
//            do
//            {
//                System.out.println("Triangleindex: " + it.returnData().returnIndex());
//                it = it.returnParent();
//            }while(it.returnData().returnIndex() != -2);
            
                
        }
        
        if(!r.checkHasParent())
        {
            int length = 1;
            
            Node<Ray> it = r;
            
            while(it.checkIfParent())
            {
                ++length;
                it = it.returnChild();
            }
        }
        return resultRay;
    }
    
    public Vertex randomAngle(Direction limit)
    {
        Vertex lim = dirToVertex(limit);
        
        //limit so they can't be reflected INTO the plane
        HemisCoords refEndPol = cartToHemis(lim);
        double pi = Math.PI-(2*Math.asin(EPSILON*10));
        double randAng = (PDF1()-1)*(pi/2);
        refEndPol.phi +=randAng;
        //randAng = (Math.random()*pi) - (pi/2);
        randAng = (PDF1()-1)*(pi/2);
        refEndPol.theta +=randAng;
        
        return hemisToCart(refEndPol);
    }
    public double PDF1()
    {
        double randVal = Math.random()*2;
        return 0.75*(1-Math.pow(randVal-1,2));
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
    }
    public void rotateY(double angle)
    {
        for(int i = 0; i < objects.length; i++)
        {
            objects[i].rotateY(angle);
        }
    }
    public void rotateZ(double angle)
    {
        for(int i = 0; i < objects.length; i++)
        {
            objects[i].rotateZ(angle);
        }
    }
}
