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
public class Matrix {
    
    private final int DIMENSIONS;
    public double[] mat;
    
    public Matrix()
    {
        DIMENSIONS = (int)Math.pow(3, 2);
        mat = new double[DIMENSIONS];
        for(int idm = 0; idm < DIMENSIONS; ++idm)
        {
            if(idm == 0 || idm == 4 || idm == 8)
            {
                mat[idm] = 1;
            }
            else
            {
                mat[idm] = 0;
            }
        }
    }
    
    public Matrix(int dim)
    {
        DIMENSIONS = (int)Math.pow(dim, 2);
        mat = new double[DIMENSIONS];
        for(int idm = 0; idm < DIMENSIONS; ++idm)
        {
            if(idm % dim+1 == 0 || idm == 0)
                mat[idm] = 1;
            else
                mat[idm] = 0;
        }
    }
    
    public Matrix(Vertex p)
    {
        DIMENSIONS = (int)Math.pow(3, 2);
        mat = new double[DIMENSIONS];
        int cntr = 0;
        for(int idm = 0; idm < DIMENSIONS; ++idm)
        {
            if(idm % 4 == 0 ||idm == 0)
            {
                mat[idm] = p.returnCoordByIndex(cntr);
                ++cntr;
            }
            else
                mat[idm] = 0;
        }
    }
    
    public void matrixAddition(Matrix m)
    {
        if(DIMENSIONS != m.returnDim())
        {
            System.out.println("Dimensions do not match!");
            return;
        }
        for(int idm = 0; idm < DIMENSIONS; ++idm)
        {
            mat[idm] += m.getElementByIndex(idm);
        }
    }
    
    public void matrixSubtraction(Matrix m)
    {
        if(DIMENSIONS != m.returnDim())
        {
            System.out.println("Dimensions do not match!");
            return;
        }
        for(int idm = 0; idm < DIMENSIONS; ++idm)
        {
            mat[idm] -= m.getElementByIndex(idm);
        }
    }
    
    public void matrixMultiplication3(Matrix m)
    {
        if(DIMENSIONS != m.returnDim())
        {
            System.out.println("Dimensions do not match!");
            return;
        }
        double[] result = new double[DIMENSIONS];
        for(int idm = 0, idm1 = 0, idm2 = 0; idm < DIMENSIONS; ++idm)
        {
            result[idm] = (mat[idm1]*m.getElementByIndex(idm2) + 
                    mat[idm1 + 1]*m.getElementByIndex(idm2 + 3) +
                    mat[idm1 + 2]*m.getElementByIndex(idm2 + 6));
            ++idm2;
            if(idm % Math.sqrt(DIMENSIONS) == 0)
            {
                idm2 = 0;
                idm1 += 3;
            }
        }
        for(int idm = 0; idm < DIMENSIONS; ++idm)
        {
            mat[idm] = result[idm];
        }
    }
    
    public void matrixInverse()
    {
        double[] result = new double[DIMENSIONS];
        
    }
    
    public void elementAddition(int index, double value)
    {
        mat[index] += value;
    }
    
    public void elementSubtraction(int index, double value)
    {
        mat[index] -= value;
    }
    
    public void setElement(int index, double value)
    {
        mat[index] = value;
    }
    
    public double getElementByIndex(int index)
    {
        return mat[index];
    }
    
    public double[] getArray()
    {
        return mat;
    }
    
    public int returnDim()
    {
        return DIMENSIONS;
    }
    
}
