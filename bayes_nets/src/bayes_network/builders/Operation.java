package bayes_network.builders;

import bayes_network.BNNode;

/**
 * Represents a single operation that can be performed on the network's 
 * structure.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Operation 
{
    /**
     * Types of operations
     */
    public enum Type {ADD, REMOVE, REVERSE};
    
    /**
     * Parent node of the edge
     */
    private BNNode parent;
    
    /**
     * Child node of the edge
     */
    private BNNode child;
    
    /**
     * This operation's type
     */
    private Type type;
    
    /**
     * @param parent the parent node of the edge
     */
    public void setParent(BNNode parent)
    {
        this.parent = parent;
    }
    
    /**
     * @param child the child node of the edge
     */
    public void setChild(BNNode child)
    {
        this.child = child;
    }
    
    /**
     * @param type this operation's type
     */
    public void setType(Operation.Type type)
    {
        this.type = type;
    }
    
    /**
     * @return the child node of the edge
     */
    public BNNode getChild()
    {
        return child;
    }
    
    /**
     * @return the parent node of the edge
     */
    public BNNode getParent()
    {
        return parent;
    }
    
    /**
     * @return this operation's type
     */
    public Operation.Type getType()
    {
        return this.type;
    }
}
