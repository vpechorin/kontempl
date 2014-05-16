package net.pechorina.kontempl.data;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author vp
 */
public class GenericTreeNode<T> {
	static final Logger logger = LoggerFactory.getLogger(GenericTreeNode.class);

    private T data;
    private int items;
    private List<GenericTreeNode<T>> children;
    private GenericTreeNode<T> parent;
    
    public GenericTreeNode() {
        super();
        children = new ArrayList<GenericTreeNode<T>>();
        items = 0;
    }

    public GenericTreeNode(T data) {
        this();
        setData(data);
    }

    public List<GenericTreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<GenericTreeNode<T>> children) {
        for (GenericTreeNode<T> child : children) {
            child.parent = this;
        }
        this.children = children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public GenericTreeNode<T> getParent() {
        return parent;
    }

    public void setParent(GenericTreeNode<T> parent) {
        this.parent = parent;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }
    
    public int getNumberOfChildren() {
        return getChildren().size();
    }

    public boolean hasChildren() {
        return (getNumberOfChildren() > 0);
    }
    
    public void addChild(GenericTreeNode<T> child) {
        child.parent = this;
        children.add(child);
    }

    public void addChildAt(int index, GenericTreeNode<T> child) throws IndexOutOfBoundsException {
        child.parent = this;
        children.add(index, child);
    }

    public void removeChildren() {
        this.children = new ArrayList<GenericTreeNode<T>>();
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }
    
    public List<GenericTreeNode<T>> traceParents() {
        //logger.debug("traceParents");
        List <GenericTreeNode<T>> parents = new ArrayList<GenericTreeNode<T>>();
        boolean reachedRoot = false;
        
        int maxIterations = 100;
        int i = 0;
        GenericTreeNode<T> currentNode = this;
        
        while (!reachedRoot) {
            i++;
            //logger.debug("Iteration: " + i);
            if (i > maxIterations) {
                logger.warn("Too deep recursion, breaking");
                break;
            }
            GenericTreeNode<T> p = currentNode.getParent();
            if (p != null) {
                //logger.debug("Adding parent: " + p.getData().toString());
                parents.add(p);
                currentNode = p;
            }
            else {
                //logger.debug("this node is a root node: " + currentNode.getData().toString());
                reachedRoot = true;
            }
        }
        
        return parents;
    }

    public GenericTreeNode<T> getChildAt(int index) throws IndexOutOfBoundsException {
        return children.get(index);
    }   
    
    @Override
    public String toString() {
        return getData().toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
           return true;
        }
        if (obj == null) {
           return false;
        }
        if (getClass() != obj.getClass()) {
           return false;
        }
        GenericTreeNode<?> other = (GenericTreeNode<?>) obj;
        if (data == null) {
           if (other.data != null) {
              return false;
           }
        } else if (!data.equals(other.data)) {
           return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.data != null ? this.data.hashCode() : 0);
        hash = 67 * hash + (this.children != null ? this.children.hashCode() : 0);
        return hash;
    }
}
