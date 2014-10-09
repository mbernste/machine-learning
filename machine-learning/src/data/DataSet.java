package data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Represents a data set.  Objects of this class store all instances as well
 * as all information about the attributes that these instances have.  
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class DataSet 
{
	/**
	 * The attributes of all instances in this data set
	 */
	protected AttributeSet attributeSet;
	
	/**
	 * The instances in this data set
	 */
	protected InstanceSet instanceSet;
	
	/**
	 * Maps each nominal value ID of the class attribute to the number of
	 * instances in the data set that are of that class
	 */
	protected Map<Integer, Integer> classCounts;
	
	/**
	 * The attribute representing the class attribute
	 */
	protected Attribute classAttribute = null;
	
	/**
	 * Assign the Attribute that labels the class of each instance in the data
	 * set.
	 * 
	 * @param attrName the name of the attribute used to label the data
	 */
	public void setClassAttribute(String attrName)
	{
		if (attributeSet.contains(attrName))
		{
			classAttribute = attributeSet.getAttributeByName(attrName);
		}
		else
		{
			System.err.println("Error assigning class attribute in " +
					"data set.  " +  "The attribute, " + attrName + 
					" is not an attribute name in this dataset" );
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
	
	/**
	 * @return a mapping of each nominal value ID of the class attribute to the 
	 * number of instances in the data set that are of that class
	 */
	public Map<Integer, Integer> getClassCounts()
	{
		if (classCounts == null)
		{
			throw new RuntimeException("Error. Trying to retrieve class " +
					"counts from DataSet, but class counts Map has not been " +
					"initialized.");
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
	 * @return the ID of the class attribute
	 */
	public Integer getClassAttributeId()
	{
		return classAttribute.getId();
	}
	
	/**
	 * @param attrSet the set of attributes that all instances should have
	 */
	public void setAttributeSet(AttributeSet attrSet)
	{
		this.attributeSet = attrSet;
	}
	
	/**
	 * @param instSet the set of all instances in this dataset
	 */
	public void setInstanceSet(InstanceSet instSet)
	{
		this.instanceSet = instSet;
	}
	
	/**
	 * @return the set of all instances in this data set
	 */
	public InstanceSet getInstanceSet()
	{
		return instanceSet;
	}
	
	/**
	 * @return the set of all attributes that all instances in this data set
	 * have
	 */
	public AttributeSet getAttributeSet()
	{
		return attributeSet;
	}

	/**
	 * @param attrName an attribute's name
	 * @return the attribute with the specified name
	 */
	public Attribute getAttributeByName(String attrName)
	{
		return attributeSet.getAttributeByName(attrName);
	}
	
	/** 
	 * @param attrId an attribute's unique ID
	 * @return the attribute with the specified ID
	 */
	public Attribute getAttributeById(Integer attrId)
	{
		return attributeSet.getAttributeById(attrId);
	}
	
	
	/**
	 * Print the attributes in a descriptive easy to read format such that each
	 * attribute is printed with its integer ID and each nominal value for 
	 * nominal attributes is printed with its nominal value ID
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
		List<Instance> instances = instanceSet.getInstances();
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


        /*
         *  Create an entry in the classCounts map for each possible value
         *  (i.e. label of the class attribute) 
         */
		int classAttrId = classAttribute.getId();
		for (Integer classLabelValue : classAttribute.getNominalValueMap().values())
		{
			classCounts.put(new Integer(classLabelValue), new Integer(0));
		}
		
		/*
		 *  For each instance in the instance set, increment the count of the class label
		 *  for each instance of that class
		 */
		for (Instance instance : instanceSet.getInstances())
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
	 * @param instance the instance to print
	 */
	public void printInstance(Instance instance)
	{	
		for (Attribute attribute : attributeSet.getAttributes())
		{
			Double attrValue = instance.getAttributeValue(attribute);
			
			System.out.print(attribute.getName() + " : ");
			
			if (attrValue != null)
			{
				/*
				 *  Print the value of the attribute
				 */
				if (attribute.getType() == Attribute.Type.CONTINUOUS)
				{
					System.out.print(attrValue);
				}
				else if (attribute.getType() == Attribute.Type.NOMINAL)
				{
					/*
					 *  If the attribute is nominal, we need to get the name of the nominal value ID
					 */
					String nominalValueName = attribute.getNominalValueName(attrValue.intValue());
					System.out.print(nominalValueName);
				}
			}
			else // If we don't have the attribute in the instance,
			{    // it means that it is missing
				
				System.out.print("?");
			}
			System.out.print("\n");
		}
	}
	
	public String toString(){
		return instanceSet.toString();
	}
	
}
