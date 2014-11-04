package bayes;

import java.util.HashSet;
import java.util.Set;

import bayes.cpd.CPDQuery;
import bayes.cpd.CPDTree;

import data.Attribute;

/**
 * A single node in a {@code BayesianNetwork} object
 * 
 */
public class BNNode
{	
    /**
     * The number of free parameters at this node
     */
    protected Integer freeParameters = 0;
    
    /**
     * This node's parents
     */
    protected Set<BNNode> parents;
    
    /**
     * This node's children
     */
    protected Set<BNNode> children;

    /**
     * The Conditional Probability Distribution at this Node
     */
    protected CPDTree cpd;

    /**
     * Used for determining new IDs
     */
    private static int globalIdCount = 0;
    
    /**
     * Unique integer ID
     */
    protected int nodeId;

    /**
     * Attribute variable represented by this node
     */
    protected Attribute nodeAttribute;

    /**
     * Constructor 
     * 
     * @param nodeAttribute the Attribute variable this Node represents in the
     */
    public BNNode(Attribute nodeAttribute)
    {
        this.nodeAttribute = nodeAttribute;
        this.parents = new HashSet<BNNode>();
        this.children = new HashSet<BNNode>();
        calculateNumFreeParameters();
        
        this.nodeId = globalIdCount++;
    }
    
    /**
     * Add a parent Node to this Node's parents
     * 
     * @param parent the parent Node
     */
    public void addParent(BNNode parent)
    {		
        if (this.equals(parent))
        {
            throw new RuntimeException("Attempting to add parent node " + 
                                        parent.getName() + " to node " + 
                                        this.getName() + ".  These are the " +
                                        "same node.");
        }
        
        this.parents.add(parent);
        calculateNumFreeParameters();
    }
    
    /**
     * Remove a parent node from this node's set of parents
     * 
     * @param parent the parent node
     */
    public void removeParent(BNNode parent)
    {   
        if (this.equals(parent))
        {
            throw new RuntimeException("Attempting to remove parent node " + 
                                        parent.getName() + " from node " + 
                                        this.getName() + ".  These are the " +
                                        "same node.");
        }
        
        this.parents.remove(parent);
        calculateNumFreeParameters();
    }
    
    @Override
    public boolean equals(Object o)
    {
        return ((BNNode) o).nodeId == this.nodeId;
    }
    
    @Override
    public int hashCode()
    {
        return this.nodeId;
    }
    
    /**
     * Add a child Node to this Node's parents
     * 
     * @param parent the parent Node
     */
    public void addChild(BNNode child)
    {
        if (this.equals(child))
        {
            throw new RuntimeException("Attempting to add parent node " + 
                                        child.getName() + " to node " + 
                                        this.getName() + ".  These are the " +
                                        "same node.");
        }
        
        this.children.add(child);
    }

    /**
     * Remove a child node from this node's set of children
     * 
     * @param child the child node
     */
    public void removeChild(BNNode child)
    {
        this.children.remove(child);
    }
    
    /**
     * @return this Node's parent Nodes
     */
    public Set<BNNode> getParents()
    {
        return this.parents;
    }

    /**
     * @return this Node's children Nodes
     */
    public Set<BNNode> getChildren() 
    {
        return this.children;
    }

    /**
     * @return this Node's unique integer ID
     */
    public int getId()
    {
        return this.nodeId;
    }

    /**
     * @return the Attribute that this Node represents
     */
    public Attribute getAttribute()
    {
        return this.nodeAttribute;
    }

    /**
     * @param cpd the CPDTree associated with this Node
     */
    public void setCPDTree(CPDTree cpd)
    {
        this.cpd = cpd;
    }

    /**
     * @return the name of the attribute that this node represents
     */
    public String getName()
    {
        return this.getAttribute().getName();
    }
    
    /**
     * @return the Conditional Probability Distribution (CPD) tree associated
     * with this Node
     */
    public CPDTree getCPD()
    {
        return this.cpd;
    }

    /**
     * Query this node for a probability on its conditional probability tree
     * 
     * @param query the BNQuery object specifying the parameters of the query
     * @return the probability of this query given by this node's conditional
     * probability table
     */
    public Double query(CPDQuery query)
    {
        return this.cpd.query(query);
    }
    
    /**
     * Get the number of nominal values this node's attribute can take on.
     * 
     * @return the number of nominal values this node's attribute can take on
     */
    public Integer getNumNominalValues()
    {
        return this.nodeAttribute.getNominalValueMap().size();
    }
    
    /**
     * @return the number of free parameters at this node
     */ 
    public Integer getNumFreeParamters()
    {
        return this.freeParameters;
    }
    
    /**
     * Calculates the number of free parameters at this node
     */
    private void calculateNumFreeParameters()
    {
        /*
         * Number of values this node's attribute can take on - 1
         */
        int freeParams = this.getNumNominalValues() - 1;
        
        /*
         * Sum of all values that parent's attributes can take on
         */
        for (BNNode parent : this.parents)
        {
            freeParams *= parent.getNumNominalValues();
        }
        
        this.freeParameters = freeParams;
    }
}
