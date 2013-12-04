package bayes_network;

import java.util.ArrayList;

import data.attribute.Attribute;

import pair.Pair;

/**
 * Objects of this class are used for querying a conditional probability 
 * distribution for a single BNNode in a BayesianNetwork
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BNQuery 
{
	
	/**
	 * Each query item is a pair of integers where the first integer refers
	 * to an Attribute ID and the second integer refers to a nominal valud ID
	 * of the attribute.
	 */
	private ArrayList<Pair<Integer, Integer>> queryItems;
	
	/**
	 * Constructor
	 */
	public BNQuery()
	{
		this.queryItems = new ArrayList<Pair<Integer, Integer>>();
	}
	
	/**
	 * Add a query item to this query
	 * 
	 * @param attr the Attribute this query is querying for
	 * @param nominalValueId the nominal value ID specified for this Attribute
	 */
	public void addQueryItem(Attribute attr, Integer nomValueId)
	{		
		Pair<Integer, Integer> newItem = 
				new Pair<Integer, Integer>(attr.getId(), nomValueId);
		
		queryItems.add(newItem);
	}
	
	/**
	 * Determines whether this BNQuery includes the value of a specific
	 * attribute
	 * 
	 * @param attr the target Attribute
	 * @return true if this query is specifying a value for this specific 
	 * Attribute
	 */
	public Boolean containsAttribute(Attribute attr)
	{
		// Linear Search for a match in Attribute ID's
		for (Pair<Integer, Integer> item : queryItems)
		{
			if (item.getFirst() == attr.getId())
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the value for a specific query attribute. If this attribute
	 * is not specified by this query, this method returns null.
	 *  
	 * @param attr the target Attribute
	 * @return the value of target Attribute specified in this query.  null if
	 * this Attribute is not specified in this query
	 */
	public Integer getValueForQueryAttribute(Attribute attr)
	{
		if (attr == null)
		{
			return null;
		}
		
		// Linear search for the attribute
		for (Pair<Integer, Integer> item : queryItems)
		{
			if (item.getFirst() == attr.getId())
			{
				return item.getSecond();
			}
		}
		
		return null;
	}
	
}
