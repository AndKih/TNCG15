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

public class Node<T> {
    
    private T data;
    private Node<T> parent;
    private List<Node<T>> children;
    
    public Node()
    {
        
    }
    
    public Node(T data)
    {
        this.data = data;
    }
    
    public Node(T data, Node<T> papa)
    {
        this.data = data;
        parent = papa;
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
    
}
