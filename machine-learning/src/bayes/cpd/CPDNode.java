package bayes.cpd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import data.Attribute;

/**
 * A single node in a CPD tree.  Each node splits the instances in the training
 * set on a certain value of a certain attribute.
 * 
 */
public class CPDNode 
{
    /**
     * The Attribute that this node tests
     */
    protected Attribute attribute;

    /**
     * The values that an instance must meet to make satisfy this node's test
     */
    protected Integer nodeValue; 

    /**
     * The number of instances that have reached this node
     */
    protected Integer numInstances;

    /**
     * Each nominal value ID maps to a child node
     */
    protected Map<Integer, CPDNode> children;

    /**
     * This CPDNode's parent
     */
    protected CPDNode parent;

    public CPDNode(Attribute attribute, Integer nodeValue, Integer numInstances)
    {
        children = new HashMap<Integer, CPDNode>();
        this.attribute = attribute;
        this.nodeValue = nodeValue;
        this.numInstances = numInstances;
    }

    /**
     * @return this node's value.  For nominal attributes, this value is the 
     * nominal value ID.
     */
    public Integer getNodeValue()
    {
        return nodeValue;
    }

    /**
     * Add a CPDTransition to this CPDNode's list of transitions
     * 
     * @param transition the new CPDTransition object
     */
    public void addChild(CPDNode child)
    {
        children.put(child.getNodeValue(), child);
    }

    /**
     * @return this CPDNode's child nodes
     */
    public ArrayList<CPDNode> getChildren()
    {
        Collection<CPDNode> col = children.values();
        return new ArrayList<CPDNode>(col);
    }

    /**
     * @return the number of instances that have reached this node
     */
    public Integer getNumInstances()
    {
        return this.numInstances;
    }

    /**
     * Get the total number of instances in the data set that "filtered"
     * through to this node AND have the nominal value of the given
     * nominal value ID
     * 
     * @param nominalValueId the target nominal value ID
     * @return the total number of instances
     */
    public int getNumOfNominalValue(int nominalValueId)
    {
        return children.get(nominalValueId).getNumInstances();
    }

    @Override
    public String toString()
    {
        String result = "";

        result += this.attribute.getName();
        result += " : ";
        result += this.attribute.getNominalValueName(this.nodeValue);
        result += " ";
        result += "(" + this.numInstances + ")";

        return result;
    }

    /**
     * If this node's attribute IS NOT included in the query, we sum the 
     * probabilities of this query over all of this node's children.
     * 
     * If this node's attribute IS included in the query AND the value
     * of this attribute as specified in the query is equal to this node's
     * value for that attribute, then we sum probabilities of this query 
     * over all of this node's children.
     * 
     * We return zero, ONLY IF this attribute is specified in this query
     * AND the value of this attribute as specified in the query IS NOT
     * equal to this node's value for that attribute
     * 
     * @param the BNQuery object specifying the query
     * @return the probability of this query as calculated by the CPD tree
     * rooted at this node
     */
    public Double calculateProbability(CPDQuery query)
    {
        Double result = 0.0;

        /*
         *  Get the query value for this node's attribute
         */
        Integer queryValue = query.getValueForQueryAttribute(this.attribute);

        /*
         *  Determine whether to sum over children or to return zero
         */
        if (queryValue == null || queryValue == this.nodeValue)
        {
            for (CPDNode child : this.children.values())
            {
                result += child.calculateProbability(query);
            }
        }
        else 
        {
            result = 0.0;
        }

        return result;
    }

    /**
     * Set the parent node of this node.
     * 
     * @param parent this node's parent
     */
    protected void setParent(CPDNode parent)
    {
        this.parent = parent;
    }
}
