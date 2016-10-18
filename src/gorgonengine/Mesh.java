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
    
    public static final int TYPE_CUBE = 0, TYPE_TETRA = 1, TYPE_RECTANGLE = 2;
    public static final int COLOR_RED = 100, COLOR_BLUE = 101, COLOR_GREEN = 102;
    public static final int COLOR_MAGENTA = 103, COLOR_YELLOW = 104, COLOR_CYAN = 105;
    public static final int COLOR_PURPLE = 106, COLOR_ORANGE = 107, COLOR_BLACK = 108;
    public final int SIZE;
    public Triangle[] mesh;
    private int reflectorType;
    private boolean lightsource;
    private boolean transparent;
    
    public Mesh(Triangle[] shape)
    {
        SIZE = shape.length;
        mesh = new Triangle[SIZE];
        setReflectorType(Object.REFLECTOR_SPECULAR);
        for(int i = 0; i < shape.length; i++)
        {
//            System.out.println("TriangleID: " + shape[i].triangleIndex);
            mesh[i] = new Triangle(shape[i].p, shape[i].color, shape[i].triangleIndex);
        }
    }
    
    public Mesh(Triangle[] shape, boolean ls, boolean trans)
    {
        SIZE = shape.length;
        mesh = new Triangle[SIZE];
        setReflectorType(Object.REFLECTOR_SPECULAR);
        for(int i = 0; i < shape.length; i++)
        {
//            System.out.println("TriangleID: " + shape[i].triangleIndex);
            mesh[i] = new Triangle(shape[i].p, shape[i].color, shape[i].triangleIndex);
        }
        lightsource = ls;
        transparent = trans;
    }
    
    public Mesh(Vertex center, int type)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        switch(type)
        {
            case TYPE_CUBE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildDefaultCube(center);
                break;
            case TYPE_RECTANGLE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildDefaultRectangle(center);
                break;
            default:
                SIZE = 0;
                break;
        }
    }
    
    public Mesh(Vertex center, int type, boolean ls, boolean trans)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        switch(type)
        {
            case TYPE_CUBE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildDefaultCube(center);
                break;
            case TYPE_RECTANGLE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildDefaultRectangle(center);
                break;
            default:
                SIZE = 0;
                break;
        }
        lightsource = ls;
        transparent = trans;
    }
    
    public Mesh(double[] lengths, Vertex center, int type, ColorDbl[] colorList)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        switch(type)
        {
            case TYPE_CUBE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildCube(lengths[0], center, colorList);
                break;
            case TYPE_RECTANGLE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildRectangle(lengths[0], lengths[1], lengths[2], center, colorList);
                break;
            default:
                SIZE = 0;
                break;
                
        }
    }
    
    public Mesh(double[] lengths, Vertex center, int type, ColorDbl[] colorList, boolean ls, boolean trans)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        switch(type)
        {
            case TYPE_CUBE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildCube(lengths[0], center, colorList);
                break;
            case TYPE_RECTANGLE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildRectangle(lengths[0], lengths[1], lengths[2], center, colorList);
                break;
            default:
                SIZE = 0;
                break;
                
        }
        lightsource = ls;
        transparent = trans;
    }
    
    public Mesh(double[] lengths, Vertex center, int type, int colortype)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        switch(type)
        {
            case TYPE_CUBE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildCube(lengths[0], center, colortype);
                break;
            case TYPE_RECTANGLE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildRectangle(lengths[0], lengths[1], lengths[2], center, colortype);
                break;
            default:
                SIZE = 0;
                break;
                
        }
    }
    
    public Mesh(double[] lengths, Vertex center, int type, int colortype, boolean ls, boolean trans)
    {
        setReflectorType(Object.REFLECTOR_SPECULAR);
        switch(type)
        {
            case TYPE_CUBE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildCube(lengths[0], center, colortype);
                break;
            case TYPE_RECTANGLE:
                SIZE = 12;
                mesh = new Triangle[SIZE];
                buildRectangle(lengths[0], lengths[1], lengths[2], center, colortype);
                break;
            default:
                SIZE = 0;
                break;
                
        }
        lightsource = ls;
        transparent = trans;
    }
        
    public Ray rayIntersection(Ray r, PointLightSource[] ls)
    {
        double t = -1, smallT = -10;
        //double EPSILON = 0.00000001;
        Boolean firstHit = true;
        int savedID = -1;
        Direction normal = new Direction();
        for(int idt = 0; idt < SIZE; ++idt)
        {
            t = mesh[idt].rayIntersection(r);
            if(firstHit && t>= EPSILON)
            {
                smallT = t;
                savedID = idt;
                firstHit = false;
            }
            else if(!firstHit && t >= EPSILON && t < smallT)
            {
                smallT = t;
                savedID = idt;
            }
        }
        if(smallT != -10 && smallT > EPSILON)
        {
            normal = mesh[savedID].normal;
            ColorDbl tmpCol = new ColorDbl(mesh[savedID].color);
            ColorDbl res = new ColorDbl();
            Vertex newEnd = VektorAddition(r.start, VektorMultiplikation(VektorSubtraktion(r.end, r.start), smallT));
            for(int i = 0; i<ls.length; i++)
            {
                res.setIntensity(getLightIntensity(normal, newEnd, ls[i], mesh[savedID].triangleIndex), tmpCol);
//                if(mesh[savedID].triangleIndex == 1)
//                    System.out.println("res: " + res);
            }
            Ray resultRay = new Ray(r.start, newEnd, res, mesh[savedID].triangleIndex, Ray.RAY_IMPORTANCE);
            resultRay.setImportance(r.getImportance());
//            resultRay.setImportance(r.getImportance()*mesh[savedID].reflectionCoefficient);
//            if(resultRay.returnIndex() == 1)
//            {
//                System.out.println("tmpCol: " + tmpCol);
//                System.out.println("res: " + res);
//                System.out.println("Mesh ray color: " + resultRay.color);
//            }
                
            return resultRay;
        }
        return Ray.ERROR_RAY;
    }
    
    public boolean shadowRayIntersection(Ray r, PointLightSource ls, int triangleID)
    {
        double t = -1, smallT = -10;
        Boolean firstHit = true;
        for(int idt = 0; idt < SIZE; ++idt)
        {
            if(mesh[idt].triangleIndex == triangleID)
                continue;
            t = mesh[idt].rayIntersection(r);
            if(firstHit && t>0)
            {
                smallT = t;
                firstHit = false;
            }
            else if(!firstHit && t>0 && t < smallT)
            {
                smallT = t;
            }
        }
        if(smallT != -10 && smallT <= 1)
        {
//            if(triangleID == 1)
//                System.out.println("SmallT: " + smallT);
            return true;
        }
//        if(triangleID == 1)
//            System.out.println("SmallT: " + smallT);
        return false;
    }
    
    private void buildDefaultCube(Vertex center)
    {
        double offset = 0.5;
        Vertex[] pList = new Vertex[8];
        pList[0] = new Vertex(center.x + offset, center.y + offset, center.z + offset);
        pList[1] = new Vertex(center.x - offset, center.y + offset, center.z + offset);
        pList[2] = new Vertex(center.x + offset, center.y - offset, center.z + offset);
        pList[3] = new Vertex(center.x + offset, center.y + offset, center.z - offset);
        pList[4] = new Vertex(center.x - offset, center.y - offset, center.z + offset);
        pList[5] = new Vertex(center.x - offset, center.y + offset, center.z - offset);
        pList[6] = new Vertex(center.x + offset, center.y - offset, center.z - offset);
        pList[7] = new Vertex(center.x - offset, center.y - offset, center.z - offset);
        ColorDbl def = new ColorDbl(100, 0, 0);
        
        Vertex[] input = new Vertex[3];
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[4]);
        mesh[0] = new Triangle(input, def, Scene.counter);

        ++Scene.counter;
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        //tak
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        //golv
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        
    }
    
    private void buildDefaultRectangle(Vertex center)
    {
        double offsetX = 1, offsetY = 1.5, offsetZ = 0.75;
        
        Vertex[] pList = new Vertex[8];
        pList[0] = new Vertex(center.x + offsetX, center.y + offsetY, center.z + offsetZ);
        pList[1] = new Vertex(center.x - offsetX, center.y + offsetY, center.z + offsetZ);
        pList[2] = new Vertex(center.x + offsetX, center.y - offsetY, center.z + offsetZ);
        pList[3] = new Vertex(center.x + offsetX, center.y + offsetY, center.z - offsetZ);
        pList[4] = new Vertex(center.x - offsetX, center.y - offsetY, center.z + offsetZ);
        pList[5] = new Vertex(center.x - offsetX, center.y + offsetY, center.z - offsetZ);
        pList[6] = new Vertex(center.x + offsetX, center.y - offsetY, center.z - offsetZ);
        pList[7] = new Vertex(center.x - offsetX, center.y - offsetY, center.z - offsetZ);
        ColorDbl def = new ColorDbl(100, 0, 0);
        
        Vertex[] input = new Vertex[3];
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[4]);
        mesh[0] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, def, Scene.counter);
        
        ++Scene.counter;
        
    }
    
    private void buildCube(double diam, Vertex center, ColorDbl[] colorList)
    {
        double offsetX = diam/2, offsetY = diam/2, offsetZ = diam/2;
        //Exists for eventual use with translateVector, not needed here.
        //double hypotenuse = Math.sqrt(Math.pow(diam/2, 2) + Math.pow(diam/2, 2));
        //Points
        Vertex[] pList = new Vertex[8];
        pList[0] = new Vertex(center.x + offsetX, center.y + offsetY, center.z + offsetZ);
        pList[1] = new Vertex(center.x - offsetX, center.y + offsetY, center.z + offsetZ);
        pList[2] = new Vertex(center.x + offsetX, center.y - offsetY, center.z + offsetZ);
        pList[3] = new Vertex(center.x + offsetX, center.y + offsetY, center.z - offsetZ);
        pList[4] = new Vertex(center.x - offsetX, center.y - offsetY, center.z + offsetZ);
        pList[5] = new Vertex(center.x - offsetX, center.y + offsetY, center.z - offsetZ);
        pList[6] = new Vertex(center.x + offsetX, center.y - offsetY, center.z - offsetZ);
        pList[7] = new Vertex(center.x - offsetX, center.y - offsetY, center.z - offsetZ);
        //Mesh
        Vertex[] input = new Vertex[3];
        if(colorList.length == 6)
        {
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[4]);
            mesh[0] = new Triangle(input, colorList[0], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[0], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[1], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[1], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[2], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[2], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[3], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[3], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[4], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[4], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[5], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[5], Scene.counter);
            
            ++Scene.counter;
        }
        else if(colorList.length == 12)
        {
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[4]);
            mesh[0] = new Triangle(input, colorList[0], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[1], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[2], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[3], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[4], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[5], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[6], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[7], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[8], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[9], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[10], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[11], Scene.counter);
            
            ++Scene.counter;
        }
        else
            return;
        return;
    }
    
    private void buildCube(double diam, Vertex center, int colorType)
    {
        double offset = diam/2;
        ColorDbl color;
        switch(colorType)
        {
            case COLOR_RED:
                color = ColorDbl.RED;
                break;
            case COLOR_GREEN:
                color = ColorDbl.GREEN;
                break;
            case COLOR_BLUE:
                color = ColorDbl.BLUE;
                break;
            case COLOR_MAGENTA:
                color = ColorDbl.MAGENTA;
                break;
            case COLOR_YELLOW:
                color = ColorDbl.YELLOW;
                break;
            case COLOR_CYAN: 
                color = ColorDbl.CYAN;
                break;
            case COLOR_PURPLE:
                color = ColorDbl.PURPLE;
                break;
            case COLOR_ORANGE:
                color = ColorDbl.ORANGE;
                break;
            case COLOR_BLACK:
                color = ColorDbl.BLACK;
                break;
            default:
                color = new ColorDbl(100, 100, 100);
                break;
        }
        
        Vertex[] pList = new Vertex[8];
        pList[0] = new Vertex(center.x + offset, center.y + offset, center.z + offset);
        pList[1] = new Vertex(center.x - offset, center.y + offset, center.z + offset);
        pList[2] = new Vertex(center.x + offset, center.y - offset, center.z + offset);
        pList[3] = new Vertex(center.x + offset, center.y + offset, center.z - offset);
        pList[4] = new Vertex(center.x - offset, center.y - offset, center.z + offset);
        pList[5] = new Vertex(center.x - offset, center.y + offset, center.z - offset);
        pList[6] = new Vertex(center.x + offset, center.y - offset, center.z - offset);
        pList[7] = new Vertex(center.x - offset, center.y - offset, center.z - offset);
        
        Vertex[] input = new Vertex[3];
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[4]);
        mesh[0] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        
        return;
    }
    
    
    private void buildRectangle(double xLength, double yLength, double zLength, Vertex center, ColorDbl[] colorList)
    {
        double offsetX = xLength/2, offsetY = yLength/2, offsetZ = zLength/2;
        
        Vertex[] pList = new Vertex[8];
        pList[0] = new Vertex(center.x + offsetX, center.y + offsetY, center.z + offsetZ);
        pList[1] = new Vertex(center.x - offsetX, center.y + offsetY, center.z + offsetZ);
        pList[2] = new Vertex(center.x + offsetX, center.y - offsetY, center.z + offsetZ);
        pList[3] = new Vertex(center.x + offsetX, center.y + offsetY, center.z - offsetZ);
        pList[4] = new Vertex(center.x - offsetX, center.y - offsetY, center.z + offsetZ);
        pList[5] = new Vertex(center.x - offsetX, center.y + offsetY, center.z - offsetZ);
        pList[6] = new Vertex(center.x + offsetX, center.y - offsetY, center.z - offsetZ);
        pList[7] = new Vertex(center.x - offsetX, center.y - offsetY, center.z - offsetZ);
        
        Vertex[] input = new Vertex[3];
        if(colorList.length == 6)
        {
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[4]);
            mesh[0] = new Triangle(input, colorList[0], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[0], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[1], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[1], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[2], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[2], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[3], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[3], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[4], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[4], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[5], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[5], Scene.counter);
            
            ++Scene.counter;
        }
        else if(colorList.length == 12)
        {
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[4]);
            mesh[0] = new Triangle(input, colorList[0], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[1], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[2], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[3], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[4], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[5], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[6], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[7], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[8], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[9], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[10], Scene.counter);
            
            ++Scene.counter;
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[11], Scene.counter);
            
            ++Scene.counter;
        }
        else
            return;
    }
    
    private void buildRectangle(double xLength, double yLength, double zLength, Vertex center, int colorType)
    {
        double offsetX = xLength/2, offsetY = yLength/2, offsetZ = zLength/2;
        
        ColorDbl color;
        switch(colorType)
        {
            case COLOR_RED:
                color = ColorDbl.RED;
                break;
            case COLOR_GREEN:
                color = ColorDbl.GREEN;
                break;
            case COLOR_BLUE:
                color = ColorDbl.BLUE;
                break;
            case COLOR_MAGENTA:
                color = ColorDbl.MAGENTA;
                break;
            case COLOR_YELLOW:
                color = ColorDbl.YELLOW;
                break;
            case COLOR_CYAN: 
                color = ColorDbl.CYAN;
                break;
            case COLOR_PURPLE:
                color = ColorDbl.PURPLE;
                break;
            case COLOR_ORANGE:
                color = ColorDbl.ORANGE;
                break;
            case COLOR_BLACK:
                color = ColorDbl.BLACK;
                break;
            default:
                color = new ColorDbl(100, 100, 100);
                break;
        }
        
        Vertex[] pList = new Vertex[8];
        pList[0] = new Vertex(center.x + offsetX, center.y + offsetY, center.z + offsetZ);
        pList[1] = new Vertex(center.x - offsetX, center.y + offsetY, center.z + offsetZ);
        pList[2] = new Vertex(center.x + offsetX, center.y - offsetY, center.z + offsetZ);
        pList[3] = new Vertex(center.x + offsetX, center.y + offsetY, center.z - offsetZ);
        pList[4] = new Vertex(center.x - offsetX, center.y - offsetY, center.z + offsetZ);
        pList[5] = new Vertex(center.x - offsetX, center.y + offsetY, center.z - offsetZ);
        pList[6] = new Vertex(center.x + offsetX, center.y - offsetY, center.z - offsetZ);
        pList[7] = new Vertex(center.x - offsetX, center.y - offsetY, center.z - offsetZ);
        
        Vertex[] input = new Vertex[3];
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[4]);
        mesh[0] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, color, Scene.counter);
        
        ++Scene.counter;
        
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
    
    public void setObjectReflection(double p)
    {
        
        for(int idm = 0; idm < SIZE; ++idm)
        {
            mesh[idm].setReflectionCoefficient(p);
        }
        
    }
    
    public double returnSize()
    {
        return (double)SIZE;
    }
    
    public boolean isSphere()
    {
        return false;
    }
    
    public boolean isTransparent()
    {
        return transparent;
    }
    
    public boolean isLightsource()
    {
        return lightsource;
    }
    
    public Direction returnNormal(Vertex vr)
    {
        return Direction.DUMMY;
    }
    
    public Triangle returnTriangleByIndex(int index)
    {
        return mesh[index];
    }
    
    public Triangle returnTriangleById(int id)
    {
        for(int idt = 0; idt < SIZE; ++idt)
        {
            if(mesh[idt].triangleIndex == id)
                return mesh[idt];
        }
        return Triangle.DUMMY;
    }
    
    public int getReflectorType()
    {
        return reflectorType;
    }
    
    public void setReflectorType(int newType)
    {
        reflectorType = newType;
    }
    
    public boolean checkTriangleIndexes(int index)
    {
        boolean result = false;
        for(int idt = 0; idt < SIZE; ++idt)
        {
            if(mesh[idt].triangleIndex == index)
            {
                result = true;
                break;
            }
        }
        return result;
    }
    
}
