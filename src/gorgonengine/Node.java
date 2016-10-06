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

import java.util.List;
import java.util.ArrayList;

public class Node<T> {
    
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;
    private boolean hasPapa;
    public Node()
    {
        hasPapa = false;
    }
    
    public Node(T data)
    {
        this.data = data;
        children = new ArrayList<Node<T>>();
        hasPapa = false;
    }
    
    public Node(T data, Node<T> papa)
    {
        this.data = data;
        parent = papa;
        children = new ArrayList<Node<T>>();
        hasPapa = true;
    }
    
    public void setData(T data)
    {
        this.data = data;
    }
    
    public T returnData()
    {
        return data;
    }
    
    public void addChild(Node<T> kid)
    {
        children.add(kid);
    }
    
    public Node<T> returnParent()
    {
        if(parent.equals(null))
            return new Node();
        else
            return parent;
    }
    
    public Node<T> returnChild(int type)
    {
        switch(children.size())
        {
            case 1:
                return children.get(0);
            case 2:
                switch(type)
                {
                    case Ray.RAY_REFLECTION:
                        if(children.get(0).equals(null))
                            return new Node();
                        else
                            return children.get(0);
                    case Ray.RAY_REFRACTION:
                        if(children.get(1).equals(null))
                            return new Node();
                        else
                            return children.get(1);
                    default:
                        return new Node();
                }
            default:
                return new Node();
        }
    }
    public Node<T> returnChild()
    {
        if(children.get(0).equals(null))
            return new Node();
        return children.get(0);
    }
    
    public boolean checkIfParent()
    {
        if(children.size() == 0)
            return false;
        return true;
    }
    
    public boolean checkHasParent()
    {
        return hasPapa;
    }
    
}
