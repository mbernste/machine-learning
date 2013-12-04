package data.instance;

import java.util.HashMap;
import java.util.Map;

public class Instance 
{	
	/**
	 * This instance's attribute value.  The map maps an attribute ID
	 * to a valid value for that attribute
	 */
	private Map<Integer, Double> attributes;
	
	public Instance()
	{
		attributes = new HashMap<Integer, Double>();
	}
	
	/**
	 * Get the value for an attribute with the given attribute ID.
	 * 
	 * @param attrId the attribute ID for the value we wish to retrieve
	 * @return
	 */
	public Double getAttributeValue(Integer attrId)
	{
		return attributes.get(attrId);
	}
	
	/**
	 * Add an attribute-value pair to the instance.
	 * 
	 * @param attrId the attribute ID of the attribute being added
	 * @param value the value of the corresponding attribute
	 */
	public void addAttributeInstance(Integer attrId, Double value)
	{
		attributes.put(attrId, value);
	}
}
