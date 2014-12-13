package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

/**
 * Stores a set of attributes.
 *  
 */
public class AttributeSet 
{
	/**
	 * Maps the name of an attribute to the object
	 */
	private final Map<String, Attribute> nameAttrMap;
	
	/**
	 * The attribute that denotes the "class" or "concept"
	 */
	private String classAttribute;
	
	/**
	 * Constructor
	 */
	public AttributeSet(List<Attribute> attributes)
	{
	    ImmutableMap.Builder<String, Attribute> builder = new ImmutableMap.Builder<>();
	    
	    for (Attribute attr : attributes)
	    {
	        builder.put(attr.getName(), attr);
	    }
	    
	    this.nameAttrMap = builder.build();
	}
	
	/**
	 * Get an attribute by its attribute name.
	 * 
	 * @param attrName - the attribute name
	 * @return the attribute with this name
	 */
	public Attribute getAttributeByName(String attrName)
	{
		return nameAttrMap.get(attrName);
	}
	
	/**
	 * Get the nominal value ID for a specific attribute name and nominal
	 * value of that attribute
	 * 
	 * @param attrName the name of the target attribute
	 * @param attrValue the name of the target nominal value
	 * @return the unique integer ID of that nominal value
	 */
	public Integer getNominalValueId(String attrName, String attrValue)
	{
		return nameAttrMap.get(attrName).getNominalValueId(attrValue);
	}
	
	/**
	 * @return a list of all attributes in the attribute set
	 */
	public List<Attribute> getAttributes()
	{
		return new ArrayList<>(nameAttrMap.values());
	}
	
	/**
	 * Sets the attribute that will be used as attribute set's class attribute
	 * 
	 * @param attrName
	 */
	public void setClass(String attrName)
	{
		if (this.containsAttrWithName(attrName))
		{
			classAttribute = attrName;
		}
		else
		{
			throw new RuntimeException("Trying to set an invalid attribute, " + 
						attrName + " as the class attribute.");
		}
	}
	
	/**
	 * @return the name of the attribute denoted as the "class" or "concept" 
	 * attribute
	 */
	public String getClassAttrName()
	{
		return classAttribute;
	}
	
	/**
	 * Determines whether a given attribute name is in the attribute set.
	 * 
	 * @param attrName The name of the attribute for which we are checking is 
	 * valid
	 */
	public Boolean containsAttrWithName(String attrName)
	{
		return nameAttrMap.containsKey(attrName);
	}

}
