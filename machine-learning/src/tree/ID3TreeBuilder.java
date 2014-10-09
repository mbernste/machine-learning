package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import tree.train.Split;
import tree.train.SplitBranch;
import tree.train.SplitGenerator;

import data.Attribute;
import data.DataSet;

/**
 * Builds a decision tree using the ID3 algorithm.  Splits are
 * determined by finding the split that yields maximal information gain on each
 * iteration.  The stopping criteria is met when either a minimum number of 
 * instances are found at the leaf node, all instances at the leaf node are of 
 * the same class, or there are no more splits to split on.
 * 
 *
 */
public class ID3TreeBuilder 
{
    /**
     * The decision tree under construction
     */
	private DecisionTree decisionTree = null;
	
	/**
	 * Builds a decision tree using the ID3 algorithm
	 * 
	 * @param minInstances the minimum number of instances at the leaf node for
	 * the stopping criteria to be met.
	 * @param data the training data set
	 * @return a constructed decision tree
	 */
	public DecisionTree buildDecisionTree(Integer minInstances, DataSet data)
	{
		
	    decisionTree = new DecisionTree(data.getClassAttribute());
		List<Attribute> availableAttributes = removeAttributeById(data.getAttributeSet().getAttributes(),
		                                                          data.getClassAttributeId());
		
		DtNode root = makeSubTree(minInstances, 
								  data, 
								  null,
								  null,
								  null,
								  availableAttributes);
		
		decisionTree.setRoot(root);		
		return decisionTree;	
	}
	
	
	/**
	 * A recursive method that returns a node that roots a subtree of the 
	 * decision tree given a subset of training instances, and a set of 
	 * attributes that are available to use in order to split the instances.  
	 * 
	 * @param minInstances - the stopping criteria for the recursion is met when 
	 * the minimum number of instances reaches this node 
	 * @param data - a data set of training instances that will be used to 
	 * construct the subtree
	 * @param attribute - the attribute that the root node should test
	 * @param value - the value of the attribute the root node should test
	 * @param relation - how an instances attribute is compared to the root 
	 * node's value.
	 * @param availAttrs - a list of available attributes that are 
	 * available to split the training instances
	 * @return the root of the subtree
	 */
	private DtNode makeSubTree(Integer minInstances, 
							 DataSet data,
							 Attribute attribute,
							 Double value,
							 DtNode.Relation relation,
							 List<Attribute> availAttrs)
	{		
		DtNode newNode = null;

		/*
		 *  Determine candidate splits
		 */
		List<Split> candidateSplits 
		                      = SplitGenerator.generateSplits(data, availAttrs);

		/*
		 *  Check for stopping criteria
		 */
		boolean stoppingCriteria = checkStoppingCriteria(data, 
														 minInstances, 
														 availAttrs,
														 candidateSplits
														 );
				
		
		/*
		 *  If the stopping criteria is met, create a leaf node with a decision 
		 *  class label that is the majority class of the instances at this node
		 */ 
		if (stoppingCriteria)
		{
			DtLeaf leaf = new DtLeaf(attribute, 
								 value, 
								 relation, 
								 getMajorityClass(data));
			leaf.setClassCounts(data.getClassCounts());
			newNode = leaf;
		}
		else
		{
			newNode = new DtNode(attribute, value, relation);
			newNode.setClassCounts(data.getClassCounts());
		
			/*
			 *  Find the best split among the candidate splits (best is 
			 *  determined by highest info gain)
			 */
			Split bestSplit 
			         = SplitGenerator.determineBestSplit(data, candidateSplits);

			/*
			 *  For each branch of the best split, create a new node that roots 
			 *  a subtree
			 */
			for (SplitBranch branch : bestSplit.getSplitBranches())
			{	
				DataSet subsetData = new DataSet();
				subsetData.setAttributeSet(data.getAttributeSet());
				subsetData.setInstanceSet(branch.getInstanceSet());
				subsetData.setClassAttribute(data.getClassAttribute().getName());
				
				List<Attribute> newAvailableAttributes;
				
				/*
				 *  Only remove the attribute if it is nominal
				 */
				if (branch.getAttribute().getType() == Attribute.Type.NOMINAL)
				{
					newAvailableAttributes = removeAttributeById(availAttrs,
											                     bestSplit.getAttribute().getId());
				}
				else
				{
					newAvailableAttributes = availAttrs;
				}
					
				/*
				 *  Make the recursive call to make a subtree at each child node
				 */
				Node child = makeSubTree(minInstances,
										 subsetData,
										 bestSplit.getAttribute(),
										 branch.getValue(),
										 branch.getRelation(),
										 newAvailableAttributes);
				
				newNode.addChild(child);
			}
		}
		
		/*
		 *  Add the current node to the tree
		 */
		decisionTree.addNode(newNode);
		
		return newNode;
	}

	/**
	 * A helper method for removing an attribute with a specific attribute ID
	 * from an ArrayList of Attributes
	 *  
	 * @param currAttrs ArrayList of Attributes from which we wish to 
	 * remove an Attribute
	 * @param attrId the ID of the Attribute we wish to remove
	 * @return
	 */
	private List<Attribute> removeAttributeById(List<Attribute> currAttrs, 
	                                                 Integer attrId)
	{
		List<Attribute> newAttributes = new ArrayList<>();
		
		for (Attribute attr : currAttrs)
		{
			if (attr.getId() != attrId)
			{
				newAttributes.add(attr);
			}
		}
		
		return newAttributes;
	}
	
	/**
	 * Check for the decision-tree stopping criteria given a data set of 
	 * instances. The stopping criteria is achieved if one of the following 
	 * holds true:
	 * 
	 * 1. All candidate splits have negative information gain
	 * 2. There are no more available attributes to split on
	 * 3. The number of instances at the node is less than or equal to our 
	 *    threshold
	 * 4. All instances at this node are of the same class
	 * 
	 * @param data	- a data set that holds a subset of the training set
	 * @param minInstances - if the number of instances in the data set at the 
	 * current node is
	 * less than or equal to the this argument, than the stopping criteria has 
	 * been reached
	 * @return
	 */
	private boolean checkStoppingCriteria(DataSet data, 
										  int minInstances, 
										  List<Attribute> availAttributes,
										  List<Split> candidateSplits)
	{
	    int numInstances = data.getInstanceSet().getInstances().size();
		return isAllCandidateSplitsNegativeInfoGain(candidateSplits) || 
		       availAttributes.isEmpty() ||
		       numInstances < minInstances ||
		       isAllInstancesOfSameClass(data);
	}
	
	/**
	 * @param candidateSplits all candidate splits
	 * @return true if all candidate splits have negative information gain. 
	 * False otherwise.
	 */
	private boolean isAllCandidateSplitsNegativeInfoGain(List<Split> candidateSplits) 
	{
        for (Split split : candidateSplits)
        {
            if (split.getInfoGain() > 0)
            {
                return false;
            }
        }
        return true;
	}
	
	/**
	 * @param data the dataset
	 * @return true if if all instances are of the same class.  False otherwise.
	 */
	private boolean isAllInstancesOfSameClass(DataSet data)
	{
        for (Integer count : data.getClassCounts().values()) 
        {
            int numInstances = data.getInstanceSet().getInstances().size();
            if (count == numInstances)
            {
                return true;
            }
        }
        return false;
	}
	
	/**
	 * Get the class value most represented in a data set.
	 * 
	 * @param data the data set 
	 * @return the nominal value ID of the class attribute most represented in
	 * the given data set
	 */
	private Integer getMajorityClass(DataSet data)
	{
		int largestCount = 0;
		Integer currMajorityId = 0;
		
		for (Entry<Integer, Integer> entry : data.getClassCounts().entrySet())
		{
			if (entry.getValue() > largestCount)
			{
				currMajorityId = entry.getKey();
				largestCount = entry.getValue();
			}
		}
		
		return currMajorityId;
	}
}

