package tree.train;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tree.DtNode;

import data.Attribute;
import data.DataSet;
import data.Instance;

/**
 * This utility class is used for splitting instances by values of specific 
 * attributes.
 *
 */
public class SplitGenerator 
{
	
	
	/**
	 * Generate all possible splits along a set of attribute
	 * 
	 * @param attrId the attribute ID of the attribute we wish to split on
	 * @return a list of Splits
	 */
	public static List<Split> generateSplits(DataSet data, 
	                                              List<Attribute> availAttrs)
	{
		List<Split> splits = new ArrayList<Split>();
		
		for (Attribute currAttr : availAttrs)
		{	
			if (currAttr.getType() == Attribute.Type.NOMINAL)
			{
				Split nominalSplit = createSplitNominal(currAttr);
				nominalSplit.splitInstances(data);
				splits.add(nominalSplit);
			}
			else if (currAttr.getType() == Attribute.Type.CONTINUOUS)
			{
				/*
				 *  Create all possible splits along the continuous attribute 
				 */
				ArrayList<Split> allContinuousSplits  
				                    = createSplitsContinuous(currAttr, data);
				for (Split split : allContinuousSplits)
				{
					split.splitInstances(data);
					splits.add(split);
				}
			}
			
		}
		
		return splits;
	}
	
	/**
	 * A helper method for generating a split along a nominal attribute
	 * 
	 * @param attr the nominal attribute along which we wish to make the split
	 * @return the split along this nominal attribute
	 */
	private static Split createSplitNominal(Attribute attr)
	{				
		Split split = new Split(attr);
				
		for (Integer nominalValueId : attr.getNominalValueMap().values())
		{
			SplitBranch newBranch = new SplitBranch(attr, 
			                                        new Double(nominalValueId),  
			                                        DtNode.Relation.EQUALS);
			split.addBranch(newBranch);
		}
				
		return split;
	}
	
	/**
	 * A helper method for generating all possible splits along a continuous 
	 * attribute
	 * 
	 * @param attr the continuous attribute along which we wish to make the 
	 * split
	 * @return all splits along this continuous attribute
	 */
	private static ArrayList<Split> createSplitsContinuous(Attribute attr, 
	                                                       DataSet data)
	{
		ArrayList<Split> contSplits = new ArrayList<Split>();
		
		Map<Double, Bin> bins = new HashMap<Double, Bin>();
		
		/*
		 * Bin instances by their attribute value.  We create one bin per unique
		 * continuous value of this attribute.
		 */ 
		for (Instance instance : data.getInstanceSet().getInstances())
		{
			double value = instance.getAttributeValue(attr);
			Attribute classAttr = data.getClassAttribute();
			Integer instanceClassLabel = instance.getAttributeValue(classAttr).intValue();
			
			if (bins.containsKey(value))
			{
				bins.get(value).includeInstance(instanceClassLabel);
			}
			else
			{
				Double binValue = instance.getAttributeValue(attr);
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
			
			/*
			 *  Determine if there exists a pair of instances (i1, i2) where i1
			 *  is in bin1, i2 is in bin2, and i1's class does not equal i2's 
			 *  class
			 */
			if (binList.get(i).getExistenceMap().size() > 1)
			{
				generateSplit = true;
			}
			else if (!binsHaveSameClasses(binList.get(i), binList.get(i+1)))
			{
				generateSplit = true;
			}
			
			/*
			 *  If we have found such a pair of instances between the two bins, 
			 *  generate a candidate split
			 */
			if (generateSplit)
			{
				double splitValue = (binList.get(i).getValue() + 
				                     binList.get(i+1).getValue()) / 2.0;
								
				Split split = new Split(attr);
				SplitBranch leftBranch = new SplitBranch(attr, 
				                                         new Double(splitValue), 
				                                         DtNode.Relation.LESS_THAN_EQUAL_TO);
				SplitBranch rightBranch = new SplitBranch(attr, 
				                                          new Double(splitValue), 
				                                          DtNode.Relation.GREATER_THAN);
				split.addBranch(leftBranch);
				split.addBranch(rightBranch);
				
				contSplits.add(split);
			}
		}
		
		
		return contSplits;
	}
	
	/**
	 * Determines if two bins' existence maps are equal.  That is, if two
	 * bins have the same set of class attribute values represented in their
	 * bins, this method returns true.  
	 * 
	 * @param bin1 the first bin
	 * @param bin2 the second bin
	 * @return return true if the two bins have the same set of class attribute 
	 * values represented in their bins.  Otherwise, return false.
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
