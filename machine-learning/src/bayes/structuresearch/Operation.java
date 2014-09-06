package bayes.structuresearch;

import bayes.BNNode;

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
     * Constructor
     */
    public Operation() {}
    
    /**
     * Constructor
     * 
     * @param type this operation's type
     * @param parent the parent node of the edge
     * @param child the child node of the edge
     */
    public Operation(Operation.Type type, BNNode parent, BNNode child)
    {
        this.type = type;
        this.parent = parent;
        this.child = child;
    }
    
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
    
    @Override
    public String toString()
    {
        String result = "";
        
        switch(this.type)
        {
        case ADD:
            result += "ADD ";
            break;
        case REVERSE:
            result += "REVERSE ";
            break;
        case REMOVE:
            result += "REMOVE ";
            break;
        }
        
        result += this.parent.getName() + " -> " + this.child.getName();
        
        return result;
    }
}
