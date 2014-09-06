package bayes.cpd;

import data.AttributeSet;

/**
 * A tree data structure used for representing and storing the 
 * conditional probability distribution (CPD) for a specific node in a
 * Bayesian network.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 * 
 */
public class CPDTree 
{
    /**
     * Total instances in the training data set used for generating this CPD
     */
    protected static Integer totalInstances;

    /**
     * The set of attributes in the training set used for generating this CPD
     */
    protected AttributeSet attributeSet;

    /**
     * The root node of the CPD tree
     */
    protected CPDNode root;

    /**
     * @return the root node of this CPD tree
     */
    public CPDNode getRoot()
    {
        return root;
    }

    /**
     * Print the subtree rooted at the input Node to standard output.
     * 
     * @param root the node that roots the subtree being printed
     */
    public String toString()
    {
        String result = "";

        for (CPDNode child : root.getChildren())
        {
            ToStringHelper treePrinter = new ToStringHelper(this.attributeSet);
            result += treePrinter.getString(child, 0);
        }

        return result;
    }

    /**
     * A private helper class used for traversing the tree recursively
     * in order to convert the tree to a String
     */
    private static class ToStringHelper
    {
        @SuppressWarnings("unused")
        private AttributeSet attributeSet;

        public ToStringHelper(AttributeSet attributeSet)
        {
            this.attributeSet = attributeSet;
        }

        public String getString(CPDNode node, Integer depth)
        {
            String result = "";

            /*
             *  Print the indentated "|" characters
             */
            for (int i = 0; i < depth; i++)
            {
                result += "|     ";
            }

            /*
             *  Print the value at the current node
             */
            result += node;

            result += "\n";

            /*
             *	Generate the string for the child nodes of this node 
             */
            for (CPDNode child : node.getChildren())
            {
                result += getString(child, depth + 1);
            }	

            return result;
        }
    }

    /**
     * Make a query on this CPD tree
     * 
     * @param query the query object
     * @return the probability of this query given this CPD
     */
    public Double query(CPDQuery query)
    {
        return this.root.calculateProbability(query);
    }
}
