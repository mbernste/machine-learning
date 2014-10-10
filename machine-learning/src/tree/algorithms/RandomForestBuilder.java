package tree.algorithms;

import java.util.Set;

import data.AttributeSet;
import data.DataSet;
import data.InstanceSet;
import tree.DecisionTree;

public class RandomForestBuilder 
{

	private DataSet sampleDataSet(DataSet fullData)
	{
		AttributeSet attributes = sampleAttributes(fullData.getAttributeSet());
		InstanceSet instances = sampleInstances(fullData.getInstanceSet());
		return new DataSet(attributes, instances);
	}
	
	private AttributeSet sampleAttributes(AttributeSet fullAttributeSet)
	{
		return null;
	}
	
	private InstanceSet sampleInstances(InstanceSet fullInstanceSet)
	{
		return null;
	}
	
}
