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
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;



public class Camera extends JFrame{
    
    //Toggle = false means eye 1.
    
    public final int SIZEX, SIZEY;
    public Vertex eye[];
    private int toggle;
    public Pixel[][] cam;
    public Vertex pos;
    public double width, height;
    private double iMax, iMin;

    public static final double IMPORTANCETHRESHOLD = 0.01;

    public static final int N_REFLECTEDRAYS = 1;
    public static final int N_AREALIGHTSOURCEPOINTS = 10;
    public static final int ESTIMATOR_ITERATIONS = 100;
    public static final boolean AREALIGHTAFFECTOR = true;
    Scene scene;
    
    public int raysPerPixel = 6;
    public static boolean areaLightsource = true;
    public static boolean usePhotonmapping = true;
    public static boolean logScale = false;
    
    private double deltax;
    private double deltay;
    private double time;
    private double prevtime;
    private double startTime;
    
    public static int nrRays;
    
    public Camera(int sizex, int sizey, Vertex[] e1, Vertex pos, double wdth, double hght)
    {
        nrRays = 0;
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
        time = 0;
        prevtime = 0;
    }
    
    public void render()
    {
        prevtime = System.currentTimeMillis();
        startTime = prevtime/1000;
        double timeCalc;
        Ray r;
        Vertex vp = getViewpoint();
        Vertex target;
        double newY;
        double newZ;
        double maxMeanIntensity = 0;
        int percentEffictivicer = 0;
        int strongestIndex = 0;
        ColorDbl strongestColor = ColorDbl.BLACK;
        int currentIndex = 0;
        ColorDbl currentColor = ColorDbl.BLACK;
        for(int px = 0; px<SIZEX; px++)
        {
            //if statement just here to simplify waiting by displaying a number
            if(((double)(px+1)/SIZEX)*100 >= percentEffictivicer)
            {
                System.out.println("Rays are: " + percentEffictivicer +"% done");
                percentEffictivicer++;
                
                time = System.currentTimeMillis()/1000;
                timeCalc = time-prevtime;
                timeCalc *=(double)(((double)100)-((double)percentEffictivicer));
                System.out.println("        Runtime: "+(time-startTime)+", predicted runtime: "+timeCalc);
                timeCalc = time-prevtime;
                System.out.println("        Time passed since last percent: "+(time-prevtime));
                prevtime = time;
                
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
                    r.setObjectIndex(-2);
                    Node<Ray> raystart = new Node<Ray>(r);
                    r = scene.rayIntersection(raystart);
                    if(r.hitLightSource)
                        cam[px][py].hitLightSource = true;
//                    currentIndex = r.returnIndex();
//                    currentColor = r.color;
                    if(r.returnIndex() > 12 && r.returnIndex() < 19 && r.color.meanIntensity() > maxMeanIntensity)
                        maxMeanIntensity = r.color.meanIntensity();
                    if(r.returnIndex() > 12 && r.returnIndex() < 19 && r.color.r > strongestColor.r
                            && r.color.g > strongestColor.g && r.color.b > strongestColor.b)
                    {
                        currentColor = r.color;
                        currentIndex = r.returnIndex();
                    }
                    
                        
                    if(r.equals(Ray.ERROR_RAY))
                    {
                        System.out.println("SOMETHING HAS GONE HORRIBLY WRONG");
                    }
                    if(logScale)
                    {
                        double konstant = 1;
                        ColorDbl toAdd = new ColorDbl(Math.log10((r.color.r)*konstant),
                                Math.log10((r.color.g)*konstant),Math.log10((r.color.b)*konstant));
    ////                    ColorDbl toAdd = new ColorDbl(Math.log(r.color.r*konstant),
    ////                            Math.log(r.color.g*konstant),Math.log(r.color.b*konstant));
                        cam[px][py].addColor(toAdd);
                    }else{
                        cam[px][py].addColor(r.color);
                    }
                    
                }
//                System.out.println(r.color.toString());
                if(iMax < cam[px][py].color.r)
                {
                    strongestIndex = currentIndex;
                    strongestColor = currentColor;
                    iMax = cam[px][py].color.r;
                }
                if(iMax < cam[px][py].color.g)
                {
                    strongestIndex = currentIndex;
                    strongestColor = currentColor;
                    iMax = cam[px][py].color.g;
                }
                if(iMax < cam[px][py].color.b)
                {
                    strongestIndex = currentIndex;
                    strongestColor = currentColor;
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
//                iMin=0;
//                iMax=100;

                if(iMax>100000)
                {
                    System.out.println("MAXCOLOR LARGE AS HELL! "+iMax);
                }
            }
        }
        System.out.println("Ceiling power: " + maxMeanIntensity);
        System.out.println("Strongest index: " + strongestIndex);
        System.out.println("strongest color: " + strongestColor.toString());
        System.out.println("TIME TO COMPLETE PROGRAM: "+(time-startTime));
        createImage();
    }
    
    private void createImage()
    {
        
        System.out.println("Largest color value: " + iMax);
        System.out.println("Total number of rays used: " + nrRays);
        BufferedImage im = new BufferedImage(SIZEX, SIZEY, BufferedImage.TYPE_INT_RGB);
        for(int px = 0; px < SIZEX; ++px)
        {
            for(int py = 0; py < SIZEY; ++py)
            {
                if(cam[px][py].hitLightSource)
                {
                    cam[px][py].color.r = iMax;
                    cam[px][py].color.g = iMax;
                    cam[px][py].color.b = iMax;
                }
                int r, g, b, rgb;
                r = (int)(cam[px][py].color.r*(255.99/iMax));
                g = (int)(cam[px][py].color.g*(255.99/iMax));
                b = (int)(cam[px][py].color.b*(255.99/iMax));
                if(r>255)
                    r=255;
                if(g>255)
                    g=255;
                if(b>255)
                    b=255;
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
        
        saveImage(im);
    }
    private void saveImage(BufferedImage im)
    {
        Calendar calendar = Calendar.getInstance();
        String name = System.getProperty("user.dir")+"\\";
        
        name+="savedImage_date_"+calendar.get(Calendar.YEAR)+(calendar.get(Calendar.MONTH)+1)
                +calendar.get(Calendar.DATE)+"_time_h"+calendar.get(Calendar.HOUR_OF_DAY)
                +"m"+calendar.get(Calendar.MINUTE)+"s"+calendar.get(Calendar.SECOND)
                +".png";
        
        
        try {
            if(ImageIO.write(im, "png", new File(name)))
            {
                System.out.println("Image saved as:\n"+name);
            }
        } catch (IOException ex) {
            Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
        }
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
