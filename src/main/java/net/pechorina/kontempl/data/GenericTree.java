package net.pechorina.kontempl.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericTree<T> {
	static final Logger logger = LoggerFactory.getLogger(GenericTree.class);
    private List<GenericTreeNode<T>> children;
    
    public GenericTree() {
        super();
        children = new ArrayList<GenericTreeNode<T>>();
    }
    
    public List<GenericTreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<GenericTreeNode<T>> children) {
        this.children = children;
    }
    
    public List<GenericTreeNode<T>> listAllChildren() {
        List<GenericTreeNode<T>> l = new ArrayList<GenericTreeNode<T>>();
        //List<GenericTreeNode<T>> rootNodes = this.getChildren();
        // logger.debug("Found root nodes: " + rootNodes.size());
        //int  i = 0;
        for (GenericTreeNode<T> child : this.getChildren()) {
            //i++;
            l.add(child);
            List<GenericTreeNode<T>> childList = auxiliaryListAllChildren(child);
            // logger.debug("Found " + childList.size() + " children for root node #" + i);
            l.addAll(childList);
        }
        
        return l;
    }

    public List<GenericTreeNode<T>> auxiliaryListAllChildren(GenericTreeNode<T> node) {
        List<GenericTreeNode<T>> l = new ArrayList<GenericTreeNode<T>>();
        for (GenericTreeNode<T> child : node.getChildren()) {
            l.add(child);
            List<GenericTreeNode<T>> childList = auxiliaryListAllChildren(child);
            l.addAll(childList);
        }
        return l;
    }
    
    
    public int getNumberOfNodes() {
        int numberOfNodes = 0;


        for (GenericTreeNode<T> child : this.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
            numberOfNodes++; // add this child as well
        }

        return numberOfNodes;
    }

    private int auxiliaryGetNumberOfNodes(GenericTreeNode<T> node) {
        int numberOfNodes = node.getNumberOfChildren();

        for (GenericTreeNode<T> child : node.getChildren()) {
            numberOfNodes += auxiliaryGetNumberOfNodes(child);
        }

        return numberOfNodes;
    }

    public boolean isEmpty() {
        return this.getChildren().isEmpty();
    }

    public int getNumberOfChildren() {
        return this.getChildren().size();
    }

    public boolean hasChildren() {
        return (getNumberOfChildren() > 0);
    }

    public void addChild(GenericTreeNode<T> child) {
        this.getChildren().add(child);
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    public void removeChildren() {
        this.children = new ArrayList<GenericTreeNode<T>>();
    }
}
