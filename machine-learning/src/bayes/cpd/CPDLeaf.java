package bayes.cpd;

import data.Attribute;

/**
 * A leaf node in a CPD tree.
 * 
 * @author Mathew Bernstien - matthewb@cs.wisc.edu
 *
 */
public class CPDLeaf extends CPDNode
{
    protected double probability;

    private Integer laplaceCount;

    /**
     * Constructor 
     * 
     * @param probability the probability at this leaf CPDNode
     */
    public CPDLeaf(Attribute attribute, 
                   Integer nodeValue, 
                   Integer numInstances,
                   Integer laplaceCount)
    {
        super(attribute, nodeValue, numInstances);

        this.laplaceCount = laplaceCount;
    }

    @Override
    public String toString()
    {
        String result = super.toString();
        result += "  [" + probability + "]";
        return result;
    }

    /**
     * @return the probability at this leaf node
     */
    public double getProbability()
    {
        return this.probability;
    }

    /**
     * Calculate the probability of a specific query on this leaf node
     * 
     * @param query the query object used to specify specific values of the
     * attributes for which this CPD's leaf attribute is conditioned on
     */
    @Override
    public Double calculateProbability(CPDQuery query)
    {
        /*
         *  Get the query value for this node's attribute
         */
        Integer queryValue = query.getValueForQueryAttribute(this.attribute);

        /*
         *  Return this leaf's probability if no specific value for this 
         *  CPDNode's attribute was specified in the query or if the value
         *  matches this CPDNode's attribute in the query.  Otherwise, we
         *  return null.
         */
        if (queryValue == null || queryValue == this.nodeValue)
        {
            return this.probability;
        }
        else 
        {
            return 0.0;
        }
    }

    /**
     * Set the parent for this leaf node.  This, in turn, will calculate the
     * probability at this leaf. 
     * 
     * @param parent the parent CPDNode
     */
    @Override
    public void setParent(CPDNode parent)
    {
        /*
         * Set the parent
         */
        this.parent = parent;

        /*
         * Calculate leaf probability using Laplace counts
         */
        double numerator = (double) numInstances + laplaceCount;
        double denominator = parent.numInstances 
                + (laplaceCount * attribute.getNominalValueMap().size());

        this.probability = numerator / denominator;
    }
}
