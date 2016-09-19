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
    
    public Mesh(Triangle[] shape)
    {
        SIZE = shape.length;
        mesh = new Triangle[SIZE];
        for(int i = 0; i < shape.length; i++)
        {
            mesh[i] = new Triangle(shape[i].p, shape[i].color);
        }
    }
    
    public Mesh(Vertex center, int type)
    {
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
    
    public Mesh(double[] lengths, Vertex center, int type, ColorDbl[] colorList)
    {
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
    
    public Mesh(double[] lengths, Vertex center, int type, int colortype)
    {
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
        
    public Ray rayIntersection(Ray r, PointLightSource[] ls)
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
            ColorDbl tmpCol = new ColorDbl(mesh[savedID].color);
            ColorDbl res = new ColorDbl();
            Vertex newEnd = VektorAddition(r.start, VektorMultiplikation(VektorSubtraktion(r.end, r.start), smallT));
            for(int i = 0; i<ls.length; i++)
            {
                res.setIntensity(getLightIntensity(normal, newEnd, ls[i]), tmpCol);
            }
            
            return new Ray(r.start, newEnd, res, savedID);
        }
        return new Ray(r.start, VektorMultiplikation(r.end, 10000), r.color);
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
        ColorDbl def = new ColorDbl(1000000000, 0, 0);
        
        Vertex[] input = new Vertex[3];
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[4]);
        mesh[0] = new Triangle(input, def);
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, def);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, def);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, def);
        //tak
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, def);
        //golv
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, def);
        
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
        ColorDbl def = new ColorDbl(1000000000, 0, 0);
        
        Vertex[] input = new Vertex[3];
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[4]);
        mesh[0] = new Triangle(input, def);
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, def);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, def);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, def);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, def);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, def);
        
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
            mesh[0] = new Triangle(input, colorList[0]);
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[0]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[1]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[1]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[2]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[2]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[3]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[3]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[4]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[4]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[5]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[5]);
        }
        else if(colorList.length == 12)
        {
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[4]);
            mesh[0] = new Triangle(input, colorList[0]);
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[1]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[2]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[3]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[4]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[5]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[6]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[7]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[8]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[9]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[10]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[11]);
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
                color = new ColorDbl(1000000000, 0, 0);
                break;
            case COLOR_GREEN:
                color = new ColorDbl(0, 1000000000, 0);
                break;
            case COLOR_BLUE:
                color = new ColorDbl(0, 0, 1000000000);
                break;
            case COLOR_MAGENTA:
                color = new ColorDbl(1000000000, 0, 1000000000);
                break;
            case COLOR_YELLOW:
                color = new ColorDbl(0, 1000000000, 1000000000);
                break;
            case COLOR_CYAN: 
                color = new ColorDbl(1000000000, 1000000000, 0);
                break;
            case COLOR_PURPLE:
                color = new ColorDbl(900000000, 0, 1000000000);
                break;
            case COLOR_ORANGE:
                color = new ColorDbl(1000000000, 550000000, 0);
                break;
            case COLOR_BLACK:
                color = new ColorDbl(0, 0, 0);
                break;
            default:
                color = new ColorDbl(1000000000, 1000000000, 1000000000);
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
        mesh[0] = new Triangle(input, color);
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, color);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, color);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, color);
        
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
            mesh[0] = new Triangle(input, colorList[0]);
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[0]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[1]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[1]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[2]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[2]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[3]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[3]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[4]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[4]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[5]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[5]);
        }
        else if(colorList.length == 12)
        {
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[4]);
            mesh[0] = new Triangle(input, colorList[0]);
            input[0] = new Vertex(pList[0]);
            input[1] = new Vertex(pList[4]);
            input[2] = new Vertex(pList[2]);
            mesh[1] = new Triangle(input, colorList[1]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[0]);
            input[2] = new Vertex(pList[2]);
            mesh[2] = new Triangle(input, colorList[2]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[6]);
            mesh[3] = new Triangle(input, colorList[3]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[3]);
            input[2] = new Vertex(pList[6]);
            mesh[4] = new Triangle(input, colorList[4]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[3]);
            mesh[5] = new Triangle(input, colorList[5]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[7]);
            mesh[6] = new Triangle(input, colorList[6]);
            input[0] = new Vertex(pList[1]);
            input[1] = new Vertex(pList[7]);
            input[2] = new Vertex(pList[4]);
            mesh[7] = new Triangle(input, colorList[7]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[1]);
            input[2] = new Vertex(pList[0]);
            mesh[8] = new Triangle(input, colorList[8]);
            input[0] = new Vertex(pList[3]);
            input[1] = new Vertex(pList[5]);
            input[2] = new Vertex(pList[1]);
            mesh[9] = new Triangle(input, colorList[9]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[2]);
            input[2] = new Vertex(pList[4]);
            mesh[10] = new Triangle(input, colorList[10]);
            input[0] = new Vertex(pList[7]);
            input[1] = new Vertex(pList[6]);
            input[2] = new Vertex(pList[2]);
            mesh[11] = new Triangle(input, colorList[11]);
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
                color = new ColorDbl(1000000000, 0, 0);
                break;
            case COLOR_GREEN:
                color = new ColorDbl(0, 1000000000, 0);
                break;
            case COLOR_BLUE:
                color = new ColorDbl(0, 0, 1000000000);
                break;
            case COLOR_MAGENTA:
                color = new ColorDbl(1000000000, 0, 1000000000);
                break;
            case COLOR_YELLOW:
                color = new ColorDbl(0, 1000000000, 1000000000);
                break;
            case COLOR_CYAN: 
                color = new ColorDbl(1000000000, 1000000000, 0);
                break;
            case COLOR_PURPLE:
                color = new ColorDbl(900000000, 0, 1000000000);
                break;
            case COLOR_ORANGE:
                color = new ColorDbl(1000000000, 550000000, 0);
                break;
            case COLOR_BLACK:
                color = new ColorDbl(0, 0, 0);
                break;
            default:
                color = new ColorDbl(1000000000, 1000000000, 1000000000);
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
        mesh[0] = new Triangle(input, color);
        input[0] = new Vertex(pList[0]);
        input[1] = new Vertex(pList[4]);
        input[2] = new Vertex(pList[2]);
        mesh[1] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[0]);
        input[2] = new Vertex(pList[2]);
        mesh[2] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[6]);
        mesh[3] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[3]);
        input[2] = new Vertex(pList[6]);
        mesh[4] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[3]);
        mesh[5] = new Triangle(input, color);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[7]);
        mesh[6] = new Triangle(input, color);
        input[0] = new Vertex(pList[1]);
        input[1] = new Vertex(pList[7]);
        input[2] = new Vertex(pList[4]);
        mesh[7] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[1]);
        input[2] = new Vertex(pList[0]);
        mesh[8] = new Triangle(input, color);
        input[0] = new Vertex(pList[3]);
        input[1] = new Vertex(pList[5]);
        input[2] = new Vertex(pList[1]);
        mesh[9] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[2]);
        input[2] = new Vertex(pList[4]);
        mesh[10] = new Triangle(input, color);
        input[0] = new Vertex(pList[7]);
        input[1] = new Vertex(pList[6]);
        input[2] = new Vertex(pList[2]);
        mesh[11] = new Triangle(input, color);
        
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
