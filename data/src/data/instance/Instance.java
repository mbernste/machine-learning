package data.instance;

import java.util.HashMap;
import java.util.Map;

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
		attributes = new HashMap<Integer, Double>();
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
	
	@Override
	public String toString()
	{
	    return "Instance " + this.id;
	}
}
