package train;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tree.DtNode;

import data.DataSet;
import data.attribute.Attribute;
import data.instance.Instance;

public class SplitGenerator 
{
	
	/**
	 * Find the Split with the highest information gain among the candidate splits
	 * 
	 * @param data
	 * @param candidateSplits
	 * @return
	 */
	public static Split determineBestSplit(DataSet data, ArrayList<Split> candidateSplits)
	{
		Split bestSplit = null;

		double highestInfoGain = -Double.MAX_VALUE;
		
		for (Split split : candidateSplits)
		{
			if (split.getInfoGain().doubleValue() > highestInfoGain)
			{
				highestInfoGain = split.getInfoGain().doubleValue();
				bestSplit = split;
			}
		}
				
		return bestSplit;
	}
	
	/**
	 * Generate splits along a specific attribute
	 * 
	 * @param attrId the attribute ID of the attribute we wish to split on
	 * @return a list of Splits
	 */
	public static ArrayList<Split> generateSplits(DataSet data, ArrayList<Attribute> availableAttributes)
	{
		ArrayList<Split> splits = new ArrayList<Split>();
		
		for (Attribute currAttr : availableAttributes)
		{	
			if (currAttr.getType() == Attribute.NOMINAL)
			{
				Split nominalSplit = createSplitNominal(currAttr);
				nominalSplit.splitInstances(data.getInstanceSet());
				splits.add(nominalSplit);
			}
			else if (currAttr.getType() == Attribute.CONTINUOUS)
			{
				// Get all possible splits along the continuous attribute and each split
				// to the result
				ArrayList<Split> allContinuousSplits  = createSplitsContinuous(currAttr, data);
				for (Split split : allContinuousSplits)
				{
					split.splitInstances(data.getInstanceSet());
					splits.add(split);
				}
			}
			
		}
		
		
		// Calculate the information gain for each Split
		for (Split split : splits)
		{
			Double infoGain = Entropy.informationGain(data, split);
			split.setInfoGain(infoGain);
		}
		
		return splits;
	}
	
	/**
	 * A helper method for generating a split along a nominal attribute
	 * 
	 * @param attrId
	 * @return
	 */
	private static Split createSplitNominal(Attribute attr)
	{				
		Split split = new Split(attr);
				
		for (Integer nominalValueId : attr.getNominalValueMap().values())
		{
			SplitBranch newBranch = new SplitBranch(attr, new Double(nominalValueId),  DtNode.EQUALS);
			split.addBranch(newBranch);
		}
				
		return split;
	}
	
	/**
	 * A helper method for generating all possible splits along a continuous attribute
	 * 
	 * @param attrId
	 * @return
	 */
	private static ArrayList<Split> createSplitsContinuous(Attribute attr, DataSet data)
	{
		ArrayList<Split> contSplits = new ArrayList<Split>();
		
		Map<Double, Bin> bins = new HashMap<Double, Bin>();
		
		// Bin instances by their attribute value.  We create one bin per unique
		// continuous value of this attribute.
		for (Instance instance : data.getInstanceSet().getInstanceList())
		{
			// The instances value for the given attribute
			double value = instance.getAttributeValue(attr.getId());
			
			// The class attribute ID
			Integer classLabelId = data.getClassAttributeId();

			// The instance's class label
			Integer instanceClassLabel = instance.getAttributeValue(classLabelId).intValue();
			
			if (bins.containsKey(value))
			{
				bins.get(value).includeInstance(instanceClassLabel);
			}
			else
			{
				Double binValue = instance.getAttributeValue(attr.getId());
				Bin newBin = new Bin(binValue);
				newBin.includeInstance(instanceClassLabel);
				bins.put(binValue, newBin);
			}
		}
		
		List<Bin> binList = new ArrayList<Bin>(bins.values());
		Collections.sort(binList, Bin.BIN_ORDER);
		
		for (int i = 0; i < binList.size() - 1; i++)
		{
			boolean generateSplit = false;
			
			// Determine if there exists a pair of instances (i1, i2) where i1 is in
			// bin1, i2 is in bin2, and i1's class does not equal i2's class
			if (binList.get(i).getExistenceMap().size() > 1)
			{
				generateSplit = true;
			}
			else if (!binsHaveSameClasses(binList.get(i), binList.get(i+1)))
			{
				generateSplit = true;
			}
			
			// If we have found such a pair of instances between the two bins, generate a candidate split
			if (generateSplit)
			{
				double splitValue = (binList.get(i).getValue() + binList.get(i+1).getValue()) / 2.0;
								
				Split split = new Split(attr);
				SplitBranch leftBranch = new SplitBranch(attr, new Double(splitValue), DtNode.LESS_THEN_EQUAL_TO);
				SplitBranch rightBranch = new SplitBranch(attr, new Double(splitValue), DtNode.GREATER_THAN);
				split.addBranch(leftBranch);
				split.addBranch(rightBranch);
				
				contSplits.add(split);
			}
		}
		
		
		return contSplits;
	}
	
	/**
	 * Determines if two bins' existence maps are equal
	 * 
	 * @param bin1
	 * @param bin2
	 * @return
	 */
	private static boolean binsHaveSameClasses(Bin bin1, Bin bin2)
	{
		Map<Integer, Boolean> map1 = bin1.getExistenceMap();
		Map<Integer, Boolean> map2 = bin2.getExistenceMap();
		
		if (map1.size() != map2.size())
		{
			return false;
		}
		else
		{
			for (Integer key : map1.keySet())
			{
				if (map1.get(key) != map2.get(key))
				{
					return false;
				}
			}
			
			return true;
		}
	}
}
