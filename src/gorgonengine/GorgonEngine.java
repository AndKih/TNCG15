/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gorgonengine;

import javax.swing.*;
import java.awt.*;
import java.awt.Image.*;
import java.io.*;

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

        Vertex viewPoint[] = new Vertex[2];
        viewPoint[0] = new Vertex(-1, 0, 0);
        viewPoint[1] = new Vertex(-2, 0, 0);
        
        Camera camera = new Camera(1000, 1000, viewPoint, new Vertex(0, 0, 0), 2.0, 2.0);

//        camera.translateCamera(new Vertex(-10,0,0));
//        
//        camera.rotateCameraZ(Math.PI);
//        camera.rotateCameraX(0);
//        camera.rotateCameraY(0);
        
        camera.render();
        
        
    }
    
}
