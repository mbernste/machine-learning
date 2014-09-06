package bayes;

import java.util.ArrayList;

import data.Attribute;

import pair.Pair;

/**
 * Used for querying a {@code BayesianNetwork} object for a joint
 * probability of a set of attribute/value pairs.  For example, if we wish to 
 * query a Bayes net for the following probability: P(A = a, E = e, D = d), 
 * this object would represent the query, (A = a, E = e, D = d)
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BNJointQuery 
{

    /**
     * Each pair of integers represents an Attribute ID and the nominal value ID
     * of the attribute of all of the variables in the joint probability query.
     * <br>
     * <br>
     * For example, given the following query: P(A = a, D = d, E = e), this
     * set of pairs would be as follows: (A,a), (D,d), (E,e).
     */
    private VariableSet variables;

    /**
     * Constructor 
     * 
     * @param variables the set of variables in the joint probability query
     */
    public BNJointQuery(VariableSet variables)
    {
        this.variables = variables;
    }
    
    /**
     * Constructor
     */
    public BNJointQuery()
    {
        this.variables = new VariableSet();
    }

    /**
     * Add an attribute/value pair to the set of attribute/value pairs
     * used in the query.
     * 
     * @param attr the ID of the condition attribute to be added to the query 
     * @param nominalValueId the nominal value ID specified for this Attribute
     */
    public void addVariable(Attribute attr, Integer nomValueId)
    {       
        variables.addVariable(attr, nomValueId);
    }

    /**
     * Determines whether this BNQuery includes the value of a specific
     * attribute in the set of condition attributes
     * 
     * @param attr the target Attribute
     * @return true if this query is specifying a value for this specific 
     * Attribute in the set of condition attributes
     */
    public Boolean containsAttribute(Attribute attr)
    {
       return variables.containsAttribute(attr);
    }

    /**
     * Gets the value for a specific attribute in the set of
     * attributes. If this attribute is not specified by this query, this 
     * method returns null.
     *  
     * @param attr the target Attribute
     * @return the value of target Attribute specified in the
     * of the query.  null if this Attribute is not specified in this query
     */
    public Integer getValueForAttribute(Attribute attr)
    {
        return variables.getValueForAttribute(attr);
    }
    
    /**
     * @return the list of attribute/value pairs in this joint probability 
     * query.  Each pair is the ID of the attribute and the nominal value ID
     * of the value of this attribute.
     */
    public ArrayList<Pair<Attribute, Integer>> getVariables()
    {
        return this.variables.getVariables();
    }
    
    @Override
    public String toString()
    {
        String result = "P(";
        
        /*
         * Condition variables
         */
        for (Pair<Attribute, Integer> attrValPair : this.variables.getVariables())
        {
            Attribute attr = attrValPair.getFirst();
            Integer attrValue = attrValPair.getSecond();
            result += attr.getName() + " = " + attr.getNominalValueName(attrValue);
            result += ", ";
        }
        
        /*
         * Closing paranthesis
         */
        result = result.substring(0, result.length() - 2);
        result += ")";
        
        return result;
    }

}
