/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

import static gorgonengine.LinAlg.*;

/**
 *
 * @author Andreas
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;



public class Camera extends JFrame{
    
    //Toggle = false means eye 1.
    
    public final int SIZEX, SIZEY;
    public Vertex eye[];
    private int toggle;
    public Pixel[][] cam;
    public Vertex pos;
    public double width, height;
    private double iMax, iMin;
    public static final double IMPORTANCETHRESHOLD = 0.1;
    public static final int N_REFLECTEDRAYS = 4;
    Scene scene;
    
    
    public int raysPerPixel = 4;
    
    private double deltax;
    private double deltay;
    
    public Camera(int sizex, int sizey, Vertex[] e1, Vertex pos, double wdth, double hght)
    {
        SIZEX = sizex;
        SIZEY = sizey;
        eye = new Vertex[e1.length];
        for(int i = 0; i < e1.length; i++)
        {
            eye[i] = e1[i];
        }
        toggle = 0;
        cam = new Pixel[SIZEX][SIZEY];
        this.pos = pos;
        width = wdth;
        height = hght;
        scene = new Scene();
        
        iMax = 0; iMin = 1000000000;
        calcDelta();
    }
    
    public void render()
    {
        Ray r;
        Vertex vp = getViewpoint();
        Vertex target;
        double newY;
        double newZ;
                
        int percentEffictivicer = 0;
        int percentEffictivicer2 = 0;
        for(int px = 0; px<SIZEX; px++)
        {
            //if statement just here to simplify waiting by displaying a number
            if(((double)(px+1)/SIZEX)*100 >= percentEffictivicer)
            {
                System.out.println("Rays are: " + percentEffictivicer +"% done");
                percentEffictivicer++;
            }
            
            for(int py = 0; py<SIZEY; py++)
            {
                    cam[px][py] = new Pixel(new ColorDbl(), px + py*SIZEX);
                
                for(int rayInd = 0; rayInd < raysPerPixel; rayInd++)
                {
                    target = VektorAddition(VektorSubtraktion(pos, new Vertex(0, -width/2, height/2))
                            , new Vertex( 0, -(double)px*deltax, (double)py*deltay));
                    newY = Math.random()*deltax;
                    newY -= deltax/2;
                    target.y += newY;
                    newZ = Math.random()*deltay;
                    newZ-= deltay/2;
                    target.z += newZ;
                    
    //                System.out.println("Target: " + target);
    //                System.out.println("Start: " + vp);
                    r = new Ray(vp, target, new ColorDbl(0, 0, 0), -2, Ray.RAY_IMPORTANCE);
                    r.setReflectionType(Ray.RAY_REFLECTION);
                    r.setObjectIndex(-1);
                    Node<Ray> raystart = new Node<Ray>(r);
                    r = scene.rayIntersection(raystart);
                    cam[px][py].addColor(r.color);
                }
//                System.out.println(r.color.toString());
                if(iMax < cam[px][py].color.r)
                {
                    iMax = cam[px][py].color.r;
                }
                if(iMax < cam[px][py].color.g)
                {
                    iMax = cam[px][py].color.g;
                }
                if(iMax < cam[px][py].color.b)
                {
                    iMax = cam[px][py].color.b;
                }
                if(iMin > cam[px][py].color.r)
                {
                    iMin = cam[px][py].color.r;
                }
                if(iMin > cam[px][py].color.g)
                {
                    iMin = cam[px][py].color.g;
                }
                if(iMin > cam[px][py].color.b)
                {
                    iMin = cam[px][py].color.b;
                }
            }
        }
        createImage();
    }
    
    private void createImage()
    {
        BufferedImage im = new BufferedImage(SIZEX, SIZEY, BufferedImage.TYPE_INT_RGB);
        for(int px = 0; px < SIZEX; ++px)
        {
            for(int py = 0; py < SIZEY; ++py)
            {
                int r, g, b, rgb;
                r = (int)(cam[px][py].color.r*(255.99/iMax));
                g = (int)(cam[px][py].color.g*(255.99/iMax));
                b = (int)(cam[px][py].color.b*(255.99/iMax));
                rgb = ( r << 16) | ( g << 8 ) | b;
                im.setRGB(px, SIZEY-1-py, rgb);
            }
        }
        getContentPane().setLayout(new FlowLayout());
        ImageIcon imIcon = new ImageIcon(im);
        JLabel imLabel = new JLabel(imIcon);
        getContentPane().add(imLabel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        
    }
    
    public Vertex getViewpoint()
    {
        return eye[toggle];
    }
    public void setViewpoint(int tog)
    {
        toggle = tog;
    }
    private void calcDelta()
    {
        deltax =  width/SIZEX;
        deltay = height/SIZEY;
    }
    public double getDeltaX()
    {
        return deltax;
    }
    public double getDeltaY()
    {
        return deltay;
    }
    
    public void translateCamera(Vertex pos)
    {
        
        for(int i = 0; i < eye.length; i++)
        {
            eye[i] = VektorAddition(eye[i],pos);
        }
        
        this.pos = VektorAddition(this.pos,pos);
    }
    public void rotateCameraX(double angle)
    {
        scene.rotateX(angle);
//        pos = rotateVertex(pos, angle);
//        for(int i = 0; i < eye.length; i++)
//        {
//            eye[i] = rotateVertex(eye[i], angle);
//        }
    }
    public void rotateCameraY(double angle)
    {
        scene.rotateY(angle);
    }
    public void rotateCameraZ(double angle)
    {
        scene.rotateZ(angle);
    }
    
}
