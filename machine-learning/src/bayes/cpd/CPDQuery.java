package bayes.cpd;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import data.Attribute;

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
        if (attr.isValidNominalValueId(nomValueId))
        {
            queryItems.put(attr, nomValueId);
        }
        else
        {
            throw new RuntimeException(nomValueId + " is not a valid nominal" +
                                        " value ID for the attribute " + 
                                        attr.getName());
        }
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
        String result = "CPD(";

        for (Entry<Attribute, Integer> entry : queryItems.entrySet())
        {
            Attribute attr = entry.getKey();
            Integer attrValue = entry.getValue();
            result += attr.getName() + " = " 
                    + attr.getNominalValueName(attrValue) + ", ";
        }
        
        result = result.substring(0, result.length() - 2);
        result += ")";
        
        return result;
    }

}
