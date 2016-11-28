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
    public static final double PROP_AIR = 1.000277, PROP_ICE = 1.31, PROP_LIQUIDHELIUM = 1.025, 
                               PROP_VACUUM = 1, PROP_LIVER = 1.396, PROP_GERMANIUM = 4.05, PROP_GLASS = 1.458;
    private int reflectorType;
    public double reflectionCoefficient;
    private boolean lightsource;
    private boolean transparent;
    private final double MATERIAL_PROPERTY;
    
    public Object()
    {
        MATERIAL_PROPERTY = PROP_AIR;
    }
    
    public abstract Ray rayIntersection(Ray r, PointLightSource ls[]);
    
    public abstract void setObjectReflection(double p);
    
    public abstract boolean shadowRayIntersection(Ray r, PointLightSource ls, int triangleID);
    
    public abstract Ray lightRayIntersection(Ray r);
    
    public abstract double returnSize();
    public abstract Triangle returnTriangleByIndex(int index);
    public abstract boolean checkTriangleIndexes(int index);
    public abstract Triangle returnTriangleById(int id);
    public abstract boolean isSphere();
    public abstract boolean isTransparent();
    public abstract boolean isLightsource();
    public abstract void setLightsource();
    public abstract Direction returnNormal(Vertex vr);
    public abstract int getReflectorType();
    public abstract void setReflectorType(int newType);
    public abstract double returnProperty();
    public abstract int getSize();
    public abstract int getObjectID();

    public abstract void rotateX(double angle);
    public abstract void rotateY(double angle);
    public abstract void rotateZ(double angle);
}
