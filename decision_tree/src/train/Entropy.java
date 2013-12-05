package train;

import java.util.Map;

import data.DataSet;

public class Entropy 
{
	public static Double informationGain(DataSet data, Split split)
	{
		Double entropy = entropy(data);
		
		Double conditionalEntropy = conditionalEntropy(data, split);
		
		Double infoGain = entropy - conditionalEntropy;
		
		return infoGain;
	}
	
	public static Double entropy(DataSet data)
	{
		double entropy = 0;
		
		Map<Integer, Integer> classCounts = data.getClassCounts();
		
		double totalInstances = data.getNumInstances();

		// Calculate the entropy by summer P*log(P) for each P,
		// where P is the probability of seeing a class label 
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
	
	public static Double conditionalEntropy(DataSet data, Split split)
	{
		double conditionalEntropy = 0;
		
		double totalInstances = data.getNumInstances();
		
		if (totalInstances > 0)
		{
			for (SplitBranch branch : split.getSplitBranches())
			{
				DataSet branchData = new DataSet();
				branchData.setAttributeSet(data.getAttributeSet());
				branchData.setInstanceSet(branch.getInstanceSet());
				branchData.setClassAttribute(data.getClassAttribute().getName());
				
				conditionalEntropy += 
						((branchData.getNumInstances() / totalInstances) * entropy(branchData));
			}
		}
		
		return conditionalEntropy;	
	}
	
}
