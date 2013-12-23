package tree;

import java.util.ArrayList;
import java.util.Map.Entry;

import tree.train.Split;
import tree.train.SplitBranch;
import tree.train.SplitGenerator;

import data.DataSet;
import data.attribute.Attribute;

public class TreeGenerator 
{
	DecisionTree decisionTree = null;
	
	public TreeGenerator() {}
	
	public DecisionTree generateDecisionTree(Integer minInstances, DataSet data)
	{
		decisionTree = new DecisionTree(data.getAttributeSet(), data.getClassAttribute());
		
		ArrayList<Attribute> availableAttributes = removeAttributeById(data.getAttributeList(),
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
	 * A recursive method that returns a node that roots a subtree of the decision tree given
	 * a subset of training instances, and a set of attributes that are available to use in 
	 * order to split the instances.  
	 * 
	 * @param minInstances - the stopping criteria for the recursion is met when the minimum number of 
	 * instances reaches this node 
	 * @param data - a data set of training instances that will be used to construct the subtree
	 * @param attribute - the attribute that the root node should test
	 * @param value - the value of the attribute the root node should test
	 * @param relation - how an instances attribute is compared to the root node's value
	 * 					 See {@DtNode} for valid relations 
	 * @param availableAttributes - a list of available attributes that are available to split the
	 * training instances
	 * @return the root of the subtree
	 */
	private DtNode makeSubTree(Integer minInstances, 
							 DataSet data,
							 Attribute attribute,
							 Double value,
							 Integer relation,
							 ArrayList<Attribute> availableAttributes)
	{		
		DtNode newNode = null;

		// Determine candidate splits
		ArrayList<Split> candidateSplits = SplitGenerator.generateSplits(data, availableAttributes);

		// Check for stopping criteria
		boolean stoppingCriteria = checkStoppingCriteria(data, 
														 minInstances, 
														 availableAttributes,
														 candidateSplits
														 );
				
		
		// If the stopping criteria is met, create a leaf node with a decision class label that is
		// the majority class of the instances at this node
		if (stoppingCriteria)
		{
			Leaf leaf = new Leaf(attribute, 
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
		
			// Find the best split among the candidate splits (best is determined by highest info gain)
			Split bestSplit = SplitGenerator.determineBestSplit(data, candidateSplits);

			// For each branch of the best split, create a new node that roots a subtree
			for (SplitBranch branch : bestSplit.getSplitBranches())
			{	
				DataSet subsetData = new DataSet();
				subsetData.setAttributeSet(data.getAttributeSet());
				subsetData.setInstanceSet(branch.getInstanceSet());
				subsetData.setClassAttribute(data.getClassAttribute().getName());
				
				ArrayList<Attribute> newAvailableAttributes;
				
				// Only remove the attribute if it is nominal
				if (branch.getAttribute().getType() == Attribute.NOMINAL)
				{
					newAvailableAttributes = removeAttributeById(availableAttributes,
																			  bestSplit.getAttribute().getId());
				}
				else
				{
					newAvailableAttributes = availableAttributes;
				}
					
				// Make the recursive call to make a subtree at each child node
				Node child = makeSubTree(minInstances,
										 subsetData,
										 bestSplit.getAttribute(),
										 branch.getValue(),
										 branch.getRelation(),
										 newAvailableAttributes);
				
				newNode.addChild(child);
			}
		}
		
		// Add the current node to the tree
		decisionTree.addNode(newNode);
		
		return newNode;
	}

	/**
	 * A helper method for removing an attribute with a specific attribute ID
	 * from an ArrayList of Attributes
	 *  
	 * @param currAttributes ArrayList of Attributes from which we wish to remove an Attribute
	 * @param attrId the ID of the Attribute we wish to remove
	 * @return
	 */
	private ArrayList<Attribute> removeAttributeById(ArrayList<Attribute> currAttributes, Integer attrId)
	{
		ArrayList<Attribute> newAttributes = new ArrayList<Attribute>();
		
		for (Attribute attr : currAttributes)
		{
			if (attr.getId() != attrId)
			{
				newAttributes.add(attr);
			}
		}
		
		return newAttributes;
	}
	
	/**
	 * Check for the decision-tree stopping criteria given a data set of instances.
	 * The stopping criteria is achieved if one of the following holds true:
	 * 
	 * 1. All candidate splits have negative information gain
	 * 2. There are no more available attributes to split on
	 * 3. The number of instances at the node is less than or equal to our threshold
	 * 4. All instances at this node are of the same class
	 * 
	 * @param data	- a data set that holds a subset of the training set
	 * @param minInstances - if the number of instances in the data set at the current node is
	 * less than or equal to the this argument, than the stopping criteria has been reached
	 * @return
	 */
	private Boolean checkStoppingCriteria(DataSet data, 
										  int minInstances, 
										  ArrayList<Attribute> availableAttributes,
										  ArrayList<Split> candidateSplits)
	{
		
		// **************************************************************************************
		// ****  1. Check whether or not all candidate splits have negative information gain ****
		// **************************************************************************************
		
		int numPosInfoGain = 0;
		for (Split split : candidateSplits)
		{
			if (split.getInfoGain() > 0)
			{
				numPosInfoGain++;
			}
		}
		
		if (numPosInfoGain == 0)
		{
			return true;
		}
					
		// **************************************************************************************
		// ****  2. Check that we still have available attributes to split on			     ****
		// **************************************************************************************
		
		if (availableAttributes.size() < 1)
		{
				return true;
		}
		
		// **************************************************************************************
		// ****  3. Check that number of instances is not less than our threshold			 ****
		// **************************************************************************************
		
		if (data.getNumInstances() < minInstances)
		{
			return true;
		}
		
		// **************************************************************************************
		// ****  4. Check if all instances are of the same class							 ****
		// **************************************************************************************
		for (Integer count : data.getClassCounts().values()) 
		{
			if (count == data.getNumInstances())
			{
				return true;
			}
		}
		
		// If none of the conditions are met, we have not met the stopping criteria
		return false;
	}
	
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

