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
public class Photon {
    
    public static final int PHOTON_DIRECT = 100, PHOTON_SHADOW = 101;
    
    public Vertex position;
    public double flux;
    public Direction dir;
    public final int photonType;
    
    public Photon(Vertex p, double f, Direction d, int pType)
    {
        position = p;
        flux = f;
        dir = d;
        photonType = pType;
    }

    public Photon() {
        position = new Vertex(0,0,0);
        flux = 0;
        dir = new Direction();
        photonType = PHOTON_DIRECT;
    }
    
    public String toString()
    {
        String result = "Position: " + position + "\n"
                + "Flux: " + flux + "\n"
                + "Direction: " + dir + "\n"
                + "PhotonType: " + photonType;
        return result;
    }
    
}
