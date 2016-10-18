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
public abstract class Object {
    
    public static final int REFLECTOR_DIFFUSE = 500, REFLECTOR_SPECULAR = 501;
    private int reflectorType;
    public boolean transparent = false;
    
    public Object()
    {
        
    }
    
    public abstract Ray rayIntersection(Ray r, PointLightSource ls[]);
    
    public abstract void setObjectReflection(double p);
    
    public abstract boolean shadowRayIntersection(Ray r, PointLightSource ls, int triangleID);
    
    public abstract double returnSize();
    public abstract Triangle returnTriangleByIndex(int index);
    public abstract boolean checkTriangleIndexes(int index);
    public abstract Triangle returnTriangleById(int id);
    public abstract boolean isSphere();
    public abstract boolean isTransparent();
    public abstract boolean isLightsource();
    public abstract Direction returnNormal(Vertex vr);
    public abstract int getReflectorType();
    public abstract void setReflectorType(int newType);

    public abstract void rotateX(double angle);
    public abstract void rotateY(double angle);
    public abstract void rotateZ(double angle);
}
