package data.attribute;

import java.util.Map;

import bimap.BiMap;

public class Attribute 
{
	public final static int NOMINAL = 0;
	public final static int CONTINUOUS = 1;
	
	private String name;
	BiMap<String, Integer> nominalValueMap = null;
	private int type;
	private int id;
	
	/**
	 * Attribute constructor
	 * 
	 * @param name - name of the attribute
	 * @param id - integer id of the attribute 
	 * @param type - {nominal, continuous}
	 */
	public Attribute(String name, int id, int type, String[] nominalValues)
	{
		this.name = name;
		this.id = id;
		this.type = type;
		
		if (type == NOMINAL)
		{
			nominalValueMap = new BiMap<String, Integer>();
			
			int count = 0;
			for (String value : nominalValues)
			{
				nominalValueMap.put(value, count);
				count++;
			}
			
		}
		else if (type == CONTINUOUS)
		{
			nominalValueMap = null;	
		}
	}
	
	/**
	 * Get the name of the attribute.
	 * 
	 * @return the name of the attribute
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get this attribute's id.  In an ARFF file, this ID corresponds
	 * to its index in the ARFF header.
	 * 
	 * @return Integer - the attribute's ID
	 */
	public Integer getId()
	{
		return id;
	}
	
	/**
	 * Get the attribute's type (i.e. continuous or nominal)
	 * 
	 * @return The attribute's type
	 */
	public Integer getType()
	{
		return type;
	}
	
	/**
	 * Get the attribute's nominal value map. The map's keys correspond
	 * to the name of the value and it's values correspond to that value's 
	 * integer id.  If this attribute is continuous, then this method will 
	 * return null.
	 *  
	 * @return the nominal value map
	 */
	public Map<String, Integer> getNominalValueMap()
	{
		return nominalValueMap;
	}
	
	/**
	 * If this attribute is a nominal attribute, this method will return the 
	 * name of a nominal value for this attribute given the nominal value ID of 
	 * that value
	 * 
	 * @param attrValueId - The Id of the attribute's value
	 * @return the name of the attribute's value with this ID or
	 * 		   null if this is an invalid attribute value or the attribute is
	 * 		   continuous
	 */
	public String getNominalValueName(Integer attrValueId)
	{
		
		String nominalValueName = null;
		if (this.type == Attribute.CONTINUOUS)
		{
			System.err.println("Error retrieving nominal value name for" +
					" the ID " + attrValueId +
					". Trying to retrieve a nominal value from a continuous " +
					"attribute, " + 
					this.name + ".");
			return nominalValueName;
		}
		
		if  (!nominalValueMap.containsValue(attrValueId))
		{
			System.err.println("Error retrieving nominal value name " +
					"for the ID " + attrValueId +
					".  This value ID is not a possible value for the " +
					"attribute " + this.name + ".");
			return nominalValueName;
		}
		
		return nominalValueMap.getKey(attrValueId);
	}
	
	/**
	 * If this attribute is a nominal attribute, this method will return an 
	 * integer corresponding to the ID of this attribute's possible value.
	 * 
	 * @param attrValueName - The name of the attribute's value
	 * @return Integer - The ID of the attributes value.
	 * 		   null    - If this is an invalid attribute value or the attribute 
	 * 					 is continuous
	 */
	public Integer getNominalValueId(String attrValueName)
	{
		Integer valueId = null;
		
		if (this.type == Attribute.CONTINUOUS)
		{
			System.err.println("Error retrieving nominal value Id for the " +
					"value " + attrValueName +
					". Trying to retrieve a nominal value from a continuous " +
					"attribute, " + this.name + ".");
			return valueId;
		}
		
		if  (!nominalValueMap.containsKey(attrValueName))
		{
			System.err.println("Error retrieving nominal value Id for the " +
					"value " + attrValueName + ".  This value name is not a " +
					"possible value for the attribute " + this.name + ".");
			return valueId;
		}
		
		return nominalValueMap.getValue(attrValueName);
	}
}
