package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.attribute.Attribute;
import data.attribute.AttributeSet;
import data.instance.Instance;
import data.instance.InstanceSet;

public class DataSet 
{
	protected AttributeSet attributeSet;
	protected InstanceSet instanceSet;
	protected Map<Integer, Integer> classCounts; // Maps a class label ID to a count
	protected Attribute classAttribute = null;
	
	/**
	 * Assign the Attribute that labels the class of each instance in the data
	 * set.
	 * 
	 * @param attrName the name of the attribute used to lable the data
	 */
	public void setClassAttribute(String attrName)
	{
		if (attributeSet.contains(attrName))
		{
			classAttribute = attributeSet.getAttributeByName(attrName);
		}
		else
		{
			System.err.println("Error assigning class attribute in data set.  " +
					"The attribute, " + attrName + " is not an attribute name in" +
					"this dataset" );
		}
		
		this.calculateClassCounts();
	}
	
	/**
	 * Get the attribute that represents the class label for instances
	 * in the data set.
	 * 
	 * @return the attribute that labels the class of each instance
	 */
	public Attribute getClassAttribute()
	{
		return classAttribute;
	}
	
	public Map<Integer, Integer> getClassCounts()
	{
		if (classCounts == null)
		{
			throw new RuntimeException("Error. Trying to retrieve class counts from DataSet," +
					"but class counts Map has not been initialized.");
		}
		else
		{
			return classCounts;
		}
	}
	
	/**
	 * Get the Attribute ID of the attribute used to label the instances in
	 * the data set
	 * 
	 * @return
	 */
	public Integer getClassAttributeId()
	{
		return classAttribute.getId();
	}
	
	public void setAttributeSet(AttributeSet attrSet)
	{
		this.attributeSet = attrSet;
	}
	
	public void setInstanceSet(InstanceSet instSet)
	{
		this.instanceSet = instSet;
	}
	
	public InstanceSet getInstanceSet()
	{
		return instanceSet;
	}
	
	public AttributeSet getAttributeSet()
	{
		return attributeSet;
	}
	
	public ArrayList<Attribute> getAttributeList()
	{
		return attributeSet.getAttributes();
	}

	/**
	 * Returns the number of instances in the data set
	 * 
	 * @return
	 */
	public int getNumInstances()
	{
		return instanceSet.getNumInstances();
	}
	
	public int getNumAttributes()
	{
		return attributeSet.getNumAttributes();
	}
	
	/**
	 * Print the attributes in a descriptive easy to read format such that each
	 * attribute is printed with its integer ID and each nominal value for nominal
	 * attributes is printed with its nominal value ID
	 */
	public void printAttributes()
	{
		attributeSet.printAttributeSet();
	}
	
	/**
	 * Print the instances in a descriptave easy to read format.  This method checks
	 * that the data being read mirrors the ARFF file.  Use this method to check the data
	 * being read in.
	 */
	public void printInstances()
	{
		ArrayList<Instance> instances = instanceSet.getInstanceList();
		ArrayList<Attribute> attributes = attributeSet.getAttributes();
		
		// For each instance, iterate through each attribute and print the 
		// instances value for each attribute
		for (Instance instance : instances)
		{
			printInstance(instance);	
			System.out.print("\n\n");
		}
	}
	
	
	/**
	 * Print class distribution to the console
	 */
	public void printClassCounts()
	{
		System.out.println("Class Attribute -> " + classAttribute.getName());
		
		for (Integer classLabelValue : classCounts.keySet())
		{
			System.out.print( classAttribute.getNominalValueName(classLabelValue) );
			System.out.print( " : ");
			System.out.print( classCounts.get(classLabelValue) );
			System.out.print("\n");
		}
		
		System.out.print("\n");
	}
	
	/**
	 * This method calculates how many instances are of each class label.  
	 * It stores the results in a map that maps a nominal value ID of each
	 * nominal value of the class attribute to a count 
	 */
	public void calculateClassCounts()
	{
		classCounts = new HashMap<Integer, Integer>();

		// Get the ID of the class attribute
		int classAttrId = classAttribute.getId();
		
		// Create an entry in the classCounts map for each possible value 
		// (i.e. label of the class attribute) 
		for (Integer classLabelValue : classAttribute.getNominalValueMap().values())
		{
			classCounts.put(new Integer(classLabelValue), new Integer(0));
		}
		
		// For each instance in the instance set, increment the count of the class label
		// for each instance of that class
		for (Instance instance : instanceSet.getInstanceList())
		{
			classCounts.put(
					new Integer( instance.getAttributeValue(classAttrId).intValue() ),
					new Integer(classCounts.get(instance.getAttributeValue(classAttrId).intValue()) + 1)
					);
		}
	}
	
	/**
	 * Print an instance to standard output.
	 * 
	 * @param instance
	 */
	public void printInstance(Instance instance)
	{	
		for (Attribute attribute : attributeSet.getAttributes())
		{
			Integer attrId = attribute.getId();
			Double attrValue = instance.getAttributeValue(attrId);
			
			System.out.print(attribute.getName() + " : ");
			
			if (attrValue != null)
			{
				// Print the value of the attribute
				if (attribute.getType() == Attribute.CONTINUOUS)
				{
					System.out.print(attrValue);
				}
				else if (attribute.getType() == Attribute.NOMINAL)
				{
					// If the attribute is nominal, we need to get the name of the nominal value ID
					String nominalValueName = attribute.getNominalValueName(attrValue.intValue());
					System.out.print(nominalValueName);
				}
			}
			else // If we don't have the attribute in the instance, it means that it is missing
			{
				System.out.print("?");
			}
			System.out.print("\n");
		}
	}
	
}
