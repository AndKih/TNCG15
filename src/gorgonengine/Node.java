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
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Node<T> //implements Iterable<Node<T>>, Iterator<Node<T>>
{
    
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;
    private boolean hasPapa;
    private int numChildren;
    public Node()
    {
        hasPapa = false;
        numChildren = 0;
    }
    
    public Node(T data)
    {
        this.data = data;
        children = new ArrayList<Node<T>>();
        hasPapa = false;
        numChildren = 0;
    }
    
    public Node(T data, Node<T> papa)
    {
        this.data = data;
        parent = papa;
        children = new ArrayList<Node<T>>();
        hasPapa = true;
        numChildren = 0;
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
        ++numChildren;
    }
    
    public Node<T> returnParent()
    {
        if(parent.equals(null))
            return new Node();
        else
            return parent;
    }
    
//    public Node<T> returnChild(int type)
//    {
//        switch(children.size())
//        {
//            case 1:
//                return children.get(0);
//            case 2:
//                switch(type)
//                {
//                    case Ray.RAY_REFLECTION:
//                        if(children.get(0).equals(null))
//                            return new Node();
//                        else
//                            return children.get(0);
//                    case Ray.RAY_REFRACTION:
//                        if(children.get(1).equals(null))
//                            return new Node();
//                        else
//                            return children.get(1);
//                    default:
//                        return new Node();
//                }
//            default:
//                return new Node();
//        }
//    }
    
    public Node<T> returnChild(int index)
    {
        if(index >= numChildren)
            return new Node();
        if(children.get(index).equals(null))
            return new Node();
        return children.get(index);
    }
    
    public void removeChild(int index)
    {
        if(index >= numChildren)
            return;
        if(children.get(index).equals(null))
            return;
        children.remove(index);
        --numChildren;
    }
    
    public void removeChild(T data)
    {
        for(int idd = 0; idd < numChildren; ++idd)
        {
            if(returnChild(idd).returnData().equals(data))
            {
//                System.out.println("Node, removing child: " + data.toString());
//                System.out.println("Current size: " + numChildren);
                removeChild(idd);
//                System.out.println("Current size: " + numChildren);
                break;
            }
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
    
    public boolean isEmpty()
    {
        if(data == null)
            return true;
        return false;
    }
    
    public boolean checkHasParent()
    {
        return hasPapa;
    }
    
    public int returnChildrenAmount()
    {
        return numChildren;
    }
    
    public int traverse(Node<T> n, int l)
    {
        int length = l;
        ++length;
        System.out.println("Current level: " + length);
        System.out.println(n.returnData().toString());
        List<Node<T>> storage = new ArrayList<Node<T>>();
        for(int i = 0; i < n.returnChildrenAmount(); ++i)
        {
            storage.add(n.returnChild(i));
        }
        for(int j = 0; j < storage.size(); ++j)
        {
            Node<T> it = storage.get(j);
            length = traverse(it, length);
        }
        return length;
    }
    
//    public Iterator<Node<T>> iterator()
//    {
//        return this;
//    }
//    
//    public Node<T> next(int index)
//    {
//        return returnChild
//    }
//    
//    public boolean hasNext()
//    {
//        
//    }
    
}
