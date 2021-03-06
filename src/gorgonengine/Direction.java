package gorgonengine;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andreas
 */
public class Direction {
    
    public static final Direction DUMMY = new Direction();
    double x, y, z;
    
    public Direction()
    {
        x = 0;
        y = 0;
        z = 0;
    }
    
    public Direction(double x, double y, double z)
    {
        this.x = x;
        this.y = y; 
        this.z = z;
    }
    public Direction(Vertex v)
    {
        x = v.x;
        y = v.y; 
        z = v.z;
    }
    
    public double returnX()
    {
        return x;
    }
    
    public double returnY()
    {
        return y;
    }
    
    public double returnZ()
    {
        return z;
    }
    
    public boolean equals(Direction dir)
    {
//        System.out.println("Stats:\n" + toString() + "\n" + dir.toString());
        boolean result = true;
        if(x - dir.x > Mesh.EPSILON)
        {
//            System.out.println("X fail: " + (x - dir.x));
            return false;
        }
        else if(y - dir.y  > Mesh.EPSILON)
        {
//            System.out.println("Y fail: " + (y - dir.y));
            return false; 
        }
        else if(z - dir.z  > Mesh.EPSILON)
        {
//            System.out.println("Z fail: " + (z - dir.z));
            return false;  
        }
           
        else
            return result;
    }
    
    public String toString()
    {
        return "(" + x + ", " + y + ", " + z + ")";
    }
    
}
