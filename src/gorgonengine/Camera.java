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
    public Vertex eye1, eye2;
    private Boolean toggle;
    public Pixel[][] cam;
    public Vertex pos;
    public double width, height;
    private double iMax, iMin;
    Scene scene;
    
    
    private double deltax;
    private double deltay;
    
    public Camera(int sizex, int sizey, Vertex e1, Vertex e2, Vertex pos, double wdth, double hght)
    {
        SIZEX = sizex;
        SIZEY = sizey;
        eye1 = e1;
        eye2 = e2;
        toggle = false;
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
        for(int px = 0; px<SIZEX; px++)
        {
            for(int py = 0; py<SIZEY; py++)
            {
                target = VektorAddition(VektorSubtraktion(pos, new Vertex(0, -width/2, height/2))
                        , new Vertex( 0, -(double)px*deltax, (double)py*deltay));
//                System.out.println("Target: " + target);
//                System.out.println("Start: " + vp);
                r = new Ray(vp, target, new ColorDbl(0, 0, 0));
                r = scene.rayIntersection(r);
                cam[px][py] = new Pixel(r.color, r.returnIndex());
//                System.out.println(r.color.toString());
                if(iMax < r.color.r)
                {
                    iMax = r.color.r;
                }
                if(iMax < r.color.g)
                {
                    iMax = r.color.g;
                }
                if(iMax < r.color.b)
                {
                    iMax = r.color.b;
                }
                if(iMin > r.color.r)
                {
                    iMin = r.color.r;
                }
                if(iMin > r.color.g)
                {
                    iMin = r.color.g;
                }
                if(iMin > r.color.b)
                {
                    iMin = r.color.b;
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
                im.setRGB(px, py, rgb);
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
        if(toggle)
        {
            return eye2;
        }else{
            return eye1;
        }
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
    
}
