package tree.algorithms;

import java.util.List;

import tree.train.Split;
import data.Attribute;
import data.DataSet;

/**
 * Builds a decision tree using the ID3 algorithm.  Splits are
 * determined by finding the split that yields maximal information gain on each
 * iteration.  The stopping criteria is met when either a minimum number of 
 * instances are found at the leaf node, all instances at the leaf node are of 
 * the same class, or there are no more splits to split on.
 * 
 */
public class ID3TreeBuilder extends DecisionTreeBuilder
{
	/**
	 * The minimum number of instances at the leaf node for
	 * the stopping criteria to be met.
	 */
	private final int minInstances;
	
	public ID3TreeBuilder(Integer minInstances)
	{
		this.minInstances = minInstances;
	}
	
	@Override
	public boolean checkStoppingCriteria(DataSet data, 
			List<Attribute> availAttributes,
			List<Split> candidateSplits) {
		
		 int numInstances = data.getInstanceSet().getInstances().size();
			return isAllCandidateSplitsNegativeInfoGain(candidateSplits) || 
			       availAttributes.isEmpty() ||
			       numInstances < minInstances ||
			       isAllInstancesOfSameClass(data);
	}
	
	/**
	 * Find the Split with the highest information gain among the candidate 
	 * splits
	 * 
	 * @param data
	 * @param candidateSplits
	 * @return
	 */
	@Override
	public Split determineBestSplit(DataSet data,  List<Split> candidateSplits)
	{
		Split bestSplit = null;
		double maxInfoGain = -Double.MAX_VALUE;
		for (Split split : candidateSplits)
		{
			if (split.getInfoGain().doubleValue() > maxInfoGain)
			{
				maxInfoGain = split.getInfoGain().doubleValue();
				bestSplit = split;
			}
		}		
		return bestSplit;
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

}

