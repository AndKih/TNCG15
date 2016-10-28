/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

import static gorgonengine.LinAlg.cartToHemis;
import static gorgonengine.LinAlg.hemisToCart;
import javax.swing.*;
import java.awt.*;
import java.awt.Image.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Andreas
 */
public class GorgonEngine 
{

    /**
     * @param args the command line arguments
     */
    
    
    
    
    public static void main(String[] args) {
        // TODO code application logic here

        Vertex viewPoint[] = new Vertex[3];
        viewPoint[0] = new Vertex(-1, 0, 0);
        viewPoint[1] = new Vertex(-2, 0, 0);
        viewPoint[2] = new Vertex(-0.2, 0, 0);
        

        Camera camera = new Camera(1000, 800, viewPoint, new Vertex(0, 0, 0), 3.2, 2.0);

        camera.setViewpoint(0);
//        camera.translateCamera(new Vertex(-10,0,0));
//        
//        camera.rotateCameraZ(0.3*Math.PI);
//        camera.rotateCameraX(0);
//        camera.rotateCameraY(0);
        
        camera.render();
        
        
    }
    
}
