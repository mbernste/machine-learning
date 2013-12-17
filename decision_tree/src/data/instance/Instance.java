package data.instance;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
	
	/**
	 * Test if this instance is equal to another.
	 * 
	 * @param o the other Instance
	 * @return
	 */
	public boolean equals(Object o){
		HashMap<Integer, Double> other = (HashMap<Integer, Double>) ((Instance)o).attributes;
		for(Entry<Integer, Double> attr: attributes.entrySet()){
			if(!(other.containsKey(attr.getKey()) && other.containsValue(attr.getValue()))){
				return false;
			}
		}
		return true;
	}
}
