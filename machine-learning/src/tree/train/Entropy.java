package tree.train;

import java.util.Map;


import data.DataSet;

/**
 * Used for calculating information theory metrics used for determining the
 * best splits in a decision tree.
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Entropy 
{
    /**
     * Calculate the information gain of the class attribute on a given split.  
     * A split consists of an attribute and a set of instances.  This method
     * calculates
     * <br>
     * <br>
     * H(C) - H(C | X)
     * <br>
     * <br>
     * where H(C) is the entropy of the class attribute and H(C | X) is the 
     * conditional entropy of the class attribute given some other attribute, X
     * 
     * @param data the data set
     * @param split a split on the data
     * @return the information gained on the dataset's class attribute by
     * knowing the split
     */
	public static Double informationGain(DataSet data, Split split)
	{
		Double entropy = entropy(data);	
		Double conditionalEntropy = conditionalEntropy(data, split);
		Double infoGain = entropy - conditionalEntropy;
		
		return infoGain;
	}
	
	/**
	 * Calculate the entropy of the class attribute in a data set.  This method
	 * calculates
	 * <br>
	 * <br>
	 * H(C)
	 * <br>
	 * <br>
	 * where C is the class attribute
	 * 
	 * @param data the data set for which to calculate entropy
	 * @return the entropy of the class attribute
	 */
	public static Double entropy(DataSet data)
	{
		double entropy = 0;
	    double totalInstances = data.getInstanceSet().getInstances().size();
		Map<Integer, Integer> classCounts = data.getClassCounts();

		/*
		 *  Calculate the entropy by summer P*log(P) for each P,
		 *  where P is the probability of seeing a class label 
		 */
		for (Integer count : classCounts.values())
		{
			if (count > 0)
			{
				double P = count / totalInstances;
				entropy += ( P * Math.log(P) / Math.log(2d) );
			}
		}
		
		entropy *= -1;
		
		return entropy;
	}
	
	/**
	 * Calculate the conditional entropy of the class attribute given another
	 * attribute.  That is, this method calculates
	 * <br>
	 * <br>
	 * H(C | X) 
	 * <br>
	 * <br>
	 * where C is the class attribute and X is some other attribute
	 * in the data set. 
	 * 
	 * @param data the data set
	 * @param split the split of the attribute for which we condition the class
	 * attribute
	 * @return
	 */
	public static Double conditionalEntropy(DataSet data, Split split)
	{
		double conditionalEntropy = 0;	
		double totalInstances = data.getInstanceSet().getInstances().size();
		
		if (totalInstances > 0)
		{
			for (SplitBranch branch : split.getSplitBranches())
			{
				DataSet branchData = new DataSet(data.getAttributeSet(), branch.getInstanceSet());
				branchData.setClassAttribute(data.getClassAttribute().getName());
				
				int branchNumInstances = branchData.getInstanceSet().getInstances().size();
				conditionalEntropy += 
						((branchNumInstances / totalInstances) * entropy(branchData));
			}
		}
		
		return conditionalEntropy;	
	}
	
}
