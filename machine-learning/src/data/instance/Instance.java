package data.instance;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import data.attribute.Attribute;

/**
 * Represents a single instance
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class Instance 
{	
	/**
	 * This instance's attribute value.  The map maps an attribute ID
	 * to a valid value for that attribute
	 */
	private Map<Integer, Double> attributes;
	
	/**
	 * This instance's id
	 */
	private int id;
	
	/**
	 * Constructor
	 */
	public Instance(int id)
	{
		attributes = new HashMap<>();
		this.id = id;
	}
	
	/**
	 * Get the value for an attribute with the given attribute ID.
	 * 
	 * @param attrId the attribute ID for the value we wish to retrieve
	 * @return
	 */
	//TODO REFACTOR!
	@Deprecated
	public Double getAttributeValue(Integer attrId)
	{
		return attributes.get(attrId);
	}
	
	/**
	 * Get the value for an attribute.
	 * 
	 * @param attr the specified attribute
	 * @return this instance's value of the specified attribute
	 */
	public Double getAttributeValue(Attribute attr)
	{
	    return attributes.get(attr.getId());
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
	 * Checks if this Instance is equal to another Instance.
	 * 
	 * @param o the other Instance
	 * @return
	 */
	@Override
	public boolean equals(Object o){
		Map<Integer, Double> other = ((Instance)o).attributes;
		for(Entry<Integer, Double> attr: attributes.entrySet()){
			if(!other.get(attr.getKey()).equals(attr.getValue())){
				return false;
			}
		}
		return true;
	}
	
	public String toString(){
		String result = "";
		for(Entry<Integer, Double> entry: attributes.entrySet()){
			result += "(" + entry.getKey() + " -> " + entry.getValue() + ") ";
		}
		return result;
	}
}
