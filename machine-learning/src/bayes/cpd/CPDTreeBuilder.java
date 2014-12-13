package bayes.cpd;

import java.util.ArrayList;


import data.Attribute;
import data.DataSet;

/**
 * Used for creating a new {@code CPDTree} object
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class CPDTreeBuilder 
{	
    /**
     * Build a CPD tree 
     * 
     * @param data the data set used to construct the tree
     * @param cpdAttrs the attributes in the CPD tree
     * @param laplaceCount the Laplace count used when calculating each 
     * probability
     * @return the constructed CPD tree
     */
    public CPDTree buildCPDTree(DataSet data, 
                                ArrayList<Attribute> cpdAttrs,
                                Integer laplaceCount)
    {
        /*
         *  Create tree
         */
        CPDTree tree = new CPDTree();

        /*
         *  Set total instances
         */
        CPDTree.totalInstances = data.getInstanceSet().getInstances().size();

        /*
         * Build the tree
         */
        tree.root = makeSubTree(data,
                                null,
                                null,
                                cpdAttrs,
                                laplaceCount);

        return tree;
    }

    /**
     * A recursive method that returns a subtree of the CPD 
     * tree rooted at a specific attribute.  
     * 
     * @param data - a data set of training instances that will be used to
     * 	construct the subtree
     * 
     * @param attribute - the attribute that the root node should test
     * 	 
     * @param relation - how an instances attribute is compared to the root 
     *  node's value.  See {@DtNode} for valid relations 
     * 
     * @param availAttributes - a list of available attributes that are 
     * 	available to split the training instances
     * 
     * @return the root of the subtree
     */
    private CPDNode makeSubTree(DataSet data,
                                Attribute attribute,
                                Integer value,
                                ArrayList<Attribute> availAttributes,
                                Integer laplaceCount)
    {
        CPDNode newNode = null;

        /*
         *  Check for stopping criteria
         */
        boolean stoppingCriteria = checkStoppingCriteria(data,  
                availAttributes
                );


        /*
         *  If the stopping criteria is met, create a leaf node with a 
         *	probability of the condition being met
         */
        int numInstances = data.getInstanceSet().getInstances().size();
        if (stoppingCriteria)
        {
            CPDLeaf leaf = new CPDLeaf(attribute, 
                                       value, 
                                       numInstances,
                                       laplaceCount);
            newNode = leaf;
        }
        else
        {
            newNode = new CPDNode(attribute, value, numInstances);

            /*
             *  Create the split on the current attribute
             */
            Split split = Split.createSplitNominal(availAttributes.get(0),
                                                   data);

            /*
             *  For each branch of the best split, create a new node that 
             *	roots a subtree
             */
            for (SplitBranch branch : split.getSplitBranches())
            {	
                /*
                 * Create a subset of the data with only the instances that
                 * have filtered down to this node
                 */
                DataSet subsetData = new DataSet(data.getAttributeSet(), branch.getInstanceSet());

                /*
                 *  Remove the attribute we just split on
                 */
                availAttributes.remove(split.getAttribute());
                
                /*
                 *  Make the recursive call to make a subtree at each child node
                 */
                CPDNode child = makeSubTree(subsetData,
                                            split.getAttribute(),
                                            branch.getValue().intValue(),
                                            availAttributes,
                                            laplaceCount);

                child.setParent(newNode);
                newNode.addChild(child);
            }
        }

        return newNode;
    }

    /**
     * Check for the tree stopping criteria given a data set of instances.
     * The stopping criteria is achieved if one of the following holds true:
     * 
     * 1. There are no more available attributes to split on
     * 2. The number of instances at the node is zero
     * 
     * @param data	- a data set that holds a subset of the training set
     * @param minInstances - if the number of instances in the data set at the 
     * current node is
     * less than or equal to the this argument, than the stopping criteria has 
     * been reached
     * @return true if the stopping criteria has been met, false otherwise
     */
    private Boolean checkStoppingCriteria(DataSet data, 
            ArrayList<Attribute> availableAttributes
            )
    {
        /*
         * Check that we still have available attributes 
         * to split on	
         */
        if (availableAttributes.size() < 1)
        {
            return true;
        }

        /*
         *  If none of the conditions are met, we have not met the stopping 
         *	criteria
         */
        return false;
    }
}
