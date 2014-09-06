package bayes;

import java.util.ArrayList;

import data.Attribute;

import pair.Pair;

/**
 * Used for querying a {@code BayesianNetwork} object for a conditional
 * probability of the value of a single attribute conditioned on the values
 * of a set of other attributes.  For example, if we wish to query a Bayes net
 * for the following probability: P(A = a | E = e, D = d), this object 
 * would represent the query, (A = a | E = e, D = d)
 * 
 * @author Matthew Bernstein - matthewb@cs.wisc.edu
 *
 */
public class BNConditionalQuery 
{

    /**
     * Each pair of integers represents an Attribute ID and the nominal value ID
     * of the attribute.  These pairs represent the values of specific
     * attributes that the target attribute/value pair is conditioned on.
     * <br>
     * <br>
     * For example, given the following query: P(A = a | D = d, E = e), this
     * set of pairs would be as follows: (D,d), (E,e).
     */
    private VariableSet conditionVariables;
    
    /**
     * The target attribute value pair for which we would like to know the 
     * probability of observing.
     * <br>
     * <br>
     * For example, given the following query: P(A = a | D = d, E = e), this
     * pairs would be (A, a).
     */
    private Pair<Attribute, Integer> targetVariable;

    /**
     * Constructor
     */
    public BNConditionalQuery()
    {
        this.conditionVariables = new VariableSet();
        this.targetVariable = new Pair<Attribute, Integer>();
    }

    /**
     * Add a condition attribute/value pair to the set of attribute/value pairs
     * used in the query for finding the probability of finding the target
     * attribute.
     * 
     * @param attr the ID of the condition attribute to be added to the query 
     * @param nominalValueId the nominal value ID specified for this Attribute
     */
    public void addConditionVariable(Attribute attr, Integer nomValueId)
    {		
        conditionVariables.addVariable(attr, nomValueId);
    }
    
    /**
     * Set the target variable for which this query wants to know the 
     * probability of observing.  For example, if this query is of the following
     * form: (A = a | B = b, E = e), then this method sets A = a
     * 
     * @param attr the attribute of the target variable
     * @param nomValueId the value of the target variable
     */
    public void setTargetVariable(Attribute attr, Integer nomValueId)
    {
        targetVariable = new Pair<Attribute, Integer>(attr, nomValueId);
    }

    /**
     * Determines whether this BNQuery includes the value of a specific
     * attribute in the set of condition attributes
     * 
     * @param attr the target Attribute
     * @return true if this query is specifying a value for this specific 
     * Attribute in the set of condition attributes
     */
    public Boolean containsConditionAttribute(Attribute attr)
    {
        return conditionVariables.containsAttribute(attr);
    }

    /**
     * Gets the value for a specific attribute in the set of condition
     * attributes. If this attribute is not specified by this query, this 
     * method returns null.
     *  
     * @param attr the target Attribute
     * @return the value of target Attribute specified in the condition part
     * of the query.  null if this Attribute is not specified in this query
     */
    public Integer getValueForConditionAttribute(Attribute attr)
    {
        return conditionVariables.getValueForAttribute(attr);
    }
    
    /**
     * @return the list of attribute/value pairs in the set of condition
     * variables specified in this query.  Each pair is the ID of the attribute 
     * and the nominal value ID of the value of this attribute.  If this
     * query represents (A = a | E = e, D = d), this method returns
     * ((E,e), (D,d))
     */
    public ArrayList<Pair<Attribute, Integer>> getConditionVariableList()
    {
        return this.conditionVariables.getVariables();
    }
    
    /**
     * @return the list of attribute/value pairs in the set of condition
     * variables specified in this query.  Each pair is the ID of the attribute 
     * and the nominal value ID of the value of this attribute.  If this
     * query represents (A = a | E = e, D = d), this method returns
     * ((E,e), (D,d))
     */
    public VariableSet getConditionalVariableSet()
    {
        return this.conditionVariables;
    }
    
    /**
     * @return the target attribute/value pair specified in this conditional 
     * probability query. If this query represents (A = a | E = e, D = d), this
     * method returns (A,a)
     */
    public Pair<Attribute, Integer> getTargetVariable()
    {
        return this.targetVariable;
    }
   
    /**
     * @return the list of attribute/value pairs in this query including the
     * target variable.  Each pair is the ID of the attribute 
     * and the nominal value ID of the value of this attribute.  If this
     * query represents (A = a | E = e, D = d), this method returns
     * ((A,a), (E,e), (D,d))
     */
    public VariableSet getAllVariableSet()
    {
        VariableSet allVariables = (VariableSet) conditionVariables.clone();
        
        allVariables.addVariable(targetVariable.getFirst(),
                                 targetVariable.getSecond());
        
        return allVariables;
    }
    
    /**
     * @return the list of attribute/value pairs in this query including the
     * target variable.  Each pair is the ID of the attribute 
     * and the nominal value ID of the value of this attribute.  If this
     * query represents (A = a | E = e, D = d), this method returns
     * ((A,a), (E,e), (D,d))
     */
    public ArrayList<Pair<Attribute, Integer>> getAllVariableList()
    {
        return this.getAllVariableSet().getVariables();
    }
    
    @Override
    public String toString()
    {
        String result = "";
        
        Attribute attr = this.targetVariable.getFirst();
        Integer attrValue = this.targetVariable.getSecond();
        
        /*
         * Target variable
         */
        result += "P(" + attr.getName() + " = " + attr.getNominalValueName(attrValue);
        
        /*
         * Conditional pipe
         */
        if (!this.conditionVariables.getVariables().isEmpty())
        {
            result += " | ";
        }
        
        /*
         * Condition variables
         */
        for (Pair<Attribute, Integer> attrValPair : this.conditionVariables.getVariables())
        {
            attr = attrValPair.getFirst();
            attrValue = attrValPair.getSecond();
            result += attr.getName() + " = " + attr.getNominalValueName(attrValue);
            result += ", ";
        }
        
        /*
         * Closing paranthesis
         */
        if (!this.conditionVariables.getVariables().isEmpty())
        {
            result = result.substring(0, result.length() - 2);
        }
        result += ")";
        
        return result;
    }
    
}
