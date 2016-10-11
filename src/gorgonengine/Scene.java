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
import java.util.Vector;

public class Scene {
    
    public final static int SIZE = 24;
    public static final double EPSILON  = 0.0000001;
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
        
        objects = new Object[7];
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
        
        objects[1] = new Mesh(mesh2);
        objects[1].setObjectReflection(0.5);
        
        objects[2] = new Sphere(new ColorDbl(40, 20, 60), new Vertex(11, -2, 1), 1, 2);
        objects[2].setObjectReflection(0.5);
        
        objects[3] = new Mesh(new Vertex(7, 2, 2), Mesh.TYPE_RECTANGLE);
        objects[3].setObjectReflection(0.5);
        
        objects[4] = new Mesh(new double[] {2}, new Vertex(5, -3, -2), Mesh.TYPE_CUBE, Mesh.COLOR_ORANGE);
        objects[4].setObjectReflection(0.5);
        
        objects[5] = new Mesh(new double[] {2, 3, 4}, new Vertex(6, 3, -2), Mesh.TYPE_RECTANGLE, Mesh.COLOR_PURPLE);
        objects[5].setObjectReflection(0.5);
        
        objects[6] = new Sphere(ColorDbl.GREEN, new Vertex(10, -3, -4), 1, 6);
        objects[6].setObjectReflection(0.5);
        
        
        lights = new PointLightSource[2];
        lights[0] = new PointLightSource(new Vertex(2,-3,-1),1.0);
        lights[1] = new PointLightSource(new Vertex(3,4,2),0.7);
        
        
        
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
                    largestRay.setImportance(newRay.getImportance());
                }
            }
        }
//        if(largestRay.returnIndex() ==  -3)
//        {
//            System.out.println("LargestRay is now an error ray!");
//        }
        
        if(r.returnData().returnIndex() == largestRay.returnIndex() && r.returnData().returnIndex() != -1 && largestRay.returnIndex() != -1)
            System.out.println("Same index.");
        if(largestRay.getImportance() > Camera.IMPORTANCETHRESHOLD)
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
                Vertex refEnd = VektorSubtraktion(dirToVertex(largestRay.dir), VektorMultiplikation( 
                            VektorMultiplikation(dirToVertex(normal), SkalärProdukt(
                                    dirToVertex(largestRay.dir), dirToVertex(normal))/Math.pow(returnLength(dirToVertex(normal)), 2))
                                    , 2));
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
                            Vertex refEnd = VektorSubtraktion(dirToVertex(largestRay.dir), VektorMultiplikation( 
                                VektorMultiplikation(dirToVertex(normal), SkalärProdukt(
                                        dirToVertex(largestRay.dir), dirToVertex(normal))/Math.pow(returnLength(dirToVertex(normal)), 2))
                                        , 2));
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
            resultRay = rayIntersection(rayit);
            ColorDbl col = new ColorDbl(largestRay.color);
            col.setIntensity(r.returnData().getImportance());
            resultRay.color.addColor(col);
            
//            System.out.println("Triangleindex: " + rayit.returnData().returnIndex());
            
//            if(largestRay.returnIndex() == 1 && resultRay.getImportance() < EPSILON)
//            {
//                System.out.println("resultRay importance: " + resultRay.getImportance());
//                System.out.println("rayit importance: " + rayit.returnData().getImportance());
//                System.out.println("largestRay importance: " + largestRay.getImportance());
//                System.out.println("col value: " + col);
//                System.out.println("largestRay color: " + largestRay.color);
//                System.out.println("Assigned ray color: " + resultRay.color);
//            }
//            if(largestRay.returnIndex() == 1 && resultRay.color.b < 60)
//            {
//                System.out.println("Still going recursive!!!");
////                System.out.println("Largest ray start: " + largestRay.start);
////                System.out.println("Northern wall normal: " + normal);
////                System.out.println("Northern wall largest ray end: " + largestRay.end);
//                System.out.println("Northern wall largest ray dir: " + largestRay.dir);
////                System.out.println("Northern wall largest ray dir normalized: " + normalize(largestRay.dir));
////                System.out.println("refEnd: " + refEnd);
////                System.out.println("Northern wall reflected ray:" + VektorAddition(largestRay.end, refEnd));
//                System.out.println("Reflected ray dir: " + reflectedRay.dir);
//                System.out.println("resultRay importance: " + resultRay.getImportance());
//                System.out.println("rayit importance: " + rayit.returnData().getImportance());
//                System.out.println("largestRay importance: " + largestRay.getImportance());
//                System.out.println("col value: " + col);
//                System.out.println("largestRay color: " + largestRay.color);
//                System.out.println("Assigned ray color: " + resultRay.color);
//            }
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
//            System.out.println("Traversing tree...");
            int length = 1;
            
//            System.out.println("Triangleindex: " + r.returnData().returnIndex());
//            System.out.println("Ray Dir: " + normalize(r.returnData().dir));
//            System.out.println("Ray start: " + r.returnData().start);
//            System.out.println("Ray end point: " + r.returnData().end);
            Node<Ray> it = r;
            while(it.checkIfParent())
            {
                ++length;
                it = it.returnChild();
                if(it.returnData().returnIndex() == -1)
                {
//                    System.out.println("Current is sphere!");
//                    if(it.checkIfParent())
//                    {
//                        if(it.returnChild().returnData().returnIndex() == -1)
//                        {
//                            
//                            System.out.println("Ray Dir: " + it.returnData().dir);
//                            System.out.println("Ray start: " + it.returnData().start);
//                            System.out.println("Ray end point: " + it.returnData().end);
//                            System.out.println("Child ray Dir: " + it.returnChild().returnData().dir);
//                            System.out.println("Child ray start: " + it.returnChild().returnData().start);
//                            System.out.println("Child ray end point: " + it.returnChild().returnData().end);
//                        }
//                    }
//                    if(it.checkHasParent())
//                        System.out.println("Previous index: " + it.returnParent().returnData().returnIndex());
//                    else
//                        System.out.println("No parent.");
//                    if(it.checkIfParent())
//                        System.out.println("Next Index: " + it.returnChild().returnData().returnIndex());
//                    else
//                        System.out.println("No child.");
                }
//                System.out.println("Triangleindex: " + it.returnData().returnIndex());
//                System.out.println("Ray Dir: " + normalize(it.returnData().dir));
//                System.out.println("Ray start: " + it.returnData().start);
//                System.out.println("Ray end point: " + it.returnData().end);
            }
//            System.out.println("Length of tree: " + length);
        }
        return resultRay;
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
