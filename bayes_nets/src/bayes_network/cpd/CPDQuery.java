package bayes_network.cpd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.attribute.Attribute;

import pair.Pair;

/**
 * Objects of this class are used for querying a conditional probability 
 * distribution for a single BNNode in a BayesianNetwork
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class CPDQuery 
{

    /**
     * Maps an attribute to the value of this attribute specified in the query
     */
    private Map<Attribute, Integer> queryItems;
    
    /**
     * Constructor
     */
    public CPDQuery()
    {
        this.queryItems = new HashMap<Attribute, Integer>();
    }

    /**
     * Add a query item to this query
     * 
     * @param attr the Attribute this query is querying for
     * @param nominalValueId the nominal value ID specified for this Attribute
     */
    public void addQueryItem(Attribute attr, Integer nomValueId)
    {	
        queryItems.put(attr, nomValueId);
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
        return queryItems.keySet().contains(attr);
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
        return queryItems.get(attr);
    }
    
    @Override
    public String toString()
    {
        String result = "";
              
        return result;
    }

}
