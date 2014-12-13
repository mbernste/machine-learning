package data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Represents an instance.
 * 
 */
public class Instance 
{	
	/**
	 * This instance's attribute value.  The map maps an attribute ID
	 * to a valid value for that attribute
	 */
	private final Map<Attribute, Double> attributesToValues;
	
	/**
	 * Constructor
	 */
	public Instance()
	{
		attributesToValues = new HashMap<>();
	}
	
	
	/**
	 * Get the value for an attribute.
	 * 
	 * @param attr the specified attribute
	 * @return this instance's value of the specified attribute
	 */
	public Double getAttributeValue(Attribute attr)
	{
	    return attributesToValues.get(attr);
	}
	
	/**
	 * Add an attribute-value pair to the instance.
	 * 
	 * @param attrId the attribute ID of the attribute being added
	 * @param value the value of the corresponding attribute
	 */
	public void addAttributeValue(Attribute attr, Double value)
	{
		attributesToValues.put(attr, value);
	}
	
	/**
	 * Checks if this Instance is equal to another Instance.
	 * 
	 * @param o the other Instance
	 * @return
	 */
	@Override
	public boolean equals(Object o)
	{
		Map<Attribute, Double> other = ((Instance)o).attributesToValues;
		for(Entry<Attribute, Double> attr: attributesToValues.entrySet())
		{
			if(!other.get(attr.getKey()).equals(attr.getValue()))
			{
				return false;
			}
		}
		return true;
	}
	
	public String toString()
	{
		String result = "";
		for(Entry<Attribute, Double> entry: attributesToValues.entrySet())
		{
		    if (entry.getKey().getType() == Attribute.Type.NOMINAL)
		    {
		          result += "(" + entry.getKey().getName() + " = " + entry.getKey().getNominalValueName(entry.getValue().intValue()) + ") ";
		    }
		    else
		    {
		        result += "(" + entry.getKey().getName() + " = " + entry.getValue() + ") ";
		    }
		}
		return result;
	}
}
